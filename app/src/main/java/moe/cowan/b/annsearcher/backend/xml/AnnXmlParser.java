package moe.cowan.b.annsearcher.backend.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.Ids.StringIdSetter;
import moe.cowan.b.annsearcher.backend.Language;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.backend.Person;

/**
 * Created by user on 26/07/2015.
 */
public class AnnXmlParser {
    // We don't use namespaces
    private static final String ns = null;
    private StringIdSetter idSetter = new StringIdSetter(StringIdKey.ANN);

    public List<Anime> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readAnn(parser);
        } finally {
            in.close();
        }
    }

    private List<Anime> readAnn(XmlPullParser parser) throws XmlPullParserException, IOException {

        List<Anime> entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "ann");
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
        anime.setTitle(parser.getAttributeValue(null, "name"));
        Id id = new Id();
        idSetter.setString(id, parser.getAttributeValue(null, "id"));
        anime.setId(id);
        PeopleOfTitle peopleOfTitle = new PeopleOfTitle();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("staff")) {
                peopleOfTitle.addStaff( readStaff(parser) );
            } else if (name.equals("cast")) {
                peopleOfTitle.addCast( readCast(parser) );
            } else {
                skip(parser);
            }
        }

        anime.setPeopleOfTitle(peopleOfTitle);

        parser.require(XmlPullParser.END_TAG, ns, "anime");
        return anime;
    }

    private Person readStaff(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "staff");

        Person person = null;
        String role = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("task")) {
                role = readTask(parser);
            } else if (name.equals("person")) {
                person = readPerson(parser);
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "staff");

        person.setRole(role);

        return person;
    }

    private Person readCast(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "cast");

        Person person = null;
        String role = null;
        Language language = Language.stringToLanguage( parser.getAttributeValue(null, "lang") );

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("role")) {
                role = readRole(parser);
            } else if (name.equals("person")) {
                person = readPerson(parser);
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "cast");

        person.setRole(role);
        person.setLanguage(language);

        return person;
    }

    private String readTask(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "task");
        String task = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "task");
        return task;
    }

    private String readRole(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "role");
        String role = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "role");
        return role;
    }

    private Person readPerson(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "person");

        Person person = new Person();
        Id id = new Id();
        idSetter.setString(id, parser.getAttributeValue(null, "id"));
        person.setId(id);
        person.setName(readText(parser));

        parser.require(XmlPullParser.END_TAG, ns, "person");
        return person;
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
