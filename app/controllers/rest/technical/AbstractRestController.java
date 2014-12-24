package controllers.rest.technical;

import dto.RoommateDTO;
import dto.technical.DTO;
import dto.technical.verification.NotNull;
import dto.technical.verification.Pattern;
import dto.technical.verification.Size;
import models.entities.Language;
import net.sf.ehcache.hibernate.management.impl.BeanUtils;
import play.Logger;
import play.api.i18n.Messages;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import services.TranslationService;
import services.impl.TranslationServiceImpl;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

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

        //control dto
        try {
            validation(DTOclass, dto, securityRestController.getCurrentLanguage(ctx()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyRuntimeException(e.getMessage());
        }

        return dto;
    }

    private <T extends DTO> void validation(Class<T> DTOclass, T dto, Language language) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {


        Map<String,String> values = org.apache.commons.beanutils.BeanUtils.describe(dto);
        Form<T> photoForm = Form.form(DTOclass).bind(values);
        if (photoForm.hasErrors()){

            String errorMessage="";

            for (Map.Entry<String, List<ValidationError>> stringListEntry : photoForm.errors().entrySet()) {
                for (ValidationError validationError : stringListEntry.getValue()) {
                    errorMessage+= stringListEntry.getKey()+":"+play.i18n.Messages.get(lang(),validationError.message())+"<br/>";
                }
            }
            throw new MyRuntimeException(errorMessage);
        }
    }

}
