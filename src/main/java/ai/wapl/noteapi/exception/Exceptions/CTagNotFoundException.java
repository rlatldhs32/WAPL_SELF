package ai.wapl.noteapi.exception.Exceptions;

public class CTagNotFoundException extends RuntimeException{
    public CTagNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CTagNotFoundException(String msg) {
        super(msg);
    }

    public CTagNotFoundException() {
        super();
    }
}
