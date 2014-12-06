package dto;

import dto.technical.DTO;
import util.EqualList;

import java.util.List;

/**
 * Created by florian on 11/11/14.
 */
public class LoginSuccessDTO extends DTO {

    private RoommateDTO currentRoommate;
    private HomeDTO home;
    private List<RoommateDTO> roommates;
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

    public List<RoommateDTO> getRoommates() {
        return roommates;
    }

    public void setRoommates(List<RoommateDTO> roommates) {
        this.roommates = roommates;
    }

    public void addRoommate(RoommateDTO roommate) {
        if (roommates == null) {
            roommates = new EqualList<>();
        }
        roommates.add(roommate);
    }

    @Override
    public String toString() {
        return "LoginSuccessDTO{" +
                "currentRoommate=" + currentRoommate +
                ", home=" + home +
                ", roommates=" + roommates +
                '}';
    }
}
