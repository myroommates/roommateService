package be.flo.roommateService.services.impl;

import be.flo.roommateService.models.entities.Home;
import be.flo.roommateService.models.entities.ShoppingItem;
import play.db.jpa.JPA;
import be.flo.roommateService.services.ShoppingItemService;

import java.util.List;

/**
 * Created by florian on 4/12/14.
 */
public class ShoppingItemServiceImpl extends CrudServiceImpl<ShoppingItem> implements ShoppingItemService {

    @Override
    public List<ShoppingItem> findByHome(Home home) {

        String r ="SELECT s from ShoppingItem s WHERE s.home = :home";

        return JPA.em().createQuery(r,ShoppingItem.class)
                .setParameter("home",home)
                .getResultList();
    }

    @Override
    public int getCount() {
        String r ="SELECT s from ShoppingItem s";

        return JPA.em().createQuery(r,ShoppingItem.class)
                .getResultList().size();
    }

    @Override
    public int getCountTotal() {
        String r ="SELECT s from ShoppingItem s";

        return JPA.em().createQuery(r,ShoppingItem.class)
                .getResultList().size();
    }

}
