package services;

import com.avaje.ebean.Ebean;
import entities.Home;

/**
 * Created by florian on 11/11/14.
 */
public class HomeService {

    public void saveOrUpdate(Home home) {
        if(home.getId()!=null){
            home.update();
        }
        else{
            home.save();
        }
    }
}
