package controllers;

import play.db.jpa.Transactional;
import controllers.rest.EmailRestController;
import controllers.rest.RoommateRestController;
import controllers.technical.AbstractController;
import models.RegistrationForm;
import models.entities.Home;
import models.entities.Roommate;
import models.entities.Session;
import play.data.Form;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.mvc.Result;
import services.HomeService;
import services.RoommateService;
import services.SessionService;
import services.impl.HomeServiceImpl;
import services.impl.RoommateServiceImpl;
import services.impl.SessionServiceImpl;
import util.tool.ColorGenerator;

/**
 * Created by florian on 18/12/14.
 */
public class RegistrationController extends AbstractController {

    //service
    private RoommateService accountService = new RoommateServiceImpl();
    private HomeService homeService = new HomeServiceImpl();
    private SessionService sessionService = new SessionServiceImpl();
    //form
    private Form<RegistrationForm> registrationForm = Form.form(RegistrationForm.class);
    //controller
    private HomeController homeController = new HomeController();
    private RoommateRestController roommateRestController = new RoommateRestController();
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
        home.setMoneySymbol(Home.DEFAULT_MONEY_SYMBOL);

        //roommate
        roommate = new Roommate();
        roommate.setName(registrationForm.field("name").value());
        roommate.setNameAbrv(registrationForm.field("nameAbrv").value());
        roommate.setEmail(registrationForm.field("email").value());
        roommate.setPassword(registrationForm.field("password").value());
        roommate.setKeepSessionOpen(Boolean.parseBoolean(registrationForm.field("keepSessionOpen").value()));
        roommate.setLanguage(lang());
        roommate.setHome(home);
        roommate.setIsAdmin(true);
        roommate.setIconColor(ColorGenerator.getColorWeb(0));

        //send email
        emailController.sendApplicationRegistrationEmail(roommate);

        //save
        accountService.saveOrUpdate(roommate);

        //Store
        securityController.storeAccount(ctx(),roommate);

        //session
        sessionService.saveOrUpdate(new Session(roommate,true));

        //return
        return redirect("/admin/roommate_list");
    }
}
