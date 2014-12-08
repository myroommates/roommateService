package services.impl;

import com.avaje.ebean.Ebean;
import model.entities.Home;
import services.HomeService;

import java.util.List;

/**
 * Created by florian on 11/11/14.
 */
public class HomeServiceImpl implements HomeService {

    @Override
    public void saveOrUpdate(Home home) {
        if(home.getId()!=null){
            home.update();
        }
        else{
            home.save();
        }
    }

    @Override
    public List<Home> findAll() {
        return Ebean.find(Home.class).findList();
    }
}
