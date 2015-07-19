package moe.cowan.b.annsearcher.frontend.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import moe.cowan.b.annsearcher.R;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.activities.AnimeSearchActivity;
import moe.cowan.b.annsearcher.frontend.activities.CharacterSearchActivity;
import moe.cowan.b.annsearcher.frontend.activities.LauncherActivityOld;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by user on 24/01/2015.
 * Fragment for the main screen, where you can search for voices
 */
public class VoiceSearchFragment extends RoboFragment {

    public static final String ANIME_TITLE = "ANIME_TITLE";
    public static final int FIND_ANIME = 0;
    public static final int FIND_CHARACTER = 1;
    private Anime currentAnime = null;
    private Person currentPerson = null;

    @InjectView(R.id.search_anime_button) Button searchAnimeButton;
    @InjectView(R.id.search_character_button) Button searchCharacterButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.voice_search_view, container, false);
        addListeners(rootView);
        return rootView;
    }

    private void addListeners(View fragmentView) {
        searchAnimeButton.setOnClickListener(new SearchAnimeClickListener());
        searchCharacterButton.setOnClickListener(new SearchCharacterClickListener());
    }

    private class SearchAnimeClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            startAnimeSearchView();
        }
    }

    public class SearchCharacterClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            startCharacterSearchView();
        }
    }

    private void startAnimeSearchView() {
        Intent intent = createAnimeSearchViewIntent();
        startActivityForResult(intent, FIND_ANIME);
    }

    @NonNull
    private Intent createAnimeSearchViewIntent() {
        Intent intent = new Intent(getActivity(), AnimeSearchActivity.class);
        DatabaseProxy dbp = getActivityDatabaseProxy();
        intent.putExtra(LauncherActivityOld.DATABASE_PARCELABLE_NAME,dbp);
        return intent;
    }

    private DatabaseProxy getActivityDatabaseProxy() {
        Bundle bun = getActivity().getIntent().getExtras();
        return bun.getParcelable(LauncherActivityOld.DATABASE_PARCELABLE_NAME);
    }

    private void startCharacterSearchView() {
        Intent intent = getCharacterSearchViewIntent();
        startActivityForResult(intent, FIND_CHARACTER);
    }

    @NonNull
    private Intent getCharacterSearchViewIntent() {
        Intent intent = new Intent(getActivity(), CharacterSearchActivity.class);
        DatabaseProxy dbp = getActivityDatabaseProxy();
        intent.putExtra(LauncherActivityOld.DATABASE_PARCELABLE_NAME,dbp);
        intent.putExtra(ANIME_TITLE, currentAnime);
        return intent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            onActivityResultCodeOk(requestCode, data);
    }

    private void onActivityResultCodeOk(int requestCode, Intent data) {
        if (requestCode == FIND_ANIME)
            findAnimeActivityResult(data);
        else if (requestCode == FIND_CHARACTER)
            findCharacterActivityResult(data);
    }

    private void findCharacterActivityResult(Intent data) {
        currentPerson = data.getParcelableExtra(SearchFragment.SEARCH_RESULT);
        TextView personText = (TextView) getActivity().findViewById(R.id.search_character_title_set_text);
        personText.setText(currentPerson.getRole());
    }

    private void findAnimeActivityResult(Intent data) {
        currentAnime = data.getParcelableExtra(SearchFragment.SEARCH_RESULT);
        TextView animeText = (TextView) getActivity().findViewById(R.id.search_anime_title_set_text);
        animeText.setText(currentAnime.getTitle());
    }

}
