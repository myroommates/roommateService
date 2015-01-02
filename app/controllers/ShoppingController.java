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
 * Created by florian on 25/12/14.
 */
public class ShoppingController extends AbstractController {

    //convert
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
    private ShoppingItemToShoppingItemDTOConverter shoppingItemToShoppingItemDTOConverter= new ShoppingItemToShoppingItemDTOConverter();
    private RoommateToInterfaceDataDTOConverter roommateToInterfaceDataDTOConverter = new RoommateToInterfaceDataDTOConverter();

    //service
    private RoommateService roommateService = new RoommateServiceImpl();
    private ShoppingItemService shoppingItemService = new ShoppingItemServiceImpl();

    @Security.Authenticated(SecurityController.class)
    @Transactional
    public Result list() {

        //load and convert roommate
        ListDTO<RoommateDTO> roommateDTOListDTO  = new ListDTO<>();

        for (Roommate roommate : roommateService.findByHome(securityController.getCurrentUser().getHome())) {
            roommateDTOListDTO.addElement(roommateToRoommateDTOConverter.convert(roommate));


        }
        ListDTO<ShoppingItemDTO> shoppingItemDTOList = new ListDTO<>();

       // load all ticket
        for (ShoppingItem shoppingItem : shoppingItemService.findByHome(securityController.getCurrentUser().getHome())) {
            if(shoppingItem.isWasBought()==false && (shoppingItem.getOnlyForMe()==false || shoppingItem.getCreator().equals(securityController.getCurrentUser()))) {
                shoppingItemDTOList.addElement(shoppingItemToShoppingItemDTOConverter.convert(shoppingItem));
            }
        }


        return ok(views.html.home.shopping.shoppingList.render(roommateToInterfaceDataDTOConverter.convert(securityController.getCurrentUser()),roommateDTOListDTO,shoppingItemDTOList));
    }
}
