package services;

import com.avaje.ebean.Ebean;
import entities.Home;
import entities.Roommate;
import entities.technical.AuditedAbstractEntity;

import java.util.List;

/**
 * Created by florian on 10/11/14.
 */
public class RoommateService {

    public Roommate findByEmail(String email) {

        return Ebean.createNamedQuery(Roommate.class, Roommate.FIND_BY_EMAIL)
                .setParameter(Roommate.PARAM_EMAIL, email)
                .findUnique();
    }

    public Roommate findByEmailAndPassword(String email, String password) {

        return Ebean.createNamedQuery(Roommate.class, Roommate.FIND_BY_EMAIL_AND_PASSWORD)
                .setParameter(Roommate.PARAM_EMAIL, email)
                .setParameter(Roommate.PARAM_PASSWORD, password)
                .findUnique();
    }

    public void saveOrUpdate(Roommate roommate) {
        if (roommate.getId() != null) {
            roommate.update();
        } else {
            roommate.save();
        }
    }

    public List<Roommate> findByHome(Home home) {
        return Ebean.createNamedQuery(Roommate.class, Roommate.FIND_BY_HOME)
                .setParameter(Roommate.PARAM_HOME, home)
                .findList();
    }

    public Roommate findById(Long id) {
        return Ebean.createNamedQuery(Roommate.class,Roommate.FIND_BY_ID)
                .setParameter(AuditedAbstractEntity.PARAM_ID,id)
                .findUnique();
    }

    public Roommate findByAuthenticationKey(String authenticationKey) {
        return Ebean.createNamedQuery(Roommate.class,Roommate.FIND_BY_AUTHENTICATION_KEY)
                .setParameter(Roommate.PARAM_AUTHENTICATION_KEY,authenticationKey)
                .findUnique();
    }

    public void remove(Roommate roommate) {
        roommate.delete();
    }
}
