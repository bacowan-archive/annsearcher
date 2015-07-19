package moe.cowan.b.annsearcher.presenter;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Collection;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.fragments.AnimeListFragment;

/**
 * Created by user on 02/02/2015.
 * Controller for the Anime list
 */
public class AnimeListPresenter implements Presenter {

    private DatabaseProxy proxy;
    private AnimeListFragment alFrag;

    public AnimeListPresenter(AnimeListFragment frag, DatabaseProxy proxy, Application app) {
        alFrag = frag;
        this.proxy = proxy;
        this.proxy.setContext(app);
        RefreshTask rt = new RefreshTask();
        rt.execute();
    }

    public void fillList() {
        try {
            proxy.getAllSeenAnime();
        } catch (Exception e) {
            // TODO: properly handle these exceptions
            Log.e("", "initialize anime list controller error", e);
        }
    }

    private class RefreshTask extends AsyncTask<Void,Void,Collection<Anime>> {

        @Override
        protected Collection<Anime> doInBackground(Void... params) {
            try {
                proxy.resync();
            } catch (Exception e) {
                //TODO: something
                Log.e("", "initialize anime list controller error", e);
            }
            Collection<Anime> result = proxy.getAllSeenAnime();
            return result;
        }

        @Override
        protected void onPostExecute(Collection<Anime> result) {
            sendNotification(result);
        }
    }

    public void sendNotification(Object arg) {
        alFrag.notify(arg);
    }

}