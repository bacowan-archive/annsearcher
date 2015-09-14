package moe.cowan.b.annsearcher.backend.Database;

import android.test.InstrumentationTestCase;

import java.util.Collection;
import java.util.Map;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.database.internalDatabase.SqliteDatabaseProxy;
import moe.cowan.b.annsearcher.exceptions.NoInternalIdException;

/**
 * Created by user on 02/09/2015.
 */
public class SqlDatabaseTest extends InstrumentationTestCase {

    private static final String tempDatabaseName = "__tempdb__.db";

    private SqliteDatabaseProxy proxy;

    public void setUp() {
        proxy = new SqliteDatabaseProxy(getInstrumentation().getTargetContext(), tempDatabaseName);
    }

    public void test_AddAnime_StoresAnime() {
        proxy.addAnime(Utils.TEST_ANIME);
        Anime returnedAnime = proxy.getAnime(Utils.TEST_ANIME.getId());
        Utils.assertOtherAnimeSame(returnedAnime);
    }

    public void test_AddDuplicateAnime_KeepsValuesSame() {
        proxy.addAnime(Utils.TEST_ANIME);
        proxy.addAnime(Utils.TEST_ANIME);
        Anime returnedAnime = proxy.getAnime(Utils.TEST_ANIME.getId());
        Utils.assertOtherAnimeSame(returnedAnime);
    }

    public void test_ChangedAnimeValues_UpdatesValues() {
        String initialTitle = "title1";
        String secondTitle = "title2";

        Anime changeAnime = animeWithTitle(initialTitle);
        proxy.addAnime(changeAnime);
        changeAnime.setTitle(secondTitle);
        proxy.addAnime(changeAnime);

        Anime returnedAnime = proxy.getAnime(changeAnime.getId());

        assertEquals(secondTitle, returnedAnime.getTitle());
    }

    public void test_CrossReferenceVoiceActor() {
        proxy.addAnime(Utils.TEST_ANIME);

        Map<String, Collection<String>> animeCharacterPairs = proxy.crossReferenceActor(Utils.CAST1);

        assertEquals(1, animeCharacterPairs.size());
        assertTrue(animeCharacterPairs.containsKey(Utils.TEST_ANIME_TITLE));
        assertEquals(1, animeCharacterPairs.get(Utils.TEST_ANIME_TITLE).size());
        assertTrue(animeCharacterPairs.get(Utils.TEST_ANIME_TITLE).contains(Utils.CAST1_ROLE));
    }

    public void test_GetAnimeSynonyms() {
        proxy.addAnime(Utils.TEST_ANIME);
        Collection<String> synonyms = proxy.getAnimeSynonyms(Utils.TEST_ANIME_ID);
        Utils.assertSynonymsAreCorrect(synonyms);
    }

    public void test_GetPeopleOfTitle() {
        proxy.addAnime(Utils.TEST_ANIME);
        PeopleOfTitle peopleOfTitle = proxy.getPeopleOfTitle(Utils.TEST_ANIME_ID);
        Utils.assertPeopleOfTitleAreCorrect(peopleOfTitle);
    }

    public void test_AddAnimeWithNoInternalId_ThrowsException() {
        try {
            proxy.addAnime(new Anime());
            fail();
        } catch (NoInternalIdException e) {}
    }

    public void test_getAnimeWithNoInternalId_ReturnsBlank() {
        Anime anime = proxy.getAnime(new Id());
        assertNull(anime);
    }

    public void test_CrossReferenceActorWithNoInternalId_ReturnsBlank() {
        Map<String, Collection<String>> crossreferences = proxy.crossReferenceActor(new Person());
        assertTrue(crossreferences.isEmpty());
    }

    public void test_GetSynonymsWithNoInternalId_ReturnsBlank() {
        Collection<String> synonyms = proxy.getAnimeSynonyms(new Id());
        assertTrue(synonyms.isEmpty());
    }

    public void test_GetPeopleOfTitleWithNoInternalId_ReturnsBlank() {
        PeopleOfTitle people = proxy.getPeopleOfTitle(new Id());
        assertTrue(people.getCast().isEmpty());
        assertTrue(people.getStaff().isEmpty());
    }

    public void tearDown() {
        proxy.close();
        getInstrumentation().getTargetContext().deleteDatabase(tempDatabaseName);
    }

    private Anime animeWithTitle(String title) {
        Id id = new Id("111");
        Anime anime = Utils.createBlankAnime();
        anime.setId(id);
        anime.setTitle(title);
        return anime;
    }

}
