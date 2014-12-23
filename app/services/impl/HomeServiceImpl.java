package services.impl;

import com.avaje.ebean.Ebean;
import models.entities.Home;
import services.HomeService;

import java.util.List;

/**
 * Created by florian on 11/11/14.
 */
public class HomeServiceImpl extends CrudServiceImpl<Home>  implements HomeService {

    @Override
    public List<Home> findAll() {
        return Ebean.find(Home.class).findList();
    }

}