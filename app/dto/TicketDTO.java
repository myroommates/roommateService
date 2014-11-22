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
    private RoommateDTO creator;
    private CategoryDTO category;
    private List<RoommateDTO> prayers;

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

    public RoommateDTO getCreator() {
        return creator;
    }

    public void setCreator(RoommateDTO creator) {
        this.creator = creator;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public List<RoommateDTO> getPrayers() {
        return prayers;
    }

    public void setPrayers(List<RoommateDTO> prayers) {
        this.prayers = prayers;
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "description='" + description + '\'' +
                ", value=" + value +
                ", date=" + date +
                ", creator=" + creator +
                ", category=" + category +
                ", prayers=" + prayers +
                '}';
    }

    public void addPrayer(RoommateDTO roommate) {
        if (prayers == null) {
            prayers = new ArrayList<>();
        }
        prayers.add(roommate);
    }
}
