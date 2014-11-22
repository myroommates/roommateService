package controllers;

import com.avaje.ebean.Ebean;
import controllers.technical.AbstractController;
import converter.RoommateToLoginSuccessConverter;
import dto.LoginSuccessDTO;
import dto.post.LoginDTO;
import dto.post.RegistrationCDTO;
import dto.technical.ResultDTO;
import entities.Home;
import entities.Roommate;
import play.Logger;
import play.mvc.Result;
import services.HomeService;
import services.RoommateService;
import util.ErrorMessage;
import util.exception.MyRuntimeException;
import util.tool.ColorGenerator;

import javax.persistence.EntityManager;

/**
 * Created by florian on 10/11/14.
 */
public class LoginController extends AbstractController {

    private RoommateService roommateService = new RoommateService();
    private HomeService homeService = new HomeService();

    public Result registration() {

        RegistrationCDTO dto = extractDTOFromRequest(RegistrationCDTO.class);

        //control roommate name
        if (homeService.findByName(dto.getHomeName()) != null) {
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.HOME_NAME_ALREADY_USED));
        }

        //Control email
        if (roommateService.findByEmail(dto.getEmail()) != null) {
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.EMAIL_ALREADY_USED));
        }

        //home
        Home home = new Home();
        home.setName(dto.getHomeName());

        //roommate
        Roommate roommate = new Roommate();
        roommate.setEmail(dto.getEmail());
        roommate.setFirstName(dto.getFirstName());
        roommate.setLastName(dto.getLastName());
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

        Logger.info("roommate : "+roommate);
        Logger.info("home : "+roommate.getHome());
        Logger.info("roomm : "+roommate.getHome().getRoommateList());


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

        return ok(converter.convert(roommate));
    }


}
