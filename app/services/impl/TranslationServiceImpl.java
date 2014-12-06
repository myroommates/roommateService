package services.impl;

import model.entities.Language;
import model.storage.TranslationStorage;
import services.TranslationService;
import util.EmailMessage;
import util.ErrorMessage;

/**
 * Created by florian on 11/11/14.
 */
public class TranslationServiceImpl implements TranslationService {

    @Override
    public String getTranslation(ErrorMessage errorMessage, Language language, Object... params) {
        String translateMessage = TranslationStorage.getTranslation(errorMessage.name(), language.getAbrv());

        //replace
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                translateMessage = translateMessage.replace("{" + i + "}", param.toString());
            }

        }
        return translateMessage;
    }

    @Override
    public String getTranslation(EmailMessage emailMessage, Language language, Object... params) {
        String translateMessage = TranslationStorage.getTranslation(emailMessage.name(), language.getAbrv());

        //replace
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                translateMessage = translateMessage.replace("{" + i + "}", param.toString());
            }

        }
        return translateMessage;
    }
}
