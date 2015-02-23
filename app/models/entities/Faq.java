package models.entities;

import models.entities.technical.AuditedAbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by florian on 19/02/15.
 */
@Entity
public class Faq extends AuditedAbstractEntity {

    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    private Translation question;

    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    private Translation answer;

    public Faq() {
    }

    public Translation getQuestion() {
        return question;
    }

    public void setQuestion(Translation question) {
        this.question = question;
    }

    public Translation getAnswer() {
        return answer;
    }

    public void setAnswer(Translation answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Faq{" +
                "answer='" + answer + '\'' +
                ", question='" + question + '\'' +
                '}';
    }
}
