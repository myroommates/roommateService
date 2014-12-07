package util.exception;

import util.ErrorMessage;

/**
 * Created by florian on 10/11/14.
 */
public class MyRuntimeException  extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private String translatedMessage=null;
    private ErrorMessage errorMessage=null;
    private Object[] params;

    public MyRuntimeException(ErrorMessage errorMessage, Object... params) {
        super();

        this.errorMessage = errorMessage;
        this.params = params;
    }

    public MyRuntimeException(String translatedMessage) {
        super();
        this.translatedMessage = translatedMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public Object[] getParams() {
        return params;
    }

    public String getTranslatedMessage() {
        return translatedMessage;
    }
}

