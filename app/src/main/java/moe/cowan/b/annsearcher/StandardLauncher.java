package moe.cowan.b.annsearcher;

import moe.cowan.b.annsearcher.backend.database.AnnMalDatabaseProxy;
import moe.cowan.b.annsearcher.backend.database.DatabaseProxy;
import moe.cowan.b.annsearcher.frontend.activities.LauncherActivity;

/**
 * Created by user on 18/07/2015.
 */
public class StandardLauncher extends LauncherActivity {
    @Override
    protected DatabaseProxy getDatabaseProxy() {
        return new AnnMalDatabaseProxy();
    }
}
