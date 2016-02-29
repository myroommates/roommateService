package be.flo.roommateService.services;

import be.flo.roommateService.models.entities.Home;
import be.flo.roommateService.models.entities.Roommate;

import java.util.List;

/**
 * Created by florian on 6/12/14.
 */
public interface RoommateService extends CrudService<Roommate>{
    Roommate findByEmail(String email);

    List<Roommate> findByHome(Home home);

    Roommate findByAuthenticationKey(String authenticationKey);

    boolean controlAuthenticationKey(String authenticationKey, Roommate account);

    boolean controlAuthenticationGoogle(String googleKey, Roommate account);

    public boolean controlPassword(String password, Roommate account);

    Integer getCount();

    String generateEncryptingPassword(String password);
}
