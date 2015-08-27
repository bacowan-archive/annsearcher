package moe.cowan.b.annsearcher.backend.Database;

import android.test.InstrumentationTestCase;

import java.util.List;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.Ids.StringIdSetter;
import moe.cowan.b.annsearcher.backend.database.AnnDatabaseProxy;
import moe.cowan.b.annsearcher.backend.database.MalDatabaseProxy;
import moe.cowan.b.annsearcher.backend.database.RequestGetter;
import moe.cowan.b.annsearcher.backend.xml.XmlParser;

import static org.mockito.Mockito.*;

/**
 * Created by user on 27/07/2015.
 */
public class MalDatabaseProxyTest extends InstrumentationTestCase {

    private MalDatabaseProxy proxy;
    private RequestGetter getter;
    private XmlParser<List<Anime>> listParser;
    private XmlParser<List<Anime>> animeParser;
    private String username = " ";

    public void setUp() throws Exception {
        getter = mock(RequestGetter.class);
        listParser = mock(XmlParser.class);
        animeParser = mock(XmlParser.class);
        proxy = new MalDatabaseProxy(getter, listParser, animeParser, username);
    }

    public void testBasicGetCallsCorrectUrl() throws Exception {
        proxy.getAllSeenAnime();

        verify(getter, times(1)).getRequestByUrl("http://myanimelist.net/malappinfo.php?u=" + username + "&status=all&type=anime");
    }

    public void testSearchCorrectUrl() throws Exception {
        proxy.getAllSeenAnime();
        String searchQuery = "fullmetal";

        verify(getter, times(1)).getRequestByUrl("http://myanimelist.net/api/anime|manga/search.xml?q=" + searchQuery);
    }

}
