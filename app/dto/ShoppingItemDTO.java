package dto;

import dto.technical.DTO;
import dto.technical.verification.Size;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by florian on 4/12/14.
 */
public class ShoppingItemDTO extends DTO {

    private Long id;

    @Size(min = 1, max = 1000)
    private String description;

    private Date creationDate;

    private Long homeId;

    private Long creatorId;

    private Boolean wasBought;
    private Boolean onlyForMe;

    private List<CommentDTO> comments;

    public ShoppingItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getHomeId() {
        return homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Boolean getWasBought() {
        return wasBought;
    }

    public void setWasBought(Boolean wasBought) {
        this.wasBought = wasBought;
    }

    public Boolean getOnlyForMe() {
        return onlyForMe;
    }

    public void setOnlyForMe(Boolean onlyForMe) {
        this.onlyForMe = onlyForMe;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
    public void addComment(CommentDTO comment) {
        if(comments==null){
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }

    @Override
    public String toString() {
        return "ShoppingItemDTO{" +
                "description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", homeId=" + homeId +
                ", creatorId=" + creatorId +
                ", wasBought=" + wasBought +
                '}';
    }


}
