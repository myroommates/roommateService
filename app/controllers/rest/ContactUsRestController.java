package controllers.rest;

import controllers.technical.AbstractController;
import controllers.technical.SecurityRestController;
import dto.HomeDTO;
import dto.post.ContactUsDTO;
import dto.technical.ResultDTO;
import models.entities.Home;
import models.entities.Roommate;
import play.Configuration;
import play.i18n.Lang;
import play.mvc.Result;
import play.mvc.Security;
import services.EmailService;
import services.TranslationService;
import services.impl.EmailServiceImpl;
import services.impl.TranslationServiceImpl;
import util.EmailMessage;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

/**
 * Created by florian on 6/12/14.
 */
public class ContactUsRestController extends AbstractController {

    //service
    private EmailService emailService = new EmailServiceImpl();

    final String contactEmail = Configuration.root().getString("mail.default.contact");

    @Security.Authenticated(SecurityRestController.class)
    @com.avaje.ebean.annotation.Transactional
    public Result contactUs() {

        ContactUsDTO dto = extractDTOFromRequest(ContactUsDTO.class);

        String content = "";

        content += "Subject:" + dto.getSubject() + "\n\ncontent:" + dto.getContent();

        emailService.sendEmail(contactEmail, "Roommate:contact", content);

        return ok(new ResultDTO());
    }
}
