package services;

import com.avaje.ebean.Ebean;
import entities.ShoppingItem;
import entities.Home;
import entities.technical.AuditedAbstractEntity;

import java.util.List;

/**
 * Created by florian on 4/12/14.
 */
public class ShoppingItemService {


    public void saveOrUpdate(ShoppingItem shoppingItem) {
        if (shoppingItem.getId() != null) {
            shoppingItem.update();
        } else {
            shoppingItem.save();
        }
    }

    public List<ShoppingItem> findByHome(Home home) {
        return Ebean.createNamedQuery(ShoppingItem.class, ShoppingItem.FIND_BY_HOME)
                .setParameter(ShoppingItem.PARAM_HOME, home)
                .findList();
    }

    public ShoppingItem findById(Long id) {
        return Ebean.createNamedQuery(ShoppingItem.class,ShoppingItem.FIND_BY_ID)
                .setParameter(AuditedAbstractEntity.PARAM_ID,id)
                .findUnique();
    }

    public void remove(ShoppingItem shoppingItem) {
        shoppingItem.delete();
    }
}
