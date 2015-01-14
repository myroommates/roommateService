package controllers.technical;

import dto.technical.ExceptionDTO;
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
        return unauthorized(new ExceptionDTO(Messages.get(Lang.defaultLang(),ErrorMessage.NOT_CONNECTED.name())));
    }
}
