package be.flo.roommateService.controllers.technical;

import be.flo.roommateService.controllers.LoginController;
import be.flo.roommateService.models.entities.Roommate;
import play.mvc.Http;
import play.mvc.Result;

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

    @Override
    public boolean testRight(Roommate roommate) {
        return true;
    }
}
