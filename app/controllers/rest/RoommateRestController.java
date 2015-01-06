package controllers.rest;

import com.avaje.ebean.annotation.Transactional;
import controllers.rest.technical.AbstractRestController;
import controllers.rest.technical.SecurityRestController;
import controllers.technical.SecurityController;
import converter.RoommateToRoommateDTOConverter;
import dto.ChangeEmailDTO;
import dto.ChangePasswordDTO;
import dto.ListDTO;
import dto.RoommateDTO;
import dto.technical.ResultDTO;
import models.entities.Roommate;
import play.mvc.Result;
import play.mvc.Security;
import services.RoommateService;
import services.impl.RoommateServiceImpl;
import util.ErrorMessage;
import util.KeyGenerator;
import util.exception.MyRuntimeException;
import util.tool.ColorGenerator;

import java.util.Set;

/**
 * Created by florian on 11/11/14.
 */
public class RoommateRestController extends AbstractRestController {

    //services
    private RoommateService roommateService = new RoommateServiceImpl();
    private EmailRestController emailController = new EmailRestController();
    private SecurityController securityController = new SecurityController();


    //converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result changeEmail(long id) {

        if(!securityRestController.getCurrentUser().getId().equals(id)){
            throw new MyRuntimeException(ErrorMessage.NOT_YOURSELF, id);
        }

        ChangeEmailDTO changeEmailDTO = extractDTOFromRequest(ChangeEmailDTO.class);

        //control last password
        if(!roommateService.controlPassword(changeEmailDTO.getOldPassword(),securityRestController.getCurrentUser())){
            throw new MyRuntimeException(ErrorMessage.NOT_YOUR_OLD_PASSWORD);
        }

        Roommate currentUser = securityRestController.getCurrentUser();

        currentUser.setEmail(changeEmailDTO.getNewEmail());

        //operation
        roommateService.saveOrUpdate(currentUser);

        //store
        securityController.storeAccount(currentUser);


        return ok(roommateToRoommateDTOConverter.convert(currentUser));
    }

    public Result changePassword(long id) {

        if(!securityRestController.getCurrentUser().getId().equals(id)){
            throw new MyRuntimeException(ErrorMessage.NOT_YOURSELF, id);
        }

        ChangePasswordDTO changePasswordDTO = extractDTOFromRequest(ChangePasswordDTO.class);

        Roommate currentUser = securityRestController.getCurrentUser();

        //control last password
        if(!roommateService.controlPassword(changePasswordDTO.getOldPassword(),currentUser)){
            throw new MyRuntimeException(ErrorMessage.WRONG_PASSWORD);
        }

        currentUser.setPassword(changePasswordDTO.getNewPassword());

        //operation
        roommateService.saveOrUpdate(currentUser);

        return ok(roommateToRoommateDTOConverter.convert(currentUser));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result getById(Long id) {

        //load
        Roommate roommate = roommateService.findById(id);

        //control
        if (roommate == null || !roommate.getHome().equals(securityRestController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_ROOMMATE, id);
        }

        //convert and return
        return ok(roommateToRoommateDTOConverter.convert(roommate));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result getAll() {

        //load
        Set<Roommate> roommateList = securityRestController.getCurrentUser().getHome().getRoommateList();

        //conversion
        ListDTO<RoommateDTO> result = new ListDTO<>();

        for (Roommate roommate : roommateList) {
            result.addElement(roommateToRoommateDTOConverter.convert(roommate));
        }

        //return
        return ok(result);
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result create() {

        RoommateDTO dto = extractDTOFromRequest(RoommateDTO.class);

        Roommate currentUser = securityRestController.getCurrentUser();

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
        roommate.setIconColor(ColorGenerator.getColorWeb(securityRestController.getCurrentUser().getHome().getRoommateList().size()));
        roommate.setLanguage(lang());

        //create password
        roommate.setPassword(KeyGenerator.generateRandomPassword(12));

        //send email
        emailController.sendInvitationEmail(roommate, securityRestController.getCurrentUser(), lang());

        //operation
        roommateService.saveOrUpdate(roommate);

        //return
        return ok(roommateToRoommateDTOConverter.convert(roommate));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result update(long id) {

        RoommateDTO dto = extractDTOFromRequest(RoommateDTO.class);

        Roommate currentUser = securityRestController.getCurrentUser();

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
        roommate.setName(dto.getName());
        roommate.setNameAbrv(dto.getNameAbrv());
        roommate.setKeepSessionOpen(dto.getKeepSessionOpen());

        //operation
        roommateService.saveOrUpdate(roommate);

        //return
        return ok(roommateToRoommateDTOConverter.convert(roommate));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result delete(Long id) {

        Roommate currentUser = securityRestController.getCurrentUser();

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
