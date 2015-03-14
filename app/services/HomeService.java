package services;

import models.entities.Home;

import java.util.List;

/**
 * Created by florian on 6/12/14.
 */
public interface HomeService extends CrudService<Home>{

    List<Home> findAll();

    Integer getCount();
}
