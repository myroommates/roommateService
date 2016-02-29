package be.flo.roommateService.converter;

import be.flo.roommateService.dto.SurveyDTO;
import be.flo.roommateService.models.entities.Survey;
import be.flo.roommateService.models.entities.SurveyAnswer;
import play.mvc.Http;
import be.flo.roommateService.services.TranslationService;
import be.flo.roommateService.services.impl.TranslationServiceImpl;

import java.util.HashMap;

/**
 * Created by florian on 25/02/15.
 */
public class SurveyToSurveyDTOConverter implements ConverterInterface<Survey, SurveyDTO> {


    //service
    private TranslationService translationService = new TranslationServiceImpl();

    @Override
    public SurveyDTO convert(Survey entity) {
        SurveyDTO dto = new SurveyDTO();

        dto.setSurveyKey(entity.getKey());
        dto.setMultipleAnswers(entity.getIsMultipleAnswer());
        dto.setQuestion(translationService.getTranslation(entity.getQuestion(), Http.Context.current().lang()));

        HashMap<Long, String> answers = new HashMap<>();

        for (SurveyAnswer surveyAnswer : entity.getAnswers()) {
            answers.put(surveyAnswer.getId(), translationService.getTranslation(surveyAnswer.getAnswer(), Http.Context.current().lang()));
        }

        dto.setAnswers(answers);


        return dto;
    }
}
