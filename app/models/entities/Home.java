package models.entities;

import models.entities.technical.AuditedAbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Created by florian on 22/12/14.
 */
@Entity
public class Home extends AuditedAbstractEntity{


    public static final String DEFAULT_MONEY_SYMBOL = "€";
    @Column
    private String moneySymbol;

    @OneToMany(mappedBy = "home")
    private Set<Roommate> roommateList;

    @OneToMany(mappedBy = "home")
    private Set<Ticket> tickets;

    @OneToMany(mappedBy = "home")
    private Set<Event> events;


    @OneToMany(mappedBy = "home")
    private Set<ShoppingItem> shoppingItems;

    public String getMoneySymbol() {
        return moneySymbol;
    }

    public void setMoneySymbol(String moneySymbol) {
        this.moneySymbol = moneySymbol;
    }

    public Set<ShoppingItem> getShoppingItems() {
        return shoppingItems;
    }

    public void setShoppingItems(Set<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
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
}
