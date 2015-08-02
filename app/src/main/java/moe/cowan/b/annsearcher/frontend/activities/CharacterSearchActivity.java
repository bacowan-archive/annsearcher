package moe.cowan.b.annsearcher.frontend.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import moe.cowan.b.annsearcher.R;
import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.utils.ClassWithItemClick;
import moe.cowan.b.annsearcher.frontend.utils.Observer;
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
public class CharacterSearchActivity extends RoboFragmentActivity implements ClassWithItemClick, Observer {

    private CharacterSearchPresenter presenter;
    private Anime anime;
    private SearchFragment frag;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        getMembersFromIntent();
        initializeListItems();
    }

    private void initializeListItems() {
        frag = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment);
        frag.setSearchItemStringSelector(new PersonRoleSearchItemStringSelector());
        presenter.loadAllCastOfAnime(anime.getId());
    }

    private void getMembersFromIntent() {
        Bundle bun = getIntent().getExtras();
        DatabaseProxy proxy = bun.getParcelable(LauncherActivity.DATABASE_PARCELABLE_NAME);
        presenter = new CharacterSearchPresenter(proxy, getApplication(), this);
        anime = (Anime) bun.getSerializable(VoiceSearchFragment.ANIME_TITLE);
    }

    @Override
    public void notify(Object arg) {
        List<Person> animes = new ArrayList<>(getPeopleFromArg(arg));
        frag.setSearchItems(animes);
    }

    private Collection<Person> getPeopleFromArg(Object arg) {
        Collection<Person> persons;
        try {
            persons = (Collection<Person>) arg;
        } catch (ClassCastException e) {
            throw new RuntimeException(e);
        }
        return persons;
    }

    @Override
    public void itemClicked(Serializable obj) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(SearchFragment.SEARCH_RESULT, obj);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
