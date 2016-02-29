package be.flo.roommateService.dto.post;

import be.flo.roommateService.dto.technical.DTO;

import java.util.HashMap;

/**
 * Created by florian on 27/12/14.
 */
public class FaqCreatorDTO extends DTO {

    private HashMap<String,String> questions;

    private HashMap<String,String> answers;

    public FaqCreatorDTO() {
    }

    public HashMap<String, String> getQuestions() {
        return questions;
    }

    public void setQuestions(HashMap<String, String> questions) {
        this.questions = questions;
    }

    public HashMap<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(HashMap<String, String> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "FaqCreatorDTO{" +
                "questions=" + questions +
                ", answers=" + answers +
                '}';
    }
}
