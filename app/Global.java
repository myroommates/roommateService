import play.GlobalSettings;

/**
 * Created by florian on 10/11/14.
 */
public class Global extends GlobalSettings {
/*
    //be.flo.roommateService.services
    private TranslationService translationService = new TranslationServiceImpl();

    @Override
    public void onStart(Application app) {
    }
*/
/*
    @Override
    public F.Promise<SimpleResult> onError(Http.RequestHeader request, Throwable t) {

        //load language expected
        Lang language;
        if (request.getHeader(SecurityRestController.REQUEST_HEADER_LANGUAGE) != null) {
            language = Lang.forCode(request.getHeader(SecurityRestController.REQUEST_HEADER_LANGUAGE));
        }
        else {
            language = Lang.availables().get(0);
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
            exceptionsDTO = new ExceptionDTO(message);
        } else {
            exceptionsDTO = new ExceptionDTO(t.getCause().getMessage());
        }
        return F.Promise.<SimpleResult>pure(Results.internalServerError(exceptionsDTO));
    }
    */
}
