package moe.cowan.b.annsearcher.backend.XmlParsers;

import android.content.res.AssetFileDescriptor;
import android.test.InstrumentationTestCase;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.StringIdGetter;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.xml.AnnXmlParser;
import moe.cowan.b.annsearcher.exceptions.AnimeNotFoundException;

/**
 * Created by user on 26/07/2015.
 */
public class AnnXmlParserTest extends InstrumentationTestCase {

    private AnnXmlParser parser;
    private static final StringIdGetter annIdGetter = new StringIdGetter(StringIdKey.ANN);

    public void setUp() throws Exception {
        parser = new AnnXmlParser();
    }

    public void test_ValidSingleAnimeParse() throws Exception {
        InputStream exampleAnimeInput = getAnnExampleInputStream();
        Anime parsedAnime = parser.parse(exampleAnimeInput).get(0);
        assertAnimeAreRecursivelyEqual(AnnExampleAnimeBuilder.buildAnime(), parsedAnime);
    }

    public void test_AnimeNotFound_ThrowsException() throws Exception {
        String exampleInput = "<ann><warning>no result for anime=1</warning></ann>";
        try {
            parser.parse(exampleInput);
            fail();
        } catch (AnimeNotFoundException e) {}
    }

    private InputStream getAnnExampleInputStream() throws Exception {
        return getInstrumentation().getContext().getResources().getAssets().open("annExample.xml");
    }

    private void assertAnimeAreRecursivelyEqual(Anime expectedAnime, Anime anime2) {
        assertEquals(expectedAnime.getTitle(), anime2.getTitle());
        assertEquals(annIdGetter.getStringId(expectedAnime.getId()), annIdGetter.getStringId(anime2.getId()));
        assertAllCastEqual(expectedAnime.getPeopleOfTitle(), anime2.getPeopleOfTitle());
        assertAllStaffEqual(expectedAnime.getPeopleOfTitle(), anime2.getPeopleOfTitle());
    }

    private void assertAllCastEqual(PeopleOfTitle expectedAnimePeople, PeopleOfTitle anime2People) {
        Set<Person> anime1CastSet = new HashSet<>(expectedAnimePeople.getCast());
        Set<Person> anime2CastSet = new HashSet<>(anime2People.getCast());
        assertEquals(anime1CastSet.size(), anime2CastSet.size());
        for (Person anime1Person : anime1CastSet) {
            Person anime2Person = getOtherPerson(anime1Person, anime2CastSet);
            assertAllPeopleAreRecursivelyEqual(anime1Person, anime2Person);
        }
    }

    private void assertAllStaffEqual(PeopleOfTitle expectedAnimePeople, PeopleOfTitle anime2People) {
        Set<Person> anime1StaffSet = new HashSet<>(expectedAnimePeople.getStaff());
        Set<Person> anime2StaffSet = new HashSet<>(anime2People.getStaff());
        assertEquals(anime1StaffSet.size(), anime2StaffSet.size());
        for (Person anime1Person : anime1StaffSet) {
            Person anime2Person = getOtherPerson(anime1Person, anime2StaffSet);
            assertAllPeopleAreRecursivelyEqual(anime1Person, anime2Person);
        }
    }

    private Person getOtherPerson(Person expectedPerson, Collection<Person> otherPersons) {
        for (Person otherPerson : otherPersons) {
            if (annIdGetter.getStringId(otherPerson.getId()).equals(annIdGetter.getStringId(expectedPerson.getId())))
                return otherPerson;
        }
        fail("A person in the expected anime was not present in the actual anime: " + expectedPerson.getName());
        return null;
    }

    private void assertAllPeopleAreRecursivelyEqual(Person p1, Person p2) {
        assertEquals(annIdGetter.getStringId(p1.getId()), annIdGetter.getStringId(p2.getId()));
        assertEquals(p1.getLanguage(), p2.getLanguage());
        assertEquals(p1.getName(), p2.getName());
        assertEquals(p1.getRole(), p2.getRole());
    }

}
