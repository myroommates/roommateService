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
public class SuperAdminSecurityController extends SecurityRestController {

    @Override
    public boolean testRight(Roommate currentUser){
        return currentUser.getIsSuperAdmin();
    }

}
