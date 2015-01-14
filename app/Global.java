import controllers.technical.SecurityRestController;
import dto.technical.ExceptionDTO;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.i18n.Lang;
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
        Lang language;
        if (request.getHeader(SecurityRestController.REQUEST_HEADER_LANGUAGE) != null) {
            language = Lang.forCode(request.getHeader(SecurityRestController.REQUEST_HEADER_LANGUAGE));
        }
        else {
            language = request.acceptLanguages().get(0);
        }

        final ExceptionDTO exceptionsDTO;

        if (t.getCause() instanceof MyRuntimeException) {
            MyRuntimeException exception = ((MyRuntimeException) t.getCause());
            String message;

            if (exception.getMessage() != null) {
                message = exception.getMessage();
            } else {
                message = translationService.getTranslation(exception.getErrorMessage(), language, exception.getParams());
            }
            Logger.error("exceptionMessage:"+message);
            exceptionsDTO = new ExceptionDTO(message);
        } else {
            exceptionsDTO = new ExceptionDTO(t.getCause().getMessage());
        }

        return F.Promise.<SimpleResult>pure(Results.internalServerError(exceptionsDTO));
    }
}
