package moe.cowan.b.annsearcher.exceptions;

/**
 * Created by user on 01/08/2015.
 */
public class XmlParserException extends RuntimeException {
    public XmlParserException(String message) {
        super(message);
    }
    public XmlParserException(Exception e) { super(e); }
}
