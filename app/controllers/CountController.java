package controllers;

import com.avaje.ebean.annotation.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.RoommateToRoommateDTOConverter;
import converter.TicketToTicketConverter;
import dto.ListDTO;
import dto.RoommateDTO;
import dto.TicketDTO;
import dto.count.CountResumeDTO;
import models.LoginForm;
import models.RegistrationForm;
import models.TicketForm;
import models.entities.Roommate;
import models.entities.Ticket;
import models.entities.TicketDebtor;
import play.Logger;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import services.RoommateService;
import services.TicketDebtorService;
import services.TicketService;
import services.impl.RoommateServiceImpl;
import services.impl.TicketDebtorServiceImpl;
import services.impl.TicketServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by florian on 25/12/14.
 */
public class CountController extends AbstractController {

    //convert
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
    private TicketToTicketConverter ticketToTicketConverter = new TicketToTicketConverter();

    //service
    private RoommateService roommateService = new RoommateServiceImpl();
    private TicketService ticketService = new TicketServiceImpl();
    private TicketDebtorService ticketDebtorService = new TicketDebtorServiceImpl();

    //form
    //private Form<TicketForm> ticketForm = Form.form(TicketForm.class);

    public CountController() {
    }

    @Security.Authenticated(SecurityController.class)
    @Transactional
    public Result resume() {


        ListDTO<CountResumeDTO> listDTO = new ListDTO<>();


        RoommateDTO roommateDTO = roommateToRoommateDTOConverter.convert(securityController.getCurrentUser());


        for (Roommate roommate : roommateService.findByHome(securityController.getCurrentUser().getHome())) {

            //compute values
            List<Ticket> myTickets = ticketService.findByPayer(roommate);

            double spend = 0.0;

            for (Ticket myTicket : myTickets) {
                for (TicketDebtor ticketDebtor : myTicket.getDebtorList()) {
                    Logger.info("  ->" + ticketDebtor);
                }

                for (TicketDebtor ticketDebtor : myTicket.getDebtorList()) {
                    spend += ticketDebtor.getValue();
                }
            }


            //payer
            List<TicketDebtor> ticketDebtorList = ticketDebtorService.findByRoommate(roommate);

            double dept = 0.0;
            for (TicketDebtor ticketDebtor : ticketDebtorList) {
                dept += ticketDebtor.getValue();
            }

            CountResumeDTO countResumeDTO = new CountResumeDTO();

            countResumeDTO.setRoommate(roommateToRoommateDTOConverter.convert(roommate));
            countResumeDTO.setDept(dept);
            countResumeDTO.setSpend(spend);

            listDTO.addElement(countResumeDTO);
        }

        ListDTO<TicketDTO> ticketDTOList = new ListDTO<>();

        // load all ticket
        for (Ticket ticket : ticketService.findByHome(securityController.getCurrentUser().getHome())) {
            ticketDTOList.addElement(ticketToTicketConverter.convert(ticket));
        }
        return ok(views.html.home.count.resume.render(roommateDTO, listDTO,ticketDTOList));
    }

    @Security.Authenticated(SecurityController.class)
    @Transactional
    public Result tickets() {

        RoommateDTO roommateDTO = roommateToRoommateDTOConverter.convert(securityController.getCurrentUser());

        //load and convert roommate
        ListDTO<RoommateDTO> roommateDTOListDTO  = new ListDTO<>();

        for (Roommate roommate : roommateService.findByHome(securityController.getCurrentUser().getHome())) {
            roommateDTOListDTO.addElement(roommateToRoommateDTOConverter.convert(roommate));

            TicketForm.TicketDebtorForm ticketDebtorForm = new TicketForm.TicketDebtorForm();
            ticketDebtorForm.setRoommate(roommateDTO);

        }
        ListDTO<TicketDTO> ticketDTOList = new ListDTO<>();

       // load all ticket
        for (Ticket ticket : ticketService.findByHome(securityController.getCurrentUser().getHome())) {
            ticketDTOList.addElement(ticketToTicketConverter.convert(ticket));
        }


        return ok(views.html.home.count.tickets.render(roommateDTO,roommateDTOListDTO,ticketDTOList));
    }

    @Security.Authenticated(SecurityController.class)
    @Transactional
    public Result details() {
        return play.mvc.Results.TODO;
    }
}
