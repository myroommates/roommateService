package controllers.technical;

import play.mvc.Controller;
import services.TranslationService;
import services.impl.TranslationServiceImpl;

/**
 * Created by florian on 10/11/14.
 */
public class AbstractController extends Controller {

    //controllers
    protected final SecurityController securityController = new SecurityController();
    //service
    protected final TranslationService translationService = new TranslationServiceImpl();

}
