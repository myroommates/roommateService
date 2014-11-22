package services;

import com.avaje.ebean.Ebean;
import entities.Home;
import entities.Ticket;
import entities.technical.AuditedAbstractEntity;

import java.util.List;

/**
 * Created by florian on 10/11/14.
 */
public class TicketService {

    public void saveOrUpdate(Ticket Ticket) {
        if (Ticket.getId() != null) {
            Ticket.update();
        } else {
            Ticket.save();
        }
    }

    public List<Ticket> findByHome(Home home) {
        return Ebean.createNamedQuery(Ticket.class, Ticket.FIND_BY_HOME)
                .setParameter(Ticket.COL_HOME, home)
                .findList();
    }

    public Ticket findById(Long id) {
        return Ebean.createNamedQuery(Ticket.class,Ticket.FIND_BY_ID)
                .setParameter(AuditedAbstractEntity.COL_ID,id)
                .findUnique();
    }

    public void remove(Ticket Ticket) {
        Ticket.delete();
    }
}
