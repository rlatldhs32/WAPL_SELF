package ai.wapl.noteapi.exception.Exceptions;

public class CNoteNotFoundException extends RuntimeException{
    public CNoteNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CNoteNotFoundException(String msg) {
        super(msg);
    }

    public CNoteNotFoundException() {
        super();
    }
}
