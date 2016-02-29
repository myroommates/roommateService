package converter;

import controllers.technical.CommonSecurityController;
import controllers.technical.SecurityRestController;
import dto.ShoppingItemDTO;
import models.entities.Comment;
import models.entities.CommentLastVisualization;
import models.entities.Roommate;
import models.entities.ShoppingItem;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by florian on 4/12/14.
 */
public class ShoppingItemToShoppingItemDTOConverter implements ConverterInterface<ShoppingItem, ShoppingItemDTO> {

    private CommentToCommentDTOConverter commentToCommentDTOConverter = new CommentToCommentDTOConverter();
    private Roommate currentUser;

    public ShoppingItemToShoppingItemDTOConverter(Roommate currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public ShoppingItemDTO convert(ShoppingItem entity) {
        ShoppingItemDTO dto = new ShoppingItemDTO();


        dto.setId(entity.getId());
        dto.setCreationDate(Date.from(entity.getCreationDate().atZone(ZoneId.systemDefault()).toInstant()));
        dto.setOnlyForMe(entity.getOnlyForMe());
        dto.setCreatorId(entity.getCreator().getId());
        dto.setDescription(entity.getDescription());
        dto.setHomeId(entity.getHome().getId());
        dto.setWasBought(entity.isWasBought());

        //comments
        for (Comment comment : entity.getComments()) {
            dto.addComment(commentToCommentDTOConverter.convert(comment));
        }

        //compute hasNewComment
        boolean hasNewComment = false;
        if (entity.getComments().size() > 0) {

            LocalDateTime lastVisualization = null;

            for (CommentLastVisualization commentLastVisualization : entity.getCommentLastVisualizations()) {
                if (commentLastVisualization.getRoommate().equals(currentUser)) {
                    lastVisualization = commentLastVisualization.getDate();
                    break;
                }
            }

            if (lastVisualization == null) {
                hasNewComment=true;
            } else {
                for (Comment comment : entity.getComments()) {
                    if(comment.getCreationDate().compareTo(lastVisualization)>0){
                        hasNewComment=true;
                        break;
                    }
                    for (Comment comment1 : comment.getChildren()) {
                        if(comment1.getCreationDate().compareTo(lastVisualization)>0){
                            hasNewComment=true;
                            break;
                        }
                    }

                }
            }
        }
        dto.setHasNewComment(hasNewComment);


        return dto;
    }
}
