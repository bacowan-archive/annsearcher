package moe.cowan.b.annsearcher.backend;

/**
 * Created by user on 18/07/2015.
 */
public enum Language {
    ENGLISH("EN"),
    JAPANESE("JA"),
    OTHER("OT");

    private String xmlRepresentation;

    Language(String xmlRepresentation) {
        this.xmlRepresentation = xmlRepresentation;
    }

    @Override
    public String toString() {
        return xmlRepresentation;
    }

    public static Language stringToLanguage(String str) {
        if (str.equals("EN"))
            return ENGLISH;
        else if (str.equals("JA"))
            return JAPANESE;
        return OTHER;
    }

}