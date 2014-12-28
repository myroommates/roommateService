package services;

import dto.TranslationsDTO;
import play.i18n.Lang;
import util.EmailMessage;
import util.ErrorMessage;
import models.entities.Language;
/**
 * Created by florian on 6/12/14.
 */
public interface TranslationService {

    TranslationsDTO getTranslations(Lang lang);

    String getTranslation(ErrorMessage errorMessage, Language language, Object... params);

    String getTranslation(String messageRef, Language language, Object... params);

    String getTranslation(EmailMessage emailMessage, Language language, Object... params);
}
