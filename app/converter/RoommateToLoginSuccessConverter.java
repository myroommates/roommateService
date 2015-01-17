package converter;

import dto.LoginSuccessDTO;
import models.entities.Roommate;
import models.entities.ShoppingItem;
import models.entities.Ticket;

/**
 * Created by florian on 11/11/14.
 */
public class RoommateToLoginSuccessConverter implements ConverterInterface<Roommate, LoginSuccessDTO> {

    //converter
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
    private HomeToHomeConverter homeToHomeConverter = new HomeToHomeConverter();
    private TicketToTicketConverter ticketToTicketConverter = new TicketToTicketConverter();
    private ShoppingItemToShoppingItemDTOConverter shoppingItemToShoppingItemDTOConverter = new ShoppingItemToShoppingItemDTOConverter();

    public LoginSuccessDTO convert(Roommate roommate) {

        LoginSuccessDTO dto = new LoginSuccessDTO();

        dto.setCurrentRoommate(roommateToRoommateDTOConverter.convert(roommate));
        dto.setHome(homeToHomeConverter.convert(roommate.getHome()));
        dto.setAuthenticationKey(roommate.getAuthenticationKey());

        //convert other roommate
        for (Roommate otherRoommate : roommate.getHome().getRoommateList()) {
            dto.addRoommate(roommateToRoommateDTOConverter.convert(otherRoommate));
        }

        //convert ticket
        for (Ticket ticket : roommate.getHome().getTickets()) {
            dto.addTicket(ticketToTicketConverter.convert(ticket));
        }

        //convert shopping items
        for (ShoppingItem shoppingItem : roommate.getHome().getShoppingItems()) {
            dto.addShoppingItem(shoppingItemToShoppingItemDTOConverter.convert(shoppingItem));
        }


        dto.addRoommate(roommateToRoommateDTOConverter.convert(roommate));

        return dto;
    }
}
