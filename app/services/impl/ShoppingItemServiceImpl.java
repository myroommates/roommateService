package services.impl;

import com.avaje.ebean.Ebean;
import model.entities.Home;
import model.entities.ShoppingItem;
import model.entities.technical.AuditedAbstractEntity;
import services.ShoppingItemService;

import java.util.List;

/**
 * Created by florian on 4/12/14.
 */
public class ShoppingItemServiceImpl implements ShoppingItemService {

    @Override
    public ShoppingItem findById(Long id) {
        return Ebean.createNamedQuery(ShoppingItem.class, ShoppingItem.FIND_BY_ID)
                .setParameter(AuditedAbstractEntity.PARAM_ID, id)
                .findUnique();
    }

    @Override
    public void saveOrUpdate(ShoppingItem shoppingItem) {
        if (shoppingItem.getId() != null) {
            shoppingItem.update();
        } else {
            shoppingItem.save();
        }
    }

    @Override
    public List<ShoppingItem> findByHome(Home home) {
        return Ebean.createNamedQuery(ShoppingItem.class, ShoppingItem.FIND_BY_HOME)
                .setParameter(ShoppingItem.PARAM_HOME, home)
                .findList();
    }

    @Override
    public void remove(ShoppingItem shoppingItem) {
        shoppingItem.delete();
    }
}
