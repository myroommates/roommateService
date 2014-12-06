package model.storage;

import model.entities.Language;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by florian on 6/12/14.
 */
public class TranslationStorage {

    public static final String BUNDLES_LOCATION = "translation/";
    public static final String[] BUNDLES = {"message", "email"};

    private static final HashMap<String, HashMap<Language, String>> TRANSLATION_MAP = new HashMap<>();

    private static void loadingStorage() {

        for (String bundleName : BUNDLES) {
            for (Language language : Language.values()) {

                ResourceBundle bundle = ResourceBundle.getBundle(BUNDLES_LOCATION + bundleName, Locale.forLanguageTag(language.getAbrv()));
                Enumeration<String> bundleKeys = bundle.getKeys();

                while (bundleKeys.hasMoreElements()) {
                    String key = bundleKeys.nextElement();
                    String value = bundle.getString(key);
                    try {
                        value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (!TRANSLATION_MAP.containsKey(key)) {
                        TRANSLATION_MAP.put(key, new HashMap<Language, String>());
                    }
                    TRANSLATION_MAP.get(key).put(language, value);
                }


            }
        }
    }


    public static String getTranslation(String key, String language) {
        if(TRANSLATION_MAP.containsKey(key)){
            if(TRANSLATION_MAP.get(key).containsKey(language)){
                return TRANSLATION_MAP.get(key).get(language);
            }
            else{
                return TRANSLATION_MAP.get(key).get(Language.getDefaultLanguage());
            }
        }
        return "MESSAGE_NOT_FOUND : "+key;
    }
}
