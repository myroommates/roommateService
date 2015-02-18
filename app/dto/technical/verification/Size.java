package dto.technical.verification;

import util.ErrorMessage;

/**
 * Created by florian on 7/12/14.
 */
public @interface Size {

    ErrorMessage message() default ErrorMessage.VALIDATION_SIZE;

    int min() default 1;

    int max() default 255;
}
