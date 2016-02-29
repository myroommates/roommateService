package be.flo.roommateService.models.entities;

import be.flo.roommateService.models.entities.technical.AbstractEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by florian on 2/12/14.
 */
@Entity
public class ShoppingItem extends AbstractEntity {

    @Column(nullable = false, columnDefinition = "Text")
    private String description;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean onlyForMe = false;

    @ManyToOne(optional = false)
    private Home home;

    @ManyToOne(optional = false)
    private Roommate creator;

    @Column(columnDefinition = " boolean default false", nullable = false)
    private Boolean wasBought = false;

    @OneToMany(mappedBy = "shoppingItem", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "shoppingItem", cascade = CascadeType.ALL)
    private List<CommentLastVisualization> commentLastVisualizations;

    public ShoppingItem() {

    }

    public List<CommentLastVisualization> getCommentLastVisualizations() {
        return commentLastVisualizations;
    }

    public void setCommentLastVisualizations(List<CommentLastVisualization> commentLastVisualizations) {
        this.commentLastVisualizations = commentLastVisualizations;
    }

    public Boolean getOnlyForMe() {
        return onlyForMe;
    }

    public void setOnlyForMe(Boolean onlyForMe) {
        this.onlyForMe = onlyForMe;
    }

    public Boolean isWasBought() {
        return wasBought;
    }

    public void setWasBought(Boolean wasBought) {
        this.wasBought = wasBought;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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
