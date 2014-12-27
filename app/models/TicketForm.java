package models;

import dto.RoommateDTO;
import play.data.Form;
import play.data.validation.Constraints;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by florian on 18/12/14.
 */
public class TicketForm {

    @Constraints.Required
    private String description;

    @Constraints.Required
    private Date date;

    private List<Form<TicketDebtorForm>> ticketDebtors;


    public TicketForm() {
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

    public List<Form<TicketDebtorForm>> getTicketDebtors() {
        return ticketDebtors;
    }

    public void setTicketDebtors(List<Form<TicketDebtorForm>> ticketDebtors) {
        this.ticketDebtors = ticketDebtors;
    }

    @Override
    public String toString() {
        return "TicketForm{" +
                "description='" + description + '\'' +
                ", date=" + date +
                ", ticketDebtors=" + ticketDebtors +
                '}';
    }

    public void addTicketDebtor(Form<TicketDebtorForm> form) {
        if(ticketDebtors==null){
            ticketDebtors = new ArrayList<>();
        }

        ticketDebtors.add(form);
    }

    public static class TicketDebtorForm {

        private RoommateDTO roommate;

        private boolean selected;

        private double value;

        public TicketDebtorForm() {
        }

        public RoommateDTO getRoommate() {
            return roommate;
        }

        public void setRoommate(RoommateDTO roommate) {
            this.roommate = roommate;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "TicketDebtorForm{" +
                    "roommate=" + roommate +
                    ", selected=" + selected +
                    ", value=" + value +
                    '}';
        }
    }
}
