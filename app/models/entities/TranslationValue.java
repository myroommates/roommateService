package models.entities;

import models.entities.technical.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by florian on 19/02/15.
 */
@Entity
public class TranslationValue extends AbstractEntity {

    @ManyToOne
    private Translation translation;

    @Column(nullable = false)
    private String languageCode;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    public TranslationValue() {
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Translation{" +
                "languageCode='" + languageCode + '\'' +
                ", content='" + content + '\'' +
                '}';
    }


}
