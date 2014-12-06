package model.entities;

import model.entities.technical.AuditedAbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by florian on 10/11/14.
 */
@Entity
public class Home extends AuditedAbstractEntity {

    @OneToMany(mappedBy = "home")
    private Set<Roommate> roommateList;

    @OneToMany(mappedBy = "home")
    private Set<Ticket> tickets;

    @OneToMany(mappedBy = "home")
    private Set<Event> events;

    @OneToMany(mappedBy = "home")
    private Set<ShoppingItem> shoppingItems;

    @ManyToOne(optional = false)
    private Roommate admin;

    public Home() {
        roommateList = new HashSet<>();
    }

    public Set<Roommate> getRoommateList() {
        return roommateList;
    }

    public void setRoommateList(Set<Roommate> roommateList) {
        this.roommateList = roommateList;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<ShoppingItem> getShoppingItems() {
        return shoppingItems;
    }

    public void setShoppingItems(Set<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }

    @Override
    public String toString() {
        return "Home{" +
                ", roommateList=" + roommateList +
                '}';
    }


    public void setAdmin(Roommate admin) {
        this.admin = admin;
    }

    public Roommate getAdmin() {
        return admin;
    }
}
