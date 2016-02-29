package be.flo.roommateService.converter;

import be.flo.roommateService.dto.FaqDTO;
import be.flo.roommateService.models.entities.Faq;
import play.mvc.Http;
import be.flo.roommateService.services.TranslationService;
import be.flo.roommateService.services.impl.TranslationServiceImpl;

/**
 * Created by florian on 19/02/15.
 */
public class FaqToFaqDTOConverter implements ConverterInterface<Faq, FaqDTO> {

    //service
    private TranslationService translationService = new TranslationServiceImpl();

    @Override
    public FaqDTO convert(Faq entity) {
        FaqDTO dto = new FaqDTO();
        dto.setAnswer(translationService.getTranslation(entity.getAnswer(),Http.Context.current().lang()));
        dto.setQuestion(translationService.getTranslation(entity.getQuestion(), Http.Context.current().lang()));

        return dto;
    }
}
