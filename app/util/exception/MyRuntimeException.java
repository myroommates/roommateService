package util.exception;

import util.ErrorMessage;

/**
 * Created by florian on 10/11/14.
 */
public class MyRuntimeException  extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private ErrorMessage errorMessage;
    private Object[] params;

    public MyRuntimeException(ErrorMessage errorMessage, Object... params) {
        super();

        this.errorMessage = errorMessage;
        this.params = params;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public Object[] getParams() {
        return params;
    }
}

