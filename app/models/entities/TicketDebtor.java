package models.entities;

import models.entities.technical.AuditedAbstractEntity;

import javax.persistence.*;

/**
 * Created by florian on 6/12/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = TicketDebtor.FIND_BY_ROOMMATE, query = "where " + TicketDebtor.COL_ROOMMATE+ " = :" + TicketDebtor.PARAM_ROOMMATE),
})
public class TicketDebtor extends AuditedAbstractEntity{

    //request
    public static final String FIND_BY_ROOMMATE = "TicketDebtor_FIND_BY_ROOMMATE";

    //column
    public static final String COL_ROOMMATE = "roommate.id";

    //roommate
    public static final String PARAM_ROOMMATE = "roommate";

    @ManyToOne
    private Ticket ticket;

    @ManyToOne
    private Roommate roommate;

    @Column(nullable = false)
    private Double value;

    public TicketDebtor() {
    }

    public TicketDebtor(Roommate roommate, Double value) {
        this.roommate = roommate;
        this.value = value;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Roommate getRoommate() {
        return roommate;
    }

    public void setRoommate(Roommate roommate) {
        this.roommate = roommate;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TicketPayer{" +
                "ticket=" + ticket +
                ", roommate=" + roommate +
                ", value=" + value +
                '}';
    }
}
