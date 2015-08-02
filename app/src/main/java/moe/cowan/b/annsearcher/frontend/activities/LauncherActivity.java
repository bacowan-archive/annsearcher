package moe.cowan.b.annsearcher.frontend.activities;

import android.content.Intent;
import android.os.Bundle;

import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import roboguice.activity.RoboActivity;

/**
 * Created by user on 17/07/2015.
 */
public abstract class LauncherActivity extends RoboActivity {

    public static final String DATABASE_PARCELABLE_NAME = "SERIALIZED_DATABASE";

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        startInitialScreen(getDatabaseProxy());
        finish();
    }

    private void startInitialScreen(DatabaseProxy database) {
        // TODO: if the user has logged in before, don't start the login activity.
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(DATABASE_PARCELABLE_NAME, database);
        startActivity(intent);
    }

    protected abstract DatabaseProxy getDatabaseProxy();

}
