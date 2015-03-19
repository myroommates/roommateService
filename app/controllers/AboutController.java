package controllers;

import com.avaje.ebean.annotation.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.RoommateToInterfaceDataDTOConverter;
import converter.RoommateToRoommateDTOConverter;
import dto.RoommateDTO;
import play.mvc.Result;
import play.mvc.Security;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by florian on 29/12/14.
 */
public class AboutController extends AbstractController {

    //converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
    private RoommateToInterfaceDataDTOConverter roommateToInterfaceDataDTOConverter = new RoommateToInterfaceDataDTOConverter(securityController);

    @Security.Authenticated(SecurityController.class)
    @Transactional
    public Result index(){

        String currentYear = new SimpleDateFormat("yyyy").format(new Date());

        return ok(views.html.home.about.render(roommateToInterfaceDataDTOConverter.convert(securityController.getCurrentUser()),currentYear));
    }
}
