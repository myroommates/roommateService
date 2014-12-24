package dto.technical.verification;

import util.ErrorMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by florian on 7/12/14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Size {

    ErrorMessage message() default ErrorMessage.VALIDATION_SIZE;

    int min() default 1;

    int max() default 255;
}
