package dto;

import dto.technical.DTO;
import models.entities.Faq;
import util.EqualList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 11/11/14.
 */
public class LoginSuccessDTO extends DTO {

    private RoommateDTO currentRoommate;
    private HomeDTO home;
    private List<RoommateDTO> roommates;
    private List<TicketDTO> tickets;
    private List<ShoppingItemDTO> shoppingItems;
    private List<FaqDTO> faqs;
    private String authenticationKey;
    private SurveyDTO surveyDTO;

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

    public List<RoommateDTO> getRoommates() {
        return roommates;
    }

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }

    public List<ShoppingItemDTO> getShoppingItems() {
        return shoppingItems;
    }

    public void setShoppingItems(List<ShoppingItemDTO> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }

    public void setRoommates(List<RoommateDTO> roommates) {
        this.roommates = roommates;
    }

    public SurveyDTO getSurveyDTO() {
        return surveyDTO;
    }

    public void setSurveyDTO(SurveyDTO surveyDTO) {
        this.surveyDTO = surveyDTO;
    }

    public void addRoommate(RoommateDTO roommate) {
        if (roommates == null) {
            roommates = new EqualList<>();
        }
        roommates.add(roommate);
    }

    public void addShoppingItem(ShoppingItemDTO shoppingItemDTO) {
        if (shoppingItems == null) {
            shoppingItems = new ArrayList<>();
        }
        shoppingItems.add(shoppingItemDTO);
    }

    public void addTicket(TicketDTO ticketDTO) {
        if (tickets == null) {
            tickets = new ArrayList<>();
        }
        tickets.add(ticketDTO);
    }

    public List<FaqDTO> getFaqs() {
        return faqs;
    }

    public void setFaqs(List<FaqDTO> faqs) {
        this.faqs = faqs;
    }

    public void addFaq(FaqDTO faqDTO) {
        if (faqs == null) {
            faqs = new ArrayList<>();
        }
        faqs.add(faqDTO);
    }

    @Override
    public String toString() {
        return "LoginSuccessDTO{" +
                "currentRoommate=" + currentRoommate +
                ", home=" + home +
                ", roommates=" + roommates +
                ", tickets=" + tickets +
                ", shoppingItems=" + shoppingItems +
                ", authenticationKey='" + authenticationKey + '\'' +
                '}';
    }
}
