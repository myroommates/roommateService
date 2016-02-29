package be.flo.roommateService.controllers.technical;

import be.flo.roommateService.controllers.LoginController;
import be.flo.roommateService.models.entities.Roommate;
import play.mvc.Http;
import play.mvc.Security;
import be.flo.roommateService.services.RoommateService;
import be.flo.roommateService.services.impl.RoommateServiceImpl;
import be.flo.roommateService.util.ErrorMessage;
import be.flo.roommateService.util.exception.MyRuntimeException;

/**
 * Created by florian on 10/11/14.
 */
public abstract class CommonSecurityController extends Security.Authenticator {

    public static final String REQUEST_HEADER_LANGUAGE = "language";
    public static final String SESSION_IDENTIFIER_STORE = "email";
    public static final String COOKIE_KEEP_SESSION_OPEN = "session_key";
    public static final String ACCOUNT_IDENTIFIER = "account_identifier";
    public static final String REQUEST_HEADER_AUTHENTICATION_KEY = "authenticationKey";

    public static final String FAILED_AUTHENTICATION_CAUSE = "FAILED_AUTHENTICATION_CAUSE";
    public static final String FAILED_AUTHENTICATION_CAUSE_WRONG_RIGHTS = "WRONG_RIGHT";

    //service
    private static final RoommateService USER_SERVICE = new RoommateServiceImpl();
    //controller
    private static LoginController loginController = new LoginController();


    /**
     * two way : by the session or by the authentication key into the http request
     *
     * @param ctx
     * @return
     */
    @Override
    public String getUsername(Http.Context ctx) {

        if (ctx.session().get(CommonSecurityController.SESSION_IDENTIFIER_STORE) == null) {

            String authenticationKey = ctx.request().getHeader(REQUEST_HEADER_AUTHENTICATION_KEY);

            if (authenticationKey == null) {
                return null;
            }

            //control authentication
            Roommate currentUser = getCurrentUser();
            if (currentUser == null) {
                return null;
            }

            //test right
            if (!testRight(currentUser)) {
                ctx.args.put(FAILED_AUTHENTICATION_CAUSE, FAILED_AUTHENTICATION_CAUSE_WRONG_RIGHTS);
                return null;
            }

            ctx.changeLang(currentUser.getLanguage().code());
            return currentUser.getEmail();
        } else {
            return ctx.session().get(CommonSecurityController.SESSION_IDENTIFIER_STORE);
        }
    }

    public abstract boolean testRight(Roommate roommate);

    public Roommate getCurrentUser() {

        //by session
        if (Http.Context.current().session().get(SESSION_IDENTIFIER_STORE) != null) {

            Roommate account = USER_SERVICE.findByEmail(Http.Context.current().session().get(SESSION_IDENTIFIER_STORE));
            if (!Http.Context.current().session().containsKey(ACCOUNT_IDENTIFIER)) {
                Http.Context.current().session().put(ACCOUNT_IDENTIFIER, account.getId() + "-" + account.getEmail());
            }
            return account;
        }

        //by request
        if (Http.Context.current().request().getHeader(REQUEST_HEADER_AUTHENTICATION_KEY) != null) {

            String  authentication = Http.Context.current().request().getHeader(REQUEST_HEADER_AUTHENTICATION_KEY);
            Roommate account        = USER_SERVICE.findByAuthenticationKey(authentication);
            if (account == null) {
                throw new MyRuntimeException("not logged");
            }
            storeAccount(Http.Context.current(), account);
            if (!Http.Context.current().session().containsKey(ACCOUNT_IDENTIFIER)) {
                Http.Context.current().session().put(ACCOUNT_IDENTIFIER, account.getId() + "-" + account.getEmail());
            }
            return account;
        }

        //by coockie
        if (Http.Context.current().request().cookie(CommonSecurityController.COOKIE_KEEP_SESSION_OPEN) != null) {
            String key = Http.Context.current().request().cookie(CommonSecurityController.COOKIE_KEEP_SESSION_OPEN).value();

            String keyElements[] = key.split(":");

            Roommate account = USER_SERVICE.findById(Long.parseLong(keyElements[0]));

            if (account != null && USER_SERVICE.controlAuthenticationKey(keyElements[1], account)) {
                //connection
                storeAccount(Http.Context.current(), account);

                if (!Http.Context.current().session().containsKey(ACCOUNT_IDENTIFIER)) {
                    Http.Context.current().session().put(ACCOUNT_IDENTIFIER, account.getId() + "-" + account.getEmail());
                }
                return account;
            }

        }
        throw new MyRuntimeException(ErrorMessage.NOT_CONNECTED);
    }

    public boolean isAuthenticated(Http.Context ctx) {

        if (ctx.session().get(SESSION_IDENTIFIER_STORE) != null) {
            return true;
        }

        if (ctx.request().cookie(CommonSecurityController.COOKIE_KEEP_SESSION_OPEN) != null) {

            String key = ctx.request().cookie(CommonSecurityController.COOKIE_KEEP_SESSION_OPEN).value();

            String keyElements[] = key.split(":");

            try {
                Roommate account = USER_SERVICE.findById(Long.parseLong(keyElements[0]));

                if (account != null && USER_SERVICE.controlAuthenticationKey(keyElements[1], account)) {
                    //connection
                    storeAccount(ctx, account);
                    return true;
                }
            } catch (NumberFormatException e) {

            }
        }

        return false;
    }

    public void logout(Http.Context ctx) {

        if (getCurrentUser() != null && getCurrentUser().isKeepSessionOpen()) {

            Roommate currentUser = getCurrentUser();
            currentUser.setKeepSessionOpen(false);

            USER_SERVICE.saveOrUpdate(currentUser);
        }
        ctx.session().clear();
    }

    public void storeAccount(Http.Context context, Roommate roommate) {

        //if the login and the password are ok, refresh the session
        Http.Context.current().session().clear();
        Http.Context.current().session().put(SESSION_IDENTIFIER_STORE, roommate.getEmail());

        context.changeLang(roommate.getLanguage().code());
    }

    public String getCookieKey() {
        if (getCurrentUser() != null) {
            return getCurrentUser().getId() + ":" + getCurrentUser().getAuthenticationKey();
        }
        return null;
    }
}
