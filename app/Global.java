import dto.technical.ExceptionDTO;
import play.Application;
import play.GlobalSettings;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Results;
import play.mvc.SimpleResult;

/**
 * Created by florian on 10/11/14.
 */
public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application) {
    }

    @Override
    public F.Promise<SimpleResult> onError(Http.RequestHeader request, Throwable t) {

        t.printStackTrace();

        ExceptionDTO exceptionsDTO = new ExceptionDTO(t.getCause().getMessage());

        return F.Promise.<SimpleResult>pure(Results.internalServerError(exceptionsDTO
        ));
    }
}
