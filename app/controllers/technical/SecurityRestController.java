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
public class SecurityRestController extends CommonSecurityController {

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        if(ctx.args.get(CommonSecurityController.FAILED_AUTHENTICATION_CAUSE) == CommonSecurityController.FAILED_AUTHENTICATION_CAUSE_WRONG_RIGHTS){
            return unauthorized(new ExceptionDTO(Messages.get(Lang.defaultLang(),ErrorMessage.WRONG_AUTHORIZATION.name())));
        }
        return unauthorized(new ExceptionDTO(Messages.get(Lang.defaultLang(),ErrorMessage.NOT_CONNECTED.name())));
    }

    @Override
    public boolean testRight(Roommate roommate) {
        return true;
    }
}
