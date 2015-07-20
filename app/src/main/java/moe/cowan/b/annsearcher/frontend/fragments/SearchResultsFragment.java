package moe.cowan.b.annsearcher.frontend.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moe.cowan.b.annsearcher.R;
import roboguice.fragment.RoboFragment;

/**
 * Created by user on 24/01/2015.
 */
public class SearchResultsFragment extends RoboFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.anime_list_view, container, false);
    }
}
