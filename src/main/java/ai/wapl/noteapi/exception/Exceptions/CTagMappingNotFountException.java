package ai.wapl.noteapi.exception.Exceptions;

public class CTagMappingNotFountException extends RuntimeException{
    public CTagMappingNotFountException(String msg, Throwable t) {
        super(msg, t);
    }

    public CTagMappingNotFountException(String msg) {
        super(msg);
    }

    public CTagMappingNotFountException() {
        super();
    }
}
