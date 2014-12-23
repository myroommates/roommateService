package services.impl;

import com.avaje.ebean.Ebean;
import models.entities.Event;
import models.entities.Home;
import services.EventService;

import java.util.List;

/**
 * Created by florian on 4/12/14.
 */
public class EventServiceImpl extends CrudServiceImpl<Event> implements EventService{

    @Override
    public List<Event> findByHome(Home home) {
        return Ebean.createNamedQuery(Event.class, Event.FIND_BY_HOME)
                .setParameter(Event.PARAM_HOME, home)
                .findList();
    }
}
