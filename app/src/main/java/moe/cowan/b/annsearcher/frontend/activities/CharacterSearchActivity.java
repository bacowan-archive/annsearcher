package moe.cowan.b.annsearcher.frontend.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import moe.cowan.b.annsearcher.R;
import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.utils.ClassWithItemClick;
import moe.cowan.b.annsearcher.frontend.utils.StringSelectors.PersonRoleSearchItemStringSelector;
import moe.cowan.b.annsearcher.frontend.fragments.SearchFragment;
import moe.cowan.b.annsearcher.frontend.fragments.VoiceSearchFragment;
import moe.cowan.b.annsearcher.presenter.CharacterSearchPresenter;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;

/**
 * Created by user on 24/01/2015.
 */
@ContentView(R.layout.character_search_activity)
public class CharacterSearchActivity extends RoboFragmentActivity implements ClassWithItemClick {

    private CharacterSearchPresenter presenter;
    private Anime anime;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        getMembersFromIntent();
        initializeListItems();
    }

    private void initializeListItems() {
        SearchFragment frag = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment);
        frag.setSearchItemStringSelector(new PersonRoleSearchItemStringSelector());
        Collection<Person> searchItems = presenter.getAllCastOfAnime(anime.getId());
        frag.setSearchItems(new LinkedList<>(searchItems));
    }

    private void getMembersFromIntent() {
        Bundle bun = getIntent().getExtras();
        DatabaseProxy proxy = bun.getParcelable(LauncherActivity.DATABASE_PARCELABLE_NAME);
        presenter = new CharacterSearchPresenter(proxy, getApplication());
        anime = (Anime) bun.getSerializable(VoiceSearchFragment.ANIME_TITLE);
    }

    @Override
    public void itemClicked(Serializable obj) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(SearchFragment.SEARCH_RESULT, obj);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
