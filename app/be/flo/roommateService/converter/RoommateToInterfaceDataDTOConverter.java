package be.flo.roommateService.converter;

import be.flo.roommateService.dto.InterfaceDataDTO;
import be.flo.roommateService.models.entities.Roommate;
import play.i18n.Lang;
import be.flo.roommateService.services.TranslationService;
import be.flo.roommateService.services.impl.TranslationServiceImpl;

/**
 * Created by florian on 29/12/14.
 */
public class RoommateToInterfaceDataDTOConverter implements ConverterInterface<Roommate, InterfaceDataDTO>{

    //service
    private TranslationService translationService = new TranslationServiceImpl();

    //be.flo.roommateService.converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
    private LanguageToLanguageDTOConverter languageToLanguageDTOConverter = new LanguageToLanguageDTOConverter();

    @Override
    public InterfaceDataDTO convert(Roommate entity) {

        InterfaceDataDTO dto = new InterfaceDataDTO();

        for (Lang lang : Lang.availables()) {
            dto.addLanguage(languageToLanguageDTOConverter.convert(lang));
        }


        dto.setTranslations(translationService.getTranslations(entity.getLanguage()));

        dto.setMySelf(roommateToRoommateDTOConverter.convert(entity));

        dto.setLangId(entity.getLanguage().code());

        HomeToHomeConverter homeToHomeConverter = new HomeToHomeConverter(entity);

        dto.setHome(homeToHomeConverter.convert(entity.getHome()));

        return dto;
    }
}
