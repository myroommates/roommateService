package services;

import com.avaje.ebean.Ebean;
import entities.Event;
import entities.Home;
import entities.Ticket;
import entities.technical.AuditedAbstractEntity;

import java.util.List;

/**
 * Created by florian on 4/12/14.
 */
public class EventService {


    public void saveOrUpdate(Event event) {
        if (event.getId() != null) {
            event.update();
        } else {
            event.save();
        }
    }

    public List<Event> findByHome(Home home) {
        return Ebean.createNamedQuery(Event.class, Event.FIND_BY_HOME)
                .setParameter(Event.PARAM_HOME, home)
                .findList();
    }

    public Event findById(Long id) {
        return Ebean.createNamedQuery(Event.class,Event.FIND_BY_ID)
                .setParameter(AuditedAbstractEntity.PARAM_ID,id)
                .findUnique();
    }

    public void remove(Event event) {
        event.delete();
    }
}
