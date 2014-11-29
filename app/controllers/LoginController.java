package controllers;

import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.RoommateToLoginSuccessConverter;
import dto.LoginSuccessDTO;
import dto.post.LoginDTO;
import dto.post.RegistrationCDTO;
import entities.Home;
import entities.Roommate;
import play.Logger;
import play.mvc.Result;
import play.mvc.Security;
import services.HomeService;
import services.RoommateService;
import util.ErrorMessage;
import util.exception.MyRuntimeException;
import util.tool.ColorGenerator;

/**
 * Created by florian on 10/11/14.
 */
public class LoginController extends AbstractController {

    private RoommateService roommateService = new RoommateService();
    private HomeService homeService = new HomeService();


    public Result registration() {

        RegistrationCDTO dto = extractDTOFromRequest(RegistrationCDTO.class);

        //Control email
        if (roommateService.findByEmail(dto.getEmail()) != null) {
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.EMAIL_ALREADY_USED));
        }

        //home
        Home home = new Home();

        //roommate
        Roommate roommate = new Roommate();
        roommate.setEmail(dto.getEmail());
        roommate.setName(dto.getName());
        roommate.setPassword(dto.getPassword());
        roommate.setHome(home);
        roommate.setIconColor(ColorGenerator.getColorWeb(0));

        //operation
        //roommateService.saveOrUpdate(roommate);
        // => useless : saved during the storeIdentifier

        //connection
        securityController.storeIdentifier(roommate);


        //result
        RoommateToLoginSuccessConverter converter = new RoommateToLoginSuccessConverter();

        LoginSuccessDTO success = converter.convert(roommate);

        Logger.info("DTO : "+success);

        return ok(success);
    }


    public Result login() {

        LoginDTO dto = extractDTOFromRequest(LoginDTO.class);

        Roommate roommate = roommateService.findByEmailAndPassword(dto.getEmail(), dto.getPassword());

        if (roommate != null) {
            securityController.storeIdentifier(roommate);
        } else {
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.LOGIN_WRONG_PASSWORD_LOGIN));
        }

        //result
        RoommateToLoginSuccessConverter converter = new RoommateToLoginSuccessConverter();

        LoginSuccessDTO result = converter.convert(roommate);

        Logger.info(result+"");

        Logger.info(result.getRoommates().size()+"");

        return ok(result);
    }


    @Security.Authenticated(SecurityController.class)
    public Result loadData() {

        //result
        RoommateToLoginSuccessConverter converter = new RoommateToLoginSuccessConverter();

        LoginSuccessDTO result = converter.convert(securityController.getCurrentUser());

        Logger.info(result+"");

        Logger.info(result.getRoommates().size()+"");

        return ok(result);
    }


}
