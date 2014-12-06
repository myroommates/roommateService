package services;

import model.entities.Event;
import model.entities.Home;

import java.util.List;

/**
 * Created by florian on 6/12/14.
 */
public interface EventService {

    void saveOrUpdate(Event event);

    Event findById(Long id);

    List<Event> findByHome(Home home);

    void remove(Event event);
}
