package be.flo.roommateService.services.impl;

import be.flo.roommateService.models.entities.Home;
import be.flo.roommateService.models.entities.Roommate;
import be.flo.roommateService.models.entities.Ticket;
import be.flo.roommateService.models.entities.TicketDebtor;
import play.db.jpa.JPA;
import be.flo.roommateService.services.TicketDebtorService;
import be.flo.roommateService.services.TicketService;

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

        String r = "SELECT t FROM Ticket t WHERE t.home = :home";

        return JPA.em().createQuery(r,Ticket.class)
                .setParameter("home",home)
                .getResultList();
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

        String r = "SELECT t FROM Ticket t WHERE t.payer = :payer";

        return JPA.em().createQuery(r,Ticket.class)
                .setParameter("payer",payer)
                .getResultList();
    }

    @Override
    public Integer getCount() {
        //TODO

        String r = "SELECT t FROM Ticket t";

        return JPA.em().createQuery(r,Ticket.class)
                .getResultList().size();
    }

    @Override
    public Double getTotalSum() {

        //TODO

        String r = "SELECT t FROM Ticket t";

        return new Double(JPA.em().createQuery(r,Ticket.class)
                .getResultList().size());

        /*

        double sum = 0;
        for (Ticket ticket : Ebean.createQuery(Ticket.class).findList()) {
            for (TicketDebtor ticketDebtor : ticket.getDebtorList()) {
                sum += ticketDebtor.getValue();
            }
        }
        return sum;
        */
    }
}
