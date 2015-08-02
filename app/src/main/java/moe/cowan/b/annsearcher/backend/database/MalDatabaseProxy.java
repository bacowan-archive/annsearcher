package moe.cowan.b.annsearcher.backend.database;

import android.content.Context;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.xml.MalXmlParser;
import moe.cowan.b.annsearcher.backend.xml.XmlParser;
import moe.cowan.b.annsearcher.exceptions.DatabaseRuntimeException;
import moe.cowan.b.annsearcher.exceptions.XmlParserException;

/**
 * Created by user on 01/08/2015.
 */
public class MalDatabaseProxy implements DatabaseListProxy {

    private String username = "";
    private Context context;
    private RequestGetter requestGetter;
    private XmlParser<List<Anime>> xmlParser;

    public MalDatabaseProxy() {
        this(null,null);
    }

    public MalDatabaseProxy(String username, Context context) {
        this(new BasicRequestGetter(context), new MalXmlParser(), username);
    }

    public MalDatabaseProxy(RequestGetter getter, XmlParser<List<Anime>> parser, String username) {
        this.requestGetter = getter;
        this.xmlParser = parser;
        this.username = username;
    }

    @Override
    public Collection<Anime> getAllSeenAnime() {
        String xml = getResponse(username);
        return parseAnimeResponse(xml);
    }

    private Collection<Anime> parseAnimeResponse(String xml) {
        try {
            return xmlParser.parse(xml);
        } catch (XmlPullParserException |IOException e) {
            throw new XmlParserException(e);
        }
    }

    private String getResponse(String username) {
        try {
            return requestGetter.getRequestByUrl("http://myanimelist.net/malappinfo.php?u=" + username + "&status=all&type=anime");
        } catch (ExecutionException |InterruptedException|TimeoutException e) {
            throw new DatabaseRuntimeException(e);
        }
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void setContext(Context context) {
        requestGetter.setContext(context);
    }

    @Override
    public void resync() {

    }
}
