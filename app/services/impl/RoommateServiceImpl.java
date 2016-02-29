package services.impl;

import models.entities.Home;
import models.entities.Roommate;
import org.jasypt.util.password.StrongPasswordEncryptor;
import play.Logger;
import play.db.jpa.JPA;
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

        String r = "SELECT r from Roommate r where r.email = :email";


        return JPA.em().createQuery(r,Roommate.class)
                .setParameter("email", email)
                .getSingleResult();
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

        String r = "SELECT r from Roommate r where r.home = :home";

        return JPA.em().createQuery(r,Roommate.class)
                .setParameter("home", home)
                .getResultList();
    }

    @Override
    public Roommate findByAuthenticationKey(String authenticationKey) {

        String r = "SELECT r from Roommate r where r.authenticationKey = :authenticationKey";

        return JPA.em().createQuery(r,Roommate.class)
                .setParameter("authenticationKey", authenticationKey)
                .getSingleResult();
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

        //TODO
        String r = "SELECT r from Roommate r";

        return JPA.em().createQuery(r,Roommate.class)
                .getResultList().size();
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
