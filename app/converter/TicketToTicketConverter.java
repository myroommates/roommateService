package converter;

import controllers.technical.CommonSecurityController;
import dto.TicketDTO;
import dto.TicketDebtorDTO;
import models.entities.Comment;
import models.entities.CommentLastVisualization;
import models.entities.Ticket;
import models.entities.TicketDebtor;

import java.util.Date;

/**
 * Created by florian on 11/11/14.
 */
public class TicketToTicketConverter implements ConverterInterface<Ticket, TicketDTO> {

    private CommonSecurityController securityController;

    private CommentToCommentDTOConverter commentToCommentDTOConverter = new CommentToCommentDTOConverter();

    public TicketToTicketConverter(CommonSecurityController securityController) {
        this.securityController = securityController;
    }

    public TicketDTO convert(Ticket entity) {

        TicketDTO dto = new TicketDTO();

        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setDate(entity.getDate());

        dto.setCategory(entity.getCategory());

        dto.setPayerId(entity.getPayer().getId());

        for (TicketDebtor ticketDebtor : entity.getDebtorList()) {
            dto.addTicketDebtor(new TicketDebtorDTO(ticketDebtor.getRoommate().getId(),ticketDebtor.getValue()));
        }

        //comments
        for (Comment comment : entity.getComments()) {
            dto.addComment(commentToCommentDTOConverter.convert(comment));
        }

        //compute hasNewComment
        boolean hasNewComment = false;
        if (entity.getComments().size() > 0) {

            Date lastVisualization = null;

            for (CommentLastVisualization commentLastVisualization : entity.getCommentLastVisualizations()) {
                if (commentLastVisualization.getRoommate().equals(securityController.getCurrentUser())) {
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
