package converter;

import dto.ShoppingItemDTO;
import models.entities.Comment;
import models.entities.ShoppingItem;

/**
 * Created by florian on 4/12/14.
 */
public class ShoppingItemToShoppingItemDTOConverter implements ConverterInterface<ShoppingItem, ShoppingItemDTO>{

    private CommentToCommentDTOConverter commentToCommentDTOConverter = new CommentToCommentDTOConverter();

    @Override
    public ShoppingItemDTO convert(ShoppingItem entity) {
        ShoppingItemDTO dto = new ShoppingItemDTO();

        dto.setId(entity.getId());
        dto.setCreationDate(entity.getCreationDate());
        dto.setOnlyForMe(entity.getOnlyForMe());
        dto.setCreatorId(entity.getCreator().getId());
        dto.setDescription(entity.getDescription());
        dto.setHomeId(entity.getHome().getId());
        dto.setWasBought(entity.isWasBought());

        //comments
        for (Comment comment : entity.getComments()) {
            dto.addComment(commentToCommentDTOConverter.convert(comment));
        }


        return dto;
    }
}
