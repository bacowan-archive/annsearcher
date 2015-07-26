package moe.cowan.b.annsearcher.frontend.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.inject.Inject;

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
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by user on 25/07/2015.
 */
@ContentView(R.layout.search_results_view)
public class SearchResultsActivity extends RoboFragmentActivity {

    @InjectView(R.id.search_result_table) TableLayout table;
    @InjectView(R.id.actor_name_title) TextView actor_name_title;
    @Inject AnimeCrossreferencer crossRef;

    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        displayItems();
    }

    private ArrayAdapter<String> createListAdapter(List<String> listItems) {
        return new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
    }

    private void displayItems() {
        Bundle bun = getIntent().getExtras();
        DatabaseProxy proxy = bun.getParcelable(LauncherActivity.DATABASE_PARCELABLE_NAME);
        Person person = (Person) bun.getSerializable(VoiceSearchFragment.PERSON_NAME);
        Map<Anime, Collection<String>> otherCharactersByAnime = crossRef.getOtherCharactersActedBy(person, proxy);
        displayOtherCharactersByAnime(otherCharactersByAnime);
        actor_name_title.setText(person.getName());
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

}
