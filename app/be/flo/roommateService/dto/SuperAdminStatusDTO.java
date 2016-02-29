package be.flo.roommateService.dto;

import be.flo.roommateService.dto.technical.DTO;

/**
 * Created by florian on 27/12/14.
 */
public class SuperAdminStatusDTO extends DTO {

    private Integer homes;

    private Integer roommates;

    private Integer tickets;

    private Double totalSum;
    private int shoppings;
    private int shoppingsTotal;

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


    public void setShoppings(int shoppings) {
        this.shoppings = shoppings;
    }

    public int getShoppings() {
        return shoppings;
    }

    public void setShoppingsTotal(int shoppingsTotal) {
        this.shoppingsTotal = shoppingsTotal;
    }

    public int getShoppingsTotal() {
        return shoppingsTotal;
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
