package controllers.technical;

import controllers.LoginController;
import models.entities.Roommate;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.RoommateService;
import services.impl.RoommateServiceImpl;

/**
 * Created by florian on 10/11/14.
 */
public class SecurityController extends CommonSecurityController {

    //controller
    private static LoginController loginController = new LoginController();

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return loginController.loginPage();
    }
}
