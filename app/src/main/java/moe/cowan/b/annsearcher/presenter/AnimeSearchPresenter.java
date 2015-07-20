package moe.cowan.b.annsearcher.presenter;

import android.app.Application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;

/**
 * Created by user on 04/07/2015.
 */
public class AnimeSearchPresenter {

    private DatabaseProxy proxy;

    public AnimeSearchPresenter(DatabaseProxy proxy, Application app) {
        this.proxy = proxy;
        this.proxy.setContext(app);
    }

    /**
     * return a list of strings of all seen titles
     * @return
     */
    public List<Anime> getAllSeenAnime() {
        return new ArrayList(proxy.getAllSeenAnime());
    }

}