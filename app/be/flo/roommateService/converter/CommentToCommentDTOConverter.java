package be.flo.roommateService.converter;

import be.flo.roommateService.dto.CommentDTO;
import be.flo.roommateService.models.entities.Comment;

/**
 * Created by florian on 18/03/15.
 */
public class CommentToCommentDTOConverter implements ConverterInterface<Comment,CommentDTO> {


    @Override
    public CommentDTO convert(Comment entity) {

        CommentDTO dto = new CommentDTO();

        dto.setId(entity.getId());
        dto.setComment(entity.getComment());
        dto.setDateCreation(entity.getDateCreation());
        dto.setDateEdit(entity.getDateEdit());
        dto.setCreatorId(entity.getCreator().getId());
        if(entity.getParent()!=null) {
            dto.setParentId(entity.getParent().getId());
        }

        return dto;
    }
}
