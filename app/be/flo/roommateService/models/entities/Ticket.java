package be.flo.roommateService.models.entities;

import be.flo.roommateService.models.entities.technical.AbstractEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by florian on 10/11/14.
 */
@Entity
public class Ticket extends AbstractEntity {

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

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Set<CommentLastVisualization> commentLastVisualizations;

    public Ticket() {
    }

    public Set<CommentLastVisualization> getCommentLastVisualizations() {
        return commentLastVisualizations;
    }

    public void setCommentLastVisualizations(Set<CommentLastVisualization> commentLastVisualizations) {
        this.commentLastVisualizations = commentLastVisualizations;
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
