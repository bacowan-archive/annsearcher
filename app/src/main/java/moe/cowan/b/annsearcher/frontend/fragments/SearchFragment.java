package moe.cowan.b.annsearcher.frontend.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import moe.cowan.b.annsearcher.R;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.utils.ClassWithItemClick;
import moe.cowan.b.annsearcher.frontend.utils.Observer;
import moe.cowan.b.annsearcher.frontend.utils.Searchers.SearchCallback;
import moe.cowan.b.annsearcher.frontend.utils.StringSelectors.SearchItemStringSelector;
import moe.cowan.b.annsearcher.frontend.utils.StringSelectors.StringSelectorArrayAdapter;
import moe.cowan.b.annsearcher.presenter.AnimeSearchPresenter;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by user on 24/01/2015.
 * fragment used to search through a given array, and display the results in a list. When one
 * of the items in the list is clicked, this information is returned to its parent activity
 */
public class SearchFragment extends RoboFragment implements Observer {

    public static final String SEARCH_RESULT = "SEARCH_RESULT";

    private List<? extends Serializable> searchItems;
    private SearchItemStringSelector searchItemStringSelector;
    private AnimeSearchPresenter presenter;


    @InjectView(R.id.search_list) ListView listView;
    @InjectView(R.id.search_anime_title_text) EditText searchBox;
    @InjectView(R.id.search_anime_button) Button searchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchItems = new ArrayList<>();
        return inflater.inflate(
                R.layout.search_item_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView.setOnItemClickListener(new itemClickListener());
        searchButton.setOnClickListener(new SearchButtonClickListener());
    }



    public void setSearchItems(List<? extends Serializable> items) {
        searchItems = items;
        refreshList(searchItems);
    }

    public void setSearchItemStringSelector(SearchItemStringSelector searchItemStringSelector) {
        this.searchItemStringSelector = searchItemStringSelector;
    }

    /**
     * refresh the list with the given items
     * @param dispList
     */
    private void refreshList(List<? extends Serializable> dispList) {
        ArrayAdapter adapter = new StringSelectorArrayAdapter(searchItemStringSelector, getActivity(), android.R.layout.simple_list_item_1, dispList);
        listView.setAdapter(adapter);
    }

    private class SearchButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick( View v ) {
            search(searchBox.getText().toString());
        }
    }

    private void search(String query) {
        presenter.search(query);
    }

    public void setPresenter(AnimeSearchPresenter presenter) {
        this.presenter = presenter;
    }

    private class itemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Serializable item = (Serializable) parent.getItemAtPosition(position);
            ((ClassWithItemClick)getActivity()).itemClicked(item);
        }
    }

    @Override
    public void notify(Object arg) {
        List<? extends Serializable> dispList = (List<? extends Serializable>) arg;
        ArrayAdapter adapter = new StringSelectorArrayAdapter(searchItemStringSelector, getActivity(), android.R.layout.simple_list_item_1, dispList);
        listView.setAdapter(adapter);
    }
}