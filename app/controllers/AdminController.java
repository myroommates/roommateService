package controllers;

import com.avaje.ebean.annotation.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.RoommateToInterfaceDataDTOConverter;
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
public class AdminController extends AbstractController{

    //converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
    private RoommateToInterfaceDataDTOConverter roommateToInterfaceDataDTOConverter = new RoommateToInterfaceDataDTOConverter(securityController);
    //service
    private RoommateService roommateService = new RoommateServiceImpl();


    @Security.Authenticated(SecurityController.class)
    @Transactional
    public Result roommateList() {

        RoommateDTO roommateDTO = roommateToRoommateDTOConverter.convert(securityController.getCurrentUser());

        ListDTO<RoommateDTO> roommateDTOListDTO  = new ListDTO<>();

        for (Roommate roommate : roommateService.findByHome(securityController.getCurrentUser().getHome())) {
            roommateDTOListDTO.addElement(roommateToRoommateDTOConverter.convert(roommate));
        }

        return ok(views.html.home.admin.roommate_list.render(roommateToInterfaceDataDTOConverter.convert(securityController.getCurrentUser()),roommateDTOListDTO ));
    }

    @Security.Authenticated(SecurityController.class)
    @Transactional
    public Result preferences(){

        RoommateDTO roommateDTO = roommateToRoommateDTOConverter.convert(securityController.getCurrentUser());

        return ok(views.html.home.admin.preferences.render(roommateToInterfaceDataDTOConverter.convert(securityController.getCurrentUser())));
    }
}
