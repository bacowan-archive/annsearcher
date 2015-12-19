package moe.cowan.b.annsearcher.backend.Database;

import android.app.Instrumentation;

import java.util.ArrayList;
import java.util.Collection;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.Language;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.WatchingStatus;

import static junit.framework.Assert.*;

/**
 * Created by user on 03/09/2015.
 */
public class Utils {

    public static final String TEST_ANIME_TITLE = "anime_title";
    public static final String TEST_ANIME_SYNONYM_1 = "a";
    public static final String TEST_ANIME_SYNONYM_2 = "b";
    public static final WatchingStatus TEST_ANIME_WATCHING_STATUS = WatchingStatus.WATCHING;
    public static final String INTERNAL_ID = "0";
    public static final String ANN_ID = "1";
    public static final String MAL_ID = "2";
    public static final Id TEST_ANIME_ID;
    public static final Person CAST1;
    public static final String CAST1_ID_STR = "3";
    public static final Id CAST1_ID = new Id(CAST1_ID_STR);
    public static final String CAST1_ROLE = "character1";
    public static final Language CAST1_LANGUAGE = Language.ENGLISH;
    public static final String CAST1_NAME = "person1";
    public static final Person CAST2;
    public static final String CAST2_ID_STR = "4";
    public static final Id CAST2_ID = new Id(CAST2_ID_STR);
    public static final String CAST2_ROLE = "character2";
    public static final Language CAST2_LANGUAGE = Language.JAPANESE;
    public static final String CAST2_NAME = "person2";
    public static final Person STAFF1;
    public static final String STAFF1_ID_STR = "5";
    public static final Id STAFF1_ID = new Id(STAFF1_ID_STR);
    public static final String STAFF1_ROLE = "staff1";
    public static final String STAFF1_NAME = "person3";
    public static final Person STAFF2;
    public static final String STAFF2_ID_STR = "6";
    public static final Id STAFF2_ID = new Id(STAFF2_ID_STR);
    public static final String STAFF2_ROLE = "staff2";
    public static final String STAFF2_NAME = "person4";

    public static final Anime TEST_ANIME;

    static {
        TEST_ANIME_ID = new Id();
        TEST_ANIME_ID.addId(StringIdKey.INTERNAL, INTERNAL_ID);
        TEST_ANIME_ID.addId(StringIdKey.ANN, ANN_ID);
        TEST_ANIME_ID.addId(StringIdKey.MAL, MAL_ID);
        CAST1 = new Person();
        CAST1.setId(CAST1_ID);
        CAST1.setRole(CAST1_ROLE);
        CAST1.setLanguage(CAST1_LANGUAGE);
        CAST1.setName(CAST1_NAME);
        CAST2 = new Person();
        CAST2.setId(CAST2_ID);
        CAST2.setRole(CAST2_ROLE);
        CAST2.setLanguage(CAST2_LANGUAGE);
        CAST2.setName(CAST2_NAME);
        STAFF1 = new Person();
        STAFF1.setId(STAFF1_ID);
        STAFF1.setRole(STAFF1_ROLE);
        STAFF1.setName(STAFF1_NAME);
        STAFF2 = new Person();
        STAFF2.setId(STAFF2_ID);
        STAFF2.setRole(STAFF2_ROLE);
        STAFF2.setName(STAFF2_NAME);

        TEST_ANIME = new Anime();
        TEST_ANIME.setTitle(TEST_ANIME_TITLE);
        TEST_ANIME.setId(TEST_ANIME_ID);
        PeopleOfTitle animePeople = new PeopleOfTitle();
        animePeople.addCast(CAST1);
        animePeople.addCast(CAST2);
        animePeople.addStaff(STAFF1);
        animePeople.addStaff(STAFF2);
        TEST_ANIME.setPeopleOfTitle(animePeople);
        Collection<String> synonyms = new ArrayList<>();
        synonyms.add(TEST_ANIME_SYNONYM_1);
        synonyms.add(TEST_ANIME_SYNONYM_2);
        TEST_ANIME.setSynonyms(synonyms);
        TEST_ANIME.setStatus(TEST_ANIME_WATCHING_STATUS);
    }

    public static void assertOtherAnimeSame(Anime otherAnime) {
        assertEquals(TEST_ANIME_TITLE, otherAnime.getTitle());

        assertEquals(INTERNAL_ID, otherAnime.getId().getString(StringIdKey.INTERNAL));
        assertEquals(ANN_ID, otherAnime.getId().getString(StringIdKey.ANN));
        assertEquals(MAL_ID, otherAnime.getId().getString(StringIdKey.MAL));

        PeopleOfTitle peopleOfTitle = otherAnime.getPeopleOfTitle();
        Collection<Person> castOfTitle = peopleOfTitle.getCast();
        Collection<Person> staffOfTitle = peopleOfTitle.getStaff();

        assertEquals(2, castOfTitle.size());
        assertEquals(2, staffOfTitle.size());
        assertCollectionContainsPerson(castOfTitle, CAST1);
        assertCollectionContainsPerson(castOfTitle, CAST2);
        assertCollectionContainsPerson(staffOfTitle, STAFF1);
        assertCollectionContainsPerson(staffOfTitle, STAFF2);

        assertEquals(2, otherAnime.getSynonyms().size());
        assertTrue(otherAnime.getSynonyms().contains(TEST_ANIME_SYNONYM_1));
        assertTrue(otherAnime.getSynonyms().contains(TEST_ANIME_SYNONYM_2));

        assertEquals(TEST_ANIME_WATCHING_STATUS, otherAnime.getStatus());
    }

    private static void assertCollectionContainsPerson(Collection<Person> collection, Person person) {
        Person expectedPerson = findPersonInCollection(collection, person);
        assertEquals(expectedPerson.getName(), person.getName());
        assertEquals(expectedPerson.getId().getString(StringIdKey.INTERNAL), person.getId().getString(StringIdKey.INTERNAL));
        assertEquals(expectedPerson.getId().getString(StringIdKey.ANN), person.getId().getString(StringIdKey.ANN));
        assertEquals(expectedPerson.getId().getString(StringIdKey.MAL), person.getId().getString(StringIdKey.MAL));
        assertEquals(expectedPerson.getRole(), person.getRole());
        assertEquals(expectedPerson.getLanguage(), expectedPerson.getLanguage());
    }

    private static Person findPersonInCollection(Collection<Person> collection, Person person) {
        for (Person p : collection) {
            if (p.getId().getString(StringIdKey.INTERNAL).equals(person.getId().getString(StringIdKey.INTERNAL)))
                return p;
        }
        fail(person.toString() + " not found in anime");
        return null;
    }

    public static Anime createBlankAnime() {
        Anime anime = new Anime();
        anime.setTitle("");
        anime.setId(new Id());
        anime.setPeopleOfTitle(new PeopleOfTitle());
        anime.setSynonyms(new ArrayList<String>());
        anime.setStatus(WatchingStatus.COMPLETED);
        return anime;
    }

    public static void assertSynonymsAreCorrect(Collection<String> synonyms) {
        assertEquals(2, synonyms.size());
        assertTrue(synonyms.contains(TEST_ANIME_SYNONYM_1));
        assertTrue(synonyms.contains(TEST_ANIME_SYNONYM_2));
    }

    public static void assertPeopleOfTitleAreCorrect(PeopleOfTitle peopleOfTitle) {
        assertStaffOfTitleAreCorrect(peopleOfTitle.getStaff());
        assertCastOfTitleAreCorrect(peopleOfTitle.getCast());
    }

    private static void assertStaffOfTitleAreCorrect(Collection<Person> staff) {
        Collection<String> names = namesOfPeople(staff);
        assertEquals(2, names.size());
        assertTrue(names.contains(STAFF1_NAME));
        assertTrue(names.contains(STAFF2_NAME));
    }

    private static void assertCastOfTitleAreCorrect(Collection<Person> cast) {
        Collection<String> names = namesOfPeople(cast);
        assertEquals(2, names.size());
        assertTrue(names.contains(CAST1_NAME));
        assertTrue(names.contains(CAST2_NAME));
    }

    private static Collection<String> namesOfPeople(Collection<Person> persons) {
        Collection<String> names = new ArrayList<>();
        for (Person p : persons) {
            names.add(p.getName());
        }
        return names;
    }

    public static void setupDexmaker(Instrumentation instrumentation) {
        System.setProperty(
                "dexmaker.dexcache",
                instrumentation.getTargetContext().getCacheDir().getPath());
    }


}
