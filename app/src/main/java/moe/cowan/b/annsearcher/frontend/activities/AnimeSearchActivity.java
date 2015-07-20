package moe.cowan.b.annsearcher.frontend.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import java.io.Serializable;

import moe.cowan.b.annsearcher.R;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
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
public class AnimeSearchActivity extends RoboFragmentActivity implements ClassWithItemClick {

    private AnimeSearchPresenter presenter;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        getMembersFromIntent();
        initializeListItems();
    }

    private void initializeListItems() {
        SearchFragment frag = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment);
        frag.setSearchItemStringSelector(new AnimeTitleStringSelector());
        frag.setSearchItems(presenter.getAllSeenAnime());
    }

    private void getMembersFromIntent() {
        Bundle bun = getIntent().getExtras();
        DatabaseProxy proxy = bun.getParcelable(LauncherActivity.DATABASE_PARCELABLE_NAME);
        presenter = new AnimeSearchPresenter(proxy, getApplication());
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
}
