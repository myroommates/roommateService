package converter;

import dto.EventDTO;
import entities.Event;

/**
 * Created by florian on 4/12/14.
 */
public class EventToEventDTOConverter implements ConverterInterface<Event, EventDTO>{
    @Override
    public EventDTO convert(Event entity) {
        EventDTO dto = new EventDTO();

        dto.setCreatorId(entity.getCreator().getId());
        dto.setDescription(entity.getDescription());
        dto.setEndDate(entity.getEndDate());
        dto.setHomeId(entity.getHome().getId());
        dto.setRepeatableFrequency(entity.getRepeatableFrequency().name());
        dto.setStartDate(entity.getStartDate());

        return dto;
    }
}
