package controllers.rest;

import controllers.rest.technical.AbstractRestController;
import models.entities.Roommate;
import play.i18n.Lang;
import services.EmailService;
import services.TranslationService;
import services.impl.EmailServiceImpl;
import services.impl.TranslationServiceImpl;
import util.EmailMessage;

/**
 * Created by florian on 6/12/14.
 */
public class EmailRestController extends AbstractRestController {

    //service
    private EmailService emailService = new EmailServiceImpl();
    private TranslationService translationService = new TranslationServiceImpl();

    public void sendRegistrationEmail(Roommate  roommate){

        String title = translationService.getTranslation(EmailMessage.REGISTRATION_EMAIL_TITLE,roommate.getLanguage());

        // 0 => roommate.name
        // 1 => roommate.reactionKey
        String body = translationService.getTranslation(EmailMessage.REGISTRATION_EMAIL_BODY,roommate.getLanguage(),
                roommate.getName(),
                roommate.getReactivationKey());

        emailService.sendEmail(roommate,title,body);
    }

    public void sendInvitationEmail(Roommate  roommate,Roommate  inviter,Lang language){

        String title = translationService.getTranslation(EmailMessage.REGISTRATION_EMAIL_TITLE,language);

        // 0 => roommate.name
        // 1 => inviter.name
        // 2=> roommate.reactionKey
        String body = translationService.getTranslation(EmailMessage.INVITATION_EMAIL_BODY,language,
                roommate.getName(),
                inviter.getName(),
                roommate.getPassword());

        emailService.sendEmail(roommate,title,body);
    }
}
