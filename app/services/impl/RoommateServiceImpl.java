package services.impl;

import com.avaje.ebean.Ebean;
import models.entities.Home;
import models.entities.Roommate;
import org.jasypt.util.password.StrongPasswordEncryptor;
import services.RoommateService;

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
    public Roommate findByReactivationKey(String reactivationKey) {

        return Ebean.createNamedQuery(Roommate.class, Roommate.FIND_BY_REACTIVATION_KEY)
                .setParameter(Roommate.PARAM_REACTIVATION_KEY, reactivationKey)
                .findUnique();
    }

    @Override
    public void saveOrUpdate(Roommate roommate) {

        if (roommate.getPassword().length() < 50) {
            roommate.setPassword(generateEncryptingPassword(roommate.getPassword()));
        }
        if (roommate.getCookieValue() != null && roommate.getCookieValue().length() < 50) {
            roommate.setCookieValue(generateEncryptingPassword(roommate.getCookieValue()));
        }

        //compute abrv
        if (roommate.getId() == null) {
            List<Roommate> roommateList = new ArrayList<>();
            if (roommate.getHome().getId() != null) {
                roommateList = findByHome(roommate.getHome());
            }

            roommate.setNameAbrv(computeAcronym(roommate, roommateList));
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
    public boolean controlCookieKey(String cookieValue, Roommate account) {
        return account.getCookieValue() != null && new StrongPasswordEncryptor().checkPassword(cookieValue, account.getCookieValue());
    }

    @Override
    public boolean controlPassword(String password, Roommate account) {
        return new StrongPasswordEncryptor().checkPassword(password,
                account.getPassword());
    }


    private String generateEncryptingPassword(final String password) {

        return new StrongPasswordEncryptor().encryptPassword(password);
    }

    private String computeAcronym(Roommate roommate, List<Roommate> roommateList) {
        //start by the first letter of the first name
        String acronym = "";
        acronym += roommate.getName().toCharArray()[0];
        //and test
        if (roommateList.size() > 0) {
            //continue to add letters
            int counter = 1;
            while (findByAcronym(acronym, roommate, roommateList) && counter < roommate.getName().length()) {
                acronym += roommate.getName().toCharArray()[counter];
                counter++;
            }
        }
        acronym = acronym.toUpperCase();
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
