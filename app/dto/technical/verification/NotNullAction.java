package dto.technical.verification;

import dto.technical.ExceptionDTO;
import org.springframework.stereotype.Service;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;

/**
 * Created by florian on 19/02/15.
 */
@Service
public class NotNullAction  extends Action<NotNull> {

    @Override
    public F.Promise<SimpleResult> call(Http.Context context) throws Throwable {

        return F.Promise.promise(new F.Function0<SimpleResult>() {
            @Override
            public SimpleResult apply() throws Throwable {
                return unauthorized(new ExceptionDTO("ee"));
            }
        });


    }

}
