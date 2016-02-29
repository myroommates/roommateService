package be.flo.roommateService.controllers.rest;

import play.db.jpa.Transactional;
import be.flo.roommateService.controllers.technical.AbstractController;
import be.flo.roommateService.controllers.technical.SecurityRestController;
import be.flo.roommateService.converter.TicketToTicketConverter;
import be.flo.roommateService.dto.ListDTO;
import be.flo.roommateService.dto.TicketDTO;
import be.flo.roommateService.dto.TicketDebtorDTO;
import be.flo.roommateService.dto.technical.ResultDTO;
import be.flo.roommateService.models.entities.Roommate;
import be.flo.roommateService.models.entities.Ticket;
import be.flo.roommateService.models.entities.TicketDebtor;
import play.mvc.Result;
import play.mvc.Security;
import be.flo.roommateService.services.RoommateService;
import be.flo.roommateService.services.TicketDebtorService;
import be.flo.roommateService.services.TicketService;
import be.flo.roommateService.services.impl.RoommateServiceImpl;
import be.flo.roommateService.services.impl.TicketDebtorServiceImpl;
import be.flo.roommateService.services.impl.TicketServiceImpl;
import be.flo.roommateService.util.ErrorMessage;
import be.flo.roommateService.util.exception.MyRuntimeException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by florian on 11/11/14.
 */
public class TicketRestController extends AbstractController {

    //service
    private TicketService ticketService = new TicketServiceImpl();
    private TicketDebtorService ticketDebtorService = new TicketDebtorServiceImpl();
    private RoommateService roommateService = new RoommateServiceImpl();



    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result getAll() {

        ListDTO<TicketDTO> result = new ListDTO<>();

        //be.flo.roommateService.converter
        TicketToTicketConverter ticketToTicketConverter = new TicketToTicketConverter(securityController.getCurrentUser());

        for (Ticket ticket : securityController.getCurrentUser().getHome().getTickets()) {
            result.addElement(ticketToTicketConverter.convert(ticket));
        }

        return ok(result);
    }


    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result getById(Long id) {

        //load
        Ticket ticket = ticketService.findById(id);

        //control
        if (ticket == null || !ticket.getHome().equals(securityController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_TICKET, id);
        }
        //be.flo.roommateService.converter
        TicketToTicketConverter ticketToTicketConverter = new TicketToTicketConverter(securityController.getCurrentUser());

        //convert and return
        return ok(ticketToTicketConverter.convert(ticket));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result create() {

        TicketDTO dto = extractDTOFromRequest(TicketDTO.class);

        Roommate currentUser = securityController.getCurrentUser();

        //control
        //prayers
        Set<TicketDebtor> prayers = new HashSet<>();

        for (TicketDebtorDTO ticketDebtorDTO: dto.getDebtorList()) {

            Roommate roommate = roommateService.findById(ticketDebtorDTO.getRoommateId());
            if (!roommate.getHome().equals(currentUser.getHome())) {
                throw new MyRuntimeException(ErrorMessage.NOT_YOU_ROOMMATE, ticketDebtorDTO.getRoommateId());
            }
            prayers.add(new TicketDebtor(roommate,ticketDebtorDTO.getValue()));
        }
        //operation

        Ticket ticket = new Ticket();
        ticket.setDescription(dto.getDescription());
        ticket.setDate(dto.getDate());
        ticket.setCategory(dto.getCategory());
        ticket.setHome(currentUser.getHome());
        ticket.setPayer(currentUser);
        ticket.setDebtorList(prayers);

        //operation
        ticketService.saveOrUpdate(ticket);

        //be.flo.roommateService.converter
        TicketToTicketConverter ticketToTicketConverter = new TicketToTicketConverter(securityController.getCurrentUser());

        //result
        return ok(ticketToTicketConverter.convert(ticket));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result update(Long id) {

        TicketDTO dto = extractDTOFromRequest(TicketDTO.class);

        Roommate currentUser = securityController.getCurrentUser();

        //control
        //load ticket
        Ticket ticket = ticketService.findById(id);
        //control
        if (ticket == null) {
            throw new MyRuntimeException(ErrorMessage.ENTITY_NOT_FOUND, Ticket.class.getName(), id);
        }

        if (!ticket.getHome().equals(currentUser.getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_TICKET, id);
        }
        //control creator
        if (!ticket.getPayer().equals(currentUser)) {
            throw new MyRuntimeException(ErrorMessage.NOT_TICKET_CREATOR, id);
        }

        //remove old debtor
        ticketDebtorService.removeByTicket(ticket);

        //prayers
        Set<TicketDebtor> prayers = new HashSet<>();

        for (TicketDebtorDTO ticketDebtorDTO: dto.getDebtorList()) {

            Roommate roommate = roommateService.findById(ticketDebtorDTO.getRoommateId());
            if (!roommate.getHome().equals(currentUser.getHome())) {
                throw new MyRuntimeException(ErrorMessage.NOT_YOU_ROOMMATE, ticketDebtorDTO.getRoommateId());
            }
            prayers.add(new TicketDebtor(roommate,ticketDebtorDTO.getValue()));
        }

        ticket.setDescription(dto.getDescription());
        ticket.setDate(dto.getDate());
        ticket.setCategory(dto.getCategory());
        ticket.setDebtorList(prayers);

        //operation
        ticketService.saveOrUpdate(ticket);

        //be.flo.roommateService.converter
        TicketToTicketConverter ticketToTicketConverter = new TicketToTicketConverter(securityController.getCurrentUser());

        //result
        return ok(ticketToTicketConverter.convert(ticket));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result remove(Long id) {

        Roommate currentUser = securityController.getCurrentUser();

        //load entity
        Ticket ticket = ticketService.findById(id);

        if (ticket == null) {
            throw new MyRuntimeException(ErrorMessage.ENTITY_NOT_FOUND, Ticket.class.getName(), id);
        }

        //control home
        if (!ticket.getHome().equals(currentUser.getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_TICKET, id);
        }

        ticketService.remove(ticket);

        return ok(new ResultDTO());
    }


}
