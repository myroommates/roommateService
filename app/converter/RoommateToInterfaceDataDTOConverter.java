package converter;

import dto.InterfaceDataDTO;
import models.entities.Roommate;
import services.TranslationService;
import services.impl.TranslationServiceImpl;

/**
 * Created by florian on 29/12/14.
 */
public class RoommateToInterfaceDataDTOConverter implements ConverterInterface<Roommate, InterfaceDataDTO>{
    
    //service
    private TranslationService translationService = new TranslationServiceImpl();

    //converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
    private HomeToHomeConverter homeToHomeConverter = new HomeToHomeConverter();


    @Override
    public InterfaceDataDTO convert(Roommate entity) {

        InterfaceDataDTO dto = new InterfaceDataDTO();

        dto.setTranslations(translationService.getTranslations(entity.getLanguage()));

        dto.setMySelf(roommateToRoommateDTOConverter.convert(entity));

        dto.setHome(homeToHomeConverter.convert(entity.getHome()));

        dto.setLangId(entity.getLanguage().code());

        return dto;
    }
}
