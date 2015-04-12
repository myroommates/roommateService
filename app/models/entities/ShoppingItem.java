package models.entities;

import models.entities.technical.AbstractEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by florian on 2/12/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = ShoppingItem.FIND_BY_ID, query = "where " + AbstractEntity.COL_ID + " = :" + AbstractEntity.PARAM_ID),
        @NamedQuery(name = ShoppingItem.FIND_BY_HOME, query = "where " + ShoppingItem.COL_HOME + "= :" + ShoppingItem.PARAM_HOME),
        @NamedQuery(name = ShoppingItem.FIND_NOT_BOUGHT_YET, query = "where wasBought = false"),
})
public class ShoppingItem extends AbstractEntity {

    //request
    public static final String FIND_BY_HOME = "ShoppingItem_FIND_BY_HOME";
    public static final String FIND_BY_ID = "ShoppingItem_FIND_BY_ID";
    public static final String FIND_NOT_BOUGHT_YET = "ShoppingItem_FIND_NOT_BOUGTH_YET";

    //column
    public final static String COL_HOME = "home.id";

    //params
    public final static String PARAM_HOME = "home_id";


    @Column(nullable = false, columnDefinition = "Text")
    private String description;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean onlyForMe = false;

    @Column(nullable = false)
    private Date creationDate;

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
        creationDate = new Date();
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
