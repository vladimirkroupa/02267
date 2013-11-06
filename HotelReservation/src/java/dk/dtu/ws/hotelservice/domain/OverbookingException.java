package dk.dtu.ws.hotelservice.domain;

public class OverbookingException extends Exception {

    public OverbookingException() {
    }

    public OverbookingException(String message) {
        super(message);
    }

    public OverbookingException(String message, Throwable cause) {
        super(message, cause);
    }

    public OverbookingException(Throwable cause) {
        super(cause);
    }

    public OverbookingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
