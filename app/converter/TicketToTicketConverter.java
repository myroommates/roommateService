package converter;

import dto.TicketDTO;
import entities.Roommate;
import entities.Ticket;

/**
 * Created by florian on 11/11/14.
 */
public class TicketToTicketConverter {

    private CategoryToCategoryDTOConverter categoryToCategoryDTOConverter = new CategoryToCategoryDTOConverter();
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();

    public TicketDTO converter(Ticket ticket) {

        TicketDTO dto = new TicketDTO();

dto.setId(ticket.getId());
        dto.setDescription(ticket.getDescription());
        dto.setDate(ticket.getDate());
        dto.setValue(ticket.getValue());

        if (dto.getCategory() != null) {
            dto.setCategory(categoryToCategoryDTOConverter.convert(ticket.getCategory()));
        }

        dto.setCreator(roommateToRoommateDTOConverter.converter(ticket.getCreator()));

        for (Roommate roommate : ticket.getPrayerList()) {
            dto.addPrayer(roommateToRoommateDTOConverter.converter(roommate));
        }

        return dto;

    }
}
