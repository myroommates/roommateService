package models.entities;

import models.entities.technical.AuditedAbstractEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by florian on 10/11/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Ticket.FIND_BY_ID, query = "where " + AuditedAbstractEntity.COL_ID + " = :" + AuditedAbstractEntity.PARAM_ID),
        @NamedQuery(name = Ticket.FIND_BY_HOME, query = "where " + Ticket.COL_HOME + " = :" + Ticket.PARAM_HOME),
})
public class Ticket extends AuditedAbstractEntity {

    //requests
    public static final String FIND_BY_HOME = "Ticket_FIND_BY_HOME";
    public static final String FIND_BY_ID = "Ticket_FIND_BY_ID";

    //column
    public static final String COL_HOME = "home";
    public static final String PARAM_HOME = COL_HOME;

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

    public Ticket() {
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
