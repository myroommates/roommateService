package controllers;

import controllers.rest.EmailRestController;
import controllers.rest.RoommateRestRestController;
import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.RoommateToRoommateDTOConverter;
import dto.RoommateDTO;
import models.RegistrationForm;
import play.data.Form;
import play.db.ebean.Transactional;
import play.mvc.Result;
import play.mvc.Security;
import services.HomeService;
import services.RoommateService;
import services.impl.HomeServiceImpl;
import services.impl.RoommateServiceImpl;

/**
 * Created by florian on 27/12/14.
 */
public class ProfileController extends AbstractController{

    //service
    private RoommateService accountService = new RoommateServiceImpl();
    private HomeService homeService = new HomeServiceImpl();
    //form

    //controller
    private HomeController homeController = new HomeController();
    private RoommateRestRestController roommateRestRestController = new RoommateRestRestController();
    private EmailRestController emailController = new EmailRestController();

    //converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();

    @Transactional
    @Security.Authenticated(SecurityController.class)
    public Result myProfile() {

        RoommateDTO account = roommateToRoommateDTOConverter.convert(securityController.getCurrentUser());

        //build form
        RegistrationForm registrationForm1  = new RegistrationForm();

        registrationForm1.setKeepSessionOpen(securityController.getCurrentUser().isKeepSessionOpen());
        registrationForm1.setEmail(securityController.getCurrentUser().getEmail());
        registrationForm1.setName(securityController.getCurrentUser().getName());
        registrationForm1.setNameAbrv(securityController.getCurrentUser().getNameAbrv());

        Form<RegistrationForm> registrationForm = Form.form(RegistrationForm.class).fill(registrationForm1);

        return ok(views.html.home.profile.myProfile.render(translationService.getTranslations(lang()),registrationForm,account));
    }
}
