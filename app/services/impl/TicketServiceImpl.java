package services.impl;

import com.avaje.ebean.Ebean;
import model.entities.Home;
import model.entities.Ticket;
import model.entities.technical.AuditedAbstractEntity;
import services.TicketService;

import java.util.List;

/**
 * Created by florian on 10/11/14.
 */
public class TicketServiceImpl implements TicketService {

    @Override
    public void saveOrUpdate(Ticket ticket) {
        if (ticket.getId() != null) {
            ticket.update();
        } else {
            ticket.save();
        }
    }

    @Override
    public Ticket findById(Long id) {
        return Ebean.createNamedQuery(Ticket.class, Ticket.FIND_BY_ID)
                .setParameter(AuditedAbstractEntity.PARAM_ID, id)
                .findUnique();
    }

    @Override
    public List<Ticket> findByHome(Home home) {
        return Ebean.createNamedQuery(Ticket.class, Ticket.FIND_BY_HOME)
                .setParameter(Ticket.PARAM_HOME, home)
                .findList();
    }

    @Override
    public void remove(Ticket ticket) {
        ticket.delete();
    }
}
