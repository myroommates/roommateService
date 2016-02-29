package be.flo.roommateService.controllers;

import be.flo.roommateService.controllers.rest.EmailRestController;
import be.flo.roommateService.controllers.rest.RoommateRestController;
import be.flo.roommateService.controllers.technical.AbstractController;
import be.flo.roommateService.controllers.technical.SecurityController;
import be.flo.roommateService.converter.RoommateToInterfaceDataDTOConverter;
import be.flo.roommateService.converter.RoommateToRoommateDTOConverter;
import be.flo.roommateService.dto.RoommateDTO;
import be.flo.roommateService.models.RegistrationForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Result;
import play.mvc.Security;
import be.flo.roommateService.services.HomeService;
import be.flo.roommateService.services.RoommateService;
import be.flo.roommateService.services.impl.HomeServiceImpl;
import be.flo.roommateService.services.impl.RoommateServiceImpl;

/**
 * Created by florian on 27/12/14.
 */
public class ProfileController extends AbstractController {

    //service
    private RoommateService accountService = new RoommateServiceImpl();
    private HomeService homeService = new HomeServiceImpl();

    //be.flo.roommateService.converter
    private RoommateToInterfaceDataDTOConverter roommateToInterfaceDataDTOConverter = new RoommateToInterfaceDataDTOConverter();

    //controller
    private HomeController homeController = new HomeController();
    private RoommateRestController roommateRestController = new RoommateRestController();
    private EmailRestController emailController = new EmailRestController();

    //be.flo.roommateService.converter
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

        return ok(views.html.home.profile.myProfile.render(roommateToInterfaceDataDTOConverter.convert(securityController.getCurrentUser()),registrationForm));
    }
}
