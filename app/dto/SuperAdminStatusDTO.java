package dto;

import dto.technical.DTO;
import dto.technical.verification.Pattern;
import util.ErrorMessage;

/**
 * Created by florian on 27/12/14.
 */
public class SuperAdminStatusDTO extends DTO {

    private Integer homes;

    private Integer roommates;

    private Integer tickets;

    private Double totalSum;

    public Integer getHomes() {
        return homes;
    }

    public void setHomes(Integer homes) {
        this.homes = homes;
    }

    public Integer getRoommates() {
        return roommates;
    }

    public void setRoommates(Integer roommates) {
        this.roommates = roommates;
    }

    public Integer getTickets() {
        return tickets;
    }

    public void setTickets(Integer tickets) {
        this.tickets = tickets;
    }

    public Double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Double totalSum) {
        this.totalSum = totalSum;
    }

    @Override
    public String toString() {
        return "SuperAdminStatusDTO{" +
                "homes=" + homes +
                ", roommates=" + roommates +
                ", tickets=" + tickets +
                ", totalSum=" + totalSum +
                '}';
    }
}
