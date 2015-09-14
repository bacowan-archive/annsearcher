package moe.cowan.b.annsearcher.backend.database.internalDatabase;

import android.provider.BaseColumns;

/**
 * Created by user on 02/09/2015.
 */
public final class SqlDatabaseContract {

    public static abstract class AnimeDatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "anime";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_STATUS = "status";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_NAME_TITLE + " VARCHAR(255), " +
                        COLUMN_NAME_STATUS + " INTEGER, " +
                        "FOREIGN KEY(" + COLUMN_NAME_ID + ") REFERENCES " + IdDatabaseEntry.TABLE_NAME + "(" + IdDatabaseEntry.COLUMN_NAME_INTERNAL + ")" +
                ")";

        public static final String DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class IdDatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "id";
        public static final String COLUMN_NAME_INTERNAL = "internal";
        public static final String COLUMN_NAME_ANN = "ann";
        public static final String COLUMN_NAME_MAL = "mal";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_INTERNAL + " INTEGER PRIMARY KEY, " +
                        COLUMN_NAME_ANN + " INTEGER, " +
                        COLUMN_NAME_MAL + " INTEGER" +
                        ")";

        public static final String DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class CharacterDatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "characters";
        public static final String COLUMN_NAME_NAME = "name";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_NAME_NAME + " VARCHAR(255)" +
                        ")";

        public static final String DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class PersonDatabseEntry implements BaseColumns {
        public static final String TABLE_NAME = "persons";
        public static final String COLUMN_NAME_NAME = "name";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_NAME_NAME + " VARCHAR(255)" +
                        ")";

        public static final String DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class AnimeSynonymsRelation implements BaseColumns {
        public static final String TABLE_NAME = "synonyms";
        public static final String COLUMN_NAME_ANIME = "anime";
        public static final String COLUMN_NAME_SYNONYM = "synonym";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ANIME + " INTEGER, " +
                        COLUMN_NAME_SYNONYM + " VARCHAR(255), " +
                        "PRIMARY KEY(" + COLUMN_NAME_ANIME + ", " + COLUMN_NAME_SYNONYM + "), " +
                        "FOREIGN KEY(" + COLUMN_NAME_ANIME + ") REFERENCES " + AnimeDatabaseEntry.TABLE_NAME + "(" + AnimeDatabaseEntry.COLUMN_NAME_ID + ")" +
                        ")";

        public static final String DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class AnimeCharactersRelation implements BaseColumns {
        public static final String TABLE_NAME = "anime_characters";
        public static final String COLUMN_NAME_ANIME = "anime";
        public static final String COLUMN_NAME_CHARACTER = "character";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ANIME + " INTEGER, " +
                        COLUMN_NAME_CHARACTER + " INTEGER, " +
                        "PRIMARY KEY(" + COLUMN_NAME_ANIME + ", " + COLUMN_NAME_CHARACTER + "), " +
                        "FOREIGN KEY(" + COLUMN_NAME_ANIME + ") REFERENCES " + AnimeDatabaseEntry.TABLE_NAME + "(" + AnimeDatabaseEntry.COLUMN_NAME_ID + "), " +
                        "FOREIGN KEY(" + COLUMN_NAME_CHARACTER + ") REFERENCES " + CharacterDatabaseEntry.TABLE_NAME + "(" + CharacterDatabaseEntry._ID + ")" +
                        ")";

        public static final String DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class CharacterVoiceRelation implements BaseColumns {
        public static final String TABLE_NAME = "character_voices";
        public static final String COLUMN_NAME_CHARACTER = "character";
        public static final String COLUMN_NAME_PERSON = "person";
        public static final String COLUMN_NAME_LANGUAGE = "language";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_PERSON + " INTEGER, " +
                        COLUMN_NAME_CHARACTER + " INTEGER, " +
                        COLUMN_NAME_LANGUAGE + " VARCHAR(255), " +
                        "PRIMARY KEY(" + COLUMN_NAME_PERSON + ", " + COLUMN_NAME_CHARACTER + ", " + COLUMN_NAME_LANGUAGE+ "), " +
                        "FOREIGN KEY(" + COLUMN_NAME_PERSON + ") REFERENCES " +  PersonDatabseEntry.TABLE_NAME + "(" + PersonDatabseEntry._ID + "), " +
                        "FOREIGN KEY(" + COLUMN_NAME_CHARACTER + ") REFERENCES " + CharacterDatabaseEntry.TABLE_NAME + "(" + CharacterDatabaseEntry._ID + ")" +
                        ")";

        public static final String DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class AnimeStaffRelation implements BaseColumns {
        public static final String TABLE_NAME = "anime_staff";
        public static final String COLUMN_NAME_ANIME = "anime";
        public static final String COLUMN_NAME_STAFF = "staff";
        public static final String COLUMN_NAME_ROLE = "role";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ANIME + " INTEGER, " +
                        COLUMN_NAME_STAFF + " INTEGER, " +
                        COLUMN_NAME_ROLE + " VARCHAR(255), " +
                        "PRIMARY KEY(" + COLUMN_NAME_STAFF + ", " + COLUMN_NAME_ROLE + ", " + COLUMN_NAME_ANIME + "), " +
                        "FOREIGN KEY(" + COLUMN_NAME_ANIME + ") REFERENCES " + AnimeDatabaseEntry.TABLE_NAME + "(" + AnimeDatabaseEntry.COLUMN_NAME_ID + "), " +
                        "FOREIGN KEY(" + COLUMN_NAME_STAFF + ") REFERENCES " + PersonDatabseEntry.TABLE_NAME + "(" + PersonDatabseEntry._ID + ")" +
                        ")";

        public static final String DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
