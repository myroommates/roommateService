package models.storage;

import play.i18n.Lang;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by florian on 28/12/14.
 */
public class TranslationStore {
    //first key : language
    //second key : message key
    //value : translatable message
    public static final Map<Lang, Map<String, String>> TRANSLATIONS = new HashMap<>();
}
