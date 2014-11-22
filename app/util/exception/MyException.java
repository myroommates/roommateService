package util.exception;

/**
 * Created by florian on 10/11/14.
 */
public class MyException extends Exception{

    private static final long serialVersionUID = 1L;

    private final String toClientMessage;

    public MyException(String message) {
        super(message);
        toClientMessage = message;
    }

    public MyException(Throwable cause, String toClientMessage) {
        super(cause);
        this.toClientMessage = toClientMessage;
    }

    public String getToClientMessage() {
        return toClientMessage;
    }


}
