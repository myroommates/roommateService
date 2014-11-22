package converter;

import dto.RoommateDTO;
import entities.Roommate;

/**
 * Created by florian on 11/11/14.
 */
public class RoommateToRoommateDTOConverter {

    private HomeToHomeConverter homeToHomeConverter = new HomeToHomeConverter();

    public RoommateDTO converter(Roommate roommate) {
        RoommateDTO dto = new RoommateDTO();

        dto.setId(roommate.getId());
        dto.setEmail(roommate.getEmail());
        dto.setFirstName(roommate.getFirstName());
        dto.setLastName(roommate.getLastName());
        dto.setIconColor(roommate.getIconColor());

        return dto;
    }
}
