package controllers;

import com.avaje.ebean.annotation.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.LanguageToLanguageDTOConverter;
import dto.LangDTO;
import dto.ListDTO;
import models.LoginForm;
import models.entities.Roommate;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.i18n.Lang;
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
    private LanguageToLanguageDTOConverter languageToLanguageDTOConverter = new LanguageToLanguageDTOConverter();

    public Result changeLanguage(String lang) {
        Logger.info("lang:"+lang);
        ctx().changeLang(lang);
        return ok();
    }

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


        return ok(views.html.welcome.render(formLogin,getAvaiableLanguage()));
    }
    @Transactional
    public Result login() {

        Form<LoginForm> loginFormForm = formLogin.bindFromRequest();

        if (loginFormForm.hasErrors()) {
            return badRequest(views.html.welcome.render(loginFormForm,getAvaiableLanguage()));
        }

        String email = loginFormForm.field("email").value();
        String password = loginFormForm.field("password").value();


        //login
        Roommate roommate= accountService.findByEmail(email);
        if (roommate == null || !accountService.controlPassword(password, roommate)) {
            loginFormForm.reject(new ValidationError("email", Messages.get("login.form.wrongCredential")));
            return badRequest(views.html.welcome.render(loginFormForm,getAvaiableLanguage()));
        }

        //edit
        if (Boolean.parseBoolean(loginFormForm.field("keepSessionOpen").value()) != roommate.isKeepSessionOpen()) {
            roommate.setKeepSessionOpen(Boolean.parseBoolean(loginFormForm.field("keepSessionOpen").value()));
            accountService.saveOrUpdate(roommate);
        }

        //store session
        securityController.storeAccount(ctx(),roommate);

        //return
        return redirect("/");
    }

    private ListDTO<LangDTO> getAvaiableLanguage(){

        //compute list lang
        ListDTO<LangDTO> langDTOListDTO = new ListDTO<>();
        for (Lang lang : Lang.availables()) {
            langDTOListDTO.addElement(languageToLanguageDTOConverter.convert(lang));
        }
        return langDTOListDTO;
    }
}
