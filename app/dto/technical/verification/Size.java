package dto.technical.verification;

import play.mvc.With;
import util.ErrorMessage;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by florian on 7/12/14.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Size {

    ErrorMessage message() default ErrorMessage.VALIDATION_SIZE;

    int min() default 1;

    int max() default 255;
}
