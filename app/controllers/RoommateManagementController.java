package controllers;

import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.RoommateToRoommateDTOConverter;
import dto.ListDTO;
import dto.RoommateDTO;
import models.entities.Roommate;
import play.mvc.Result;
import play.mvc.Security;
import services.RoommateService;
import services.impl.RoommateServiceImpl;

/**
 * Created by florian on 22/12/14.
 */
public class RoommateManagementController extends AbstractController{

    //converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
    //service
    private RoommateService roommateService = new RoommateServiceImpl();


    @Security.Authenticated(SecurityController.class)
    public Result index() {

        RoommateDTO roommateDTO = roommateToRoommateDTOConverter.convert(securityController.getCurrentUser());

        ListDTO<RoommateDTO> roommateDTOListDTO  = new ListDTO<>();

        for (Roommate roommate : roommateService.findByHome(securityController.getCurrentUser().getHome())) {
            roommateDTOListDTO.addElement(roommateDTO);
        }

        return ok(views.html.home.roommate.render(roommateDTO,roommateDTOListDTO ));
    }
}
