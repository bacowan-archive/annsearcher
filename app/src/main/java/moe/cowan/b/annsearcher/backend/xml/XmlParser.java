package moe.cowan.b.annsearcher.backend.xml;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by user on 27/07/2015.
 */
public interface XmlParser<OutputType> {

    OutputType parse(InputStream in) throws XmlPullParserException, IOException;

}
