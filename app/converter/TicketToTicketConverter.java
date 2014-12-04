package converter;

import dto.TicketDTO;
import entities.Roommate;
import entities.Ticket;

/**
 * Created by florian on 11/11/14.
 */
public class TicketToTicketConverter implements ConverterInterface<Ticket, TicketDTO> {

    public TicketDTO convert(Ticket ticket) {

        TicketDTO dto = new TicketDTO();

        dto.setId(ticket.getId());
        dto.setDescription(ticket.getDescription());
        dto.setDate(ticket.getDate());
        dto.setValue(ticket.getValue());

        dto.setCategory(ticket.getCategory());

        dto.setCreatorId(ticket.getCreator().getId());

        for (Roommate roommate : ticket.getPrayerList()) {
            dto.addPrayer(roommate.getId());
        }

        return dto;

    }
}
