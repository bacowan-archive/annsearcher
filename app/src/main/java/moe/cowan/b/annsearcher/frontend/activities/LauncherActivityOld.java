package moe.cowan.b.annsearcher.frontend.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.backend.database.SqlDatabaseBuilder;

/**
 * Created by user on 01/02/2015.
 *
 * Activity specifically designed to launch the initial activity
 */
public class LauncherActivityOld extends Activity {

    public static final String DATABASE_PARCELABLE_NAME = "SERIALIZED_DATABASE";
    public static final String DATABASE_NAME = "ANN_SEARCHER_DATABASE";

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        DatabaseProxy database = SqlDatabaseBuilder.createDatabase(DATABASE_NAME, getApplication());
        startLoginScreen(database);
        finish();

    }

    private void startLoginScreen(DatabaseProxy database) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(DATABASE_PARCELABLE_NAME,database);
        startActivity(intent);
    }

}