package models.entities;

import models.entities.technical.AuditedAbstractEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by florian on 25/02/15.
 */
@Entity
public class SurveyAnswer extends AuditedAbstractEntity {


    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    private Translation answer;

    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    private Survey survey;

    public SurveyAnswer() {
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Translation getAnswer() {
        return answer;
    }

    public void setAnswer(Translation answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "SurveyAnswer{" +
                "answer=" + answer +
                '}';
    }
}