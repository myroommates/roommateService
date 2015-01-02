package services.impl;

import com.avaje.ebean.Ebean;
import models.entities.Home;
import models.entities.ShoppingItem;
import services.ShoppingItemService;

import java.util.List;

/**
 * Created by florian on 4/12/14.
 */
public class ShoppingItemServiceImpl extends CrudServiceImpl<ShoppingItem> implements ShoppingItemService {

    @Override
    public List<ShoppingItem> findByHome(Home home) {
        return Ebean.createNamedQuery(ShoppingItem.class, ShoppingItem.FIND_BY_HOME)
                .setParameter(ShoppingItem.PARAM_HOME, home.getId())
                .findList();
    }

}
