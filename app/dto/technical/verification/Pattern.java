package dto.technical.verification;

import util.ErrorMessage;

/**
 * Created by florian on 7/12/14.
 */
public @interface Pattern {
    /**
     * RFC 5322 EMAIL VALIDATION
     */
    public static final String EMAIL = "^[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$;";
    public static final String PASSWORD = "^[a-zA-Z0-9_-]{6,18}$";

    public String regexp() default "^.*$";

    ErrorMessage message() default ErrorMessage.VALIDATION_PATTERN;
}
