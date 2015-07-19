package moe.cowan.b.annsearcher.frontend.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import moe.cowan.b.annsearcher.R;
import moe.cowan.b.annsearcher.frontend.ClassWithItemClick;
import roboguice.inject.InjectView;

/**
 * Created by user on 24/01/2015.
 * fragment used to search through a given array, and display the results in a list. When one
 * of the items in the list is clicked, this information is returned to its parent activity
 */
public class SearchFragment extends Fragment {

    public static final String SEARCH_RESULT = "SEARCH_RESULT";
    private List<? extends Parcelable> searchItems;
    @InjectView(R.id.search_list) ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchItems = new ArrayList();
        View rootView = inflater.inflate(
                R.layout.search_item_view, container, false);
        listView.setOnItemClickListener(new itemClickListener());
        return rootView;
    }

    public void setSearchItems(List<? extends Parcelable> items) {
        searchItems = items;
        refreshList(searchItems);
    }

    /**
     * refresh the list with the given items
     * @param dispList
     */
    private void refreshList(List<? extends Parcelable> dispList) {
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, dispList);
        listView.setAdapter(adapter);
    }

    /**
     * search through the search items for the given search term
     * @param query what to search
     * @return all items containing query
     */
    private List<? extends Parcelable> search(String query) {
        return searchItems; // TODO: search not yet implemented
    }

    private class itemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Parcelable item = (Parcelable) parent.getItemAtPosition(position);
            ((ClassWithItemClick)getActivity()).itemClicked(item);
        }
    }
}