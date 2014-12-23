package services;

import util.EmailMessage;
import util.ErrorMessage;
import models.entities.Language;
/**
 * Created by florian on 6/12/14.
 */
public interface TranslationService {

    String getTranslation(ErrorMessage errorMessage, Language language, Object... params);

    String getTranslation(EmailMessage emailMessage, Language language, Object... params);
}
