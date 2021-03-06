package be.flo.roommateService.controllers.rest;

import be.flo.roommateService.controllers.technical.AbstractController;
import be.flo.roommateService.controllers.technical.SecurityRestController;
import be.flo.roommateService.converter.RoommateToLoginSuccessConverter;
import be.flo.roommateService.dto.GoogleConnectionDTO;
import be.flo.roommateService.dto.GoogleRegistrationDTO;
import be.flo.roommateService.dto.post.ForgotPasswordDTO;
import be.flo.roommateService.dto.LoginSuccessDTO;
import be.flo.roommateService.dto.post.LoginDTO;
import be.flo.roommateService.dto.post.RegistrationDTO;
import be.flo.roommateService.dto.technical.ResultDTO;
import be.flo.roommateService.models.entities.Home;
import be.flo.roommateService.models.entities.Roommate;
import be.flo.roommateService.models.entities.Session;
import play.Logger;
import play.db.jpa.Transactional;
import play.i18n.Lang;
import play.mvc.Result;
import play.mvc.Security;
import be.flo.roommateService.services.RoommateService;
import be.flo.roommateService.services.SessionService;
import be.flo.roommateService.services.impl.RoommateServiceImpl;
import be.flo.roommateService.services.impl.SessionServiceImpl;
import be.flo.roommateService.util.ErrorMessage;
import be.flo.roommateService.util.KeyGenerator;
import be.flo.roommateService.util.exception.MyRuntimeException;
import be.flo.roommateService.util.tool.ColorGenerator;

/**
 * Created by florian on 10/11/14.
 */
public class LoginRestController extends AbstractController {

    //service
    private RoommateService roommateService = new RoommateServiceImpl();
    private SessionService sessionService = new SessionServiceImpl();

    //controller
    private EmailRestController emailController = new EmailRestController();


    @Transactional
    public Result googleConnection() {
        GoogleConnectionDTO dto = extractDTOFromRequest(GoogleConnectionDTO.class);

        Logger.info(dto+"");

        //find by email
        Roommate roommate = roommateService.findByEmail(dto.getEmail());
        if (roommate != null) {

            //test
            if(roommate.getGoogleKey()==null){
                //need a confirmation
                return unauthorized();
            }

            //connection
            roommate = connection(dto.getEmail(), null, dto.getKey());
        } else {
            //registration
            //TODO lang
            Lang lang = lang();
            roommate = createNewAccount(dto.getEmail(), dto.getName(), lang,dto.getKey());
        }

        RoommateToLoginSuccessConverter converter = new RoommateToLoginSuccessConverter();

        LoginSuccessDTO success = converter.convert(roommate);

        Logger.error(success+"");

        return ok(success);
    }

    @Transactional
    public Result registerGoogleAccount(){

        GoogleRegistrationDTO dto = extractDTOFromRequest(GoogleRegistrationDTO.class);

        //find by email
        Roommate roommate = roommateService.findByEmail(dto.getEmail());
        if (roommate == null || !roommateService.controlPassword(dto.getPassword(),roommate)) {
            throw new MyRuntimeException(ErrorMessage.LOGIN_WRONG_PASSWORD_LOGIN);
        }

        //assign google key to account
        roommate.setGoogleKey(roommateService.generateEncryptingPassword(dto.getGoogleKey()));

        roommateService.saveOrUpdate(roommate);

        RoommateToLoginSuccessConverter converter = new RoommateToLoginSuccessConverter();

        LoginSuccessDTO success = converter.convert(roommate);

        return ok(success);

    }


    @Transactional
    public Result registration() {

        RegistrationDTO dto = extractDTOFromRequest(RegistrationDTO.class);

        //Control email
        if (roommateService.findByEmail(dto.getEmail()) != null) {
            throw new MyRuntimeException(ErrorMessage.EMAIL_ALREADY_USED);
        }
        Lang lang = null;
        if (dto.getLang() != null) {
            lang = Lang.forCode(dto.getLang());
        }

        Roommate roommate = createNewAccount(dto.getEmail(), dto.getName(), lang, null);

        //result
        RoommateToLoginSuccessConverter converter = new RoommateToLoginSuccessConverter();

        LoginSuccessDTO success = converter.convert(roommate);

        return ok(success);
    }

    private Roommate createNewAccount(String email, String name, Lang lang, String googleKey) {

        //home
        Home home = new Home();
        home.setMoneySymbol(Home.DEFAULT_MONEY_SYMBOL);

        //roommate
        Roommate roommate = new Roommate();
        roommate.setEmail(email);
        roommate.setName(name);
        roommate.setHome(home);
        roommate.setIconColor(ColorGenerator.getColorWeb(0));
        if (lang != null) {
            //Lang lang = Lang.forCode(be.flo.roommateService.dto.getLang());
            roommate.setLanguage(lang);
            changeLang(lang.code());
        } else {
            roommate.setLanguage(lang());
        }
        roommate.setIsAdmin(true);

        //google key
        if(googleKey!=null){
            roommate.setGoogleKey(roommateService.generateEncryptingPassword(googleKey));
        }

        //generate password
        roommate.setPassword(KeyGenerator.generateRandomPassword());


        //send email
        try {
            emailController.sendApplicationRegistrationEmail(roommate);
        }
        catch(Exception e){}

        roommateService.saveOrUpdate(roommate);

        //session
        sessionService.saveOrUpdate(new Session(roommate, true));

        return roommate;
    }

    @Transactional
    public Result login() {

        LoginDTO dto = extractDTOFromRequest(LoginDTO.class);

        Logger.info("be.flo.roommateService.dto:" + dto + "/" + ErrorMessage.LOGIN_WRONG_PASSWORD_LOGIN);


        Roommate roommate = connection(dto.getEmail(), dto.getPassword(), null);

        //result
        RoommateToLoginSuccessConverter converter = new RoommateToLoginSuccessConverter();

        LoginSuccessDTO result = converter.convert(roommate);

        return ok(result);

    }

    private Roommate connection(String email, String password, String googleKey) {

        Roommate roommate = roommateService.findByEmail(email);

        if (roommate == null) {
            throw new MyRuntimeException(ErrorMessage.LOGIN_WRONG_PASSWORD_LOGIN);
        }

        //by password
        if (password != null) {
            if (!roommateService.controlPassword(password, roommate)) {
                throw new MyRuntimeException(ErrorMessage.LOGIN_WRONG_PASSWORD_LOGIN);
            }
        } else if (googleKey == null || !roommateService.controlAuthenticationGoogle(googleKey, roommate)) {
            throw new MyRuntimeException(ErrorMessage.LOGIN_WRONG_PASSWORD_LOGIN);
        }



        return roommate;
    }


    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result loadData() {

        //result
        RoommateToLoginSuccessConverter converter = new RoommateToLoginSuccessConverter();

        LoginSuccessDTO result = converter.convert(securityController.getCurrentUser());

        //session
        sessionService.saveOrUpdate(new Session(securityController.getCurrentUser(), true));

        return ok(result);
    }

    @Transactional
    public Result forgotPassword() {

        ForgotPasswordDTO dto = extractDTOFromRequest(ForgotPasswordDTO.class);

        //test email
        Roommate roommate = roommateService.findByEmail(dto.getEmail());

        if (roommate == null) {
            throw new MyRuntimeException(ErrorMessage.UNKNOWN_EMAIL);
        }

        //generate the new password
        roommate.setPassword(KeyGenerator.generateRandomPassword());

        //send the email
        emailController.sendNewPasswordEmail(roommate);

        //save roommate
        roommateService.saveOrUpdate(roommate);

        //return success
        return ok(new ResultDTO());


    }


}
