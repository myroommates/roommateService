package be.flo.roommateService.dto;

import be.flo.roommateService.dto.technical.DTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 29/12/14.
 */
public class InterfaceDataDTO extends DTO{

    private RoommateDTO mySelf;

    private TranslationsDTO translations;

    private HomeDTO home;

    private String langId;

    private List<LangDTO> languages;

    public List<LangDTO> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LangDTO> languages) {
        this.languages = languages;
    }

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

    public void addLanguage(LangDTO language) {
        if(languages == null){
            languages = new ArrayList<>();
        }
        languages.add(language);
    }
}
