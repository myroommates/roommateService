package converter;

import dto.LoginSuccessDTO;
import entities.Roommate;
import entities.Ticket;

/**
 * Created by florian on 11/11/14.
 */
public class RoommateToLoginSuccessConverter {

    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
    private HomeToHomeConverter homeToHomeConverter = new HomeToHomeConverter();
    private TicketToTicketConverter ticketToTicketConverter = new TicketToTicketConverter();

    public LoginSuccessDTO convert(Roommate roommate) {

        LoginSuccessDTO dto = new LoginSuccessDTO();

        dto.setCurrentRoommate(roommateToRoommateDTOConverter.converter(roommate));
        dto.setHome(homeToHomeConverter.converter(roommate.getHome()));
        dto.setAuthenticationKey(roommate.getAuthenticationKey());

        for (Roommate otherRoommate : roommate.getHome().getRoommateList()) {
            dto.addRoommate(roommateToRoommateDTOConverter.converter(otherRoommate));
        }
        dto.addRoommate(roommateToRoommateDTOConverter.converter(roommate));

        for (Ticket ticket : roommate.getHome().getTickets()) {
            dto.addTicket(ticketToTicketConverter.converter(ticket));
        }

        return dto;
    }
}
