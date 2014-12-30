package services.impl;

import dto.TranslationsDTO;
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
    public String getTranslation(ErrorMessage errorMessage, Lang language, Object... params) {

        return Messages.get(language,errorMessage.name(),params);

    }

    @Override
    public String getTranslation(String messageRef, Lang language, Object... params) {

        return Messages.get(language,messageRef,params);

    }

    @Override
    public String getTranslation(EmailMessage emailMessage, Lang language, Object... params) {
        return Messages.get(language,emailMessage.name(),params);
    }
}
