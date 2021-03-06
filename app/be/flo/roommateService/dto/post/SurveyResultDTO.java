package be.flo.roommateService.dto.post;

import be.flo.roommateService.dto.technical.DTO;
import be.flo.roommateService.dto.technical.verification.NotNull;

/**
 * Created by florian on 26/02/15.
 */
public class SurveyResultDTO extends DTO{

    @NotNull
    private String surveyKey;

    private Long surveyAnswerId;

    public String getSurveyKey() {
        return surveyKey;
    }

    public void setSurveyKey(String surveyKey) {
        this.surveyKey = surveyKey;
    }

    public Long getSurveyAnswerId() {
        return surveyAnswerId;
    }

    public void setSurveyAnswerId(Long surveyAnswerId) {
        this.surveyAnswerId = surveyAnswerId;
    }
}
