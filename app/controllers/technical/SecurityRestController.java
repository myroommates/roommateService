package controllers.technical;

import play.mvc.Http;
import play.mvc.Result;

/**
 * Created by florian on 10/11/14.
 */
public class SecurityRestController extends CommonSecurityController {

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return unauthorized();
    }
}
