package moe.cowan.b.annsearcher.frontend.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moe.cowan.b.annsearcher.R;

/**
 * Created by user on 24/01/2015.
 */
public class SearchResultsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.anime_list_view, container, false);
        return rootView;
    }
}
