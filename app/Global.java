import controllers.technical.SecurityController;
import dto.technical.ExceptionDTO;
import model.entities.Language;
import play.Application;
import play.GlobalSettings;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Results;
import play.mvc.SimpleResult;
import services.TranslationService;
import services.impl.TranslationServiceImpl;
import util.exception.MyRuntimeException;

/**
 * Created by florian on 10/11/14.
 */
public class Global extends GlobalSettings {

    //services
    private TranslationService translationService = new TranslationServiceImpl();

    @Override
    public void onStart(Application application) {
    }

    @Override
    public F.Promise<SimpleResult> onError(Http.RequestHeader request, Throwable t) {

        //load language expected
        Language language = null;
        if (request.getHeader(SecurityController.REQUEST_HEADER_LANGUAGE) != null) {
            language = Language.getByAbrv(request.getHeader(SecurityController.REQUEST_HEADER_LANGUAGE));
        }
        language = Language.ENGLISH;

        final ExceptionDTO exceptionsDTO;

        if (t.getCause() instanceof MyRuntimeException) {
            MyRuntimeException exception = ((MyRuntimeException) t.getCause());
            String message = translationService.getTranslation(exception.getErrorMessage(),language,exception.getParams() );
            exceptionsDTO = new ExceptionDTO(message);
        } else {
            exceptionsDTO = new ExceptionDTO(t.getCause().getMessage());
        }

        return F.Promise.<SimpleResult>pure(Results.internalServerError(exceptionsDTO));
    }
}
