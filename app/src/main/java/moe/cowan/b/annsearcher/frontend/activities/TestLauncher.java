package moe.cowan.b.annsearcher.frontend.activities;

import android.content.Context;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Id;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.exceptions.TitleNotFoundException;
import moe.cowan.b.annsearcher.frontend.activities.LauncherActivity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by user on 18/07/2015.
 */
public class TestLauncher extends LauncherActivity {
    @Override
    protected DatabaseProxy getDatabaseProxy() {
        return new MockDatabaseProxy();
    }

    private class MockDatabaseProxy implements DatabaseProxy {

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
            people.add(personWithName("person1"));
            people.add(personWithName("person2"));
            return people;
        }

        private Person personWithName(String name) {
            Person person = mock(Person.class);
            when(person.toString()).thenReturn(name);
            return person;
        }

        @Override
        public Collection<Anime> getAllSeenAnime() {
            Collection<Anime> animes = new ArrayList<>();
            animes.add(animeWithTitle("Title1"));
            animes.add(animeWithTitle("Title2"));
            return animes;
        }

        private Anime animeWithTitle(String title) {
            Anime anime = mock(Anime.class);
            when(anime.getTitle()).thenReturn(title);
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
    }

}