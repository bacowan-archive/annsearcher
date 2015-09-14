package moe.cowan.b.annsearcher.backend.Database;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.Ids.StringIdSetter;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.database.AnnDatabaseProxy;
import moe.cowan.b.annsearcher.backend.database.InternalDatabaseProxy;
import moe.cowan.b.annsearcher.backend.database.RequestGetter;
import moe.cowan.b.annsearcher.backend.xml.XmlParser;

import static org.mockito.Mockito.*;

/**
 * Created by user on 27/07/2015.
 */
public class AnnDatabaseProxyTest extends InstrumentationTestCase {

    private AnnDatabaseProxy proxy;
    private RequestGetter getter;
    private InternalDatabaseProxy internalDatabaseProxy;
    private XmlParser<List<Anime>> parser;

    public void setUp() throws Exception {
        getter = mock(RequestGetter.class);
        parser = mock(XmlParser.class);
        internalDatabaseProxy = mock(InternalDatabaseProxy.class);
        when(internalDatabaseProxy.getPeopleOfTitle(any(Id.class))).thenReturn(new PeopleOfTitle());
        proxy = new AnnDatabaseProxy(getter, parser);
        proxy.setInternalProxy(internalDatabaseProxy);
    }

    public void testBasicGetPeopleOfTitleCallsCorrectUrl() throws Exception {
        String idString = "4658";
        Id id = new Id();
        StringIdSetter stringIdSetter = new StringIdSetter(StringIdKey.ANN);
        stringIdSetter.setString(id, idString);
        parserReturnSingleAnime();

        proxy.getPeopleOfTitle(id);

        verify(getter, times(1)).getRequestByUrl("http://cdn.animenewsnetwork.com/encyclopedia/api.xml?anime=" + idString);
    }

    public void test_getAllPeopleOfTitle_firstChecksInternalDB() throws Exception {
        Id id = new Id("1");
        PeopleOfTitle peopleOfTitle = new PeopleOfTitle();
        Person cast1 = new Person();
        peopleOfTitle.addCast(cast1);
        when(internalDatabaseProxy.getPeopleOfTitle(id)).thenReturn(peopleOfTitle);

        PeopleOfTitle returnedPeopleOfTitle = proxy.getPeopleOfTitle(id);

        assertEquals(peopleOfTitle, returnedPeopleOfTitle);
    }

    public void test_synonymsFromInternalDbAreUsedIfPossible() throws Exception {
        Id id = new Id("1");
        Anime fakeAnime = new Anime();
        fakeAnime.setId(id);
        Collection<Anime> fakeAnimeCollection = new ArrayList<>();
        fakeAnimeCollection.add(fakeAnime);
        String fakeSynonym = "one";
        Collection<String> fakeSynonyms = new ArrayList<>();
        fakeSynonyms.add(fakeSynonym);
        when(internalDatabaseProxy.getAnimeSynonyms(id)).thenReturn(fakeSynonyms);

        proxy.getAnnIdsForAnime(fakeAnimeCollection);

        assertEquals(1, fakeAnime.getSynonyms().size());
        assertTrue(fakeAnime.getSynonyms().contains(fakeSynonym));
    }

    public void test_synonymsFromInternalDbAreNotUsedIfNotPossible() throws Exception {
        Id id = new Id("1");
        Anime fakeAnime = new Anime();
        fakeAnime.setId(id);
        Collection<Anime> fakeAnimeCollection = new ArrayList<>();
        fakeAnimeCollection.add(fakeAnime);
        when(internalDatabaseProxy.getAnimeSynonyms(id)).thenReturn(new ArrayList<String>());
        parserReturnNoAnime();

        proxy.getAnnIdsForAnime(fakeAnimeCollection);

        assertEquals(0, fakeAnime.getSynonyms().size());
    }

    private void parserReturnSingleAnime() throws Exception {
        Anime anime = new Anime();
        List<Anime> animes = new ArrayList<>();
        animes.add(anime);
        when(parser.parse(anyString())).thenReturn(animes);
    }

    private void parserReturnNoAnime() throws Exception {
        when(parser.parse(anyString())).thenReturn(new ArrayList<Anime>());
    }

}
