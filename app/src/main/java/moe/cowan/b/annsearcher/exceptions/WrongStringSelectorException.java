package moe.cowan.b.annsearcher.exceptions;

/**
 * Created by user on 19/07/2015.
 */
public class WrongStringSelectorException extends RuntimeException {
    public WrongStringSelectorException(String message) {
        super(message);
    }
    public WrongStringSelectorException(Exception e) { super(e); }
}