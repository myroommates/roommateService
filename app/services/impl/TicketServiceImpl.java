package services.impl;

import com.avaje.ebean.Ebean;
import models.entities.Home;
import models.entities.Roommate;
import models.entities.Ticket;
import models.entities.TicketDebtor;
import services.TicketDebtorService;
import services.TicketService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 10/11/14.
 */
public class TicketServiceImpl extends CrudServiceImpl<Ticket> implements TicketService {

    //service
    private TicketDebtorService ticketDebtorService = new TicketDebtorServiceImpl();

    @Override
    public List<Ticket> findByHome(Home home) {
        return Ebean.createNamedQuery(Ticket.class, Ticket.FIND_BY_HOME)
                .setParameter(Ticket.PARAM_HOME, home.getId())
                .findList();
    }

    @Override
    public List<Ticket> findByDebtor(Roommate roommate) {
        //load ticketDebtor
        List<TicketDebtor> ticketDebtorList = ticketDebtorService.findByRoommate(roommate);

        List<Ticket> ticketList = new ArrayList<>();

        //load ticket
        for (TicketDebtor ticketDebtor : ticketDebtorList) {
            ticketList.add(ticketDebtor.getTicket());
        }
        return ticketList;

    }

    @Override
    public List<Ticket> findByPayer(Roommate payer) {
        return Ebean.createNamedQuery(Ticket.class, Ticket.FIND_BY_PAYER)
                .setParameter(Ticket.PARAM_PAYER, payer.getId())
                .findList();

    }

    @Override
    public Integer getCount() {
        return Ebean.createQuery(Ticket.class).findRowCount();
    }

    @Override
    public Double getTotalSum() {
        double sum = 0;
        for (Ticket ticket : Ebean.createQuery(Ticket.class).findList()) {
            for (TicketDebtor ticketDebtor : ticket.getDebtorList()) {
                sum += ticketDebtor.getValue();
            }
        }
        return sum;
    }
}
