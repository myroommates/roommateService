package services.impl;

import com.avaje.ebean.Ebean;
import models.entities.Roommate;
import models.entities.Ticket;
import models.entities.TicketDebtor;
import services.TicketDebtorService;

import java.util.List;

/**
 * Created by florian on 25/12/14.
 */
public class TicketDebtorServiceImpl extends CrudServiceImpl<TicketDebtor> implements TicketDebtorService {

    public List<TicketDebtor> findByRoommate(Roommate roommate) {
        return Ebean.createNamedQuery(TicketDebtor.class, TicketDebtor.FIND_BY_ROOMMATE)
                .setParameter(TicketDebtor.PARAM_ROOMMATE, roommate.getId())
                .findList();


    }

    @Override
    public void removeByTicket(Ticket ticket) {
        for (TicketDebtor ticketDebtor : ticket.getDebtorList()) {
            ticketDebtor.delete();
        }

    }
}
