package moe.cowan.b.annsearcher.backend.database.internalDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.IsolatedContext;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;

/**
 * Created by user on 02/09/2015.
 */
public class SqliteDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "anime.db";

    /*public SqliteDBHelper() {
        this(new IsolatedContext(new MockContentResolver(), new RenamingDelegatingContext(new MockContext(), "")));
    }*/

    public SqliteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SqliteDBHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlDatabaseContract.AnimeDatabaseEntry.CREATE_TABLE);
        db.execSQL(SqlDatabaseContract.IdDatabaseEntry.CREATE_TABLE);
        db.execSQL(SqlDatabaseContract.CharacterDatabaseEntry.CREATE_TABLE);
        db.execSQL(SqlDatabaseContract.PersonDatabseEntry.CREATE_TABLE);
        db.execSQL(SqlDatabaseContract.AnimeCharactersRelation.CREATE_TABLE);
        db.execSQL(SqlDatabaseContract.CharacterVoiceRelation.CREATE_TABLE);
        db.execSQL(SqlDatabaseContract.AnimeStaffRelation.CREATE_TABLE);
        db.execSQL(SqlDatabaseContract.AnimeSynonymsRelation.CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SqlDatabaseContract.AnimeDatabaseEntry.DELETE_ENTRIES);
        db.execSQL(SqlDatabaseContract.IdDatabaseEntry.DELETE_ENTRIES);
        db.execSQL(SqlDatabaseContract.CharacterDatabaseEntry.DELETE_ENTRIES);
        db.execSQL(SqlDatabaseContract.PersonDatabseEntry.DELETE_ENTRIES);
        db.execSQL(SqlDatabaseContract.AnimeCharactersRelation.DELETE_ENTRIES);
        db.execSQL(SqlDatabaseContract.CharacterVoiceRelation.DELETE_ENTRIES);
        db.execSQL(SqlDatabaseContract.AnimeStaffRelation.DELETE_ENTRIES);
        db.execSQL(SqlDatabaseContract.AnimeSynonymsRelation.DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
