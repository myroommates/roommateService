package models.entities;

import models.entities.technical.AuditedAbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by florian on 4/12/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Event.FIND_BY_ID, query = "where " + AuditedAbstractEntity.COL_ID + " = :" + AuditedAbstractEntity.PARAM_ID),
        @NamedQuery(name = Event.FIND_BY_HOME, query = "where " + Event.COL_HOME + "= :" + Event.PARAM_HOME),
})
public class Event extends AuditedAbstractEntity {

    //request
    public static final String FIND_BY_HOME = "Event_FIND_BY_HOME";
    public static final String FIND_BY_ID = "Event_FIND_BY_ID";

    //col
    public static final String COL_HOME = "home.id";

    //param
    public static final String PARAM_HOME = "home_id";


    @Column(nullable = false, columnDefinition = "Text")
    private String description;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column
    private EventRepeatableFrequencyEnum repeatableFrequency;

    @ManyToOne(optional = false)
    private Home home;

    @ManyToOne(optional = false)
    private Roommate creator;

    public Event() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public EventRepeatableFrequencyEnum getRepeatable() {
        return repeatableFrequency;
    }

    public EventRepeatableFrequencyEnum getRepeatableFrequency() {
        return repeatableFrequency;
    }

    public void setRepeatableFrequency(EventRepeatableFrequencyEnum repeatableFrequency) {
        this.repeatableFrequency = repeatableFrequency;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Roommate getCreator() {
        return creator;
    }

    public void setCreator(Roommate creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "Event{" +
                "description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", repeatable=" + repeatableFrequency +
                ", home=" + home +
                ", creator=" + creator +
                '}';
    }
}
