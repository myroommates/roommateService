package converter;

import dto.LoginSuccessDTO;
import models.entities.Roommate;

/**
 * Created by florian on 11/11/14.
 */
public class RoommateToLoginSuccessConverter implements ConverterInterface<Roommate, LoginSuccessDTO> {

    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
    private HomeToHomeConverter homeToHomeConverter = new HomeToHomeConverter();

    public LoginSuccessDTO convert(Roommate roommate) {

        LoginSuccessDTO dto = new LoginSuccessDTO();

        dto.setCurrentRoommate(roommateToRoommateDTOConverter.convert(roommate));
        //dto.setHome(homeToHomeConverter.convert(roommate.getHome()));
        dto.setAuthenticationKey(roommate.getAuthenticationKey());
/*
        for (Roommate otherRoommate : roommate.getHome().getRoommateList()) {
            dto.addRoommate(roommateToRoommateDTOConverter.convert(otherRoommate));
        }
  */      dto.addRoommate(roommateToRoommateDTOConverter.convert(roommate));

        return dto;
    }
}
