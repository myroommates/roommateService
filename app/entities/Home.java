package entities;

import entities.technical.AuditedAbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by florian on 10/11/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Home.FIND_BY_NAME, query = "where " + Home.COL_NAME + " = :" + Home.COL_NAME),
})
public class Home extends AuditedAbstractEntity {

    //request
    public static final String FIND_BY_NAME = "Home_FIND_BY_NAME";

    //column
    public final static String COL_NAME = "name";

    @Column(name = COL_NAME, unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "home")
    private Set<Roommate> roommateList;

    @OneToMany(mappedBy = "home")
    private Set<Ticket> tickets;

    public Home() {
        roommateList = new HashSet<>();
    }

    public Home(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                "name='" + name + '\'' +
                ", roommateList=" + roommateList +
                '}';
    }
}
