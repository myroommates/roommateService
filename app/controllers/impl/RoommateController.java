package controllers.impl;

import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.RoommateToRoommateDTOConverter;
import dto.ListDTO;
import dto.RoommateDTO;
import dto.technical.ResultDTO;
import model.entities.Roommate;
import play.mvc.Result;
import play.mvc.Security;
import services.RoommateService;
import services.impl.RoommateServiceImpl;
import util.ErrorMessage;
import util.exception.MyRuntimeException;
import util.tool.ColorGenerator;

import java.util.Set;

/**
 * Created by florian on 11/11/14.
 */
public class RoommateController extends AbstractController {

    //services
    private RoommateService roommateService = new RoommateServiceImpl();
    private EmailControllerImpl emailController = new EmailControllerImpl();

    //converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();

    @Security.Authenticated(SecurityController.class)
    public Result getById(Long id) {

        //load
        Roommate roommate = roommateService.findById(id);

        //control
        if (roommate == null || !roommate.getHome().equals(securityController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_ROOMMATE, id);
        }

        //convert and return
        return ok(roommateToRoommateDTOConverter.convert(roommate));
    }

    @Security.Authenticated(SecurityController.class)
    public Result getAll() {

        //load
        Set<Roommate> roommateList = securityController.getCurrentUser().getHome().getRoommateList();

        //conversion
        ListDTO<RoommateDTO> result = new ListDTO<>();

        for (Roommate roommate : roommateList) {
            result.addElement(roommateToRoommateDTOConverter.convert(roommate));
        }

        //return
        return ok(result);
    }

    @Security.Authenticated(SecurityController.class)
    public Result create() {

        RoommateDTO dto = extractDTOFromRequest(RoommateDTO.class);

        Roommate currentUser = securityController.getCurrentUser();

        //control email
        Roommate roommateWithSameEmail = roommateService.findByEmail(dto.getEmail());
        if (roommateWithSameEmail != null) {
            throw new MyRuntimeException(ErrorMessage.EMAIL_ALREADY_USED);
        }

        //build entity
        Roommate roommate = new Roommate();
        roommate.setHome(currentUser.getHome());
        roommate.setEmail(dto.getEmail());
        roommate.setName(dto.getName());
        roommate.setIconColor(ColorGenerator.getColorWeb(securityController.getCurrentUser().getHome().getRoommateList().size()));

        //send email
        emailController.sendInvitationEmail(roommate, securityController.getCurrentUser(),securityController.getCurrentLanguage(ctx()));

        //operation
        roommateService.saveOrUpdate(roommate);

        //return
        return ok(roommateToRoommateDTOConverter.convert(roommate));
    }

    @Security.Authenticated(SecurityController.class)
    public Result update(long id) {

        RoommateDTO dto = extractDTOFromRequest(RoommateDTO.class);

        Roommate currentUser = securityController.getCurrentUser();

        //load entity
        Roommate roommate = roommateService.findById(id);

        if (roommate == null) {
            throw new MyRuntimeException(ErrorMessage.ENTITY_NOT_FOUND, Roommate.class.getName(), id);
        }

        //control home
        if (!roommate.getHome().equals(currentUser.getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_ROOMMATE, id);
        }

        //control email
        Roommate roommateWithSameEmail = roommateService.findByEmail(dto.getEmail());
        if (roommateWithSameEmail != null && !roommateWithSameEmail.equals(roommate)) {
            throw new MyRuntimeException(ErrorMessage.EMAIL_ALREADY_USED);
        }

        //build entity
        roommate.setEmail(dto.getEmail());
        roommate.setName(dto.getName());

        //operation
        roommateService.saveOrUpdate(roommate);

        //return
        return ok(roommateToRoommateDTOConverter.convert(roommate));
    }

    @Security.Authenticated(SecurityController.class)
    public Result delete(Long id) {

        Roommate currentUser = securityController.getCurrentUser();

        //load entity
        Roommate roommate = roommateService.findById(id);

        if (roommate == null) {
            throw new MyRuntimeException(ErrorMessage.ENTITY_NOT_FOUND, Roommate.class.getName(), id);
        }

        //control home
        if (!roommate.getHome().equals(currentUser.getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_ROOMMATE, id);
        }

        //control roommate
        if (roommate.equals(currentUser)) {
            throw new MyRuntimeException(ErrorMessage.CANNOT_REMOVE_YOURSELF);
        }

        //control usage
        if (roommate.getTicketList() != null && roommate.getTicketList().size() > 0) {
            throw new MyRuntimeException(ErrorMessage.ROOMMATE_USED, roommate.getName());
        }

        roommateService.remove(roommate);

        return ok(new ResultDTO());
    }

}
