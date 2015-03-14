package services;

import models.entities.Home;
import models.entities.Roommate;
import models.entities.Ticket;

import java.util.List;

/**
 * Created by florian on 6/12/14.
 */
public interface TicketService extends CrudService<Ticket> {

    List<Ticket> findByHome(Home home);

    List<Ticket> findByDebtor(Roommate roommate);

    List<Ticket> findByPayer(Roommate payer);

    Integer getCount();
}
