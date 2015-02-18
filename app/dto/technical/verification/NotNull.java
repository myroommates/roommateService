package dto.technical.verification;

import util.ErrorMessage;

/**
 * Created by florian on 7/12/14.
 */
public @interface NotNull {

    ErrorMessage message() default ErrorMessage.VALIDATION_NOT_NULL;
}
