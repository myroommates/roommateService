package dto;

import dto.technical.DTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by florian on 11/11/14.
 */
public class LoginSuccessDTO extends DTO {

    private RoommateDTO currentRoommate;
    private HomeDTO home;
    private Set<RoommateDTO> roommates;
    private List<TicketDTO> tickets;
    private List<CategoryDTO> categories;
    private String authenticationKey;

    public LoginSuccessDTO() {
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    public RoommateDTO getCurrentRoommate() {
        return currentRoommate;
    }

    public void setCurrentRoommate(RoommateDTO currentRoommate) {
        this.currentRoommate = currentRoommate;
    }

    public HomeDTO getHome() {
        return home;
    }

    public void setHome(HomeDTO home) {
        this.home = home;
    }

    public Set<RoommateDTO> getRoommates() {
        return roommates;
    }

    public void setRoommates(Set<RoommateDTO> roommates) {
        this.roommates = roommates;
    }

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }

    public void addCategory(CategoryDTO category){
        if(categories == null){
            categories = new ArrayList<>();
        }
        categories.add(category);
    }

    public void addTicket(TicketDTO ticket) {
        if (tickets == null) {
            tickets = new ArrayList<>();
        }
        tickets.add(ticket);
    }

    public void addRoommate(RoommateDTO roommate) {
        if (roommates == null) {
            roommates = new HashSet<>();
        }
        roommates.add(roommate);
    }

    @Override
    public String toString() {
        return "LoginSuccessDTO{" +
                "currentRoommate=" + currentRoommate +
                ", home=" + home +
                ", roommates=" + roommates +
                ", tickets=" + tickets +
                '}';
    }
}
