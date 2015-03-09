package controllers.rest;

import controllers.technical.AbstractController;
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
public class EmailRestController extends AbstractController {

    //service
    private EmailService emailService = new EmailServiceImpl();
    private TranslationService translationService = new TranslationServiceImpl();

    public void sendApplicationRegistrationEmail(Roommate  roommate){

        String title = translationService.getTranslation(EmailMessage.REGISTRATION_APP_EMAIL_TITLE,lang());

        // 0 => roommate.name
        // 1 => roommate.reactionKey
        String body = translationService.getTranslation(EmailMessage.REGISTRATION_APP_EMAIL_BODY,lang(),
                roommate.getName(),
                roommate.getPassword());

        emailService.sendEmail(roommate,title,body);
    }

    public void sendInvitationEmail(Roommate  roommate,Roommate  inviter,Lang language,String password){

        String title = translationService.getTranslation(EmailMessage.INVITATION_EMAIL_TITLE,language);

        //be.roommate.app://www.myroommatesapp.com/app/start?

        String authenticationKey = roommate.getAuthenticationKey();

        String applicationKey = "be.roommate.app://www.myroommatesapp.com/app/start?"+authenticationKey;
        String siteKey = "http://www.myroommatesapp.com?authentication="+applicationKey;

        // 0 => roommate.name
        // 1 => inviter.name
        // 2=> roommate.reactionKey
        String body = translationService.getTranslation(EmailMessage.INVITATION_EMAIL_BODY,language,
                roommate.getName(),
                inviter.getName(),
                applicationKey,
                siteKey,
                password
                );

        emailService.sendEmail(roommate,title,body);
    }

    public void sendNewPasswordEmail(Roommate roommate) {

        String title = translationService.getTranslation(
                EmailMessage.NEW_PASSWORD_EMAIL_TITLE,
                roommate.getLanguage());

        // 0 => roommate.name
        // 1 => inviter.name
        // 2=> roommate.reactionKey
        String body = translationService.getTranslation(
                EmailMessage.NEW_PASSWORD_EMAIL_BODY,
                roommate.getLanguage(),
                roommate.getName(),
                roommate.getPassword());

        emailService.sendEmail(roommate,title,body);



    }
}
