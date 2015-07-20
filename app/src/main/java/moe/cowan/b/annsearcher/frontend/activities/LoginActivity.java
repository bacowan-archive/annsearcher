package moe.cowan.b.annsearcher.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

import moe.cowan.b.annsearcher.R;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import roboguice.activity.RoboActivity;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by user on 01/02/2015.
 */
@ContentView(R.layout.login_activity)
public class LoginActivity extends RoboFragmentActivity {

    @InjectView(R.id.username) EditText userNameTextBox;

    public void onClickLogin(View view) {
        String username = userNameTextBox.getText().toString();
        goToMain(username);
    }

    public void onClickDontLogin(View view) {
        goToMain(null);
    }

    /**
     * go to the main activity, and give it the current username
     * @param username
     */
    private void goToMain(String username) {
        Intent intent = createMainActivityIntent(username);
        startActivity(intent);
        finish();
    }

    @NonNull
    private Intent createMainActivityIntent(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.USERNAME_INTENT_TAG, username);
        DatabaseProxy dbp = getDbProxyFromOldIntent(username);
        intent.putExtra(LauncherActivity.DATABASE_PARCELABLE_NAME, dbp);
        return intent;
    }

    @NonNull
    private DatabaseProxy getDbProxyFromOldIntent(String username) {
        Intent oldIntent = getIntent();
        DatabaseProxy dbp = oldIntent.getExtras().getParcelable(LauncherActivity.DATABASE_PARCELABLE_NAME);
        dbp.setUsername(username);
        return dbp;
    }

}
