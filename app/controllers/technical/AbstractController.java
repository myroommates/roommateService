package controllers.technical;

import com.fasterxml.jackson.databind.JsonNode;
import dto.technical.DTO;
import dto.technical.verification.Pattern;
import dto.technical.verification.Size;
import play.Logger;
import play.i18n.Lang;
import play.mvc.Controller;
import services.TranslationService;
import services.impl.TranslationServiceImpl;
import util.ErrorMessage;
import util.StringUtil;
import util.exception.MyRuntimeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import dto.technical.verification.NotNull;

/**
 * Created by florian on 10/11/14.
 */
public abstract class AbstractController extends Controller {

    //controllers
    protected final CommonSecurityController securityController = new SecurityController();
    //service
    protected final TranslationService translationService = new TranslationServiceImpl();

    /**
     * this function control the dto (via play.validation annotation) and return it if it's valid, or throw a runtimeException with an error message if not.
     */
    protected <T extends DTO> T extractDTOFromRequest(Class<T> DTOclass) {

        //extract the json node
        JsonNode node = request().body().asJson();
        //extract dto
        T dto = DTO.getDTO(node, DTOclass);
        if (dto == null) {
            throw new MyRuntimeException(ErrorMessage.JSON_CONVERSION_ERROR, DTOclass.getName());
        }

        //control dto
        try {
            validDTO(dto);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new MyRuntimeException(e.getMessage());
        }

        return dto;
    }


    private void validDTO(DTO dto) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        Lang language = lang();

        Logger.warn("validDTO : " + dto);

        //control this object
        // validation(dto, lang());

        //looking for other DTO object
        for (Field field : dto.getClass().getDeclaredFields()) {

            Logger.warn("   test field " + field.getName() + "...=>" + field.getClass());

            //extract
            Object v = dto.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)).invoke(dto);

            //test annotation
            String errorMessage = "";
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                Logger.warn("          annotation:" + annotation.toString() + "(totla:" + field.getDeclaredAnnotations().length + ")");
                if (annotation instanceof NotNull) {

                    Logger.warn("             not null:" + v);
                    if (v == null) {

                        //build error message
                        errorMessage += translationService.getTranslation(((NotNull) annotation).message(), language, field.getName()) + "\n";
                    }
                } else if (annotation instanceof Pattern) {
                    if (v == null) {
                        //build error message
                        errorMessage += translationService.getTranslation(((Pattern) annotation).message(), language, field.getName(), ((Pattern) annotation).regexp()) + "\n";
                    } else {
                        if (!(v instanceof String)) {
                            throw new MyRuntimeException(ErrorMessage.DTO_VERIFICATION_PATTERN_STRING_EXPECTED, field.getName(), field.getType());
                        }
                        String string = ((String) v);

                        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(((Pattern) annotation).regexp());

                        if (!pattern.matcher(string).find()) {

                            //build error message
                            errorMessage += translationService.getTranslation(((Pattern) annotation).message(), language, field.getName(), ((Pattern) annotation).regexp()) + "\n";
                        }
                    }

                } else if (annotation instanceof Size) {

                    int min = ((Size) annotation).min();
                    int max = ((Size) annotation).max();

                    Logger.warn("vvv=>>"+v);

                    if (v != null) {

                        if (v instanceof Collection) {

                            Collection string = ((Collection) v);

                            if (string.size() > max || string.size() < min) {

                                //build error message
                                errorMessage += translationService.getTranslation(((Size) annotation).message(), language, field.getName(), min, max) + "\n";
                            }
                        } else if (v instanceof String) {
                            String string = ((String) v);

                            if (string.length() > max || string.length() < min) {

                                //build error message
                                errorMessage += translationService.getTranslation(((Size) annotation).message(), language, field.getName(), min, max) + "\n";
                            }
                        } else {
                            throw new MyRuntimeException(ErrorMessage.DTO_VERIFICATION_PATTERN_STRING_EXPECTED, field.getName(), field.getType());
                        }

                    }

                }
            }

            if (errorMessage.length() > 0) {
                throw new MyRuntimeException(errorMessage);
            }

            //test subElements
            if (v != null) {

                if (v instanceof DTO) {

                    Logger.warn("       is a DTO ! => validDTO...");

                    validDTO((DTO) v);
                } else if (v instanceof Collection) {

                    Logger.warn("       is a collection !");

                    for (Object obj : ((Collection<?>) v)) {

                        Logger.warn("           collection obj : " + obj);

                        if (obj instanceof DTO) {
                            Logger.warn("               is a dto !! call validDTO...");

                            validDTO((DTO) obj);
                        }
                    }
                }
            }
        }
    }
}
