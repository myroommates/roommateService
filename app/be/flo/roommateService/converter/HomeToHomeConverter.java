package be.flo.roommateService.converter;

import be.flo.roommateService.dto.HomeDTO;
import be.flo.roommateService.models.entities.Comment;
import be.flo.roommateService.models.entities.CommentLastVisualization;
import be.flo.roommateService.models.entities.Home;
import be.flo.roommateService.models.entities.Roommate;

import java.time.LocalDateTime;

/**
 * Created by florian on 11/11/14.
 */
public class HomeToHomeConverter implements ConverterInterface<Home,HomeDTO>{

    private CommentToCommentDTOConverter commentToCommentDTOConverter = new CommentToCommentDTOConverter();
    private Roommate currentRoommate;

    public HomeToHomeConverter(Roommate currentRoommate) {
        this.currentRoommate = currentRoommate;
    }

    public HomeDTO convert(Home entity) {
        HomeDTO dto = new HomeDTO();

        dto.setMoneySymbol(entity.getMoneySymbol());
        dto.setId(entity.getId());

        //comments
        for (Comment comment : entity.getComments()) {
            dto.addComment(commentToCommentDTOConverter.convert(comment));
        }

        //compute hasNewComment
        boolean hasNewComment = false;
        if (entity.getComments().size() > 0) {

            LocalDateTime lastVisualization = null;

            for (CommentLastVisualization commentLastVisualization : entity.getCommentLastVisualizations()) {
                if (commentLastVisualization.getRoommate().equals(currentRoommate)) {
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
