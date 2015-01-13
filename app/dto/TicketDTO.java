package dto;

import dto.technical.DTO;
import play.data.format.Formats;
import play.data.validation.Constraints;

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

    private Date date;

    private List<TicketDebtorDTO> debtorList;

    private String category;

    @Constraints.Required
    private Long payerId;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<TicketDebtorDTO> getDebtorList() {
        return debtorList;
    }

    public void setDebtorList(List<TicketDebtorDTO> debtorList) {
        this.debtorList = debtorList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", debtorList=" + debtorList +
                ", category='" + category + '\'' +
                ", payerId=" + payerId +
                '}';
    }

    public void addTicketDebtor(TicketDebtorDTO ticketDebtorDTO) {
        if(debtorList ==null){
            debtorList = new ArrayList<>();
        }
        this.debtorList.add(ticketDebtorDTO);
    }
}
