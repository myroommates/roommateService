package services;

import model.entities.Home;

import java.util.List;

/**
 * Created by florian on 6/12/14.
 */
public interface HomeService {
    void saveOrUpdate(Home home);

    List<Home> findAll();
}
