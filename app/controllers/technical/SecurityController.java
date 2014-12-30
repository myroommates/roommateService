package controllers.technical;

import controllers.AccountController;
import models.entities.Roommate;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.RoommateService;
import services.impl.RoommateServiceImpl;

/**
 * Created by florian on 10/11/14.
 */
public class SecurityController extends Security.Authenticator {

    public static final String REQUEST_HEADER_LANGUAGE = "language";
    public static final String SESSION_IDENTIFIER_STORE = "email";
    public static final String COOKIE_KEEP_SESSION_OPEN = "session_key";

    //service
    private static final RoommateService accountService = new RoommateServiceImpl();
    //controller
    private static AccountController accountController = new AccountController();


    @Override
    public String getUsername(Http.Context ctx) {
        return ctx.session().get(SESSION_IDENTIFIER_STORE);
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return accountController.loginPage();
    }

    public boolean isAuthenticated(Http.Context ctx) {

        if (ctx.session().get(SESSION_IDENTIFIER_STORE) != null) {
            return true;
        }

        if (ctx.request().cookie(SecurityController.COOKIE_KEEP_SESSION_OPEN) != null) {

            String key = ctx.request().cookie(SecurityController.COOKIE_KEEP_SESSION_OPEN).value();

            String keyElements[] = key.split(":");

            Roommate account = accountService.findById(Long.parseLong(keyElements[0]));

            if (account!=null && accountService.controlCookieKey(keyElements[1], account)) {
                //connection
                storeAccount(account);
                return true;
            }
        }

        return false;
    }

    public void logout(Http.Context ctx) {

        if(getCurrentUser()!=null && getCurrentUser().isKeepSessionOpen()){

            Roommate currentUser = getCurrentUser();
            currentUser.setKeepSessionOpen(false);
            currentUser.setCookieValue(null);

            accountService.saveOrUpdate(currentUser);
        }
        ctx.session().clear();
    }

    public Roommate getCurrentUser() {
        return accountService.findByEmail(Http.Context.current().session().get(SESSION_IDENTIFIER_STORE));
    }

    public void storeAccount(Roommate roommate) {

        //if the login and the password are ok, refresh the session
        Http.Context.current().session().clear();
        Http.Context.current().session().put(SESSION_IDENTIFIER_STORE, roommate.getEmail());
    }
}
