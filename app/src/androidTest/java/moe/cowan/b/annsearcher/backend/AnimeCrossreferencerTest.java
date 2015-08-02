package moe.cowan.b.annsearcher.backend;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.database.AnimeCrossreferencer;

/**
 * Created by user on 01/08/2015.
 */
public class AnimeCrossreferencerTest extends InstrumentationTestCase {

    private Person person0a = createPerson("person0", "0", "character1");
    private Person person0b = createPerson("person0", "0", "character2");
    private Person person1 = createPerson("person1", "1");
    private Person person2 = createPerson("person2", "2");

    private Anime otherAnime0 = createAnimeWithCast("0", person0a, person1);
    private Anime otherAnime1 = createAnimeWithCast("1", person0a, person0b);
    private Anime otherAnime2 = createAnimeWithCast("2", person1, person2);
    private Collection<Anime> allAnimes = animeCollection(otherAnime0, otherAnime1, otherAnime2);

    private AnimeCrossreferencer crossreferencer;

    private static Person createPerson(String name, String id, String role) {
        Person person = createPerson(name, id);
        person.setRole(role);
        return person;
    }

    private static Person createPerson(String name, String idString) {
        Person person = new Person();
        person.setName(name);
        Id id = new Id();
        id.addId(StringIdKey.INTERNAL, idString);
        person.setId(id);
        return person;
    }

    private static Anime createAnimeWithCast(String stringId, Person... people) {
        Anime anime = new Anime();
        Id id = new Id();
        id.addId(StringIdKey.INTERNAL, stringId);
        anime.setId(id);
        PeopleOfTitle peopleOfTitle = new PeopleOfTitle();
        for (Person person : people)
            peopleOfTitle.addCast(person);
        anime.setPeopleOfTitle(peopleOfTitle);
        return anime;
    }

    private static Collection<Anime> animeCollection(Anime... animes) {
        return new ArrayList<>(Arrays.asList(animes));
    }

    public void setUp() {
        crossreferencer = new AnimeCrossreferencer();
    }

    public void testBasicCrossreference() {
        Map<Anime, Collection<String>> crossreferenced = crossreferencer.getOtherCharactersActedBy(allAnimes, person0a);

        Map<Anime, Collection<String>> expected = new HashMap<>();
        Collection<String> matchesOfAnime0 = new ArrayList<>();
        matchesOfAnime0.add(person0a.getRole());
        expected.put(otherAnime0, matchesOfAnime0);
        Collection<String> matchesOfAnime1 = new ArrayList<>();
        matchesOfAnime1.add(person0a.getRole());
        matchesOfAnime1.add(person0b.getRole());
        expected.put(otherAnime1, matchesOfAnime1);

        assertMapsEqual(expected, crossreferenced);
    }

    private void assertMapsEqual(Map<Anime, Collection<String>> expected, Map<Anime, Collection<String>> actual) {
        assertEquals(expected.size(), actual.size());
        for (Anime expectedAnime : expected.keySet()) {
            assertTrue(actual.containsKey(expectedAnime));
            for (String expectedCast : expected.get(expectedAnime))
                assertTrue(actual.get(expectedAnime).contains(expectedCast));
        }
    }

}
