package be.flo.roommateService.controllers;

import be.flo.roommateService.controllers.rest.RoommateRestController;
import play.db.jpa.Transactional;
import be.flo.roommateService.controllers.rest.EmailRestController;
import be.flo.roommateService.controllers.technical.AbstractController;
import be.flo.roommateService.models.RegistrationForm;
import be.flo.roommateService.models.entities.Home;
import be.flo.roommateService.models.entities.Roommate;
import be.flo.roommateService.models.entities.Session;
import play.data.Form;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.mvc.Result;
import be.flo.roommateService.services.HomeService;
import be.flo.roommateService.services.RoommateService;
import be.flo.roommateService.services.SessionService;
import be.flo.roommateService.services.impl.HomeServiceImpl;
import be.flo.roommateService.services.impl.RoommateServiceImpl;
import be.flo.roommateService.services.impl.SessionServiceImpl;
import be.flo.roommateService.util.tool.ColorGenerator;

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
