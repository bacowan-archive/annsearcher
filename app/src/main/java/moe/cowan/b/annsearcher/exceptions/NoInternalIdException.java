package moe.cowan.b.annsearcher.exceptions;

/**
 * Created by Brendan on 9/13/2015.
 */
public class NoInternalIdException extends RuntimeException {
    public NoInternalIdException(String message) {
        super(message);
    }
    public NoInternalIdException(Exception e) { super(e); }
}