package util.exception;

import util.ErrorMessage;

import java.util.Arrays;

/**
 * Created by florian on 10/11/14.
 */
public class MyRuntimeException  extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private ErrorMessage errorMessage=null;
    private Object[] params;

    public MyRuntimeException(ErrorMessage errorMessage, Object... params) {
        super();

        this.errorMessage = errorMessage;
        this.params = params;
    }

    public MyRuntimeException(String message) {
        super(message);
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public Object[] getParams() {
        return params;
    }

    @Override
    public String toString() {
        return "MyRuntimeException{" +super.toString()+","+
                "errorMessage=" + errorMessage +
                ", params=" + Arrays.toString(params) +
                '}';
    }

}

