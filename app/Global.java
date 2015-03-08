import controllers.technical.SecurityRestController;
import dto.technical.ExceptionDTO;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import play.Application;
import play.GlobalSettings;
import play.i18n.Lang;
import play.libs.Akka;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Results;
import play.mvc.SimpleResult;
import scala.concurrent.duration.Duration;
import services.TranslationService;
import services.impl.TranslationServiceImpl;
import util.exception.MyRuntimeException;

import java.util.concurrent.TimeUnit;

/**
 * Created by florian on 10/11/14.
 */
public class Global extends GlobalSettings {

    //services
    private TranslationService translationService = new TranslationServiceImpl();

    @Override
    public void onStart(Application app) {
        // run keepalive only in prod environment to avoid calls during test and dev targets
        if (app.isProd()) {
            final String hostname = System.getenv().get("Hostname");
            if (hostname != null) {

                Akka.system().scheduler().schedule(
                        Duration.create(10, TimeUnit.SECONDS),
                        Duration.create(10, TimeUnit.MINUTES),
                        new Runnable() {
                            public void run() {
                                try {
                                    play.Logger.info("Getting " + hostname + " for keep-alive ...");
                                    HttpClient httpClient = new DefaultHttpClient();
                                    HttpGet httpGet = new HttpGet(hostname);
                                    HttpResponse response = httpClient.execute(httpGet);
                                    play.Logger.info("Got " + hostname + " for keep-alive.");
                                } catch (Exception e) {
                                    play.Logger.info("Getting " + hostname + " for keep-alive ended with an exception", e);
                                }
                            }
                        },
                        Akka.system().dispatchers().defaultGlobalDispatcher()
                );
            } else {
                play.Logger.info("Akka keep-alive won't run because the environment variable 'AwacHostname' does not exist.");
            }
        } // end of app.isProd()
    }

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
}
