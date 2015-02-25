package controllers.technical;

import dto.technical.ExceptionDTO;
import models.entities.Roommate;
import play.api.i18n.Lang;
import play.i18n.Messages;
import play.mvc.Http;
import play.mvc.Result;
import util.ErrorMessage;

/**
 * Created by florian on 10/11/14.
 */
public class AdminSecurityRestController extends SecurityRestController {

    @Override
    public boolean testRight(Roommate currentUser){
        return currentUser.getIsAdmin();
    }
}
