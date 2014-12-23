package services.impl;

import com.avaje.ebean.Ebean;
import models.entities.Home;
import models.entities.Ticket;
import services.TicketService;

import java.util.List;

/**
 * Created by florian on 10/11/14.
 */
public class TicketServiceImpl extends CrudServiceImpl<Ticket> implements TicketService {

    @Override
    public List<Ticket> findByHome(Home home) {
        return Ebean.createNamedQuery(Ticket.class, Ticket.FIND_BY_HOME)
                .setParameter(Ticket.PARAM_HOME, home)
                .findList();
    }
}
