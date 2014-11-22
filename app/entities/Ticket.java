package entities;

import entities.technical.AuditedAbstractEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by florian on 10/11/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Ticket.FIND_BY_ID, query = "where " + AuditedAbstractEntity.COL_ID + " = :" + AuditedAbstractEntity.COL_ID),
        @NamedQuery(name = Ticket.FIND_BY_HOME, query = "where " + Ticket.COL_HOME + " = :" + Ticket.COL_HOME),
})
public class Ticket extends AuditedAbstractEntity {

    //requests
    public static final String FIND_BY_HOME = "Ticket_FIND_BY_HOME";
    public static final String FIND_BY_ID = "Ticket_FIND_BY_ID";

    //column
    public static final String COL_HOME = "home";

    @Column(nullable = false, columnDefinition = "Text")
    private String description;

    @Column(nullable = false)
    private Double value;

    @ManyToOne
    private Category category;

    @Column(nullable = false)
    private Date date;

    @ManyToOne(optional = false)
    private Home home;

    @ManyToOne(optional = false)
    private Roommate creator;

    @ManyToMany
    @JoinTable(name = "mm_ticket_prayers",
            joinColumns = @JoinColumn(name = "ticket_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roommate_id", referencedColumnName = "id"))
    private Set<Roommate> prayerList = new HashSet<>();

    public Ticket() {
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Roommate getCreator() {
        return creator;
    }

    public void setCreator(Roommate creator) {
        this.creator = creator;
    }

    public Set<Roommate> getPrayerList() {
        return prayerList;
    }

    public void setPrayerList(Set<Roommate> prayerList) {
        this.prayerList = prayerList;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "description='" + description + '\'' +
                ", value=" + value +
                ", category=" + category +
                ", date=" + date +
                ", creator=" + creator +
                ", prayerList=" + prayerList +
                '}';
    }

}
