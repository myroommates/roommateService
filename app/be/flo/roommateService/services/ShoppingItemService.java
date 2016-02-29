package be.flo.roommateService.services;

import be.flo.roommateService.models.entities.Home;
import be.flo.roommateService.models.entities.ShoppingItem;

import java.util.List;

/**
 * Created by florian on 6/12/14.
 */
public interface ShoppingItemService extends CrudService<ShoppingItem>{
    ShoppingItem findById(Long id);

    void saveOrUpdate(ShoppingItem shoppingItem);

    List<ShoppingItem> findByHome(Home home);

    void remove(ShoppingItem shoppingItem);

    int getCount();

    int getCountTotal();
}
