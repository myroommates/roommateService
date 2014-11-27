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
    private List<Long> prayersId;
    private String category;
    private Long creatorId;

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

    public List<Long> getPrayersId() {
        return prayersId;
    }

    public void setPrayersId(List<Long> prayersId) {
        this.prayersId = prayersId;
    }

    public void addPrayer(Long prayerId) {
        if (prayersId == null) {
            prayersId = new ArrayList<>();
        }
        prayersId.add(prayerId);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", value=" + value +
                ", date=" + date +
                ", prayersId=" + prayersId +
                ", category='" + category + '\'' +
                ", creatorId=" + creatorId +
                '}';
    }
}
