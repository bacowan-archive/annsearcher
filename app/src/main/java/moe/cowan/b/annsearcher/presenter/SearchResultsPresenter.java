package moe.cowan.b.annsearcher.presenter;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Collection;
import java.util.Map;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.database.AnimeCrossreferencer;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.activities.SearchResultsActivity;

/**
 * Created by user on 27/07/2015.
 */
public class SearchResultsPresenter {

    private AnimeCrossreferencer crossRef;
    private DatabaseProxy proxy;
    private Person displayPerson;
    private SearchResultsActivity activity;

    public SearchResultsPresenter(DatabaseProxy proxy, AnimeCrossreferencer crossRef, Context context, Person person, SearchResultsActivity activity) {
        this.proxy = proxy;
        this.proxy.setContext(context);
        this.activity = activity;
        this.displayPerson = person;
        this.crossRef = crossRef;
    }

    public void getOtherCharactersActedBy() {
        RefreshTask rt = new RefreshTask();
        rt.execute();
    }

    private class RefreshTask extends AsyncTask<Void,Void,Collection<Anime>> {

        @Override
        protected Collection<Anime> doInBackground(Void... params) {
            return proxy.getAllSeenAnime();
        }

        @Override
        protected void onPostExecute(Collection<Anime> result) {
            Map<Anime, Collection<String>> otherCharacters = crossRef.getOtherCharactersActedBy(result, displayPerson);
            sendNotification(otherCharacters);
        }
    }

    public void sendNotification(Object arg) {
        activity.notify(arg);
    }

}
