package services;

import models.entities.Home;
import models.entities.Ticket;

import java.util.List;

/**
 * Created by florian on 6/12/14.
 */
public interface TicketService extends CrudService<Ticket>{
    void saveOrUpdate(Ticket ticket);

    Ticket findById(Long id);

    List<Ticket> findByHome(Home home);

    void remove(Ticket ticket);
}
