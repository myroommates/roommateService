package services.impl;

import models.entities.Language;
import play.Logger;
import play.i18n.Messages;
import services.TranslationService;
import util.EmailMessage;
import util.ErrorMessage;

/**
 * Created by florian on 11/11/14.
 */
public class TranslationServiceImpl implements TranslationService {

    @Override
    public String getTranslation(ErrorMessage errorMessage, Language language, Object... params) {

        return Messages.get(errorMessage.name(),language.getAbrv(),params);

    }

    @Override
    public String getTranslation(String messageRef, Language language, Object... params) {

        return Messages.get(messageRef,language.getAbrv(),params);

    }

    @Override
    public String getTranslation(EmailMessage emailMessage, Language language, Object... params) {
        return Messages.get(emailMessage.name(),language.getAbrv(),params);
    }
}
