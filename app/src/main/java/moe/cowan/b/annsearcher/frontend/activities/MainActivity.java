package moe.cowan.b.annsearcher.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import moe.cowan.b.annsearcher.R;
import moe.cowan.b.annsearcher.frontend.fragments.AnimeListFragment;
import moe.cowan.b.annsearcher.frontend.fragments.VoiceSearchFragment;


public class MainActivity extends FragmentActivity {

    public static final String USERNAME_INTENT_TAG = "USERNAME";
    private static final String ANIME_LIST_FRAGMENT_TITLE = "List";
    private static final String VOICE_SEARCH_FRAGMENT_TITLE = "Search";
    private static final List<String> PAGE_TITLES = new ArrayList<>(Arrays.asList(
            ANIME_LIST_FRAGMENT_TITLE,
            VOICE_SEARCH_FRAGMENT_TITLE
    ));

    private MyPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linkActivityToFragments();
    }

    private void linkActivityToFragments() {
        Bundle bun = getBundleFromIntent();
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),bun);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
    }

    private Bundle getBundleFromIntent() {
        Bundle bun = new Bundle();
        Intent intent = getIntent();
        bun.putString(USERNAME_INTENT_TAG, intent.getStringExtra(USERNAME_INTENT_TAG));
        bun.putParcelable(LauncherActivityOld.DATABASE_PARCELABLE_NAME, intent.getParcelableExtra(LauncherActivityOld.DATABASE_PARCELABLE_NAME));
        return bun;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        // items to be sent to the fragments
        private Bundle bundle;

        public MyPagerAdapter(FragmentManager fm, Bundle bundle) {
            super(fm);
            this.bundle = bundle;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentBuilder(PAGE_TITLES.get(position), bundle);
        }

        @Override
        public int getCount() {
            return PAGE_TITLES.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES.get(position);
        }
    }

    /**
     * create a fragment given the identifier and an input bundle
     * @param identifier name of the fragment
     * @param bundle info to pass the fragment
     * @return the fragment
     */
    private Fragment fragmentBuilder(String identifier, Bundle bundle) {
        if (identifier.equals(ANIME_LIST_FRAGMENT_TITLE)) {
            Fragment frag = new AnimeListFragment();
            frag.setArguments(bundle);
            return frag;
        }
        else if (identifier.equals(VOICE_SEARCH_FRAGMENT_TITLE)) {
            return new VoiceSearchFragment();
        }
        return null;
    }
}