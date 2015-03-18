package models.entities;

import models.entities.technical.AbstractEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by florian on 10/11/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Ticket.FIND_BY_ID, query = "where " + AbstractEntity.COL_ID + " = :" + AbstractEntity.PARAM_ID),
        @NamedQuery(name = Ticket.FIND_BY_HOME, query = "where " + Ticket.COL_HOME + " = :" + Ticket.PARAM_HOME),
        @NamedQuery(name = Ticket.FIND_BY_PAYER, query = "where " + Ticket.COL_PAYER + " = :" + Ticket.PARAM_PAYER),
})
public class Ticket extends AbstractEntity {

    //requests
    public static final String FIND_BY_HOME = "Ticket_FIND_BY_HOME";
    public static final String FIND_BY_ID = "Ticket_FIND_BY_ID";
    public static final String FIND_BY_PAYER = "Ticket_FIND_BY_PAYER";

    //column
    public static final String COL_HOME = "home.id";
    public static final String COL_PAYER = "payer.id";
    //params
    public static final String PARAM_HOME = "home_id";
    public static final String PARAM_PAYER = "payer_id";

    @Column(nullable = false, columnDefinition = "Text")
    private String description;

    @Column
    private String category;

    @Column(nullable = false)
    private Date date;

    @ManyToOne(optional = false)
    private Home home;

    @ManyToOne(optional = false)
    private Roommate payer;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<TicketDebtor> debtorList = new HashSet<>();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    public Ticket() {
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Roommate getPayer() {
        return payer;
    }

    public void setPayer(Roommate payer) {
        this.payer = payer;
    }

    public Set<TicketDebtor> getDebtorList() {
        return debtorList;
    }

    public void setDebtorList(Set<TicketDebtor> debtorList) {
        this.debtorList = debtorList;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", date=" + date +
                ", home=" + home +
                ", payer=" + payer +
                ", debtorList=" + debtorList +
                '}';
    }


}
