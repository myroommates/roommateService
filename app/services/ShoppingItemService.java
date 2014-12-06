package services;

import model.entities.Home;
import model.entities.ShoppingItem;

import java.util.List;

/**
 * Created by florian on 6/12/14.
 */
public interface ShoppingItemService {
    ShoppingItem findById(Long id);

    void saveOrUpdate(ShoppingItem shoppingItem);

    List<ShoppingItem> findByHome(Home home);

    void remove(ShoppingItem shoppingItem);
}
