package controllers.rest.technical;

import dto.technical.DTO;
import play.Logger;
import play.mvc.Controller;
import services.TranslationService;
import services.impl.TranslationServiceImpl;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by florian on 10/11/14.
 */
public class AbstractRestController extends Controller {

    //controllers
    protected final SecurityRestController securityRestController = new SecurityRestController();
    //service
    protected final TranslationService translationService = new TranslationServiceImpl();

    protected <T extends DTO> T extractDTOFromRequest(Class<T> DTOclass) {

        //extract dto
        T dto = DTO.getDTO(request().body().asJson(), DTOclass);
        if (dto == null) {
            throw new MyRuntimeException(ErrorMessage.JSON_CONVERSION_ERROR, DTOclass.getName());
        }

        Logger.info("dto:"+dto);

        //control dto
        try {
            validation(DTOclass, dto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyRuntimeException(e.getMessage());
        }

        return dto;
    }

    private <T extends DTO> void validation(Class<T> DTOclass, T dto) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        String errorMessage = "";

        for (Field field : DTOclass.getDeclaredFields()) {

            Object v = dto.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)).invoke(dto);

            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof NotNull) {

                    if (v == null) {
                        //build error message
                        errorMessage += translationService.getTranslation(((NotNull) annotation).message(), lang(), field.getName()) + "\n";
                    }
                } else if (annotation instanceof Pattern) {
                    if (!field.getType().equals(String.class)) {
                        throw new MyRuntimeException(ErrorMessage.DTO_VERIFICATION_PATTERN_STRING_EXPECTED, field.getName(), field.getType().getName());
                    }
                    String string;

                    if (v == null) {
                        string = "";
                    } else {
                        string = ((String) v);
                    }

                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(((Pattern) annotation).regexp());

                    if (!pattern.matcher(string).find()) {

                        //build error message
                        errorMessage += translationService.getTranslation(((Pattern) annotation).message(), lang(), field.getName(), ((Pattern) annotation).regexp()) + "\n";
                    }

                } else if (annotation instanceof Size) {

                    int min = ((Size) annotation).min();
                    int max = ((Size) annotation).max();

                    if (!field.getType().equals(String.class)) {
                        throw new MyRuntimeException(ErrorMessage.DTO_VERIFICATION_PATTERN_STRING_EXPECTED, field.getName(), field.getType().getName());
                    }
                    String string;

                    if (v == null) {
                        string = "";
                    } else {
                        string = ((String) v);
                    }

                    if (string.length() > max || string.length() < min) {

                        //build error message
                        errorMessage += translationService.getTranslation(((Size) annotation).message(), lang(), field.getName(), min, max) + "\n";
                    }

                }
            }

        }
        if (errorMessage.length() > 0) {
            throw new MyRuntimeException(errorMessage);
        }

    }

    }
