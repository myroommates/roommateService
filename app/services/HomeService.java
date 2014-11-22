package services;

import com.avaje.ebean.Ebean;
import entities.Home;

/**
 * Created by florian on 11/11/14.
 */
public class HomeService {

    public Home findByName(String name) {
        return Ebean.createNamedQuery(Home.class, Home.FIND_BY_NAME)
                .setParameter(Home.COL_NAME, name)
                .findUnique();
    }

    public void saveOrUpdate(Home home) {
        if(home.getId()!=null){
            home.update();
        }
        else{
            home.save();
        }
    }
}
