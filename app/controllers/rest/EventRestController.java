package controllers.rest;

import com.avaje.ebean.annotation.Transactional;
import controllers.rest.technical.AbstractRestController;
import controllers.rest.technical.SecurityRestController;
import converter.EventToEventDTOConverter;
import dto.EventDTO;
import dto.ListDTO;
import dto.technical.ResultDTO;
import models.entities.Event;
import models.entities.EventRepeatableFrequencyEnum;
import play.mvc.Result;
import play.mvc.Security;
import services.EventService;
import services.impl.EventServiceImpl;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

import java.util.List;

/**
 * Created by florian on 4/12/14.
 */
public class EventRestController extends AbstractRestController {

    //service
    private EventService eventService = new EventServiceImpl();

    //converter
    private EventToEventDTOConverter eventToEventDTOConverter = new EventToEventDTOConverter();

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result getAll() {

        ListDTO<EventDTO> result = new ListDTO<>();

        //load
        List<Event> events = eventService.findByHome(securityRestController.getCurrentUser().getHome());

        for (Event event : events) {
            result.addElement(eventToEventDTOConverter.convert(event));
        }

        return ok(result);
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result getById(Long id) {

        //load
        Event event = eventService.findById(id);

        //control
        if (event == null || !event.getHome().equals(securityRestController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_EVENT, id);
        }

        //convert and return
        return ok(eventToEventDTOConverter.convert(event));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result update(Long id) {

        EventDTO dto = extractDTOFromRequest(EventDTO.class);

        //load
        Event event = eventService.findById(id);

        //control
        if (event == null || !event.getHome().equals(securityRestController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_EVENT, id);
        }

        //control creator
        if (!event.getCreator().equals(securityRestController.getCurrentUser())) {
            throw new MyRuntimeException(ErrorMessage.NOT_EVENT_CREATOR, id);
        }

        //update
        event.setDescription(dto.getDescription());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        if (dto.getRepeatableFrequency() != null) {
            event.setRepeatableFrequency(EventRepeatableFrequencyEnum.getByName(dto.getRepeatableFrequency()));
        }

        //operation
        eventService.saveOrUpdate(event);

        return ok(eventToEventDTOConverter.convert(event));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result create() {

        EventDTO dto = extractDTOFromRequest(EventDTO.class);

        Event event = new Event();

        event.setCreator(securityRestController.getCurrentUser());
        event.setDescription(dto.getDescription());
        event.setEndDate(dto.getEndDate());
        event.setHome(securityRestController.getCurrentUser().getHome());
        event.setStartDate(dto.getStartDate());
        if (dto.getRepeatableFrequency() != null) {
            event.setRepeatableFrequency(EventRepeatableFrequencyEnum.getByName(dto.getRepeatableFrequency()));
        }

        //operation
        eventService.saveOrUpdate(event);

        return ok(eventToEventDTOConverter.convert(event));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result remove(Long id) {

        //load
        Event event = eventService.findById(id);

        if (event != null && event.getHome().equals(securityRestController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_EVENT, id);
        }

        if (event != null) {
            eventService.remove(event);
        }

        return ok(new ResultDTO());
    }


}
