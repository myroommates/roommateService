package be.flo.roommateService.controllers;

import be.flo.roommateService.controllers.technical.SecurityController;
import play.db.jpa.Transactional;
import be.flo.roommateService.controllers.technical.AbstractController;
import be.flo.roommateService.converter.RoommateToInterfaceDataDTOConverter;
import be.flo.roommateService.converter.RoommateToRoommateDTOConverter;
import be.flo.roommateService.converter.ShoppingItemToShoppingItemDTOConverter;
import be.flo.roommateService.dto.ListDTO;
import be.flo.roommateService.dto.RoommateDTO;
import be.flo.roommateService.dto.ShoppingItemDTO;
import be.flo.roommateService.models.entities.Roommate;
import be.flo.roommateService.models.entities.ShoppingItem;
import play.mvc.Result;
import play.mvc.Security;
import be.flo.roommateService.services.RoommateService;
import be.flo.roommateService.services.ShoppingItemService;
import be.flo.roommateService.services.impl.RoommateServiceImpl;
import be.flo.roommateService.services.impl.ShoppingItemServiceImpl;

/**
 * Created by florian on 25/12/14.
 */
public class ShoppingController extends AbstractController {

    //convert
    private RoommateToRoommateDTOConverter roommateToRoommateDTOConverter = new RoommateToRoommateDTOConverter();
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

        ShoppingItemToShoppingItemDTOConverter shoppingItemToShoppingItemDTOConverter = new ShoppingItemToShoppingItemDTOConverter(securityController.getCurrentUser());

       // load all ticket
        for (ShoppingItem shoppingItem : shoppingItemService.findByHome(securityController.getCurrentUser().getHome())) {
            if(shoppingItem.isWasBought()==false && (shoppingItem.getOnlyForMe()==false || shoppingItem.getCreator().equals(securityController.getCurrentUser()))) {
                shoppingItemDTOList.addElement(shoppingItemToShoppingItemDTOConverter.convert(shoppingItem));
            }
        }


        return ok(views.html.home.shopping.shoppingList.render(roommateToInterfaceDataDTOConverter.convert(securityController.getCurrentUser()),roommateDTOListDTO,shoppingItemDTOList));
    }
}
