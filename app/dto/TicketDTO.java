package dto;

import dto.technical.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by florian on 11/11/14.
 */
public class TicketDTO extends DTO {

    private Long id;
    @Pattern(regexp = ".{1,1000}", message = "homeName must respect this pattern : .{1,1000}")
    private String description;
    @NotNull
    private Double value;
    @NotNull
    private Date date;
    private Long creatorId;
    private Long categoryId;
    private List<Long> prayersId;

    public TicketDTO() {
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<Long> getPrayersId() {
        return prayersId;
    }

    public void setPrayersId(List<Long> prayersId) {
        this.prayersId = prayersId;
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", value=" + value +
                ", date=" + date +
                ", creatorId=" + creatorId +
                ", categoryId=" + categoryId +
                ", prayersId=" + prayersId +
                '}';
    }

    public void addPrayer(Long prayerId) {
        if (prayersId == null) {
            prayersId = new ArrayList<>();
        }
        prayersId.add(prayerId);
    }
}
