package converter;

import dto.TicketDTO;
import dto.TicketDebtorDTO;
import model.entities.Ticket;
import model.entities.TicketDebtor;

/**
 * Created by florian on 11/11/14.
 */
public class TicketToTicketConverter implements ConverterInterface<Ticket, TicketDTO> {

    public TicketDTO convert(Ticket ticket) {

        TicketDTO dto = new TicketDTO();

        dto.setId(ticket.getId());
        dto.setDescription(ticket.getDescription());
        dto.setDate(ticket.getDate());

        dto.setCategory(ticket.getCategory());

        dto.setPrayerId(ticket.getPayer().getId());

        for (TicketDebtor ticketDebtor : ticket.getDebtorList()) {
            dto.addTicketDebtor(new TicketDebtorDTO(ticketDebtor.getRoommate().getId(),ticketDebtor.getValue()));
        }

        return dto;

    }
}
