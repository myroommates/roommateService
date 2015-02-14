package converter;

import dto.RoommateDTO;
import models.entities.Roommate;

/**
 * Created by florian on 11/11/14.
 */
public class RoommateToRoommateDTOConverter implements ConverterInterface<Roommate,RoommateDTO>{

    public RoommateDTO convert(Roommate roommate) {
        RoommateDTO dto = new RoommateDTO();

        dto.setId(roommate.getId());
        dto.setEmail(roommate.getEmail());
        dto.setName(roommate.getName());
        dto.setIconColor(roommate.getIconColor());
        dto.setNameAbrv(roommate.getNameAbrv());
        dto.setAdmin(roommate.getIsAdmin());
        dto.setKeepSessionOpen(roommate.isKeepSessionOpen());
        dto.setLanguageCode(roommate.getLanguage().code());

        return dto;
    }
}
