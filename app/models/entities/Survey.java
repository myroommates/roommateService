package models.entities;

import models.entities.technical.AbstractEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by florian on 25/02/15.
 */
@Entity
public class Survey extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String key;

    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    private Translation question;

    @Column(nullable = false)
    private Boolean isMultipleAnswer = false;

    @OneToMany(mappedBy = "survey",cascade = CascadeType.ALL)
    private List<SurveyAnswer> answers;

    public Translation getQuestion() {
        return question;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setQuestion(Translation question) {
        this.question = question;
    }

    public Boolean getIsMultipleAnswer() {
        return isMultipleAnswer;
    }

    public void setIsMultipleAnswer(Boolean isMultipleAnswer) {
        this.isMultipleAnswer = isMultipleAnswer;
    }

    public List<SurveyAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SurveyAnswer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "question=" + question +
                ", isMultipleAnswer=" + isMultipleAnswer +
                ", answers=" + answers +
                '}';
    }
}
