package entities;

import entities.technical.AuditedAbstractEntity;

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

    @Override
    public String toString() {
        return "Home{" +
                ", roommateList=" + roommateList +
                '}';
    }
}
