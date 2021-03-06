package moe.cowan.b.annsearcher.backend.Database;

import android.test.InstrumentationTestCase;

import java.util.List;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.database.AuthenticatedRequestGetter;
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
    private AuthenticatedRequestGetter authenticatedGetter;
    private XmlParser<List<Anime>> listParser;
    private XmlParser<List<Anime>> animeParser;
    private String username = "DoomInAJar";
    private String password = "password";

    public void setUp() throws Exception {
        Utils.setupDexmaker(getInstrumentation());
        getter = mock(RequestGetter.class);
        authenticatedGetter = mock(AuthenticatedRequestGetter.class);
        listParser = mock(XmlParser.class);
        animeParser = mock(XmlParser.class);
        proxy = new MalDatabaseProxy(getter, authenticatedGetter, listParser, animeParser, username, password);
    }

    public void testBasicGetCallsCorrectUrl() throws Exception {
        proxy.getAllSeenAnime();

        verify(getter, times(1)).getRequestByUrl("http://myanimelist.net/malappinfo.php?u=" + username + "&status=all&type=anime");
    }
}
