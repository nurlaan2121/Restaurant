package restaurant.service;

import restaurant.dto.request.MenuitemReq;
import restaurant.dto.response.SimpleResponse;
import restaurant.dto.response.menuitem.MenuitemPagination;
import restaurant.dto.response.menuitem.MenuitemRes;

public interface MenuitemService {
    SimpleResponse save(MenuitemReq menuitemReq);

    MenuitemRes getById(Long menuitemId);

    MenuitemPagination getAll(int page, int size);

    SimpleResponse delete(Long menuitemId);

    SimpleResponse update(Long menuitemId, MenuitemReq menuitemReq);
}
