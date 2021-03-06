package be.flo.roommateService.controllers.rest;

import play.db.jpa.Transactional;
import be.flo.roommateService.controllers.technical.AbstractController;
import be.flo.roommateService.controllers.technical.AdminSecurityRestController;
import be.flo.roommateService.controllers.technical.SecurityRestController;
import be.flo.roommateService.converter.RoommateToRoommateDTOConverter;
import be.flo.roommateService.dto.ChangeEmailDTO;
import be.flo.roommateService.dto.ChangePasswordDTO;
import be.flo.roommateService.dto.ListDTO;
import be.flo.roommateService.dto.RoommateDTO;
import be.flo.roommateService.dto.technical.ResultDTO;
import be.flo.roommateService.models.entities.Roommate;
import play.i18n.Lang;
import play.mvc.Result;
import play.mvc.Security;
import be.flo.roommateService.services.RoommateService;
import be.flo.roommateService.services.impl.RoommateServiceImpl;
import be.flo.roommateService.util.ErrorMessage;
import be.flo.roommateService.util.KeyGenerator;
import be.flo.roommateService.util.exception.MyRuntimeException;
import be.flo.roommateService.util.tool.ColorGenerator;

import java.util.Set;

/**
 * Created by florian on 11/11/14.
 */
public class RoommateRestController extends AbstractController {

    //be.flo.roommateService.services
    private RoommateService roommateService = new RoommateServiceImpl();
    private EmailRestController emailController = new EmailRestController();


    //be.flo.roommateService.converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result changeEmail(long id) {

        //control it's myself
        if (!securityController.getCurrentUser().getId().equals(id)) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOURSELF, id);
        }

        ChangeEmailDTO changeEmailDTO = extractDTOFromRequest(ChangeEmailDTO.class);

        Roommate currentUser = securityController.getCurrentUser();

        //control email
        Roommate sameEmailAccount = roommateService.findByEmail(changeEmailDTO.getNewEmail());
        if(sameEmailAccount!=null && !sameEmailAccount.getId().equals(currentUser.getId())){
            throw new MyRuntimeException(ErrorMessage.EMAIL_ALREADY_USED);
        }

        //control last password
        if (!roommateService.controlPassword(changeEmailDTO.getOldPassword(), securityController.getCurrentUser())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOUR_OLD_PASSWORD);
        }



        currentUser.setEmail(changeEmailDTO.getNewEmail());

        //operation
        roommateService.saveOrUpdate(currentUser);

        //store
        securityController.storeAccount(ctx(), currentUser);


        return ok(roommateToRoommateDTOConverter.convert(currentUser));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result changePassword(long id) {

        //contorl it's myself'
        if (!securityController.getCurrentUser().getId().equals(id)) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOURSELF, id);
        }

        ChangePasswordDTO changePasswordDTO = extractDTOFromRequest(ChangePasswordDTO.class);

        Roommate currentUser = securityController.getCurrentUser();

        //control last password
        if (!roommateService.controlPassword(changePasswordDTO.getOldPassword(), currentUser)) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOUR_PASSWORD);
        }

        currentUser.setPassword(changePasswordDTO.getNewPassword());

        //operation
        roommateService.saveOrUpdate(currentUser);

        return ok(roommateToRoommateDTOConverter.convert(currentUser));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result update(long id) {

        //contorl it's myself'
        if (!securityController.getCurrentUser().getId().equals(id)) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOURSELF, id);
        }

        RoommateDTO dto = extractDTOFromRequest(RoommateDTO.class);

        Roommate roommate = securityController.getCurrentUser();

        //control email
        Roommate roommateWithSameEmail = roommateService.findByEmail(dto.getEmail());
        if (roommateWithSameEmail != null && !roommateWithSameEmail.equals(roommate)) {
            throw new MyRuntimeException(ErrorMessage.EMAIL_ALREADY_USED);
        }

        //build entity
        roommate.setName(dto.getName());
        roommate.setNameAbrv(dto.getNameAbrv());
        if (!roommate.getLanguage().code().equals(dto.getLanguageCode())) {
            //control language
            boolean founded = false;
            for (Lang lang : Lang.availables()) {
                if (lang.code().equals(dto.getLanguageCode())) {
                    founded = true;
                    roommate.setLanguage(lang);
                    break;
                }
            }

            if (!founded) {
                throw new MyRuntimeException(ErrorMessage.LANGUAGE_NOT_ACCEPTED, dto.getLanguageCode());
            }
        }

        //operation
        roommateService.saveOrUpdate(roommate);

        //return
        return ok(roommateToRoommateDTOConverter.convert(roommate));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
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

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
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

    /**
     * ONLY ADMIN
     * @return
     */
    @Security.Authenticated(AdminSecurityRestController.class)
    @Transactional
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
        roommate.setLanguage(lang());

        //save language change
        ctx().changeLang(roommate.getLanguage().code());

        //create password
        String password = KeyGenerator.generateRandomPassword();
        roommate.setPassword(password);

        //operation
        roommateService.saveOrUpdate(roommate);

        //send email
        emailController.sendInvitationEmail(roommate, securityController.getCurrentUser(), lang(),password);

        //return
        return ok(roommateToRoommateDTOConverter.convert(roommate));
    }


    /**
     * ONLY ADMIN
     * @param id
     * @return
     */
    @Security.Authenticated(AdminSecurityRestController.class)
    @Transactional
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
