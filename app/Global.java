import controllers.rest.technical.SecurityRestController;
import dto.technical.ExceptionDTO;
import models.entities.Language;
import models.storage.TranslationStore;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.i18n.Lang;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Results;
import play.mvc.SimpleResult;
import services.TranslationService;
import services.impl.TranslationServiceImpl;
import util.exception.MyRuntimeException;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by florian on 10/11/14.
 */
public class Global extends GlobalSettings {

    //services
    private TranslationService translationService = new TranslationServiceImpl();

    public static final String[] BUNDLES = {"interfaces",};




    @Override
    public void beforeStart(Application app) {

        Logger.info("Global.beforeStart - START");

        // Put all translations in memory
        int languageCounter = 0;
        for (Lang lang : Lang.availables()) {

            Logger.warn("lang:"+Lang.availables());

            //first language = reference language
            HashMap<String, String> translationCache = new HashMap<>();
            TranslationStore.TRANSLATIONS.put(lang, translationCache);
            for (String bundleName : BUNDLES) {
                ResourceBundle bundle = ResourceBundle.getBundle( bundleName, Locale.forLanguageTag(lang.code()));
                Enumeration<String> bundleKeys = bundle.getKeys();
                while (bundleKeys.hasMoreElements()) {
                    String key = bundleKeys.nextElement();
                    String value = bundle.getString(key);
                    try {
                        value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    translationCache.put(key, value);
                }
            }

            if (languageCounter > 0) {

                //complete hole by comparison with reference language
                for (Map.Entry<String, String> reference : TranslationStore.TRANSLATIONS.get(Lang.availables().get(0)).entrySet()) {
                    if (!translationCache.containsKey(reference.getKey())) {
                        translationCache.put(reference.getKey(), reference.getValue());
                    }

                }

            }
            languageCounter++;
        }
        Logger.info("Global.beforeStart - END");
    }

    @Override
    public void onStart(Application application) {
    }

    @Override
    public F.Promise<SimpleResult> onError(Http.RequestHeader request, Throwable t) {

        //load language expected
        Language language = null;
        if (request.getHeader(SecurityRestController.REQUEST_HEADER_LANGUAGE) != null) {
            language = Language.getByAbrv(request.getHeader(SecurityRestController.REQUEST_HEADER_LANGUAGE));
        }
        language = Language.ENGLISH;

        final ExceptionDTO exceptionsDTO;

        if (t.getCause() instanceof MyRuntimeException) {
            MyRuntimeException exception = ((MyRuntimeException) t.getCause());
            String message;

            if (exception.getMessage() != null) {
                message = exception.getMessage();
            } else {
                message = translationService.getTranslation(exception.getErrorMessage(), language, exception.getParams());
            }
            Logger.error("exceptionMessage:"+message);
            exceptionsDTO = new ExceptionDTO(message);
        } else {
            exceptionsDTO = new ExceptionDTO(t.getCause().getMessage());
        }

        return F.Promise.<SimpleResult>pure(Results.internalServerError(exceptionsDTO));
    }
}
