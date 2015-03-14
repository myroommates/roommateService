package controllers;

import com.avaje.ebean.annotation.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.LanguageToLanguageDTOConverter;
import dto.InterfaceDataDTO;
import dto.LangDTO;
import dto.ListDTO;
import models.LoginForm;
import models.entities.Roommate;
import models.entities.Session;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.i18n.Lang;
import play.i18n.Messages;
import play.mvc.Result;
import services.RoommateService;
import services.SessionService;
import services.impl.RoommateServiceImpl;
import services.impl.SessionServiceImpl;

/**
 * Created by florian on 17/12/14.
 */
public class LoginController extends AbstractController {

    //service
    private RoommateService accountService = new RoommateServiceImpl();
    private SessionService sessionService = new SessionServiceImpl();

    //form
    //public final static Form<LoginForm> formLogin = Form.form(LoginForm.class);
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

                response().setCookie(SecurityController.COOKIE_KEEP_SESSION_OPEN, securityController.getCookieKey());
            }

            return homeController.index();
        }

        //try with param


        InterfaceDataDTO interfaceDataDTO  = new InterfaceDataDTO();
        interfaceDataDTO.setLangId(lang().code());
        interfaceDataDTO.setTranslations(translationService.getTranslations(lang()));


        return ok(views.html.welcome.render(Form.form(LoginForm.class),getAvaiableLanguage(),interfaceDataDTO));
    }

    @Transactional
    public Result loginWithAuthentication(String key){

        Roommate roommate = accountService.findByAuthenticationKey(key);

        if(roommate!=null){
            securityController.storeAccount(ctx(),roommate);
        }
        return redirect("/");
    }

    @Transactional
    public Result login() {

        Form<LoginForm> loginFormForm = Form.form(LoginForm.class).bindFromRequest();

        InterfaceDataDTO interfaceDataDTO  = new InterfaceDataDTO();

        interfaceDataDTO.setTranslations(translationService.getTranslations(lang()));

        if (loginFormForm.hasErrors()) {
            return badRequest(views.html.welcome.render(loginFormForm,getAvaiableLanguage(),interfaceDataDTO));
        }

        String email = loginFormForm.field("email").value();
        String password = loginFormForm.field("password").value();


        //login
        Roommate roommate= accountService.findByEmail(email);
        if (roommate == null || !accountService.controlPassword(password, roommate)) {
            loginFormForm.reject(new ValidationError("email", Messages.get("login.form.wrongCredential")));
            return badRequest(views.html.welcome.render(loginFormForm,getAvaiableLanguage(),interfaceDataDTO));
        }

        //edit
        if (Boolean.parseBoolean(loginFormForm.field("keepSessionOpen").value()) != roommate.isKeepSessionOpen()) {
            roommate.setKeepSessionOpen(Boolean.parseBoolean(loginFormForm.field("keepSessionOpen").value()));
            accountService.saveOrUpdate(roommate);
        }

        //store session
        securityController.storeAccount(ctx(),roommate);

        //session
        sessionService.saveOrUpdate(new Session(roommate,false));

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
