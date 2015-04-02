package services.impl;

import com.avaje.ebean.Ebean;
import models.entities.Home;
import models.entities.Roommate;
import org.jasypt.util.password.StrongPasswordEncryptor;
import play.Logger;
import services.RoommateService;
import util.KeyGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 10/11/14.
 */
public class RoommateServiceImpl extends CrudServiceImpl<Roommate> implements RoommateService {

    @Override
    public Roommate findByEmail(String email) {

        return Ebean.createNamedQuery(Roommate.class, Roommate.FIND_BY_EMAIL)
                .setParameter(Roommate.PARAM_EMAIL, email)
                .findUnique();
    }

    @Override
    public void saveOrUpdate(Roommate roommate) {

        roommate.setEmail(roommate.getEmail().toLowerCase());

        //generate the password
        if (roommate.getPassword().length() < 50) {
            roommate.setPassword(generateEncryptingPassword(roommate.getPassword()));
        }

        //crypte the authentication value
        if (roommate.getAuthenticationKey() != null && roommate.getAuthenticationKey().length() < 50) {
            roommate.setAuthenticationKey(generateEncryptingPassword(roommate.getAuthenticationKey()));
        }
        //or generate it
        else if(roommate.getAuthenticationKey()==null){
            roommate.setAuthenticationKey(generateEncryptingPassword(KeyGenerator.generateRandomKey(40)));
        }

        //compute abrv
        if (roommate.getNameAbrv() == null) {
            roommate.setNameAbrv(computeAcronym(roommate));
        }
        super.saveOrUpdate(roommate);
    }

    @Override
    public List<Roommate> findByHome(Home home) {
        return Ebean.createNamedQuery(Roommate.class, Roommate.FIND_BY_HOME)
                .setParameter(Roommate.PARAM_HOME, home.getId())
                .findList();
    }

    @Override
    public Roommate findByAuthenticationKey(String authenticationKey) {
        return Ebean.createNamedQuery(Roommate.class, Roommate.FIND_BY_AUTHENTICATION_KEY)
                .setParameter(Roommate.PARAM_AUTHENTICATION_KEY, authenticationKey)
                .findUnique();
    }

    @Override
    public boolean controlAuthenticationKey(String authenticationKey, Roommate account) {
        return !(!account.isKeepSessionOpen() || account.getAuthenticationKey().length() < 40) && account.getAuthenticationKey() != null && new StrongPasswordEncryptor().checkPassword(authenticationKey, account.getAuthenticationKey());
    }

    @Override
    public boolean controlAuthenticationGoogle(String googleKey, Roommate account) {
        return  account.getGoogleKey() != null && new StrongPasswordEncryptor().checkPassword(googleKey, account.getGoogleKey());
    }

    @Override
    public boolean controlPassword(String password, Roommate account) {
        return new StrongPasswordEncryptor().checkPassword(password,
                account.getPassword());
    }

    @Override
    public Integer getCount() {
        return Ebean.createQuery(Roommate.class).findRowCount();
    }

    @Override
    public String generateEncryptingPassword(final String password) {

        return new StrongPasswordEncryptor().encryptPassword(password);
    }

    private String computeAcronym(Roommate roommate) {
        //start by the first letter of the first name
        String acronym = "";

        if(roommate.getNameAbrv()==null) {
            int acronymSize = 3;
            if (roommate.getName().length() < 3) {
                acronymSize = roommate.getName().length();

            }
            Logger.warn("create acronym : "+acronymSize+"/"+roommate.getName().substring(0, acronymSize));
            acronym = roommate.getName().substring(0, acronymSize);
        }

        return acronym;
    }

    private boolean findByAcronym(String acronym, Roommate expected, List<Roommate> roommateList) {
        for (Roommate roommateDTO : roommateList) {
            if (!roommateDTO.equals(expected)) {

                //compare other letters for the last name
                for (int i = 1; i < acronym.toCharArray().length; i++) {
                    return acronym.toCharArray()[i] == roommateDTO.getName().toCharArray()[i];
                }
            }
        }
        return false;
    }
}
