package dto.count;

import dto.RoommateDTO;
import dto.technical.DTO;

/**
 * Created by florian on 25/12/14.
 */
public class CountResumeDTO extends DTO{

    private RoommateDTO roommate;

    private Double spend;

    private Double dept;

    public CountResumeDTO() {
    }

    public RoommateDTO getRoommate() {
        return roommate;
    }

    public void setRoommate(RoommateDTO roommate) {
        this.roommate = roommate;
    }

    public Double getSpend() {
        return spend;
    }

    public void setSpend(Double spend) {
        this.spend = spend;
    }

    public Double getDept() {
        return dept;
    }

    public void setDept(Double dept) {
        this.dept = dept;
    }

    @Override
    public String toString() {
        return "CountResumeDTO{" +
                "roommate=" + roommate +
                ", spend=" + spend +
                ", dept=" + dept +
                '}';
    }
}
