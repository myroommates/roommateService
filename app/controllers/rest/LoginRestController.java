package controllers.rest;

import com.avaje.ebean.annotation.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityRestController;
import converter.RoommateToLoginSuccessConverter;
import dto.LoginSuccessDTO;
import dto.post.LoginDTO;
import dto.post.RegistrationDTO;
import models.entities.Home;
import models.entities.Roommate;
import play.Logger;
import play.i18n.Lang;
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
public class LoginRestController extends AbstractController {

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
        home.setMoneySymbol(Home.DEFAULT_MONEY_SYMBOL);

        //roommate
        Roommate roommate = new Roommate();
        roommate.setEmail(dto.getEmail());
        roommate.setName(dto.getName());
        roommate.setHome(home);
        roommate.setIconColor(ColorGenerator.getColorWeb(0));
        if(dto.getLang()!=null){
            Lang lang = Lang.forCode(dto.getLang());
            roommate.setLanguage(lang);
            changeLang(lang.code());
        }
        else {
            roommate.setLanguage(lang());
        }
        roommate.setIsAdmin(true);

        //generate password
        roommate.setPassword(KeyGenerator.generateRandomPassword(8));


        //send email
        emailController.sendApplicationRegistrationEmail(roommate);

        roommateService.saveOrUpdate(roommate);

        //result
        RoommateToLoginSuccessConverter converter = new RoommateToLoginSuccessConverter();

        LoginSuccessDTO success = converter.convert(roommate);

        return ok(success);
    }

    @Transactional
    public Result login() {

        LoginDTO dto = extractDTOFromRequest(LoginDTO.class);

        Roommate roommate = roommateService.findByEmail(dto.getEmail());

        Logger.info("dto:"+dto+"/"+ErrorMessage.LOGIN_WRONG_PASSWORD_LOGIN);

        if (roommate == null || !roommateService.controlPassword(dto.getPassword(),roommate)) {
            throw new MyRuntimeException(ErrorMessage.LOGIN_WRONG_PASSWORD_LOGIN);
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

        LoginSuccessDTO result = converter.convert(securityController.getCurrentUser());

        return ok(result);
    }


}
