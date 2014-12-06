package model.entities;

/**
 * Created by florian on 4/12/14.
 */
public enum EventRepeatableFrequencyEnum {

    WEEKLY, EVERY_TWO_WEEK, MONTHLY, DAILY;

    public static EventRepeatableFrequencyEnum getByName(String repeatableFrequency) {
        for (EventRepeatableFrequencyEnum eventRepeatableFrequencyEnum : EventRepeatableFrequencyEnum.values()) {
            if (eventRepeatableFrequencyEnum.name().equals(repeatableFrequency)) {
                return eventRepeatableFrequencyEnum;
            }
        }
        return null;
    }
}
