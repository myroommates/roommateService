package dto;

import dto.technical.DTO;

import javax.validation.constraints.Size;
import java.util.Date;

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
    private Boolean onylForMe;

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

    public void setOnylForMe(Boolean onylForMe) {
        this.onylForMe = onylForMe;
    }

    public Boolean getOnylForMe() {
        return onylForMe;
    }
}
