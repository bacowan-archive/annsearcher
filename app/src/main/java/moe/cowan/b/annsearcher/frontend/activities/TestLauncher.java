package moe.cowan.b.annsearcher.frontend.activities;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.inject.AbstractModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Language;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.database.AnimeCrossreferencer;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.exceptions.TitleNotFoundException;
import roboguice.RoboGuice;

/**
 * Created by user on 18/07/2015.
 */
public class TestLauncher extends LauncherActivity {

    @Override
    protected DatabaseProxy getDatabaseProxy() {
        setOtherMocks();
        return new MockDatabaseProxy();
    }

    private void setOtherMocks() {
        RoboGuice.overrideApplicationInjector(getApplication(), new MyTestModule());
    }

    public class MyTestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(AnimeCrossreferencer.class).toInstance(new MockCrossref());
        }

        private class MockCrossref extends AnimeCrossreferencer {
            @Override
            public Map<Anime, Collection<String>> getOtherCharactersActedBy(Collection<Anime> otherAnime, Person person) {
                Map<Anime, Collection<String>> returnVal = new HashMap<>();
                Anime anime1 = new Anime();
                anime1.setTitle("An Anime");
                Anime anime2 = new Anime();
                anime2.setTitle("Yay, more anime");
                String character1 = "Character 1";
                String character2 = "Character 2";
                String character3 = "Character 3";
                Collection anime1Characters = new HashSet();
                anime1Characters.add(character1);
                Collection anime2Characters = new HashSet();
                anime2Characters.add(character2);
                anime2Characters.add(character3);
                returnVal.put(anime1, anime1Characters);
                returnVal.put(anime2, anime2Characters);
                return returnVal;
            }
        }
    }

    private static class MockDatabaseProxy implements DatabaseProxy {

        @Override
        public void setUsername(String username) {

        }

        @Override
        public String getUsername() {
            return "";
        }

        @Override
        public void setContext(Context context) {

        }

        @Override
        public PeopleOfTitle getPeopleOfTitle(Id animeId) throws TitleNotFoundException {
            PeopleOfTitle people = new PeopleOfTitle();
            people.setCast( createCast() );
            return people;
        }

        private List<Person> createCast() {
            List<Person> people = new ArrayList<>();
            people.add(personWithNameRoleAndId("person1", "character1", "3"));
            people.add(personWithNameRoleAndId("person2", "character2", "4"));
            return people;
        }

        private Person personWithNameRoleAndId(String name, String role, String id) {
            Person person = new Person();
            person.setId(new Id(id));
            person.setName(name);
            person.setLanguage(Language.JAPANESE);
            person.setRole(role);
            return person;
        }

        @Override
        public Collection<Anime> getAllSeenAnime() {
            Collection<Anime> animes = new ArrayList<>();
            animes.add(animeWithTitleAndId("Title1", "0"));
            animes.add(animeWithTitleAndId("Title2", "1"));
            return animes;
        }

        private Anime animeWithTitleAndId(String title, String id) {
            Anime anime = new Anime();
            anime.setTitle(title);
            anime.setId(new Id(id));
            return anime;
        }

        @Override
        public void resync() {

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }

        public static final Parcelable.Creator<MockDatabaseProxy> CREATOR =
                new Parcelable.Creator<MockDatabaseProxy>() {

                    @Override
                    public MockDatabaseProxy createFromParcel(Parcel source) {
                        return new MockDatabaseProxy();
                    }

                    @Override
                    public MockDatabaseProxy[] newArray(int size) {
                        return new MockDatabaseProxy[size];
                    }
                };

    }

}