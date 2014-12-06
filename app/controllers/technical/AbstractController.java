package controllers.technical;

import dto.technical.DTO;
import play.mvc.Controller;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

/**
 * Created by florian on 10/11/14.
 */
public class AbstractController extends Controller {

    //controllers
    protected final SecurityController securityController = new SecurityController();

    protected <T extends DTO> T extractDTOFromRequest(Class<T> DTOclass) {
        T dto = DTO.getDTO(request().body().asJson(), DTOclass);
        if (dto == null) {
            throw new MyRuntimeException(ErrorMessage.JSON_CONVERSION_ERROR,DTOclass.getName());
        }
        return dto;
    }
}
