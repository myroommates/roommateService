package be.flo.roommateService.controllers.rest;

import be.flo.roommateService.controllers.technical.AbstractController;
import play.db.jpa.Transactional;
import be.flo.roommateService.controllers.technical.SecurityRestController;
import be.flo.roommateService.dto.post.SurveyResultDTO;
import be.flo.roommateService.dto.technical.ResultDTO;
import be.flo.roommateService.models.entities.SurveyValue;
import play.mvc.Result;
import play.mvc.Security;
import be.flo.roommateService.services.SurveyAnswerService;
import be.flo.roommateService.services.SurveyValueService;
import be.flo.roommateService.services.impl.SurveyAnswerServiceImpl;
import be.flo.roommateService.services.impl.SurveyValueServiceImpl;

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
