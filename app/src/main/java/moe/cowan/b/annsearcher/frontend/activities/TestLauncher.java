package moe.cowan.b.annsearcher.frontend.activities;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Id;
import moe.cowan.b.annsearcher.backend.Language;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.exceptions.TitleNotFoundException;

import static org.mockito.Mockito.mock;

/**
 * Created by user on 18/07/2015.
 */
public class TestLauncher extends LauncherActivity {
    @Override
    protected DatabaseProxy getDatabaseProxy() {
        return new MockDatabaseProxy();
    }

    private static class MockDatabaseProxy implements DatabaseProxy {

        @Override
        public void setUsername(String username) {

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