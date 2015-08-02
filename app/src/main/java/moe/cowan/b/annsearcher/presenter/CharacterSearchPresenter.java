package moe.cowan.b.annsearcher.presenter;

import android.app.Application;
import android.os.AsyncTask;

import java.util.Collection;
import java.util.List;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.activities.CharacterSearchActivity;
import moe.cowan.b.annsearcher.frontend.utils.peopleFilters.JapanesePeopleFilter;
import moe.cowan.b.annsearcher.frontend.utils.peopleFilters.PeopleFilter;


/**
 * Created by user on 07/03/2015.
 */
public class CharacterSearchPresenter {
    private DatabaseProxy proxy;
    private PeopleFilter peopleFilter;
    private CharacterSearchActivity activity;

    public CharacterSearchPresenter(DatabaseProxy proxy, Application app, CharacterSearchActivity activity) {
        this.proxy = proxy;
        this.proxy.setContext(app);
        this.activity = activity;
        setPersonsFilter();
    }

    private void setPersonsFilter() {
        peopleFilter = new JapanesePeopleFilter();
    }

    public void loadAllCastOfAnime(Id animeId) {
        RefreshTask rt = new RefreshTask(animeId);
        rt.execute();
    }


    private class RefreshTask extends AsyncTask<Void,Void,Collection<Person>> {

        private Id animeId;

        public RefreshTask(Id animeId) {
            this.animeId = animeId;
        }

        @Override
        protected Collection<Person> doInBackground(Void... params) {
            Collection<Person> allPeopleOfTitle = proxy.getPeopleOfTitle(animeId).getCast();
            peopleFilter.filter(allPeopleOfTitle);
            return allPeopleOfTitle;
        }

        @Override
        protected void onPostExecute(Collection<Person> result) {
            sendNotification(result);
        }
    }

    public void sendNotification(Object arg) {
        activity.notify(arg);
    }

}