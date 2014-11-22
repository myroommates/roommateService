package controllers;

import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.TicketToTicketConverter;
import dto.ListDTO;
import dto.RoommateDTO;
import dto.TicketDTO;
import dto.technical.ResultDTO;
import entities.Category;
import entities.Roommate;
import entities.Ticket;
import play.mvc.Result;
import play.mvc.Security;
import services.CategoryService;
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
    private CategoryService categoryService = new CategoryService();

    //converter
    private TicketToTicketConverter ticketToTicketConverter = new TicketToTicketConverter();

    @Security.Authenticated(SecurityController.class)
    public Result getById(Long id) {

        //load
        Ticket ticket = ticketService.findById(id);

        //control
        if (ticket == null || !ticket.getHome().equals(securityController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.NOT_YOU_TICKET, id));
        }

        //convert and return
        return ok(ticketToTicketConverter.converter(ticket));
    }

    @Security.Authenticated(SecurityController.class)
    public Result getAll() {

        ListDTO<TicketDTO> result = new ListDTO<>();

        for (Ticket ticket : securityController.getCurrentUser().getHome().getTickets()) {
            result.addElement(ticketToTicketConverter.converter(ticket));
        }

        return ok(result);
    }

    @Security.Authenticated(SecurityController.class)
    public Result create() {

        TicketDTO dto = extractDTOFromRequest(TicketDTO.class);

        Roommate currentUser = securityController.getCurrentUser();

        //control
        //prayers
        Set<Roommate> prayers = new HashSet<>();

        for (RoommateDTO roommateDTO : dto.getPrayers()) {

            Roommate roommate = roommateService.findByEmail(roommateDTO.getEmail());
            if (!roommate.getHome().equals(currentUser.getHome())) {
                throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.NOT_YOU_ROOMMATE, roommateDTO.getEmail()));
            }
            prayers.add(roommate);
        }

        //category
        Category category = null;
        if (dto.getCategory() != null) {
            category = categoryService.findById(dto.getCategory().getId());

            if (category == null) {
                throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.ENTITY_NOT_FOUND, Category.class.getName(), dto.getCategory().getId()));
            }
        }

        Ticket ticket = new Ticket();
        ticket.setDescription(dto.getDescription());
        ticket.setDate(dto.getDate());
        ticket.setValue(dto.getValue());
        ticket.setCategory(category);
        ticket.setHome(currentUser.getHome());
        ticket.setCreator(currentUser);
        ticket.setPrayerList(prayers);

        //operation
        ticketService.saveOrUpdate(ticket);

        //result
        return ok(ticketToTicketConverter.converter(ticket));
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

        for (RoommateDTO roommateDTO : dto.getPrayers()) {
            Roommate roommate = roommateService.findByEmail(roommateDTO.getEmail());
            if (!roommate.getHome().equals(currentUser.getHome())) {
                throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.NOT_YOU_ROOMMATE, roommateDTO.getEmail()));
            }
            prayers.add(roommate);
        }

        //category
        Category category = null;
        if (dto.getCategory() != null) {
            category = categoryService.findById(dto.getCategory().getId());

            if (category == null) {
                throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.ENTITY_NOT_FOUND, Category.class.getName(), dto.getCategory().getId()));
            }
        }

        ticket.setDescription(dto.getDescription());
        ticket.setDate(dto.getDate());
        ticket.setValue(dto.getValue());
        ticket.setCategory(category);
        ticket.setPrayerList(prayers);

        //operation
        ticketService.saveOrUpdate(ticket);

        //result
        return ok(ticketToTicketConverter.converter(ticket));
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
