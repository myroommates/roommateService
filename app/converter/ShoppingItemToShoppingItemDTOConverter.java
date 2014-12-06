package converter;

import dto.ShoppingItemDTO;
import model.entities.ShoppingItem;

/**
 * Created by florian on 4/12/14.
 */
public class ShoppingItemToShoppingItemDTOConverter implements ConverterInterface<ShoppingItem, ShoppingItemDTO>{
    @Override
    public ShoppingItemDTO convert(ShoppingItem entity) {
        ShoppingItemDTO dto = new ShoppingItemDTO();

        dto.setCreationDate(entity.getCreationDate());
        dto.setCreatorId(entity.getCreator().getId());
        dto.setDescription(entity.getDescription());
        dto.setHomeId(entity.getHome().getId());
        dto.setWasBought(entity.isWasBought());

        return dto;
    }
}
