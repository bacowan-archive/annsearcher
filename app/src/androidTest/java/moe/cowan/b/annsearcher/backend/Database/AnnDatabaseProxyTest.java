package moe.cowan.b.annsearcher.backend.Database;

import android.test.InstrumentationTestCase;

import java.util.List;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.Ids.StringIdSetter;
import moe.cowan.b.annsearcher.backend.database.AnnDatabaseProxy;
import moe.cowan.b.annsearcher.backend.database.RequestGetter;
import moe.cowan.b.annsearcher.backend.xml.XmlParser;

import static org.mockito.Mockito.*;

/**
 * Created by user on 27/07/2015.
 */
public class AnnDatabaseProxyTest extends InstrumentationTestCase {

    private AnnDatabaseProxy proxy;
    private RequestGetter getter;
    private XmlParser<List<Anime>> parser;

    public void setUp() throws Exception {
        getter = mock(RequestGetter.class);
        parser = mock(XmlParser.class);
        proxy = new AnnDatabaseProxy(getter, parser);
    }

    public void testBasicGetCallsCorrectUrl() throws Exception {
        String idString = "4658";
        Id id = new Id();
        StringIdSetter stringIdSetter = new StringIdSetter(StringIdKey.ANN);
        stringIdSetter.setString(id, idString);

        proxy.getPeopleOfTitle(id);

        verify(getter, times(1)).getRequestByUrl("http://cdn.animenewsnetwork.com/encyclopedia/api.xml?anime=" + idString);
    }

    public void testGetAllPeopleOfTitle_firstChecksInternalDB() throws Exception {

    }

}
