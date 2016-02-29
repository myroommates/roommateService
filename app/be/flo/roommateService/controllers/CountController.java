package be.flo.roommateService.controllers;

import be.flo.roommateService.controllers.technical.AbstractController;
import be.flo.roommateService.controllers.technical.SecurityController;
import play.db.jpa.Transactional;
import be.flo.roommateService.converter.RoommateToInterfaceDataDTOConverter;
import be.flo.roommateService.converter.RoommateToRoommateDTOConverter;
import be.flo.roommateService.converter.TicketToTicketConverter;
import be.flo.roommateService.dto.ListDTO;
import be.flo.roommateService.dto.RoommateDTO;
import be.flo.roommateService.dto.TicketDTO;
import be.flo.roommateService.dto.count.CountResumeDTO;
import be.flo.roommateService.models.entities.Roommate;
import be.flo.roommateService.models.entities.Ticket;
import be.flo.roommateService.models.entities.TicketDebtor;
import play.Logger;
import play.mvc.Result;
import play.mvc.Security;
import be.flo.roommateService.services.RoommateService;
import be.flo.roommateService.services.TicketDebtorService;
import be.flo.roommateService.services.TicketService;
import be.flo.roommateService.services.impl.RoommateServiceImpl;
import be.flo.roommateService.services.impl.TicketDebtorServiceImpl;
import be.flo.roommateService.services.impl.TicketServiceImpl;

import java.util.List;

/**
 * Created by florian on 25/12/14.
 */
public class CountController extends AbstractController {

    //convert
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
    private RoommateToInterfaceDataDTOConverter roommateToInterfaceDataDTOConverter = new RoommateToInterfaceDataDTOConverter();

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

        TicketToTicketConverter ticketToTicketConverter = new TicketToTicketConverter(securityController.getCurrentUser());

        // load all ticket
        for (Ticket ticket : ticketService.findByHome(securityController.getCurrentUser().getHome())) {
            ticketDTOList.addElement(ticketToTicketConverter.convert(ticket));
        }
        return ok(views.html.home.count.resume.render(roommateToInterfaceDataDTOConverter.convert(securityController.getCurrentUser()), listDTO, ticketDTOList));
    }

    @Security.Authenticated(SecurityController.class)
    @Transactional
    public Result tickets() {

        //load and convert roommate
        ListDTO<RoommateDTO> roommateDTOListDTO = new ListDTO<>();

        for (Roommate roommate : roommateService.findByHome(securityController.getCurrentUser().getHome())) {
            roommateDTOListDTO.addElement(roommateToRoommateDTOConverter.convert(roommate));


        }
        ListDTO<TicketDTO> ticketDTOList = new ListDTO<>();

        TicketToTicketConverter ticketToTicketConverter = new TicketToTicketConverter(securityController.getCurrentUser());

        // load all ticket
        for (Ticket ticket : ticketService.findByHome(securityController.getCurrentUser().getHome())) {
            ticketDTOList.addElement(ticketToTicketConverter.convert(ticket));
        }


        return ok(views.html.home.count.tickets.render(roommateToInterfaceDataDTOConverter.convert(securityController.getCurrentUser()), roommateDTOListDTO, ticketDTOList));
    }
}
