package moe.cowan.b.annsearcher.backend.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;

import java.io.File;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.database.internalDatabase.SqliteDBHelper;
import moe.cowan.b.annsearcher.backend.database.internalDatabase.SqliteDatabaseProxy;

/**
 * Created by user on 02/09/2015.
 */
public class SqlDatabaseTest extends InstrumentationTestCase {

    private static final String tempDatabaseName = "__tempdb__.db";

    private SqliteDatabaseProxy proxy;

    public void setUp() {
        proxy = new SqliteDatabaseProxy(getInstrumentation().getTargetContext(), tempDatabaseName);
    }

    public void test_AddAnime_StoresAnime() {
        proxy.addAnime(Utils.TEST_ANIME);
        Anime returnedAnime = proxy.getAnime(Utils.TEST_ANIME.getId());
        Utils.assertOtherAnimeSame(returnedAnime);
    }

    public void tearDown() {
        proxy.close();
        getInstrumentation().getTargetContext().deleteDatabase(tempDatabaseName);
    }

}
