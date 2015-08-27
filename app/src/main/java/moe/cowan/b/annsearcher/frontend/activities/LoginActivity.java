package moe.cowan.b.annsearcher.frontend.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import moe.cowan.b.annsearcher.R;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by user on 01/02/2015.
 */
@ContentView(R.layout.login_activity)
public class LoginActivity extends RoboFragmentActivity {

    @InjectView(R.id.username) EditText userNameTextBox;
    @InjectView(R.id.password) EditText passwordTextBox;

    public void onClickLogin(View view) {
        String username = userNameTextBox.getText().toString();
        String password = passwordTextBox.getText().toString();
        goToMain(username, password);
    }

    public void onClickDontLogin(View view) {
        goToMain(null,null);
    }

    /**
     * go to the main activity, and give it the current username
     * @param username
     */
    private void goToMain(String username, String password) {
        Intent intent = createMainActivityIntent(username, password);
        startActivity(intent);
        finish();
    }

    @NonNull
    private Intent createMainActivityIntent(String username, String password) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.USERNAME_INTENT_TAG, username);
        DatabaseProxy dbp = getDbProxyFromOldIntent(username, password);
        intent.putExtra(LauncherActivity.DATABASE_PARCELABLE_NAME, dbp);
        return intent;
    }

    @NonNull
    private DatabaseProxy getDbProxyFromOldIntent(String username, String password) {
        Intent oldIntent = getIntent();
        DatabaseProxy dbp = oldIntent.getExtras().getParcelable(LauncherActivity.DATABASE_PARCELABLE_NAME);
        dbp.setUsername(username);
        dbp.setPassword(password);
        return dbp;
    }

}
