package moe.cowan.b.annsearcher.exceptions;

/**
 * Created by KDCowan on 12/28/2015.
 */
public class AnimeNotFoundException extends RuntimeException {
    public AnimeNotFoundException(String message) {
        super(message);
    }
    public AnimeNotFoundException(Exception e) { super(e); }
}