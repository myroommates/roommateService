package be.flo.roommateService.services;

import be.flo.roommateService.models.entities.Roommate;
import be.flo.roommateService.models.entities.Ticket;
import be.flo.roommateService.models.entities.TicketDebtor;

import java.util.List;

/**
 * Created by florian on 25/12/14.
 */
public interface TicketDebtorService extends CrudService<TicketDebtor>{

    List<TicketDebtor> findByRoommate(Roommate roommate);


    void removeByTicket(Ticket ticket);
}
