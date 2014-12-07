package services;

import model.entities.Home;
import model.entities.Roommate;

import java.util.List;

/**
 * Created by florian on 6/12/14.
 */
public interface RoommateService {
    Roommate findByEmail(String email);

    Roommate findByReactivationKey(String reactivationKey);

    void saveOrUpdate(Roommate roommate);

    List<Roommate> findByHome(Home home);

    Roommate findById(Long id);

    Roommate findByAuthenticationKey(String authenticationKey);

    void remove(Roommate roommate);
/*
    boolean controlAuthenticationKey(String password, Roommate roommate);

    boolean controlReactivationKey(String password, Roommate roommate);
*/
}
