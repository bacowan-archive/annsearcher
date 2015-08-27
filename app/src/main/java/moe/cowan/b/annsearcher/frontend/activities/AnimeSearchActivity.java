package moe.cowan.b.annsearcher.frontend.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import moe.cowan.b.annsearcher.R;
import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.utils.Observer;
import moe.cowan.b.annsearcher.frontend.utils.Searchers.MalAnimeSearch;
import moe.cowan.b.annsearcher.frontend.utils.StringSelectors.AnimeTitleStringSelector;
import moe.cowan.b.annsearcher.frontend.utils.ClassWithItemClick;
import moe.cowan.b.annsearcher.frontend.fragments.SearchFragment;
import moe.cowan.b.annsearcher.presenter.AnimeSearchPresenter;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;

/**
 * Created by user on 24/01/2015.
 */
@ContentView(R.layout.anime_search_activity)
public class AnimeSearchActivity extends RoboFragmentActivity implements ClassWithItemClick, Observer {

    private AnimeSearchPresenter presenter;
    private SearchFragment frag;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        Bundle bun = getIntent().getExtras();
        DatabaseProxy proxy = bun.getParcelable(LauncherActivity.DATABASE_PARCELABLE_NAME);
        getMembersFromIntent(proxy);
        initializeListItems(proxy);
    }

    private void initializeListItems(DatabaseProxy proxy) {
        frag = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment);
        frag.setSearchItemStringSelector(new AnimeTitleStringSelector());
        frag.setPresenter(presenter);
        presenter.setSearchCallback(new MalAnimeSearch(proxy));
        presenter.loadAllSeenAnime();
    }

    private void getMembersFromIntent(DatabaseProxy proxy) {
        presenter = new AnimeSearchPresenter(proxy, getApplication(), this);
    }

    /**
     * this is to be used by the SearchFragment, to return an object that was clicked on in the list
     * @param obj the object that was clicked on
     */
    @Override
    public void itemClicked(Serializable obj) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(SearchFragment.SEARCH_RESULT, obj);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void notify(Object arg) {
        List<Anime> animes = new ArrayList<>(getAnimesFromArg(arg));
        frag.setSearchItems(animes);
    }

    private Collection<Anime> getAnimesFromArg(Object arg) {
        Collection<Anime> animes;
        try {
            animes = (Collection<Anime>) arg;
        } catch (ClassCastException e) {
            throw new RuntimeException(e);
        }
        return animes;
    }

}
