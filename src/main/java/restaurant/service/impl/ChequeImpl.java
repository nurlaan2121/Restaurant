package restaurant.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.dto.request.ChequeReq;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.cheque.ChequeRes;
import restaurant.entities.*;
import restaurant.exceptions.ForbiddenException;
import restaurant.exceptions.NotFoundException;
import restaurant.repository.*;
import restaurant.service.ChequeService;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChequeImpl implements ChequeService {
    private final ChequeRepo chequeRepo;
    private final UserRepo userRepo;
    private final RestaurantRepo restaurantRepo;
    private final MenuitemRepo menuitemRepo;
    private final StopListRepo stopListRepo;

    private Cheque myFindById(Long chequeId) {
        return chequeRepo.findById(chequeId).orElseThrow(() -> new NotFoundException("With id cheque nor found!" + chequeId));
    }

    @Override
    @Transactional
    public SimpleResponse save(ChequeReq chequeReq) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Restaurant restaurant = restaurantRepo.getRestWithWaiter(authentication.getName());
        User waiter = userRepo.findByEmail(authentication.getName()).get();
        List<Long> menuitemList = chequeReq.getMenuitemList();
        List<Menuitem> ordersFood = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);
        if (!restaurant.getUsers().contains(waiter))
            return SimpleResponse.builder().httpStatus(HttpStatus.FORBIDDEN).message("403 forbidden").build();
        for (int i = 0; i < menuitemList.size(); i++) {
            Menuitem menuitem = menuitemRepo.findById(menuitemList.get(i)).orElseThrow(() -> new NotFoundException("Not found menuitem"));
            if (!restaurant.getMenuitemList().contains(menuitem))
                return SimpleResponse.builder().httpStatus(HttpStatus.FORBIDDEN).message("You no can order food any rest").build();
            if (!stopListRepo.checkWithMenuitemId(menuitem.getId())) {
                return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("This menuitem the end  :" + menuitem.getName()).build();
            }
            menuitem.setCount(menuitem.getCount() - 1);
            log.error(String.valueOf(menuitem.getCount()));
            if (menuitem.getCount() == 0) {
                StopList stopList = new StopList("The end", ZonedDateTime.now(), menuitem);
                stopListRepo.save(stopList);
            }
            totalPrice = totalPrice.add(menuitem.getPrice());
            ordersFood.add(menuitem);
        }
        Cheque cheque = new Cheque(totalPrice, ZonedDateTime.now(), ordersFood);
        chequeRepo.save(cheque);
        waiter.getCheques().add(cheque);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success saved").build();
    }

    @Override
    public ChequeRes getById(Long chequeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Restaurant restaurant = restaurantRepo.getRestWithWaiter(authentication.getName());
        if (restaurant == null) {
            restaurant = restaurantRepo.getRestWithAdmin(authentication.getName());
        }
        Cheque searchCheque = myFindById(chequeId);
        List<User> users = restaurant.getUsers();
        for (User user : users) {
            if (user.getCheques().contains(searchCheque)) {
                ChequeRes chequeRes = searchCheque.convert();
                double servicePro = restaurant.getServicePro() / 100.0;
                chequeRes.setServicePro(servicePro);
                chequeRes.setWaiterFullName(user.getFirstName() + user.getLastName());

                BigDecimal totalPrice = chequeRes.getPrice();
                totalPrice = totalPrice.multiply(BigDecimal.valueOf(1 + servicePro));
                chequeRes.setTotalPrice(totalPrice);

                return chequeRes;
            }
        }
        throw new ForbiddenException("Forbidden 403 you no can see another cheque!");
    }

    @Override
    public BigDecimal getTotalSum(Long waiterId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Restaurant restaurant = restaurantRepo.getRestWithAdmin(authentication.getName());
        User waiter = userRepo.findById(waiterId).orElseThrow(() -> new NotFoundException("This waiter not found!" + waiterId));
        if (!restaurant.getUsers().contains(waiter))
            throw new ForbiddenException("You no can see this user Forbidden 403");
        List<Cheque> cheques = waiter.getCheques();
        BigDecimal resultSum = new BigDecimal(0);
        for (int i = 0; i < cheques.size(); i++) {
            Cheque cheque = myFindById(cheques.get(i).getId());
            resultSum = resultSum.add(cheque.getPriceAverage());
        }
        return resultSum;
    }

    @Override
    public BigDecimal getAvg() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Restaurant restWithAdmin = restaurantRepo.getRestWithAdmin(authentication.getName());
      return chequeRepo.getAvg(restWithAdmin.getId());
    }

}
