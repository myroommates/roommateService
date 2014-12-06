package services.impl;

import com.avaje.ebean.Ebean;
import model.entities.Event;
import model.entities.Home;
import model.entities.technical.AuditedAbstractEntity;
import services.EventService;

import java.util.List;

/**
 * Created by florian on 4/12/14.
 */
public class EventServiceImpl implements EventService {

    @Override
    public void saveOrUpdate(Event event) {
        if (event.getId() != null) {
            event.update();
        } else {
            event.save();
        }
    }

    @Override
    public Event findById(Long id) {
        return Ebean.createNamedQuery(Event.class, Event.FIND_BY_ID)
                .setParameter(AuditedAbstractEntity.PARAM_ID, id)
                .findUnique();
    }

    @Override
    public List<Event> findByHome(Home home) {
        return Ebean.createNamedQuery(Event.class, Event.FIND_BY_HOME)
                .setParameter(Event.PARAM_HOME, home)
                .findList();
    }

    @Override
    public void remove(Event event) {
        event.delete();
    }
}
