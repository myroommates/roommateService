package controllers.technical;

import controllers.LoginController;
import models.entities.Roommate;
import play.mvc.Http;
import play.mvc.Security;
import services.RoommateService;
import services.impl.RoommateServiceImpl;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

/**
 * Created by florian on 10/11/14.
 */
public class CommonSecurityController extends Security.Authenticator {

    public static final String REQUEST_HEADER_LANGUAGE = "language";
    public static final String SESSION_IDENTIFIER_STORE = "email";
    public static final String COOKIE_KEEP_SESSION_OPEN = "session_key";
    public static final String REQUEST_HEADER_AUTHENTICATION_KEY = "authenticationKey";

    //service
    private static final RoommateService roommateService = new RoommateServiceImpl();
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
            return currentUser.getEmail();
        } else {
            return ctx.session().get(CommonSecurityController.SESSION_IDENTIFIER_STORE);
        }
    }

    public Roommate getCurrentUser() {

        //by session
        if (Http.Context.current().session().get(SESSION_IDENTIFIER_STORE) != null) {
            return roommateService.findByEmail(Http.Context.current().session().get(SESSION_IDENTIFIER_STORE));
        }

        //by request
        if(Http.Context.current().request().getHeader(REQUEST_HEADER_AUTHENTICATION_KEY)!=null) {
            String authentication = Http.Context.current().request().getHeader(REQUEST_HEADER_AUTHENTICATION_KEY);
            return  roommateService.findByAuthenticationKey(authentication);
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
                Roommate account = roommateService.findById(Long.parseLong(keyElements[0]));

                if (account != null && roommateService.controlAuthenticationKey(keyElements[1], account)) {
                    //connection
                    storeAccount(ctx, account);
                    return true;
                }
            }
            catch(NumberFormatException e){

            }
        }

        return false;
    }

    public void logout(Http.Context ctx) {

        if (getCurrentUser() != null && getCurrentUser().isKeepSessionOpen()) {

            Roommate currentUser = getCurrentUser();
            currentUser.setKeepSessionOpen(false);

            roommateService.saveOrUpdate(currentUser);
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
        if(getCurrentUser()!=null){
            return getCurrentUser().getId()+":"+getCurrentUser().getAuthenticationKey();
        }
        return null;
    }
}
