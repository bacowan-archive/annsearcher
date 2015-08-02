package moe.cowan.b.annsearcher.presenter;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.activities.AnimeSearchActivity;

/**
 * Created by user on 04/07/2015.
 */
public class AnimeSearchPresenter {

    private DatabaseProxy proxy;
    private AnimeSearchActivity activity;

    public AnimeSearchPresenter(DatabaseProxy proxy, Application app, AnimeSearchActivity activity) {
        this.proxy = proxy;
        this.proxy.setContext(app);
        this.activity = activity;
    }

    /**
     * return a list of strings of all seen titles
     * @return
     */
    public void loadAllSeenAnime() {
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
            sendNotification(result);
        }
    }

    public void sendNotification(Object arg) {
        activity.notify(arg);
    }

}