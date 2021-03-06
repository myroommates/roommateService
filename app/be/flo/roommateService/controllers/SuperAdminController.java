package be.flo.roommateService.controllers;

import be.flo.roommateService.models.entities.*;
import be.flo.roommateService.services.*;
import play.db.jpa.Transactional;
import be.flo.roommateService.controllers.technical.AbstractController;
import be.flo.roommateService.controllers.technical.SecurityController;
import be.flo.roommateService.controllers.technical.SuperAdminSecurityController;
import be.flo.roommateService.converter.FaqToFaqDTOConverter;
import be.flo.roommateService.converter.RoommateToInterfaceDataDTOConverter;
import be.flo.roommateService.converter.SurveyToSurveyDTOConverter;
import be.flo.roommateService.dto.FaqDTO;
import be.flo.roommateService.dto.SurveyDTO;
import be.flo.roommateService.dto.post.FaqCreatorDTO;
import be.flo.roommateService.dto.SuperAdminStatusDTO;
import be.flo.roommateService.dto.post.SurveyCreatorDTO;
import play.Logger;
import play.i18n.Lang;
import play.mvc.Result;
import play.mvc.Security;
import be.flo.roommateService.services.impl.*;

import java.util.*;

/**
 * Created by florian on 20/02/15.
 */
public class SuperAdminController extends AbstractController {

    //be.flo.roommateService.converter
    private FaqToFaqDTOConverter faqToFaqDTOConverter = new FaqToFaqDTOConverter();
    private SurveyToSurveyDTOConverter surveyToSurveyDTOConverter = new SurveyToSurveyDTOConverter();
    //service
    private FaqService faqService = new FaqServiceImpl();
    private SurveyService surveyService = new SurveyServiceImpl();
    private RoommateService roommateService = new RoommateServiceImpl();
    private HomeService homeService= new HomeServiceImpl();
    private TicketService ticketService = new TicketServiceImpl();
    private ShoppingItemService shoppingItemService = new ShoppingItemServiceImpl();

    //controller
    private SecurityController securityController = new SecurityController();
    private RoommateToInterfaceDataDTOConverter roommateToInterfaceDataDTOConverter = new RoommateToInterfaceDataDTOConverter();

    @Security.Authenticated(SuperAdminSecurityController.class)
    @Transactional
    public Result index() {


        List<FaqDTO> listF = new ArrayList<>();

        for (Faq faq : faqService.findAll()) {
            listF.add(faqToFaqDTOConverter.convert(faq));
        }

        List<SurveyDTO> listSurvey = new ArrayList<>();

        for (Survey survey : surveyService.findAll()) {
            listSurvey.add(surveyToSurveyDTOConverter.convert(survey));
        }

        List<String> langCode = new ArrayList<>();

        for (Lang lang : Lang.availables()) {
            langCode.add(lang.code());
        }

        //build status data
        SuperAdminStatusDTO superAdminStatusDTO  = new SuperAdminStatusDTO();
        superAdminStatusDTO.setHomes(homeService.getCount());
        superAdminStatusDTO.setRoommates(roommateService.getCount());
        superAdminStatusDTO.setTickets(ticketService.getCount());
        superAdminStatusDTO.setShoppings(shoppingItemService.getCount());
        superAdminStatusDTO.setShoppingsTotal(shoppingItemService.getCountTotal());
        superAdminStatusDTO.setTotalSum(ticketService.getTotalSum());

        Logger.warn("superAdminStatusDTO:"+superAdminStatusDTO);


        return ok(views.html.superAdmin.faq.render(
                roommateToInterfaceDataDTOConverter.convert(securityController.getCurrentUser()),
                listF,
                listSurvey,
                langCode,
                superAdminStatusDTO));
    }

    @Security.Authenticated(SuperAdminSecurityController.class)
    @Transactional
    public Result createFaq() {

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

    @Security.Authenticated(SuperAdminSecurityController.class)
    @Transactional
    public Result createSurvey() {

        SurveyCreatorDTO dto = extractDTOFromRequest(SurveyCreatorDTO.class);

        Logger.warn("DTO:"+dto);

        Survey survey = new Survey();

        survey.setKey(dto.getSurveyKey());
        survey.setIsMultipleAnswer(dto.isMultipleAnswers());

        Translation question = new Translation();

        for (Map.Entry<String, String> stringStringEntry : dto.getQuestions().entrySet()) {
            TranslationValue translationValue = new TranslationValue();
            translationValue.setLanguageCode(stringStringEntry.getKey());
            translationValue.setContent(stringStringEntry.getValue());

            question.addTranslationValue(translationValue);
        }

        survey.setQuestion(question);

        List<SurveyAnswer> surveyAnswers = new ArrayList<>();

        for (HashMap<String, String> choices : dto.getAnswers()) {

            Translation translation = new Translation();
            for (Map.Entry<String, String> answers : choices.entrySet()) {

                TranslationValue translationValue = new TranslationValue();
                translationValue.setLanguageCode(answers.getKey());
                translationValue.setContent(answers.getValue());

                translation.addTranslationValue(translationValue);
            }
            SurveyAnswer surveyAnswer = new SurveyAnswer();
            surveyAnswer.setAnswer(translation);

            surveyAnswers.add(surveyAnswer);
        }

        survey.setAnswers(surveyAnswers);

        Logger.warn("survey:"+survey);

        surveyService.saveOrUpdate(survey);


        return ok(surveyToSurveyDTOConverter.convert(survey));


    }
}
