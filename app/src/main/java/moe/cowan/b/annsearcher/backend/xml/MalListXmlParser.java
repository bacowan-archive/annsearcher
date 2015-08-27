package moe.cowan.b.annsearcher.backend.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.Ids.StringIdSetter;
import moe.cowan.b.annsearcher.backend.WatchingStatus;

/**
 * Created by user on 01/08/2015.
 */
public class MalListXmlParser implements XmlParser<List<Anime>> {
    // We don't use namespaces
    private static final String ns = null;
    private StringIdSetter idSetter = new StringIdSetter(StringIdKey.MAL);

    public List<Anime> parse(String xml) throws XmlPullParserException, IOException {
        return parse(new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
    }

    public List<Anime> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readMal(parser);
        } finally {
            in.close();
        }
    }

    private List<Anime> readMal(XmlPullParser parser) throws XmlPullParserException, IOException {

        List<Anime> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "myanimelist");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();

            if (name.equals("anime")) {
                entries.add(readAnime(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public Anime readAnime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "anime");

        Anime anime = new Anime();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("series_title")) {
                anime.setTitle(readTitle(parser));
            } else if (name.equals("series_animedb_id")) {
                anime.setId(readId(parser));
            } else if (name.equals("series_synonyms")) {
                anime.setSynonyms(readSynonyms(parser));
            } else if (name.equals("my_status")) {
                anime.setStatus(readStatus(parser));
            } else {
                skip(parser);
            }
        }

        return anime;
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "series_title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "series_title");
        return title;
    }

    private Id readId(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "series_animedb_id");
        String idStr = readText(parser);
        Id id = new Id();
        idSetter.setString(id, idStr);
        parser.require(XmlPullParser.END_TAG, ns, "series_animedb_id");
        return id;
    }

    private Collection<String> readSynonyms(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "series_synonyms");
        String unparsedSynonyms = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "series_synonyms");
        return parseSynonyms(unparsedSynonyms);
    }

    public static Collection<String> parseSynonyms(String unparsed) {
        List<String> parsedSynonyms = new LinkedList<>(Arrays.asList(unparsed.split("; ")));
        if (parsedSynonyms.contains(""))
            parsedSynonyms.remove("");
        return parsedSynonyms;
    }

    private WatchingStatus readStatus(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "my_status");
        int intStatus = Integer.parseInt(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, "my_status");
        return WatchingStatus.intToStatus(intStatus);
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
