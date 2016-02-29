package be.flo.roommateService.controllers;

import play.db.jpa.Transactional;
import be.flo.roommateService.controllers.technical.AbstractController;
import be.flo.roommateService.controllers.technical.SecurityController;
import be.flo.roommateService.converter.RoommateToInterfaceDataDTOConverter;
import be.flo.roommateService.converter.RoommateToRoommateDTOConverter;
import be.flo.roommateService.converter.ShoppingItemToShoppingItemDTOConverter;
import be.flo.roommateService.converter.TicketToTicketConverter;
import be.flo.roommateService.dto.ListDTO;
import be.flo.roommateService.dto.RoommateDTO;
import be.flo.roommateService.dto.ShoppingItemDTO;
import be.flo.roommateService.dto.TicketDTO;
import be.flo.roommateService.dto.count.CountResumeDTO;
import be.flo.roommateService.models.entities.Roommate;
import be.flo.roommateService.models.entities.ShoppingItem;
import be.flo.roommateService.models.entities.Ticket;
import be.flo.roommateService.models.entities.TicketDebtor;
import play.Logger;
import play.mvc.Result;
import play.mvc.Security;
import be.flo.roommateService.services.RoommateService;
import be.flo.roommateService.services.ShoppingItemService;
import be.flo.roommateService.services.TicketDebtorService;
import be.flo.roommateService.services.TicketService;
import be.flo.roommateService.services.impl.RoommateServiceImpl;
import be.flo.roommateService.services.impl.ShoppingItemServiceImpl;
import be.flo.roommateService.services.impl.TicketDebtorServiceImpl;
import be.flo.roommateService.services.impl.TicketServiceImpl;

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

    //be.flo.roommateService.converter
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
