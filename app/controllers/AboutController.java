package controllers;

import com.avaje.ebean.annotation.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.RoommateToRoommateDTOConverter;
import dto.RoommateDTO;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by florian on 29/12/14.
 */
public class AboutController extends AbstractController {

    //converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();

    @Security.Authenticated(SecurityController.class)
    @Transactional
    public Result index(){

        RoommateDTO roommateDTO = roommateToRoommateDTOConverter.convert(securityController.getCurrentUser());

        return ok(views.html.home.about.render(translationService.getTranslations(lang()),roommateDTO));
    }
}
