package moe.cowan.b.annsearcher.backend.database;

import android.content.Context;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.xml.MalAnimeXmlParser;
import moe.cowan.b.annsearcher.backend.xml.MalListXmlParser;
import moe.cowan.b.annsearcher.backend.xml.XmlParser;
import moe.cowan.b.annsearcher.exceptions.DatabaseRuntimeException;
import moe.cowan.b.annsearcher.exceptions.XmlParserException;

/**
 * Created by user on 01/08/2015.
 */
public class MalDatabaseProxy implements DatabaseListProxy, DatabaseSearchProxy {

    private String username = "";
    private String password = "";
    private RequestGetter requestGetter;
    private AuthenticatedRequestGetter authenticatedRequestGetter;
    private XmlParser<List<Anime>> malListXmlParser;
    private XmlParser<List<Anime>> malAnimeXmlParser;

    public MalDatabaseProxy() {
        this(null,null,null);
    }

    public MalDatabaseProxy(String username, String password, Context context) {
        this(new BasicRequestGetter(context), new AuthenticatedRequestGetter(context), new MalListXmlParser(), new MalAnimeXmlParser(), username, password);
    }

    public MalDatabaseProxy(RequestGetter getter, AuthenticatedRequestGetter authenticatedGetter, XmlParser<List<Anime>> malListParser, XmlParser<List<Anime>> malAnimeXmlParser, String username, String password) {
        this.requestGetter = getter;
        this.authenticatedRequestGetter = authenticatedGetter;
        this.malListXmlParser = malListParser;
        this.malAnimeXmlParser = malAnimeXmlParser;
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<Anime> getAllSeenAnime() {
        String xml = getListResponse();
        return parseAnimeResponse(xml);
    }

    private Collection<Anime> parseAnimeResponse(String xml) {
        try {
            return malListXmlParser.parse(xml);
        } catch (XmlPullParserException | IOException e) {
            throw new XmlParserException(e);
        }
    }

    private String getListResponse() {
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
        authenticatedRequestGetter.setContext(context);
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void resync() {

    }

    @Override
    public Collection<Anime> searchAnime(String searchString) {
        try {
            String response = getSearchResponse(searchString);
            return malAnimeXmlParser.parse(response);
        } catch (XmlPullParserException | IOException e) {
            throw new XmlParserException(e);
        }
    }

    private String getSearchResponse(String query) {
        try {
            return authenticatedRequestGetter.getRequestByUrl("http://myanimelist.net/api/anime/search.xml?q=" + query, username, password);
        } catch (ExecutionException |InterruptedException|TimeoutException e) {
            throw new DatabaseRuntimeException(e);
        }
    }

}
