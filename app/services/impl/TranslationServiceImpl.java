package services.impl;

import dto.TranslationsDTO;
import models.entities.Language;
import play.Logger;
import play.api.Play;
import play.api.i18n.MessagesPlugin;
import play.i18n.Lang;
import play.i18n.Messages;
import scala.Option;
import scala.collection.JavaConverters;
import scala.collection.immutable.Map;
import services.TranslationService;
import util.EmailMessage;
import util.ErrorMessage;

import java.util.Collections;
import java.util.TreeMap;

/**
 * Created by florian on 11/11/14.
 */
public class TranslationServiceImpl implements TranslationService {

    @Override
    public TranslationsDTO getTranslations(Lang lang){

        Option<Map<String, String>> mapOption = Play.current().plugin(MessagesPlugin.class).get().api().messages().get(lang.code());

        //convert
        java.util.Map<String, String> mapAsJava = JavaConverters.mapAsJavaMapConverter(mapOption.get()).asJava();

        return new TranslationsDTO(mapAsJava);
    }


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
