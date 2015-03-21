package controllers;

import com.avaje.ebean.annotation.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.RoommateToInterfaceDataDTOConverter;
import converter.RoommateToRoommateDTOConverter;
import converter.ShoppingItemToShoppingItemDTOConverter;
import converter.TicketToTicketConverter;
import dto.ListDTO;
import dto.RoommateDTO;
import dto.ShoppingItemDTO;
import dto.TicketDTO;
import dto.count.CountResumeDTO;
import models.entities.Roommate;
import models.entities.ShoppingItem;
import models.entities.Ticket;
import models.entities.TicketDebtor;
import play.Logger;
import play.mvc.Result;
import play.mvc.Security;
import services.RoommateService;
import services.ShoppingItemService;
import services.TicketDebtorService;
import services.TicketService;
import services.impl.RoommateServiceImpl;
import services.impl.ShoppingItemServiceImpl;
import services.impl.TicketDebtorServiceImpl;
import services.impl.TicketServiceImpl;

import java.util.List;

/**
 * Created by florian on 20/12/14.
 */
public class HomeController extends AbstractController {

    //service
    private TicketService ticketService = new TicketServiceImpl();
    private TicketDebtorService ticketDebtorService = new TicketDebtorServiceImpl();
    private ShoppingItemService shoppingItemService = new ShoppingItemServiceImpl();
    private RoommateService roommateService = new RoommateServiceImpl();

    //converter
    private RoommateToInterfaceDataDTOConverter roommateToInterfaceDataDTOConverter = new RoommateToInterfaceDataDTOConverter();
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();

    @Security.Authenticated(SecurityController.class)
    @Transactional
    public Result index() {


        return ok(views.html.home.home.render(roommateToInterfaceDataDTOConverter.convert(securityController.getCurrentUser()),  computeShoppingList(),getListTicket(),getRoommateList()));
    }

    private ListDTO<TicketDTO> getListTicket(){
        ListDTO<TicketDTO> listDTO = new ListDTO<>();

        TicketToTicketConverter ticketToTicketConverter  = new TicketToTicketConverter(securityController.getCurrentUser());
        //load
        for (Ticket ticket : ticketService.findByHome(securityController.getCurrentUser().getHome())) {
            listDTO.addElement(ticketToTicketConverter.convert(ticket));
        }
        return listDTO;
    }

    private ListDTO<RoommateDTO> getRoommateList(){

        ListDTO<RoommateDTO> listDTO = new ListDTO<>();

        for (Roommate roommate : roommateService.findByHome(securityController.getCurrentUser().getHome())) {
            listDTO.addElement(roommateToRoommateDTOConverter.convert(roommate));
        }


        return listDTO;
    }

    private ListDTO<ShoppingItemDTO> computeShoppingList() {
        ListDTO<ShoppingItemDTO> listDTO = new ListDTO<>();

        ShoppingItemToShoppingItemDTOConverter shoppingItemToShoppingItemDTOConverter = new ShoppingItemToShoppingItemDTOConverter(securityController.getCurrentUser());

        //load shopping list
        for (ShoppingItem shoppingItem : shoppingItemService.findByHome(securityController.getCurrentUser().getHome())) {
            if (shoppingItem.isWasBought() == false && (shoppingItem.getOnlyForMe() == false || shoppingItem.getCreator().equals(securityController.getCurrentUser()))) {
                listDTO.addElement(shoppingItemToShoppingItemDTOConverter.convert(shoppingItem));
            }
        }
        return listDTO;
    }

    private CountResumeDTO computeCount() {

        //compute values
        List<Ticket> myTickets = ticketService.findByPayer(securityController.getCurrentUser());

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
        List<TicketDebtor> ticketDebtorList = ticketDebtorService.findByRoommate(securityController.getCurrentUser());

        double dept = 0.0;
        for (TicketDebtor ticketDebtor : ticketDebtorList) {
            dept += ticketDebtor.getValue();
        }

        CountResumeDTO countResumeDTO = new CountResumeDTO();

        countResumeDTO.setRoommate(roommateToRoommateDTOConverter.convert(securityController.getCurrentUser()));
        countResumeDTO.setDept(dept);
        countResumeDTO.setSpend(spend);
        Logger.warn("countResumeDTO:"+countResumeDTO);
        return countResumeDTO;
    }
}
