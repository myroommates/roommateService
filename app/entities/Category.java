package entities;

import entities.technical.AuditedAbstractEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by florian on 10/11/14.
 */
@Entity
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"name", "home_id"})
)
@NamedQueries({
        @NamedQuery(name = Category.FIND_BY_ID, query = "where " + AuditedAbstractEntity.COL_ID + " = :" + AuditedAbstractEntity.COL_ID),
        @NamedQuery(name = Category.FIND_BY_HOME, query = "where " + Category.COL_HOME + " = :" + Category.COL_HOME + " and " + Category.COL_NAME + " = :" + Category.COL_NAME),
})
public class Category extends AuditedAbstractEntity {

    //requests
    public static final String FIND_BY_ID = "Category_FIND_BY_ID";
    public static final String FIND_BY_HOME = "Category_FIND_BY_HOME";
    public static final String FIND_BY_HOME_AND_NAME = "Category_FIND_BY_HOME_AND_NAME";

    //column
    public static final String COL_HOME = "home";
    public static final String COL_NAME = "name";

    @Column(nullable = false, name = "name")
    private String name;

    @ManyToOne(optional = false)
    @Column(nullable = false)
    private Home home;

    @OneToMany(mappedBy = "category")
    private List<Ticket> ticketList;

    public Category() {
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", home=" + home +
                '}';
    }
}
