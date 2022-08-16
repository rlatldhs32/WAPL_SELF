package ai.wapl.noteapi.exception.Exceptions;

public class CChannelNotFoundException extends RuntimeException{
    public CChannelNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CChannelNotFoundException(String msg) {
        super(msg);
    }

    public CChannelNotFoundException() {
        super();
    }
}
