package models.entities;

import models.entities.technical.AuditedAbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by florian on 6/12/14.
 */
@Entity
public class TicketDebtor extends AuditedAbstractEntity{

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
