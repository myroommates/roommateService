package dto;

import dto.technical.DTO;
import dto.technical.verification.NotNull;

/**
 * Created by florian on 6/12/14.
 */
public class TicketDebtorDTO extends DTO {

    @NotNull
    private Long roommateId;

    @NotNull
    private Double value;

    public TicketDebtorDTO() {
    }

    public TicketDebtorDTO(Long roommateId, Double value) {
        this.roommateId = roommateId;
        this.value = value;
    }

    public Long getRoommateId() {
        return roommateId;
    }

    public void setRoommateId(Long roommateId) {
        this.roommateId = roommateId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TicketPrayerDTO{" +
                "roommateId=" + roommateId +
                ", value=" + value +
                '}';
    }
}
