package be.flo.roommateService.models.entities;

import be.flo.roommateService.models.entities.technical.AbstractEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by florian on 18/03/15.
 */
@Entity
public class Comment extends AbstractEntity {

    @Column(columnDefinition = "TEXT(65500) not null",nullable = false)
    private String comment;

    @ManyToOne(optional = false)
    private Roommate creator;

    @Column(nullable = false)
    private Date dateCreation;

    @Column
    private Date dateEdit;

    @ManyToOne
    private Comment parent;

    @OneToMany(mappedBy = "parent",cascade = CascadeType.REMOVE)
    private List<Comment> children;

    @ManyToOne
    private ShoppingItem shoppingItem;

    @ManyToOne
    private Home home;

    @ManyToOne
    private Ticket ticket;

    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }

    public Date getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(Date dateEdit) {
        this.dateEdit = dateEdit;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Roommate getCreator() {
        return creator;
    }

    public void setCreator(Roommate creator) {
        this.creator = creator;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ShoppingItem getShoppingItem() {
        return shoppingItem;
    }

    public void setShoppingItem(ShoppingItem shoppingItem) {
        this.shoppingItem = shoppingItem;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment='" + comment + '\'' +
                ", creator=" + creator +
                ", dateCreation=" + dateCreation +
                ", shoppingItem=" + shoppingItem +
                '}';
    }
}
