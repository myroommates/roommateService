package controllers;

import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.TicketToTicketConverter;
import dto.ListDTO;
import dto.TicketDTO;
import dto.technical.ResultDTO;
import entities.Roommate;
import entities.Ticket;
import play.mvc.Result;
import play.mvc.Security;
import services.RoommateService;
import services.TicketService;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by florian on 11/11/14.
 */
public class TicketController extends AbstractController {

    //service
    private TicketService ticketService = new TicketService();
    private RoommateService roommateService = new RoommateService();

    //converter
    private TicketToTicketConverter ticketToTicketConverter = new TicketToTicketConverter();


    @Security.Authenticated(SecurityController.class)
    public Result getAll() {

        ListDTO<TicketDTO> result = new ListDTO<>();

        for (Ticket ticket : securityController.getCurrentUser().getHome().getTickets()) {
            result.addElement(ticketToTicketConverter.convert(ticket));
        }

        return ok(result);
    }


    @Security.Authenticated(SecurityController.class)
    public Result getById(Long id) {

        //load
        Ticket ticket = ticketService.findById(id);

        //control
        if (ticket == null || !ticket.getHome().equals(securityController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.NOT_YOU_TICKET, id));
        }

        //convert and return
        return ok(ticketToTicketConverter.convert(ticket));
    }

    @Security.Authenticated(SecurityController.class)
    public Result create() {

        TicketDTO dto = extractDTOFromRequest(TicketDTO.class);

        Roommate currentUser = securityController.getCurrentUser();

        //control
        //prayers
        Set<Roommate> prayers = new HashSet<>();

        for (Long prayerId: dto.getPrayersId()) {

            Roommate roommate = roommateService.findById(prayerId);
            if (!roommate.getHome().equals(currentUser.getHome())) {
                throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.NOT_YOU_ROOMMATE, prayerId));
            }
            prayers.add(roommate);
        }

        Ticket ticket = new Ticket();
        ticket.setDescription(dto.getDescription());
        ticket.setDate(dto.getDate());
        ticket.setValue(dto.getValue());
        ticket.setCategory(dto.getCategory());
        ticket.setHome(currentUser.getHome());
        ticket.setCreator(currentUser);
        ticket.setPrayerList(prayers);

        //operation
        ticketService.saveOrUpdate(ticket);

        //result
        return ok(ticketToTicketConverter.convert(ticket));
    }

    @Security.Authenticated(SecurityController.class)
    public Result update(Long id) {

        TicketDTO dto = extractDTOFromRequest(TicketDTO.class);

        Roommate currentUser = securityController.getCurrentUser();

        //control
        //load ticket
        Ticket ticket = ticketService.findById(id);
        //control
        if (ticket == null) {
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.ENTITY_NOT_FOUND, Ticket.class.getName(), id));
        }

        if (!ticket.getHome().equals(currentUser.getHome())) {
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.NOT_YOU_TICKET, id));
        }
        //control creator
        if (!ticket.getCreator().equals(currentUser)) {
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.NOT_TICKET_CREATOR, id));
        }


        //prayers
        Set<Roommate> prayers = new HashSet<>();

        for (Long prayerId: dto.getPrayersId()) {
            Roommate roommate = roommateService.findById(prayerId);
            if (!roommate.getHome().equals(currentUser.getHome())) {
                throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.NOT_YOU_ROOMMATE, prayerId));
            }
            prayers.add(roommate);
        }

        ticket.setDescription(dto.getDescription());
        ticket.setDate(dto.getDate());
        ticket.setValue(dto.getValue());
        ticket.setCategory(dto.getCategory());
        ticket.setPrayerList(prayers);

        //operation
        ticketService.saveOrUpdate(ticket);

        //result
        return ok(ticketToTicketConverter.convert(ticket));
    }

    @Security.Authenticated(SecurityController.class)
    public Result remove(Long id) {

        Roommate currentUser = securityController.getCurrentUser();

        //load entity
        Ticket ticket = ticketService.findById(id);

        if (ticket == null) {
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.ENTITY_NOT_FOUND, Ticket.class.getName(), id));
        }

        //control home
        if (!ticket.getHome().equals(currentUser.getHome())) {
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.NOT_YOU_TICKET, id));
        }

        ticketService.remove(ticket);

        return ok(new ResultDTO());
    }


}
