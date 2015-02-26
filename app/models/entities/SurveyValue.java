package models.entities;

import models.entities.technical.AuditedAbstractEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by florian on 25/02/15.
 */
@Entity
public class SurveyValue extends AuditedAbstractEntity {

    @ManyToOne(optional = false)
    private Roommate roommate;

    @ManyToOne(optional = false)
    private Survey survey;

    @ManyToOne
    private SurveyAnswer surveyAnswer;

    @Column(columnDefinition = "boolean not null default false", nullable = false)
    private boolean wasSubmit = false;

    public Roommate getRoommate() {
        return roommate;
    }

    public void setRoommate(Roommate roommate) {
        this.roommate = roommate;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public SurveyAnswer getSurveyAnswer() {
        return surveyAnswer;
    }

    public void setSurveyAnswer(SurveyAnswer surveyAnswer) {
        this.surveyAnswer = surveyAnswer;
    }


    public boolean wasSubmit() {
        return wasSubmit;
    }

    public void setWasSubmit(boolean wasSubmit) {
        this.wasSubmit = wasSubmit;
    }


    @Override
    public String toString() {
        return "SurveyValue{" +
                "roommate=" + roommate +
                ", survey=" + survey +
                ", surveyAnswer=" + surveyAnswer +
                '}';
    }
}
