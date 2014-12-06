package controllers;

import controllers.technical.AbstractController;
import model.entities.Language;
import model.entities.Roommate;
import services.EmailService;
import services.TranslationService;
import services.impl.EmailServiceImpl;
import services.impl.TranslationServiceImpl;
import util.EmailMessage;

/**
 * Created by florian on 6/12/14.
 */
public class EmailController extends AbstractController{

    //service
    private EmailService emailService = new EmailServiceImpl();
    private TranslationService translationService = new TranslationServiceImpl();

    /*package*/ void sendRegistrationEmail(Roommate  roommate,Language language){

        String title = translationService.getTranslation(EmailMessage.REGISTRATION_EMAIL_TITLE,language);

        // 0 => roommate.name
        // 1 => roommate.reactionKey
        String body = translationService.getTranslation(EmailMessage.REGISTRATION_EMAIL_BODY,language,
                roommate.getName(),
                roommate.getReactivationKey());

        emailService.sendEmail(roommate,title,body);
    }

    /*package*/ void sendInvitationEmail(Roommate  roommate,Roommate  inviter,Language language){

        String title = translationService.getTranslation(EmailMessage.REGISTRATION_EMAIL_TITLE,language);

        // 0 => roommate.name
        // 1 => inviter.name
        // 2=> roommate.reactionKey
        String body = translationService.getTranslation(EmailMessage.REGISTRATION_EMAIL_BODY,language,
                roommate.getName(),
                inviter.getName(),
                roommate.getReactivationKey());

        emailService.sendEmail(roommate,title,body);
    }
}
