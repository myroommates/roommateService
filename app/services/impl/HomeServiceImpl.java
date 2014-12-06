package services.impl;

import model.entities.Home;
import services.HomeService;

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
}
