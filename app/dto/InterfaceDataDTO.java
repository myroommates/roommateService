package dto;

import dto.technical.DTO;

/**
 * Created by florian on 29/12/14.
 */
public class InterfaceDataDTO extends DTO{

    private RoommateDTO mySelf;

    private TranslationsDTO translations;

    private HomeDTO home;

    private String langId;

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public RoommateDTO getMySelf() {
        return mySelf;
    }

    public void setMySelf(RoommateDTO mySelf) {
        this.mySelf = mySelf;
    }

    public TranslationsDTO getTranslations() {
        return translations;
    }

    public void setTranslations(TranslationsDTO translations) {
        this.translations = translations;
    }

    public HomeDTO getHome() {
        return home;
    }

    public void setHome(HomeDTO home) {
        this.home = home;
    }
}
