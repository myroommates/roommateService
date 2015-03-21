package controllers.rest;

import com.avaje.ebean.annotation.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityRestController;
import converter.TicketToTicketConverter;
import dto.ListDTO;
import dto.TicketDTO;
import dto.TicketDebtorDTO;
import dto.technical.ResultDTO;
import models.entities.Roommate;
import models.entities.Ticket;
import models.entities.TicketDebtor;
import play.mvc.Result;
import play.mvc.Security;
import services.RoommateService;
import services.TicketDebtorService;
import services.TicketService;
import services.impl.RoommateServiceImpl;
import services.impl.TicketDebtorServiceImpl;
import services.impl.TicketServiceImpl;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

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

        //converter
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
        //converter
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

        //converter
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

        //converter
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
