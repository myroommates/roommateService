package converter;

import controllers.technical.CommonSecurityController;
import dto.InterfaceDataDTO;
import models.entities.Roommate;
import play.Logger;
import play.i18n.Lang;
import services.TranslationService;
import services.impl.TranslationServiceImpl;

/**
 * Created by florian on 29/12/14.
 */
public class RoommateToInterfaceDataDTOConverter implements ConverterInterface<Roommate, InterfaceDataDTO>{

    private CommonSecurityController securityController;

    //service
    private TranslationService translationService = new TranslationServiceImpl();

    //converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
    private HomeToHomeConverter homeToHomeConverter = new HomeToHomeConverter(securityController);
    private LanguageToLanguageDTOConverter languageToLanguageDTOConverter = new LanguageToLanguageDTOConverter();

    public RoommateToInterfaceDataDTOConverter(CommonSecurityController securityController) {
        this.securityController = securityController;
    }

    @Override
    public InterfaceDataDTO convert(Roommate entity) {

        InterfaceDataDTO dto = new InterfaceDataDTO();

        for (Lang lang : Lang.availables()) {
            dto.addLanguage(languageToLanguageDTOConverter.convert(lang));
        }


        dto.setTranslations(translationService.getTranslations(entity.getLanguage()));

        dto.setMySelf(roommateToRoommateDTOConverter.convert(entity));

        dto.setHome(homeToHomeConverter.convert(entity.getHome()));

        dto.setLangId(entity.getLanguage().code());

        return dto;
    }
}
