package controllers.technical;

import controllers.LoginController;
import models.entities.Roommate;
import play.mvc.Http;
import play.mvc.Result;
import services.RoommateService;
import services.impl.RoommateServiceImpl;

/**
 * Created by florian on 20/02/15.
 */
public class SuperAdminSecurityController extends CommonSecurityController {

    //controller
    private static LoginController loginController = new LoginController();
    //service
    private RoommateService roommateService = new RoommateServiceImpl();

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return loginController.loginPage();
    }

    @Override
    public String getUsername(Http.Context ctx) {


        //only by session
        if (ctx.session().get(CommonSecurityController.SESSION_IDENTIFIER_STORE) != null &&
                roommateService.findByEmail(ctx.session().get(CommonSecurityController.SESSION_IDENTIFIER_STORE)).getIsSuperAdmin()) {
            return ctx.session().get(CommonSecurityController.SESSION_IDENTIFIER_STORE);
        }
        return null;
    }
}
