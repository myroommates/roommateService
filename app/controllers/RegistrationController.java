package controllers;

import com.avaje.ebean.annotation.Transactional;
import controllers.rest.EmailRestController;
import controllers.rest.RoommateRestRestController;
import controllers.technical.AbstractController;
import models.RegistrationForm;
import models.entities.Home;
import models.entities.Roommate;
import play.data.Form;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.mvc.Result;
import services.HomeService;
import services.RoommateService;
import services.impl.HomeServiceImpl;
import services.impl.RoommateServiceImpl;
import util.tool.ColorGenerator;

/**
 * Created by florian on 18/12/14.
 */
public class RegistrationController extends AbstractController {

    //service
    private RoommateService accountService = new RoommateServiceImpl();
    private HomeService homeService = new HomeServiceImpl();
    //form
    private Form<RegistrationForm> registrationForm = Form.form(RegistrationForm.class);
    //controller
    private HomeController homeController = new HomeController();
    private RoommateRestRestController roommateRestRestController = new RoommateRestRestController();
    private EmailRestController emailController = new EmailRestController();

    /**
     * Get request => return the registration page
     *
     * @return
     */
    @Transactional
    public Result registrationPage() {
        if (securityController.isAuthenticated(ctx())) {
            return homeController.index();
        }
        return ok(views.html.authentication.registration.render(registrationForm));
    }

    @Transactional
    public Result registration() {

        Form<RegistrationForm> registrationForm = this.registrationForm.bindFromRequest();

        if (registrationForm.hasErrors()) {
            return badRequest(views.html.authentication.registration.render(registrationForm));
        }

        //control email unicity
        Roommate roommate = accountService.findByEmail(registrationForm.field("email").value());
        if (roommate != null) {
            registrationForm.reject(new ValidationError("email", Messages.get("registration.form.email.alreadyExists")));
            return badRequest(views.html.authentication.registration.render(registrationForm));
        }

        //create home

        //Create roommate

        Home home = new Home();

        //roommate
        roommate = new Roommate();
        roommate.setName(registrationForm.field("name").value());
        roommate.setNameAbrv(registrationForm.field("nameAbrv").value());
        roommate.setEmail(registrationForm.field("email").value());
        roommate.setPassword(registrationForm.field("password").value());
        roommate.setKeepSessionOpen(Boolean.parseBoolean(registrationForm.field("keepSessionOpen").value()));
        roommate.setLanguage(securityController.getCurrentLanguage(ctx()));
        roommate.setHome(home);
        roommate.setIsAdmin(true);
        roommate.setIconColor(ColorGenerator.getColorWeb(0));

        //send email TODO
        //emailController.sendRegistrationEmail(roommate, securityController.getCurrentLanguage(ctx()));

        //save
        accountService.saveOrUpdate(roommate);

        //Store
        securityController.storeAccount(roommate);

        //return
        return redirect("/");
    }
}
