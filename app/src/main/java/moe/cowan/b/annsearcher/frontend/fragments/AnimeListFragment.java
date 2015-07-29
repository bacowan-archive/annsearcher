package moe.cowan.b.annsearcher.frontend.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import moe.cowan.b.annsearcher.R;
import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.activities.AnimeSearchActivity;
import moe.cowan.b.annsearcher.frontend.activities.LauncherActivity;
import moe.cowan.b.annsearcher.frontend.utils.Observer;
import moe.cowan.b.annsearcher.presenter.AnimeListPresenter;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by user on 24/01/2015.
 */
public class AnimeListFragment extends RoboFragment implements Observer {

    private AnimeListPresenter presenter;
    @InjectView(R.id.search_anime_button) Button searchAnimeButton;
    @InjectView(R.id.listView) ListView listView;

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        createPresenter();
    }

    private void createPresenter() {
        Bundle bun = getActivity().getIntent().getExtras();
        DatabaseProxy proxy = bun.getParcelable(LauncherActivity.DATABASE_PARCELABLE_NAME);
        presenter = new AnimeListPresenter(this, proxy, getActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(
                R.layout.anime_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addListeners();
        presenter.fillList();
    }

    private void addListeners() {
        searchAnimeButton.setOnClickListener(new SearchClickListener());
    }

    private class SearchClickListener implements View.OnClickListener {
        @Override
        public void onClick( View v ) {
            startAnimeSearchView();
        }
    }

    private void startAnimeSearchView() {
        Intent intent = new Intent(getActivity(), AnimeSearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void notify(Object arg) {
        Collection<Anime> animes = getAnimesFromArg(arg);
        AnimeListAdapter adapter = new AnimeListAdapter(animes);
        listView.setAdapter(adapter);
    }

    private Collection<Anime> getAnimesFromArg(Object arg) {
        Collection<Anime> animes;
        try {
            animes = (Collection<Anime>) arg;
        } catch (ClassCastException e) {
            throw new RuntimeException(e);
        }
        return animes;
    }

    private class AnimeListAdapter extends BaseAdapter {

        private List<Anime> animes;

        public AnimeListAdapter(Collection<Anime> animes) {
            this.animes = new ArrayList<>(animes);
        }

        @Override
        public int getCount() {
            return animes.size();
        }

        @Override
        public Object getItem(int position) {
            return animes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.anime_list_item, parent, false);
            }

            Anime currentAnime = animes.get(position);

            TextView titleText = (TextView) convertView.findViewById(R.id.main_text);
            titleText.setText(currentAnime.getTitle());

            return convertView;
        }

    }


}
