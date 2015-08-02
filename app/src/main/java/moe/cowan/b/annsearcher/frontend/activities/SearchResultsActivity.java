package moe.cowan.b.annsearcher.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.inject.Inject;

import java.io.ObjectInput;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import moe.cowan.b.annsearcher.R;
import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.database.AnimeCrossreferencer;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.fragments.VoiceSearchFragment;
import moe.cowan.b.annsearcher.frontend.utils.Observer;
import moe.cowan.b.annsearcher.presenter.SearchResultsPresenter;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by user on 25/07/2015.
 */
@ContentView(R.layout.search_results_view)
public class SearchResultsActivity extends RoboFragmentActivity implements Observer {

    @InjectView(R.id.search_result_table) TableLayout table;
    @InjectView(R.id.actor_name_title) TextView actor_name_title;
    @Inject AnimeCrossreferencer crossRef;
    private SearchResultsPresenter presenter;
    private Person displayPerson;


    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        displayPerson = (Person) intent.getSerializableExtra(VoiceSearchFragment.PERSON_NAME);
        DatabaseProxy proxy = intent.getParcelableExtra(LauncherActivity.DATABASE_PARCELABLE_NAME);
        presenter = new SearchResultsPresenter(proxy, crossRef, this, displayPerson, this);
        displayItems();
    }

    private ArrayAdapter<String> createListAdapter(List<String> listItems) {
        return new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
    }

    private void displayItems() {
        presenter.getOtherCharactersActedBy();
    }

    private void displayOtherCharactersByAnime(Map<Anime, Collection<String>> otherCharactersByAnime) {
        for (Anime anime : otherCharactersByAnime.keySet())
            displayOtherCharacterByAnime(anime, otherCharactersByAnime.get(anime));
    }

    private void displayOtherCharacterByAnime(Anime anime, Collection<String> characterNames) {
        TableRow animeRow = (TableRow) LayoutInflater.from(SearchResultsActivity.this).inflate(R.layout.search_result_item, null);
        ((TextView)animeRow.findViewById(R.id.anime_title_cell)).setText(anime.getTitle());

        List<String> listItems = new LinkedList<>();
        listItems.clear();
        for (String name : characterNames)
            listItems.add(name);
        ArrayAdapter<String> adapter = createListAdapter(listItems);

        ListView characterListView = (ListView)animeRow.findViewById(R.id.character_name_list);
        characterListView.setAdapter(adapter);
        table.addView(animeRow);
    }

    @Override
    public void notify(Object arg) {
        Map<Anime, Collection<String>> otherCharactersByAnime =(Map<Anime, Collection<String>>) arg;
        displayOtherCharactersByAnime(otherCharactersByAnime);
        actor_name_title.setText(displayPerson.getName());
    }
}
