package be.flo.roommateService.models.entities;

import be.flo.roommateService.models.entities.technical.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by florian on 4/12/14.
 */
@Entity
public class Session extends AbstractEntity {

    @ManyToOne(optional = false)
    private Roommate roommate;

    @Column
    private Date connectionDate;

    @Column
    private Boolean fromAndroid;

    public Session() {
    }

    public Session(Roommate roommate, boolean fromAndroid) {
        this.roommate = roommate;
        this.fromAndroid = fromAndroid;
        connectionDate = new Date();
    }

    public Roommate getRoommate() {
        return roommate;
    }

    public void setRoommate(Roommate roommate) {
        this.roommate = roommate;
    }

    public Date getConnectionDate() {
        return connectionDate;
    }

    public void setConnectionDate(Date connectionDate) {
        this.connectionDate = connectionDate;
    }

    public Boolean getFromAndroid() {
        return fromAndroid;
    }

    public void setFromAndroid(Boolean fromAndroid) {
        this.fromAndroid = fromAndroid;
    }

    @Override
    public String toString() {
        return "Session{" +
                "fromAndroid=" + fromAndroid +
                ", date=" + connectionDate +
                ", roommate=" + roommate +
                '}';
    }
}
