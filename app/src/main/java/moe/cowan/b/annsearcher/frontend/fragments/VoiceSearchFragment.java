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
import moe.cowan.b.annsearcher.frontend.activities.LauncherActivity;
import moe.cowan.b.annsearcher.frontend.activities.SearchResultsActivity;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by user on 24/01/2015.
 * Fragment for the main screen, where you can search for voices
 */
public class VoiceSearchFragment extends RoboFragment {

    public static final String ANIME_TITLE = "ANIME_TITLE";
    public static final String PERSON_NAME = "PERSON_NAME";
    public static final int FIND_ANIME = 0;
    public static final int FIND_CHARACTER = 1;
    private Anime currentAnime = null;
    private Person currentPerson = null;

    @InjectView(R.id.search_anime_button) Button searchAnimeButton;
    @InjectView(R.id.search_character_button) Button searchCharacterButton;
    @InjectView(R.id.crossreference_button) Button crossreferenceButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.voice_search_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        searchAnimeButton = (Button) getView().findViewById(R.id.search_anime_button);
        searchCharacterButton = (Button) getView().findViewById(R.id.search_character_button);
        crossreferenceButton = (Button) getView().findViewById(R.id.crossreference_button);
        addListeners();
    }

    private void addListeners() {
        searchAnimeButton.setOnClickListener(new SearchAnimeClickListener());
        searchCharacterButton.setOnClickListener(new SearchCharacterClickListener());
        crossreferenceButton.setOnClickListener(new SearchClickListener());
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

    public class SearchClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) { startSearchResultsView(); }
    }

    private void startAnimeSearchView() {
        Intent intent = createAnimeSearchViewIntent();
        startActivityForResult(intent, FIND_ANIME);
    }

    @NonNull
    private Intent createAnimeSearchViewIntent() {
        Intent intent = new Intent(getActivity(), AnimeSearchActivity.class);
        DatabaseProxy dbp = getActivityDatabaseProxy();
        intent.putExtra(LauncherActivity.DATABASE_PARCELABLE_NAME, dbp);
        return intent;
    }

    private DatabaseProxy getActivityDatabaseProxy() {
        Bundle bun = getActivity().getIntent().getExtras();
        return bun.getParcelable(LauncherActivity.DATABASE_PARCELABLE_NAME);
    }

    private void startCharacterSearchView() {
        Intent intent = getCharacterSearchViewIntent();
        startActivityForResult(intent, FIND_CHARACTER);
    }

    @NonNull
    private Intent getCharacterSearchViewIntent() {
        Intent intent = new Intent(getActivity(), CharacterSearchActivity.class);
        DatabaseProxy dbp = getActivityDatabaseProxy();
        intent.putExtra(LauncherActivity.DATABASE_PARCELABLE_NAME,dbp);
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
        currentPerson = (Person) data.getSerializableExtra(SearchFragment.SEARCH_RESULT);
        TextView personText = (TextView) getActivity().findViewById(R.id.search_character_title_set_text);
        personText.setText(currentPerson.getRole());
    }

    private void findAnimeActivityResult(Intent data) {
        currentAnime = (Anime) data.getSerializableExtra(SearchFragment.SEARCH_RESULT);
        TextView animeText = (TextView) getActivity().findViewById(R.id.search_anime_title_set_text);
        animeText.setText(currentAnime.getTitle());
    }

    private void startSearchResultsView() {
        Intent intent = getSearchResultsViewIntent();
        startActivity(intent);
    }

    private Intent getSearchResultsViewIntent() {
        Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
        DatabaseProxy dbp = getActivityDatabaseProxy();
        intent.putExtra(LauncherActivity.DATABASE_PARCELABLE_NAME, dbp);
        intent.putExtra(ANIME_TITLE, currentAnime);
        intent.putExtra(PERSON_NAME, currentPerson);
        return intent;
    }

}
