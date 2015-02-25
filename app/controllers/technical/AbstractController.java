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
            validation(dto, lang());

            for (Field field : DTOclass.getDeclaredFields()) {
                if (field.getDeclaringClass().isAssignableFrom(DTO.class)) {
                    Method method = dto.getClass().getMethod("get" + StringUtil.toFirstLetterUpper(field.getDeclaringClass().getName()));
                    Object o = method.invoke(dto);
                    if (o != null) {
                        validation((DTO) o, lang());
                    }
                }
                else if(field.getDeclaringClass().isAssignableFrom(Collection.class)) {
                    Collection<DTO> c;
                    //c.

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyRuntimeException(ErrorMessage.FATAL_ERROR, e.getMessage());
        }

        return dto;
    }

    private void validation(DTO dto, Lang language) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        String errorMessage = "";

        for (Field field : dto.getClass().getDeclaredFields()) {
            Logger.warn("field:" + field.getName());

            Object v = dto.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)).invoke(dto);

            for (Annotation annotation : field.getDeclaredAnnotations()) {
                Logger.warn("   annotation:" + annotation.toString());
                if (annotation instanceof NotNull) {

                    Logger.warn("       not null:" + v);
                    if (v == null) {

                        //build error message
                        errorMessage += translationService.getTranslation(((NotNull) annotation).message(), language) + "\n";
                    }
                } else if (annotation instanceof Pattern) {
                    if (!(v instanceof String)) {
                        throw new MyRuntimeException(ErrorMessage.DTO_VERIFICATION_PATTERN_STRING_EXPECTED, field.getName(), field.getType());
                    }
                    String string = ((String) v);

                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(((Pattern) annotation).regexp());

                    if (!pattern.matcher(string).find()) {

                        //build error message
                        errorMessage += translationService.getTranslation(((Pattern) annotation).message(), language, ((Pattern) annotation).regexp()) + "\n";
                    }

                } else if (annotation instanceof Size) {

                    int min = ((Size) annotation).min();
                    int max = ((Size) annotation).max();

                    if (!(v instanceof String)) {
                        throw new MyRuntimeException(ErrorMessage.DTO_VERIFICATION_PATTERN_STRING_EXPECTED, field.getName(), field.getType());
                    }
                    String string = ((String) v);

                    if (string.length() > max || string.length() < min) {

                        //build error message
                        errorMessage += translationService.getTranslation(((Size) annotation).message(), language, min, max) + "\n";
                    }

                }
            }

        }
        if (errorMessage.length() > 0) {
            throw new MyRuntimeException(errorMessage);
        }


    }
}
