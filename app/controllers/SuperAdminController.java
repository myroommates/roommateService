package controllers;

import com.avaje.ebean.annotation.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import controllers.technical.SuperAdminSecurityController;
import converter.FaqToFaqDTOConverter;
import converter.RoommateToInterfaceDataDTOConverter;
import dto.FaqDTO;
import dto.ListDTO;
import dto.post.FaqCreatorDTO;
import models.entities.Faq;
import models.entities.Translation;
import models.entities.TranslationValue;
import play.i18n.Lang;
import play.mvc.Result;
import play.mvc.Security;
import services.FaqService;
import services.impl.FaqServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by florian on 20/02/15.
 */
public class SuperAdminController extends AbstractController {

    //converter
    private FaqToFaqDTOConverter faqToFaqDTOConverter = new FaqToFaqDTOConverter();
    private RoommateToInterfaceDataDTOConverter roommateToInterfaceDataDTOConverter = new RoommateToInterfaceDataDTOConverter();
    //service
    private FaqService faqService = new FaqServiceImpl();
    private SecurityController securityController = new SecurityController();

    @Security.Authenticated(SuperAdminSecurityController.class)
    @Transactional
    public Result index() {


        List<FaqDTO> listF = new ArrayList<>();

        for (Faq faq : faqService.getAll()) {
            listF.add(faqToFaqDTOConverter.convert(faq));
        }

        List<String> langCode = new ArrayList<>();

        for (Lang lang : Lang.availables()) {
            langCode.add(lang.code());
        }


        return ok(views.html.superAdmin.faq.render(
                roommateToInterfaceDataDTOConverter.convert(securityController.getCurrentUser()),
                listF,
                langCode));
    }

    @Security.Authenticated(SuperAdminSecurityController.class)
    @Transactional
    public Result createFaq(){

        FaqCreatorDTO faqDTO = extractDTOFromRequest(FaqCreatorDTO.class);

        Translation answers = new Translation();

        Faq faq = new Faq();

        for (Map.Entry<String, String> stringStringEntry : faqDTO.getAnswers().entrySet()) {
            TranslationValue translationValue = new TranslationValue();
            translationValue.setLanguageCode(stringStringEntry.getKey());
            translationValue.setContent(stringStringEntry.getValue());

            answers.addTranslationValue(translationValue);
        }

        faq.setAnswer(answers);

        Translation question = new Translation();

        for (Map.Entry<String, String> stringStringEntry : faqDTO.getQuestions().entrySet()) {
            TranslationValue translationValue = new TranslationValue();
            translationValue.setLanguageCode(stringStringEntry.getKey());
            translationValue.setContent(stringStringEntry.getValue());

            question.addTranslationValue(translationValue);
        }

        faq.setQuestion(question);


        faqService.saveOrUpdate(faq);


        return ok(faqToFaqDTOConverter.convert(faq));
    }
}
