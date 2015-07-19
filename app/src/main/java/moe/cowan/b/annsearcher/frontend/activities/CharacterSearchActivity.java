package moe.cowan.b.annsearcher.frontend.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;

import moe.cowan.b.annsearcher.R;
import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.ClassWithItemClick;
import moe.cowan.b.annsearcher.frontend.fragments.SearchFragment;
import moe.cowan.b.annsearcher.frontend.fragments.VoiceSearchFragment;
import moe.cowan.b.annsearcher.presenter.CharacterSearchPresenter;

/**
 * Created by user on 24/01/2015.
 */
public class CharacterSearchActivity extends FragmentActivity implements ClassWithItemClick {

    private CharacterSearchPresenter presenter;
    private Anime anime;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_search_activity);
        getMembersFromIntent();
        initializeListItems();
    }

    private void initializeListItems() {
        SearchFragment frag = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment);
        frag.setSearchItems(presenter.getAllCastOfAnime(anime.getId()));
    }

    private void getMembersFromIntent() {
        Bundle bun = getIntent().getExtras();
        DatabaseProxy proxy = bun.getParcelable(LauncherActivityOld.DATABASE_PARCELABLE_NAME);
        presenter = new CharacterSearchPresenter(proxy, getApplication());
        anime = bun.getParcelable(VoiceSearchFragment.ANIME_TITLE);
    }

    @Override
    public void itemClicked(Parcelable obj) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(SearchFragment.SEARCH_RESULT, obj);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
