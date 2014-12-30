package controllers;

import com.avaje.ebean.annotation.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.RoommateToInterfaceDataDTOConverter;
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
    private RoommateToInterfaceDataDTOConverter roommateToInterfaceDataDTOConverter = new RoommateToInterfaceDataDTOConverter();

    @Security.Authenticated(SecurityController.class)
    @Transactional
    public Result index(){

        return ok(views.html.home.about.render(roommateToInterfaceDataDTOConverter.convert(securityController.getCurrentUser())));
    }
}
