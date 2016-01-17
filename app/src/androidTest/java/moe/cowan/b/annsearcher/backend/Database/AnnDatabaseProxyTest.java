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
        Anime anime = new Anime();
        anime.setId(id);
        StringIdSetter stringIdSetter = new StringIdSetter(StringIdKey.ANN);
        stringIdSetter.setString(id, idString);
        parserReturnSingleAnime();

        proxy.getPeopleOfTitle(anime);

        verify(getter, times(1)).getRequestByUrl("http://cdn.animenewsnetwork.com/encyclopedia/api.xml?anime=" + idString);
    }

    public void test_getAllPeopleOfTitle_firstChecksInternalDB() throws Exception {
        Id id = new Id("1");
        Anime anime = new Anime();
        anime.setId(id);
        PeopleOfTitle peopleOfTitle = new PeopleOfTitle();
        Person cast1 = new Person();
        peopleOfTitle.addCast(cast1);
        when(internalDatabaseProxy.getPeopleOfTitle(id)).thenReturn(peopleOfTitle);

        PeopleOfTitle returnedPeopleOfTitle = proxy.getPeopleOfTitle(anime);

        assertEquals(peopleOfTitle, returnedPeopleOfTitle);
    }

    public void test_getAnimeInformation_AddsNewInformationToInputAnime() throws Exception {
        Anime incompleteAnime = new Anime();
        Id incompleteId = new Id(Utils.INTERNAL_ID);
        incompleteAnime.setId(incompleteId);
        Collection<Anime> animes = new ArrayList<>();
        animes.add(incompleteAnime);

        when(internalDatabaseProxy.getAnime(incompleteId)).thenReturn(Utils.TEST_ANIME);

        proxy.getAnimeInformation(animes);

        Utils.assertOtherAnimeSame(incompleteAnime);
    }

    public void test_getAnimeInformation_DoesNotOverwriteOldAnimeInformation() throws Exception {
        Anime incompleteAnime = new Anime();
        Id incompleteId = new Id(Utils.INTERNAL_ID);
        incompleteAnime.setId(incompleteId);
        List<Anime> animes = new ArrayList<>();
        animes.add(incompleteAnime);
        String extraSynonym = "TEST_SYNONYM";
        incompleteAnime.addSynonym(extraSynonym);

        when(internalDatabaseProxy.getAnime(incompleteId)).thenReturn(Utils.TEST_ANIME);

        proxy.getAnimeInformation(animes);

        assertTrue(animes.get(0).getSynonyms().contains(extraSynonym));
    }

    public void test_getAnimeInformation_DoesNotIncludeDuplicateInformation() throws Exception {
        Anime incompleteAnime = new Anime();
        Id incompleteId = new Id(Utils.INTERNAL_ID);
        incompleteAnime.setId(incompleteId);
        List<Anime> animes = new ArrayList<>();
        animes.add(incompleteAnime);
        incompleteAnime.addSynonym(Utils.TEST_ANIME_SYNONYM_1);

        when(internalDatabaseProxy.getAnime(incompleteId)).thenReturn(Utils.TEST_ANIME);

        proxy.getAnimeInformation(animes);

        Utils.assertOtherAnimeSame(incompleteAnime);
    }

    public void test_getAnimeInformation_DoesNotUseInternalInformationIfNotPossible() throws Exception {
        Anime incompleteAnime = new Anime();
        Id incompleteId = new Id(Utils.INTERNAL_ID);
        StringIdSetter setter = new StringIdSetter(StringIdKey.ANN);
        setter.setString(incompleteId, "2");
        incompleteAnime.setId(incompleteId);
        List<Anime> animes = new ArrayList<>();
        animes.add(incompleteAnime);
        incompleteAnime.addSynonym(Utils.TEST_ANIME_SYNONYM_1);


        when(parser.parse(anyString())).thenReturn(animes);
        when(internalDatabaseProxy.getAnime(incompleteId)).thenReturn(null);

        proxy.getAnimeInformation(animes);

        verify(getter, atLeastOnce()).getRequestByUrl(anyString());
    }

    public void test_getAnimeInformation_UsesInternalInformationIfPossible() throws Exception {
        Anime incompleteAnime = new Anime();
        Id incompleteId = new Id(Utils.INTERNAL_ID);
        incompleteAnime.setId(incompleteId);
        List<Anime> animes = new ArrayList<>();
        animes.add(incompleteAnime);
        incompleteAnime.addSynonym(Utils.TEST_ANIME_SYNONYM_1);

        when(internalDatabaseProxy.getAnime(incompleteId)).thenReturn(Utils.TEST_ANIME);

        proxy.getAnimeInformation(animes);

        verify(getter, never()).getRequestByUrl(anyString());
    }

    public void test_CacheAnime_CallsAddAnimeForAll() throws Exception {
        Collection<Anime> animes = new ArrayList<>();
        Anime anime1 = Utils.createBlankAnime();
        Anime anime2 = Utils.createBlankAnime();
        animes.add(anime1);
        animes.add(anime2);

        proxy.cacheAnime(animes);

        verify(internalDatabaseProxy).addAnime(anime1);
        verify(internalDatabaseProxy).addAnime(anime2);
    }

    public void test_GetPeopleOfTitleFromAnn_FirstGetsAnnId() throws Exception {
        String idVal = "1";
        String title = "title";
        Id id = new Id(idVal);
        Anime anime = new Anime();
        anime.setId(id);
        anime.setTitle(title);
        List<Anime> animeList = new ArrayList<>();
        animeList.add(anime);
        when(internalDatabaseProxy.getPeopleOfTitle(id)).thenReturn(new PeopleOfTitle());
        when(parser.parse(anyString())).thenReturn(animeList);

        proxy.getPeopleOfTitle(anime);

        verify(getter).getRequestByUrl("http://cdn.animenewsnetwork.com/encyclopedia/api.xml?title=~" + title);
    }

    public void test_GetPeopleOfTitleFromAnn_DoesNotGetAnnIdsIfAlreadyHas() throws Exception {
        String idVal = "1";
        String title = "title";
        Id id = new Id();
        StringIdSetter setter = new StringIdSetter(StringIdKey.ANN);
        setter.setString(id, idVal);
        Anime anime = new Anime();
        anime.setId(id);
        anime.setTitle(title);
        List<Anime> animeList = new ArrayList<>();
        animeList.add(anime);
        when(internalDatabaseProxy.getPeopleOfTitle(id)).thenReturn(new PeopleOfTitle());
        when(parser.parse(anyString())).thenReturn(animeList);

        proxy.getPeopleOfTitle(anime);

        verify(getter, never()).getRequestByUrl(matches(".*title=~.*"));
    }

    private void parserReturnSingleAnime() throws Exception {
        Anime anime = new Anime();
        Id id = new Id("1");
        anime.setId(id);
        List<Anime> animes = new ArrayList<>();
        animes.add(anime);
        when(parser.parse(anyString())).thenReturn(animes);
    }

    private void parserReturnNoAnime() throws Exception {
        when(parser.parse(anyString())).thenReturn(new ArrayList<Anime>());
    }

}
