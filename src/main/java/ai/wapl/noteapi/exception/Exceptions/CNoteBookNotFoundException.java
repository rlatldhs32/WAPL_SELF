package ai.wapl.noteapi.exception.Exceptions;

public class CNoteBookNotFoundException extends RuntimeException{
    public CNoteBookNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CNoteBookNotFoundException(String msg) {
        super(msg);
    }

    public CNoteBookNotFoundException() {
        super();
    }
}
