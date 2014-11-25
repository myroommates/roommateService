package controllers.technical;

import dto.technical.ExceptionDTO;
import entities.Roommate;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.ErrorMessageService;
import services.RoommateService;
import util.KeyGenerator;

import java.util.Date;

/**
 * Created by florian on 10/11/14.
 */
public class SecurityController extends Security.Authenticator {

    public static final String SESSION_IDENTIFIER_STORE = "email";
    //public static final long SESSION_TIME = 30L;
    public final ErrorMessageService errorMessageService = new ErrorMessageService();

    private static final RoommateService roommateService = new RoommateService();

    @Override
    public String getUsername(Http.Context ctx) {

        String authenticationKey = ctx.request().getHeader("authenticationKey");

        if (authenticationKey == null) {
            return null;
        }

        //control authentication
        Roommate currentUser = getCurrentUser(authenticationKey);
        if (currentUser == null) {
            return null;
        }
        return currentUser.getEmail();
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return unauthorized(new ExceptionDTO("You aren't connected"));
    }

    public Roommate getCurrentUser() {
        return getCurrentUser(null);
    }

    public Roommate getCurrentUser(String authenticationKey) {

        final Roommate roommate;

        if (authenticationKey != null) {
            roommate = roommateService.findByAuthenticationKey(authenticationKey);
        } else {
            roommate = roommateService.findByEmail(getUsername(Http.Context.current()));
        }

        if (roommate == null) {
            return null;
        }
        /*
        if (roommate.getAuthenticationTime().getTime() + (SESSION_TIME * 60L * 1000L) < new Date().getTime()) {
            //session expired
            return null;
        }
        */
        return roommate;
    }

    public void storeIdentifier(Roommate roommate) {

        //create key
        String key = KeyGenerator.generateRandomKey(60);
        roommate.setAuthenticationKey(key);
        roommate.setAuthenticationTime(new Date());
        roommateService.saveOrUpdate(roommate);
    }
}
