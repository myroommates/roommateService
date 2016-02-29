package be.flo.roommateService.controllers;

import be.flo.roommateService.controllers.technical.AbstractController;
import be.flo.roommateService.controllers.technical.SecurityController;
import play.db.jpa.Transactional;
import be.flo.roommateService.converter.RoommateToInterfaceDataDTOConverter;
import be.flo.roommateService.converter.RoommateToRoommateDTOConverter;
import be.flo.roommateService.dto.ListDTO;
import be.flo.roommateService.dto.RoommateDTO;
import be.flo.roommateService.models.entities.Roommate;
import play.mvc.Result;
import play.mvc.Security;
import be.flo.roommateService.services.RoommateService;
import be.flo.roommateService.services.impl.RoommateServiceImpl;

/**
 * Created by florian on 22/12/14.
 */
public class AdminController extends AbstractController {

    //be.flo.roommateService.converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
    private RoommateToInterfaceDataDTOConverter roommateToInterfaceDataDTOConverter = new RoommateToInterfaceDataDTOConverter();
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
