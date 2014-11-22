package controllers.technical;

import dto.technical.DTO;
import dto.technical.PostedDTO;
import play.mvc.Controller;
import services.ErrorMessageService;

/**
 * Created by florian on 10/11/14.
 */
public class AbstractController extends Controller {

    protected final ErrorMessageService errorMessageService = new ErrorMessageService();
    protected final SecurityController securityController = new SecurityController();


    protected <T extends DTO> T extractDTOFromRequest(Class<T> DTOclass) {
        T dto = PostedDTO.getDTO(request().body().asJson(), DTOclass);
        if (dto == null) {
            throw new RuntimeException("The request content cannot be converted to a '" + DTOclass.getName() + "'.");
        }
        return dto;
    }
}
