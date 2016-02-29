package controllers.rest;

import play.db.jpa.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityRestController;
import dto.post.SurveyResultDTO;
import dto.technical.ResultDTO;
import models.entities.SurveyValue;
import play.Logger;
import play.mvc.Result;
import play.mvc.Security;
import services.SurveyAnswerService;
import services.SurveyValueService;
import services.impl.SurveyAnswerServiceImpl;
import services.impl.SurveyValueServiceImpl;

/**
 * Created by florian on 26/02/15.
 */
public class SurveyRestController extends AbstractController {

    //servie
    private SurveyAnswerService surveyAnswerService = new SurveyAnswerServiceImpl();
    private SurveyValueService surveyValueService = new SurveyValueServiceImpl();

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result answer() {
        SurveyResultDTO dto = extractDTOFromRequest(SurveyResultDTO.class);

        //load surveyValue
        for (SurveyValue surveyValue : securityController.getCurrentUser().getSurveyValues()) {
            if (surveyValue.getSurvey().getKey().equals(dto.getSurveyKey()) && !surveyValue.wasSubmit()) {
                surveyValue.setSurveyAnswer(surveyAnswerService.findById(dto.getSurveyAnswerId()));
                surveyValue.setWasSubmit(true);

                surveyValueService.saveOrUpdate(surveyValue);
                break;
            }
        }

        return ok(new ResultDTO());
    }
}
