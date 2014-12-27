package controllers;

import com.avaje.ebean.annotation.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import models.LoginForm;
import models.entities.Roommate;
import play.data.Form;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.mvc.Result;
import services.RoommateService;
import services.impl.RoommateServiceImpl;
import util.KeyGenerator;

/**
 * Created by florian on 17/12/14.
 */
public class AccountController extends AbstractController {

    //service
    private RoommateService accountService = new RoommateServiceImpl();
    //form
    public final static Form<LoginForm> formLogin = Form.form(LoginForm.class);
    //controller
    private HomeController homeController = new HomeController();

    public Result logout() {
        securityController.logout(ctx());
        return redirect("/");
    }
    @Transactional
    public Result loginPage() {

        // authenticated

        if (securityController.isAuthenticated(ctx())) {

            if (request().cookie(SecurityController.COOKIE_KEEP_SESSION_OPEN) == null) {

                //generate key
                String key = KeyGenerator.generateRandomKey(40);

                Roommate roommate = securityController.getCurrentUser();

                //add id
                String cookieValue = roommate.getId() + ":" + key;

                roommate.setCookieValue(key);

                accountService.saveOrUpdate(roommate);

                response().setCookie(SecurityController.COOKIE_KEEP_SESSION_OPEN, cookieValue);
            }

            return homeController.index();
        }
        return ok(views.html.welcome.render(formLogin));
    }
    @Transactional
    public Result login() {

        Form<LoginForm> loginFormForm = formLogin.bindFromRequest();

        if (loginFormForm.hasErrors()) {
            return badRequest(views.html.welcome.render(loginFormForm));
        }

        String email = loginFormForm.field("email").value();
        String password = loginFormForm.field("password").value();


        //login
        Roommate roommate= accountService.findByEmail(email);
        if (roommate == null || !accountService.controlPassword(password, roommate)) {
            loginFormForm.reject(new ValidationError("email", Messages.get("login.form.wrongCredential")));
            return badRequest(views.html.welcome.render(loginFormForm));
        }

        //edit
        if (Boolean.parseBoolean(loginFormForm.field("keepSessionOpen").value()) != roommate.isKeepSessionOpen()) {
            roommate.setKeepSessionOpen(Boolean.parseBoolean(loginFormForm.field("keepSessionOpen").value()));
            accountService.saveOrUpdate(roommate);
        }

        //store session
        securityController.storeAccount(roommate);

        //return
        return redirect("/");
    }
}
