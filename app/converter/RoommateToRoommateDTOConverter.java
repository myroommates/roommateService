package converter;

import dto.RoommateDTO;
import entities.Roommate;

/**
 * Created by florian on 11/11/14.
 */
public class RoommateToRoommateDTOConverter {

    public RoommateDTO converter(Roommate roommate) {
        RoommateDTO dto = new RoommateDTO();

        dto.setId(roommate.getId());
        dto.setEmail(roommate.getEmail());
        dto.setName(roommate.getName());
        dto.setIconColor(roommate.getIconColor());

        return dto;
    }
}
