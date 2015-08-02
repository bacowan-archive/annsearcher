package moe.cowan.b.annsearcher.exceptions;

/**
 * Created by user on 01/08/2015.
 */
public class DatabaseRuntimeException extends RuntimeException {
    public DatabaseRuntimeException(String message) {
        super(message);
    }
    public DatabaseRuntimeException(Exception e) { super(e); }
}
