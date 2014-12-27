package controllers.rest.technical;

import controllers.technical.SecurityController;
import models.entities.Language;
import models.entities.Roommate;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.RoommateService;
import services.impl.RoommateServiceImpl;
import util.KeyGenerator;

/**
 * Created by florian on 10/11/14.
 */
public class SecurityRestController extends Security.Authenticator {

    public static final String REQUEST_HEADER_LANGUAGE = "language";
    public static final String REQUEST_HEADER_AUTHENTICATION_KEY = "authenticationKey";

    private static final RoommateService ROOMMATE_SERVICE = new RoommateServiceImpl();

    @Override
    public String getUsername(Http.Context ctx) {

        if (ctx.session().get(SecurityController.SESSION_IDENTIFIER_STORE) == null) {

            String authenticationKey = ctx.request().getHeader(REQUEST_HEADER_AUTHENTICATION_KEY);

            if (authenticationKey == null) {
                return null;
            }

            //control authentication
            Roommate currentUser = getCurrentUser(authenticationKey);
            if (currentUser == null) {
                return null;
            }
            return currentUser.getEmail();
        } else {
            return ctx.session().get(SecurityController.SESSION_IDENTIFIER_STORE);
        }
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return unauthorized();
    }

    public Roommate getCurrentUser() {
        if (Http.Context.current().session().get(SecurityController.SESSION_IDENTIFIER_STORE) != null) {
            return ROOMMATE_SERVICE.findByEmail(Http.Context.current().session().get(SecurityController.SESSION_IDENTIFIER_STORE));
        }
        return getCurrentUser(null);
    }

    public Roommate getCurrentUser(String authenticationKey) {

        final Roommate roommate;

        if (authenticationKey != null) {
            roommate = ROOMMATE_SERVICE.findByAuthenticationKey(authenticationKey);
        } else {
            roommate = ROOMMATE_SERVICE.findByEmail(getUsername(Http.Context.current()));
        }

        if (roommate == null) {
            return null;
        }

        return roommate;
    }

    public void storeIdentifier(Roommate roommate) {

        //create key

        while (roommate.getReactivationKey() == null) {
            String key = KeyGenerator.generateRandomKey(40);
            if (ROOMMATE_SERVICE.findByAuthenticationKey(key) == null) {
                roommate.setAuthenticationKey(key);
            }
        }
        ROOMMATE_SERVICE.saveOrUpdate(roommate);
    }

    public Language getCurrentLanguage(Http.Context ctx) {
        if (ctx.request().getHeader(REQUEST_HEADER_LANGUAGE) != null && Language.getByAbrv(ctx.request().getHeader(REQUEST_HEADER_LANGUAGE)) != null) {
            return Language.getByAbrv(ctx.request().getHeader(REQUEST_HEADER_LANGUAGE));
        }
        return Language.getDefaultLanguage();
    }
}
