package be.flo.roommateService.services;

import be.flo.roommateService.models.entities.Home;
import be.flo.roommateService.models.entities.Roommate;
import be.flo.roommateService.models.entities.Ticket;

import java.util.List;

/**
 * Created by florian on 6/12/14.
 */
public interface TicketService extends CrudService<Ticket> {

    List<Ticket> findByHome(Home home);

    List<Ticket> findByDebtor(Roommate roommate);

    List<Ticket> findByPayer(Roommate payer);

    Integer getCount();

    Double getTotalSum();
}
