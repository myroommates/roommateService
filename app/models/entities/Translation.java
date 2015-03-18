package models.entities;

import models.entities.technical.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 19/02/15.
 */
@Entity
public class Translation extends AbstractEntity {

    @OneToMany(mappedBy = "translation",cascade = CascadeType.ALL)
    private List<TranslationValue> translationValues;

    public List<TranslationValue> getTranslationValues() {
        return translationValues;
    }

    public void setTranslationValues(List<TranslationValue> translationValues) {
        this.translationValues = translationValues;
    }

    @Override
    public String toString() {
        return "Translation{" +
                "translationValues=" + translationValues +
                '}';
    }

    public void addTranslationValue(TranslationValue translationValue) {
        if(translationValues==null){
            translationValues=new ArrayList<>();
        }
        translationValues.add(translationValue);
    }
}
