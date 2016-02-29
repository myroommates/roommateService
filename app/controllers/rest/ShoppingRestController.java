package controllers.rest;

import play.db.jpa.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityRestController;
import converter.ShoppingItemToShoppingItemDTOConverter;
import dto.ListDTO;
import dto.ShoppingItemDTO;
import dto.technical.ResultDTO;
import models.entities.ShoppingItem;
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
public class ShoppingRestController extends AbstractController {

    //service
    private ShoppingItemService shoppingItemService = new ShoppingItemServiceImpl();


    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result setWasBought(String ids) {

        //control
        for (String s : ids.split(",")) {
            Long id = Long.parseLong(s);

            ShoppingItem shoppingItem = shoppingItemService.findById(id);

            //control => from my home and I'm concerned
            if (shoppingItem == null ||
                    !shoppingItem.getHome().equals(securityController.getCurrentUser().getHome()) ||
                    (shoppingItem.getOnlyForMe() && !shoppingItem.getCreator().getId().equals(securityController.getCurrentUser().getId()))) {
                throw new MyRuntimeException(ErrorMessage.NOT_YOU_SHOPPING_ITEM);
            }

            shoppingItem.setWasBought(true);

            shoppingItemService.saveOrUpdate(shoppingItem);
        }
        return ok(new ResultDTO());
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result getAll() {
        List<ShoppingItem> shoppingItemList = shoppingItemService.findByHome(securityController.getCurrentUser().getHome());

        ListDTO<ShoppingItemDTO> result = new ListDTO<>();

        //converter
        ShoppingItemToShoppingItemDTOConverter shoppingItemToShoppingItemDTOConverter = new ShoppingItemToShoppingItemDTOConverter(securityController.getCurrentUser());

        for (ShoppingItem shoppingItem : shoppingItemList) {
            result.addElement(shoppingItemToShoppingItemDTOConverter.convert(shoppingItem));
        }
        return ok(result);
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result getById(Long id) {

        //load
        ShoppingItem shoppingItem = shoppingItemService.findById(id);

        if (shoppingItem == null || !shoppingItem.getHome().equals(securityController.getCurrentUser().getHome())) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_SHOPPING_ITEM, id);
        }
        //converter
        ShoppingItemToShoppingItemDTOConverter shoppingItemToShoppingItemDTOConverter = new ShoppingItemToShoppingItemDTOConverter(securityController.getCurrentUser());

        //convert
        return ok(shoppingItemToShoppingItemDTOConverter.convert(shoppingItem));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result create() {

        ShoppingItemDTO dto = extractDTOFromRequest(ShoppingItemDTO.class);

        //create
        ShoppingItem shoppingItem = new ShoppingItem();
        shoppingItem.setCreator(securityController.getCurrentUser());
        shoppingItem.setHome(securityController.getCurrentUser().getHome());
        shoppingItem.setDescription(dto.getDescription());
        shoppingItem.setOnlyForMe(dto.getOnlyForMe());

        shoppingItemService.saveOrUpdate(shoppingItem);
        //converter
        ShoppingItemToShoppingItemDTOConverter shoppingItemToShoppingItemDTOConverter = new ShoppingItemToShoppingItemDTOConverter(securityController.getCurrentUser());

        return ok(shoppingItemToShoppingItemDTOConverter.convert(shoppingItem));

    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result update(Long id) {

        ShoppingItemDTO dto = extractDTOFromRequest(ShoppingItemDTO.class);

        //load
        ShoppingItem shoppingItem = shoppingItemService.findById(id);

        //control => from my home and I'm concerned
        if (shoppingItem == null ||
                !shoppingItem.getHome().equals(securityController.getCurrentUser().getHome()) ||
                (shoppingItem.getOnlyForMe() && !shoppingItem.getCreator().getId().equals(securityController.getCurrentUser().getId()))) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_SHOPPING_ITEM, id);
        }

        shoppingItem.setDescription(dto.getDescription());
        shoppingItem.setWasBought(dto.getWasBought());
        shoppingItem.setOnlyForMe(dto.getOnlyForMe());

        shoppingItemService.saveOrUpdate(shoppingItem);
        //converter
        ShoppingItemToShoppingItemDTOConverter shoppingItemToShoppingItemDTOConverter = new ShoppingItemToShoppingItemDTOConverter(securityController.getCurrentUser());

        return ok(shoppingItemToShoppingItemDTOConverter.convert(shoppingItem));
    }

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result remove(Long id) {

        ShoppingItem shoppingItem = shoppingItemService.findById(id);

        //control => from my home and I'm concerned
        if (shoppingItem == null ||
                !shoppingItem.getHome().equals(securityController.getCurrentUser().getHome()) ||
                (shoppingItem.getOnlyForMe() && !shoppingItem.getCreator().getId().equals(securityController.getCurrentUser().getId()))) {
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_SHOPPING_ITEM, id);
        }

        if (shoppingItem != null) {
            shoppingItemService.remove(shoppingItem);
        }

        return ok(new ResultDTO());
    }

}
