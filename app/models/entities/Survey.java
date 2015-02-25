package models.entities;

import models.entities.technical.AuditedAbstractEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by florian on 25/02/15.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Survey.FIND_BY_KEY, query = "where " + Survey.COL_KEY + " = :" + Survey.PARAM_KEY)
})
public class Survey extends AuditedAbstractEntity {

    //request
    public static final String FIND_BY_KEY = "Survey_FIND_BY_KEY";

    //param
    public static final String PARAM_KEY = "key";

    //column
    public static final String COL_KEY = "key";

    @Column(nullable = false, name = COL_KEY, unique = true)
    private String key;

    @ManyToOne(optional = false)
    private Translation question;

    @Column(nullable = false)
    private Boolean isMultipleAnswer = false;

    @OneToMany(mappedBy = "survey")
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
