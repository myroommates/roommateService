package controllers.impl;

import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.ShoppingItemToShoppingItemDTOConverter;
import dto.ListDTO;
import dto.ShoppingItemDTO;
import dto.technical.ResultDTO;
import model.entities.ShoppingItem;
import play.mvc.Result;
import play.mvc.Security;
import services.ShoppingItemService;
import services.impl.ShoppingItemServiceImpl;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

import java.util.List;

/**
 * Created by florian on 4/12/14.
 */
public class ShoppingController extends AbstractController {

    //service
    private ShoppingItemService shoppingItemService = new ShoppingItemServiceImpl();

    //converter
    private ShoppingItemToShoppingItemDTOConverter shoppingItemToShoppingItemDTOConverter = new ShoppingItemToShoppingItemDTOConverter();

    @Security.Authenticated(SecurityController.class)
    public Result getAll() {
        List<ShoppingItem> shoppingItemList = shoppingItemService.findByHome(securityController.getCurrentUser().getHome());

        ListDTO<ShoppingItemDTO> result = new ListDTO<>();

        for (ShoppingItem shoppingItem : shoppingItemList) {
            result.addElement(shoppingItemToShoppingItemDTOConverter.convert(shoppingItem));
        }
        return ok(result);
    }

    @Security.Authenticated(SecurityController.class)
    public Result getById(Long id) {

        //load
        ShoppingItem shoppingItem = shoppingItemService.findById(id);

        if (shoppingItem == null || !shoppingItem.getHome().equals(securityController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_SHOPPING_ITEM, id);
        }

        //convert
        return ok(shoppingItemToShoppingItemDTOConverter.convert(shoppingItem));
    }

    @Security.Authenticated(SecurityController.class)
    public Result create() {

        ShoppingItemDTO dto = extractDTOFromRequest(ShoppingItemDTO.class);

        //create
        ShoppingItem shoppingItem = new ShoppingItem();
        shoppingItem.setCreator(securityController.getCurrentUser());
        shoppingItem.setHome(securityController.getCurrentUser().getHome());
        shoppingItem.setDescription(dto.getDescription());

        shoppingItemService.saveOrUpdate(shoppingItem);

        return ok(shoppingItemToShoppingItemDTOConverter.convert(shoppingItem));

    }

    @Security.Authenticated(SecurityController.class)
    public Result update(Long id) {

        ShoppingItemDTO dto = extractDTOFromRequest(ShoppingItemDTO.class);

        //load
        ShoppingItem shoppingItem = shoppingItemService.findById(id);

        //control
        if (shoppingItem == null || !shoppingItem.getHome().equals(securityController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_SHOPPING_ITEM, id);
        }

        shoppingItem.setDescription(dto.getDescription());
        shoppingItem.setWasBought(dto.isWasBought());

        shoppingItemService.saveOrUpdate(shoppingItem);

        return ok(new ResultDTO());
    }

    @Security.Authenticated(SecurityController.class)
    public Result remove(Long id) {

        ShoppingItem shoppingItem = shoppingItemService.findById(id);

        if (shoppingItem != null && !shoppingItem.getHome().equals(securityController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_SHOPPING_ITEM, id);
        }

        if (shoppingItem != null) {
            shoppingItemService.remove(shoppingItem);
        }

        return ok(new ResultDTO());
    }

}
