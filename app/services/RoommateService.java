package services;

import models.entities.Home;
import models.entities.Roommate;

import java.util.List;

/**
 * Created by florian on 6/12/14.
 */
public interface RoommateService extends CrudService<Roommate>{
    Roommate findByEmail(String email);

    Roommate findByReactivationKey(String reactivationKey);

    List<Roommate> findByHome(Home home);

    Roommate findByAuthenticationKey(String authenticationKey);

    boolean controlCookieKey(String keyElement, Roommate account);

    public boolean controlPassword(String password, Roommate account);

}
