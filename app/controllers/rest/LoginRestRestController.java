package controllers.rest;

import com.avaje.ebean.annotation.Transactional;
import controllers.rest.technical.AbstractRestController;
import controllers.rest.technical.SecurityRestController;
import converter.RoommateToLoginSuccessConverter;
import dto.LoginSuccessDTO;
import dto.post.ReactivationDTO;
import dto.post.RegistrationDTO;
import models.entities.Home;
import models.entities.Roommate;
import play.mvc.Result;
import play.mvc.Security;
import services.RoommateService;
import services.impl.RoommateServiceImpl;
import util.ErrorMessage;
import util.KeyGenerator;
import util.exception.MyRuntimeException;
import util.tool.ColorGenerator;

/**
 * Created by florian on 10/11/14.
 */
public class LoginRestRestController extends AbstractRestController {

    //service
    private RoommateService roommateService = new RoommateServiceImpl();

    //controller
    private EmailRestController emailController = new EmailRestController();

    @Transactional
    public Result registration() {

        RegistrationDTO dto = extractDTOFromRequest(RegistrationDTO.class);

        //Control email
        if (roommateService.findByEmail(dto.getEmail()) != null) {
            throw new MyRuntimeException(ErrorMessage.EMAIL_ALREADY_USED);
        }

        //home
        Home home = new Home();

        //roommate
        Roommate roommate = new Roommate();
        roommate.setEmail(dto.getEmail());
        roommate.setName(dto.getName());
        roommate.setHome(home);
        roommate.setIconColor(ColorGenerator.getColorWeb(0));

        //generate reactivationKey
        while (roommate.getReactivationKey() == null) {
            String generateRandomPassword = KeyGenerator.generateRandomPassword(12);
            if (roommateService.findByReactivationKey(generateRandomPassword) == null) {
                roommate.setReactivationKey(generateRandomPassword);
            }
        }

        //send email
        //emailController.sendRegistrationEmail(roommate, lang());

        //connection !! this operation save the roommate too
        securityRestController.storeIdentifier(roommate);

        //result
        RoommateToLoginSuccessConverter converter = new RoommateToLoginSuccessConverter();

        LoginSuccessDTO success = converter.convert(roommate);

        return ok(success);
    }

    @Transactional
    public Result reactivation() {

        ReactivationDTO dto = extractDTOFromRequest(ReactivationDTO.class);

        Roommate roommate = roommateService.findByReactivationKey(dto.getReactivationKey());

        if (roommate != null) {
            securityRestController.storeIdentifier(roommate);
        } else {
            throw new MyRuntimeException(ErrorMessage.ACTIVATION_KEY_NOT_FOUND, dto.getReactivationKey());
        }

        //result
        RoommateToLoginSuccessConverter converter = new RoommateToLoginSuccessConverter();

        LoginSuccessDTO result = converter.convert(roommate);

        return ok(result);
    }


    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result loadData() {

        //result
        RoommateToLoginSuccessConverter converter = new RoommateToLoginSuccessConverter();

        LoginSuccessDTO result = converter.convert(securityRestController.getCurrentUser());

        return ok(result);
    }


}
