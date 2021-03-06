package be.flo.roommateService.dto.technical.verification;

import be.flo.roommateService.util.ErrorMessage;

import java.lang.annotation.*;

/**
 * Created by florian on 7/12/14.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {

    ErrorMessage message() default ErrorMessage.VALIDATION_NOT_NULL;
}
