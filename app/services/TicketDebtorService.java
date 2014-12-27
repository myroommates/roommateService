package services;

import models.entities.Roommate;
import models.entities.Ticket;
import models.entities.TicketDebtor;

import java.util.List;

/**
 * Created by florian on 25/12/14.
 */
public interface TicketDebtorService extends CrudService<TicketDebtor>{

    List<TicketDebtor> findByRoommate(Roommate roommate);


    void removeByTicket(Ticket ticket);
}
