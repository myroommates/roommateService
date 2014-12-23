package models.entities;

/**
 * Created by florian on 6/12/14.
 */
public enum Language {
    FRANCAIS("fr"),
    ENGLISH("en");

    private final String abrv;

    Language(String abrv) {
        this.abrv = abrv;
    }

    public String getAbrv() {
        return abrv;
    }

    public static Language getByAbrv(String abrv) {
        for (Language language : values()) {
            if (language.abrv.equals(abrv)) {
                return language;
            }
        }
        return null;
    }

    public static Language getDefaultLanguage() {
        return Language.ENGLISH;
    }
}
