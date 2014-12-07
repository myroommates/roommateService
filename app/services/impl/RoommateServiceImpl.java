package services.impl;

import com.avaje.ebean.Ebean;
import model.entities.Home;
import model.entities.Roommate;
import model.entities.technical.AuditedAbstractEntity;
import org.jasypt.util.password.StrongPasswordEncryptor;
import services.RoommateService;

import java.util.List;

/**
 * Created by florian on 10/11/14.
 */
public class RoommateServiceImpl implements RoommateService {

    @Override
    public Roommate findByEmail(String email) {

        return Ebean.createNamedQuery(Roommate.class, Roommate.FIND_BY_EMAIL)
                .setParameter(Roommate.PARAM_EMAIL, email)
                .findUnique();
    }

    @Override
    public Roommate findByReactivationKey(String reactivationKey) {

        return Ebean.createNamedQuery(Roommate.class, Roommate.FIND_BY_REACTIVATION_KEY)
                .setParameter(Roommate.PARAM_REACTIVATION_KEY, reactivationKey)
                .findUnique();
    }

    @Override
    public void saveOrUpdate(Roommate roommate) {

        if (roommate.getId() != null) {
            roommate.update();
        } else {
            roommate.save();
        }
    }

    @Override
    public List<Roommate> findByHome(Home home) {
        return Ebean.createNamedQuery(Roommate.class, Roommate.FIND_BY_HOME)
                .setParameter(Roommate.PARAM_HOME, home)
                .findList();
    }

    @Override
    public Roommate findById(Long id) {
        return Ebean.createNamedQuery(Roommate.class, Roommate.FIND_BY_ID)
                .setParameter(AuditedAbstractEntity.PARAM_ID, id)
                .findUnique();
    }

    @Override
    public Roommate findByAuthenticationKey(String authenticationKey) {
        return Ebean.createNamedQuery(Roommate.class, Roommate.FIND_BY_AUTHENTICATION_KEY)
                .setParameter(Roommate.PARAM_AUTHENTICATION_KEY, authenticationKey)
                .findUnique();
    }

    @Override
    public void remove(Roommate roommate) {
        roommate.delete();
    }
/*
    @Override
    public boolean controlAuthenticationKey(String password, Roommate roommate) {

        return new StrongPasswordEncryptor().checkPassword(password,
                roommate.getAuthenticationKey());
    }

    @Override
    public boolean controlReactivationKey(String password, Roommate roommate) {

        return new StrongPasswordEncryptor().checkPassword(password,
                roommate.getReactivationKey());
    }

    private String generateEncryptingPassword(final String password) {

        return new StrongPasswordEncryptor().encryptPassword(password);
    }
*/
}
