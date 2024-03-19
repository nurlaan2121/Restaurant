package restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import restaurant.entities.StopList;

public interface StopListRepo extends JpaRepository<StopList, Long> {

    default boolean checkWithMenuitemId(Long menuitemId) {
        StopList stopList = checkForStopList(menuitemId);
        if (stopList == null) {
            return true;
        }
        System.out.println(stopList.getId());
        return false;
    }

    @Query("select s from StopList s where s.menuitem.id = :menuitemId")
    StopList checkForStopList(Long menuitemId);

    @Query("select st from StopList st where st.menuitem.id = :menuitemId")
    StopList getByIdMenuitemId(Long menuitemId);

    @Query("select s from Restaurant r join r.menuitemList rml join StopList s on s.menuitem.id = rml.id where r.admin.email = :emailAdmin and s.id =:stopListId")
    StopList getByIdStopListIdId(Long stopListId, String emailAdmin);
    @Query("select s from StopList s join s.menuitem sm where sm.id = :id")
    StopList get(Long id);
}
