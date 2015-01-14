package controllers.technical;

import com.fasterxml.jackson.databind.JsonNode;
import dto.technical.DTO;
import play.mvc.Controller;
import services.TranslationService;
import services.impl.TranslationServiceImpl;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

/**
 * Created by florian on 10/11/14.
 */
public class AbstractController extends Controller {

    //controllers
    protected final CommonSecurityController securityController = new CommonSecurityController();
    //service
    protected final TranslationService translationService = new TranslationServiceImpl();
    /**
     * this function control the dto (via play.validation annotation) and return it if it's valid, or throw a runtimeException with an error message if not.
     * @param DTOclass
     * @param <T>
     * @return
     */
    protected <T extends DTO> T extractDTOFromRequest(Class<T> DTOclass) {

        //extract the json node
        JsonNode node = request().body().asJson();
/*
        //control dto
        try {
            //build form from dot
            Form<T> postForm = Form.form(DTOclass).bind(node);

            //test error
            if (postForm.hasErrors()) {

                String errorMessage = "";

                //build error message
                for (Map.Entry<String, List<ValidationError>> validationError : postForm.errors().entrySet()) {
                    errorMessage += validationError.getKey() + " : ";
                    boolean first = true;
                    for (ValidationError error : validationError.getValue()) {
                        if (first) {
                            first = false;
                        } else {
                            errorMessage += ",";
                        }
                        errorMessage += Messages.get(lang(), error.message());
                    }
                    errorMessage += " / ";
                }

                //throw the exception
                throw new MyRuntimeException(errorMessage);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyRuntimeException(e.getMessage());
        }
*/
        //extract dto
        T dto = DTO.getDTO(node, DTOclass);
        if (dto == null) {
            throw new MyRuntimeException(ErrorMessage.JSON_CONVERSION_ERROR, DTOclass.getName());
        }

        return dto;
    }
}
