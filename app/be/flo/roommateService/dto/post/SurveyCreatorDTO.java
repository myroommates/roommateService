package be.flo.roommateService.dto.post;

import be.flo.roommateService.dto.technical.DTO;

import java.util.HashMap;
import java.util.List;

/**
 * Created by florian on 27/12/14.
 */
public class SurveyCreatorDTO extends DTO {

    private HashMap<String,String> questions;

    private String surveyKey;

    private List<HashMap<String,String>> answers;

    private Boolean multipleAnswers;

    public SurveyCreatorDTO() {
    }

    public String getSurveyKey() {
        return surveyKey;
    }

    public void setSurveyKey(String surveyKey) {
        this.surveyKey = surveyKey;
    }


    public HashMap<String, String> getQuestions() {
        return questions;
    }

    public void setQuestions(HashMap<String, String> questions) {
        this.questions = questions;
    }

    public List<HashMap<String, String>> getAnswers() {
        return answers;
    }

    public void setAnswers(List<HashMap<String, String>> answers) {
        this.answers = answers;
    }

    public boolean isMultipleAnswers() {
        return multipleAnswers;
    }

    public Boolean getMultipleAnswers() {
        return multipleAnswers;
    }

    public void setMultipleAnswers(boolean multipleAnswers) {
        this.multipleAnswers = multipleAnswers;
    }

    @Override
    public String toString() {
        return "SurveyCreatorDTO{" +
                "questions=" + questions +
                ", surveyKey='" + surveyKey + '\'' +
                ", answers=" + answers +
                ", multipleAnswers=" + multipleAnswers +
                '}';
    }
}
