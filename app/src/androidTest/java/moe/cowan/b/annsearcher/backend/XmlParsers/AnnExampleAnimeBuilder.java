package moe.cowan.b.annsearcher.backend.XmlParsers;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.LinkedList;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.Language;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.backend.Person;

/**
 * Created by user on 26/07/2015.
 */
public class AnnExampleAnimeBuilder {

    public static Anime buildAnime() {
        Anime anime = new Anime();
        anime.setTitle("Jinki:Extend");
        anime.setPeopleOfTitle(buildPeopleOfTitle());
        anime.setId(buildAnimeId());
        return anime;
    }

    private static PeopleOfTitle buildPeopleOfTitle() {
        PeopleOfTitle people = new PeopleOfTitle();
        people.setCast(buildCastOfTitle());
        people.setStaff(buildStaffOfTitle());
        return people;
    }

    @NonNull
    private static Collection<Person> buildStaffOfTitle() {
        Collection<Person> staff = new LinkedList<>();
        staff.add(personNameRoleId("Masahiko Murata", "Director", "3610"));
        staff.add(personNameRoleId("Kinji Yoshimoto", "Storyboard", "4422"));
        staff.add(personNameRoleId("Kenji Kawai", "Music", "140"));
        return staff;
    }

    @NonNull
    private static Collection<Person> buildCastOfTitle() {
        Collection<Person> cast = new LinkedList<>();
        cast.add(personNameRoleLanguageId("Christine Auten", "Mel J", Language.ENGLISH, "1062"));
        cast.add(personNameRoleLanguageId("Jason Douglas", "Ryohei Ogawara", Language.ENGLISH, "1064"));
        cast.add(personNameRoleLanguageId("Satsuki Yukino", "Shizuka Tsuzaki", Language.JAPANESE, "278"));
        cast.add(personNameRoleLanguageId("Rokuro Naya", "Genta Ogawara", Language.JAPANESE, "458"));
        return cast;
    }

    private static Person personNameRoleId(String name, String role, String stringId) {
        return personNameRoleLanguageId(name, role, null, stringId);
    }

    private static Person personNameRoleLanguageId(String name, String role, Language language, String stringId) {
        Person person = new Person();
        person.setName(name);
        person.setRole(role);
        person.setLanguage(language);
        Id id = new Id();
        id.addId(StringIdKey.ANN, stringId);
        id.addId(StringIdKey.INTERNAL, stringId);
        person.setId(id);
        return person;
    }

    private static Id buildAnimeId() {
        Id id = new Id();
        id.addId(StringIdKey.ANN, "4658");
        id.addId(StringIdKey.INTERNAL, "");
        return id;
    }

}
