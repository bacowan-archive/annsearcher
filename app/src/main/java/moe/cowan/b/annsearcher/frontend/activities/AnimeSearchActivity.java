package moe.cowan.b.annsearcher.frontend.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;

import moe.cowan.b.annsearcher.R;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.ClassWithItemClick;
import moe.cowan.b.annsearcher.frontend.fragments.SearchFragment;
import moe.cowan.b.annsearcher.presenter.AnimeSearchPresenter;

/**
 * Created by user on 24/01/2015.
 */
public class AnimeSearchActivity extends FragmentActivity implements ClassWithItemClick {

    private AnimeSearchPresenter presenter;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anime_search_activity);
        getMembersFromIntent();
        setSearchableItems();
    }

    private void setSearchableItems() {
        SearchFragment frag = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment);
        frag.setSearchItems(presenter.getAllSeenAnime());
    }

    private void getMembersFromIntent() {
        Bundle bun = getIntent().getExtras();
        DatabaseProxy proxy = bun.getParcelable(LauncherActivityOld.DATABASE_PARCELABLE_NAME);
        presenter = new AnimeSearchPresenter(proxy, getApplication());
    }

    /**
     * this is to be used by the SearchFragment, to return an object that was clicked on in the list
     * @param obj the object that was clicked on
     */
    @Override
    public void itemClicked(Parcelable obj) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(SearchFragment.SEARCH_RESULT, obj);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
