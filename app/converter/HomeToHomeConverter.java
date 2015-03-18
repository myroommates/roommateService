package converter;

import dto.HomeDTO;
import models.entities.Comment;
import models.entities.Home;

/**
 * Created by florian on 11/11/14.
 */
public class HomeToHomeConverter implements ConverterInterface<Home,HomeDTO>{

    private CommentToCommentDTOConverter commentToCommentDTOConverter = new CommentToCommentDTOConverter();

    public HomeDTO convert(Home home) {
        HomeDTO dto = new HomeDTO();

        dto.setMoneySymbol(home.getMoneySymbol());
        dto.setId(home.getId());

        //comments
        for (Comment comment : home.getComments()) {
            dto.addComment(commentToCommentDTOConverter.convert(comment));
        }

        return dto;
    }
}
