package models.entities;

import models.entities.converter.LocalDateTimePersistenceConverter;
import models.entities.technical.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by florian on 19/03/15.
 */
@Entity
public class CommentLastVisualization extends AbstractEntity {

    @Column(columnDefinition = "timestamp",nullable = false)
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime date;

    @ManyToOne(optional = false)
    private Roommate roommate;

    @ManyToOne
    private Ticket ticket;

    @ManyToOne
    private ShoppingItem shoppingItem;

    @ManyToOne
    private Home home;


    public CommentLastVisualization() {
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Roommate getRoommate() {
        return roommate;
    }

    public void setRoommate(Roommate roommate) {
        this.roommate = roommate;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public ShoppingItem getShoppingItem() {
        return shoppingItem;
    }

    public void setShoppingItem(ShoppingItem shoppingItem) {
        this.shoppingItem = shoppingItem;
    }

    @Override
    public String toString() {
        return "CommentLastVisualization{" +
                "date=" + date +
                ", roommate=" + roommate +
                ", ticket=" + ticket +
                ", shoppingItem=" + shoppingItem +
                '}';
    }
}
