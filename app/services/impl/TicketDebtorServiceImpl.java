package services.impl;

import com.avaje.ebean.Ebean;
import models.entities.Roommate;
import models.entities.Ticket;
import models.entities.TicketDebtor;
import play.db.jpa.JPA;
import services.TicketDebtorService;

import java.util.List;

/**
 * Created by florian on 25/12/14.
 */
public class TicketDebtorServiceImpl extends CrudServiceImpl<TicketDebtor> implements TicketDebtorService {

    public List<TicketDebtor> findByRoommate(Roommate roommate) {

        String r = "SELECT t FROM TicketDebtor t WHERE t.roommate = :roommate";

        return JPA.em().createQuery(r,TicketDebtor.class)
                .setParameter("roommate",roommate)
                .getResultList();
    }

    @Override
    public void removeByTicket(Ticket ticket) {
        for (TicketDebtor ticketDebtor : ticket.getDebtorList()) {
            JPA.em().remove(ticketDebtor);
        }

    }
}
