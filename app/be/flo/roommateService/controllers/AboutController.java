package be.flo.roommateService.controllers;

import be.flo.roommateService.controllers.technical.AbstractController;
import be.flo.roommateService.controllers.technical.SecurityController;
import play.db.jpa.Transactional;
import be.flo.roommateService.converter.RoommateToInterfaceDataDTOConverter;
import be.flo.roommateService.converter.RoommateToRoommateDTOConverter;
import play.mvc.Result;
import play.mvc.Security;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by florian on 29/12/14.
 */
public class AboutController extends AbstractController {

    //be.flo.roommateService.converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();

    @Security.Authenticated(SecurityController.class)
    @Transactional
    public Result index(){

        String currentYear = new SimpleDateFormat("yyyy").format(new Date());

        RoommateToInterfaceDataDTOConverter roommateToInterfaceDataDTOConverter = new RoommateToInterfaceDataDTOConverter();

        return ok(views.html.home.about.render(roommateToInterfaceDataDTOConverter.convert(securityController.getCurrentUser()),currentYear));
    }
}
