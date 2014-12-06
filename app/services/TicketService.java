package services;

import model.entities.Home;
import model.entities.Ticket;

import java.util.List;

/**
 * Created by florian on 6/12/14.
 */
public interface TicketService {
    void saveOrUpdate(Ticket ticket);

    Ticket findById(Long id);

    List<Ticket> findByHome(Home home);

    void remove(Ticket ticket);
}
