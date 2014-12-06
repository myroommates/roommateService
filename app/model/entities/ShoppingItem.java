package model.entities;

import model.entities.technical.AuditedAbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by florian on 2/12/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = ShoppingItem.FIND_BY_ID, query = "where " + AuditedAbstractEntity.COL_ID + " = :" + AuditedAbstractEntity.PARAM_ID),
        @NamedQuery(name = ShoppingItem.FIND_BY_HOME, query = "where " + ShoppingItem.COL_HOME + "= :" + ShoppingItem.PARAM_HOME),
})
public class ShoppingItem extends AuditedAbstractEntity {

    //request
    public static final String FIND_BY_HOME = "ShoppingItem_FIND_BY_HOME";
    public static final String FIND_BY_ID = "ShoppingItem_FIND_BY_ID";

    //column
    public final static String COL_HOME = "home";

    //params
    public final static String PARAM_HOME = COL_HOME;


    @Column(nullable = false, columnDefinition = "Text")
    private String description;

    @Column(nullable = false)
    private Date creationDate;

    @ManyToOne(optional = false)
    private Home home;

    @ManyToOne(optional = false)
    private Roommate creator;

    @Column(columnDefinition = " boolean default = false", nullable = false)
    private boolean wasBought = false;

    public ShoppingItem() {
        creationDate = new Date();
    }

    public ShoppingItem(String description, Home home, Roommate creator) {
        this.description = description;
        this.home = home;
        this.creator = creator;
        creationDate = new Date();
    }

    public boolean isWasBought() {
        return wasBought;
    }

    public void setWasBought(boolean wasBought) {
        this.wasBought = wasBought;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Roommate getCreator() {
        return creator;
    }

    public void setCreator(Roommate creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "ShoppingItem{" +
                "description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", home=" + home +
                ", creator=" + creator +
                ", wasBought=" + wasBought +
                '}';
    }
}
