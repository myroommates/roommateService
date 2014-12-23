package services;

import models.entities.Event;
import models.entities.Home;

import java.util.List;

/**
 * Created by florian on 6/12/14.
 */
public interface EventService extends CrudService<Event> {

    List<Event> findByHome(Home home);

}
