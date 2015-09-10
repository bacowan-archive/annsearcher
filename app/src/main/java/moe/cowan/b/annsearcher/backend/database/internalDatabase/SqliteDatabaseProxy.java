package moe.cowan.b.annsearcher.backend.database.internalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Collection;
import java.util.LinkedList;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Ids.StringIdGetter;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.Language;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.WatchingStatus;

import static moe.cowan.b.annsearcher.backend.database.internalDatabase.SqlDatabaseContract.*;

/**
 * Created by user on 02/09/2015.
 */
public class SqliteDatabaseProxy {

    private SqliteDBHelper helper;
    private static final StringIdGetter internalIdGetter = new StringIdGetter(StringIdKey.INTERNAL);
    private static final StringIdGetter annIdGetter = new StringIdGetter(StringIdKey.ANN);
    private static final StringIdGetter malIdGetter = new StringIdGetter(StringIdKey.MAL);

    public SqliteDatabaseProxy(Context context) {
        helper = new SqliteDBHelper(context);
    }

    public SqliteDatabaseProxy(Context context, String dbName) {
        helper = new SqliteDBHelper(context, dbName);
    }

    public void close() {
        helper.close();
    }

    public void addAnime(Anime anime) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long animeId = insertAnimeValues(db, anime);
        insertSynonyms(db, anime, animeId);
        insertIdValues(db, anime);
        insertPeopleValues(db, anime, animeId);
    }

    private long insertAnimeValues(SQLiteDatabase db, Anime anime) {
        ContentValues values = new ContentValues();
        values.put(AnimeDatabaseEntry.COLUMN_NAME_ID, internalIdGetter.getStringId(anime.getId()));
        values.put(AnimeDatabaseEntry.COLUMN_NAME_TITLE, anime.getTitle());
        values.put(AnimeDatabaseEntry.COLUMN_NAME_STATUS, anime.getStatus().getStatusInt());
        return db.insert(AnimeDatabaseEntry.TABLE_NAME, null, values);
    }

    private void insertSynonyms(SQLiteDatabase db, Anime anime, long animeId) {
        for (String synonym : anime.getSynonyms())
            insertSingleSynonym(db, synonym, animeId);
    }

    private void insertSingleSynonym(SQLiteDatabase db, String synonym, long animeId) {
        ContentValues values = new ContentValues();
        values.put(AnimeSynonymsRelation.COLUMN_NAME_ANIME, animeId);
        values.put(AnimeSynonymsRelation.COLUMN_NAME_SYNONYM, synonym);
        db.insert(AnimeSynonymsRelation.TABLE_NAME, null, values);
    }

    private void insertIdValues(SQLiteDatabase db, Anime anime) {
        ContentValues values = new ContentValues();
        values.put(IdDatabaseEntry.COLUMN_NAME_INTERNAL, internalIdGetter.getStringId(anime.getId()));
        values.put(IdDatabaseEntry.COLUMN_NAME_ANN, annIdGetter.getStringId(anime.getId()));
        values.put(IdDatabaseEntry.COLUMN_NAME_MAL, malIdGetter.getStringId(anime.getId()));
        db.insert(IdDatabaseEntry.TABLE_NAME, null, values);
    }

    private void insertPeopleValues(SQLiteDatabase db, Anime anime, long animeId) {
        insertCastValues(db, anime, animeId);
        insertStaffValues(db, anime, animeId);
    }

    private void insertCastValues(SQLiteDatabase db, Anime anime, long animeId) {
        for (Person p : anime.getPeopleOfTitle().getCast()) {
            long personId = insertSinglePersonValues(db, p);
            insertSingleCharacterValues(db, p, animeId, personId);
        }
    }

    private void insertSingleCharacterValues(SQLiteDatabase db, Person person, long animeId, long personId) {
        ContentValues values = new ContentValues();
        values.put(CharacterDatabaseEntry.COLUMN_NAME_NAME, person.getRole());
        long characterId = db.insert(CharacterDatabaseEntry.TABLE_NAME, null, values);

        insertAnimeCharacterRelation(db, characterId, animeId);
        insertCharacterVoiceRelation(db, person, characterId, personId);
    }

    private void insertAnimeCharacterRelation(SQLiteDatabase db, long characterId, long animeId) {
        ContentValues values = new ContentValues();
        values.put(AnimeCharactersRelation.COLUMN_NAME_ANIME, animeId);
        values.put(AnimeCharactersRelation.COLUMN_NAME_CHARACTER, characterId);
        db.insert(AnimeCharactersRelation.TABLE_NAME, null, values);
    }

    private void insertCharacterVoiceRelation(SQLiteDatabase db, Person person, long characterId, long personId) {
        ContentValues values = new ContentValues();
        values.put(CharacterVoiceRelation.COLUMN_NAME_CHARACTER, characterId);
        values.put(CharacterVoiceRelation.COLUMN_NAME_PERSON, personId);
        values.put(CharacterVoiceRelation.COLUMN_NAME_LANGUAGE, person.getLanguage().toString());
        db.insert(CharacterVoiceRelation.TABLE_NAME, null, values);
    }

    private void insertStaffValues(SQLiteDatabase db, Anime anime, long animeId) {
        for (Person p : anime.getPeopleOfTitle().getStaff()) {
            long personId = insertSinglePersonValues(db, p);
            insertSingleStaffValues(db, p, animeId, personId);
        }
    }

    private long insertSinglePersonValues(SQLiteDatabase db, Person person) {
        ContentValues values = new ContentValues();
        values.put(PersonDatabseEntry.COLUMN_NAME_NAME, person.getName());
        return db.insert(PersonDatabseEntry.TABLE_NAME, null, values);
    }

    private void insertSingleStaffValues(SQLiteDatabase db, Person person, long animeId, long personId) {
        ContentValues values = new ContentValues();
        values.put(AnimeStaffRelation.COLUMN_NAME_STAFF, personId);
        values.put(AnimeStaffRelation.COLUMN_NAME_ANIME, animeId);
        values.put(AnimeStaffRelation.COLUMN_NAME_ROLE, person.getRole());
        db.insert(AnimeStaffRelation.TABLE_NAME, null, values);
    }

    public Anime getAnime(Id id) {
        Anime anime = new Anime();


        SQLiteDatabase db = helper.getReadableDatabase();

        String[] animeProjection = {
                AnimeDatabaseEntry._ID,
                AnimeDatabaseEntry.COLUMN_NAME_TITLE,
                AnimeDatabaseEntry.COLUMN_NAME_STATUS
        };
        String animeWhere = AnimeDatabaseEntry.COLUMN_NAME_ID + "=" + internalIdGetter.getStringId(id);
        Cursor c = db.query(AnimeDatabaseEntry.TABLE_NAME,
                animeProjection,
                animeWhere,
                null,
                null,
                null,
                null);

        c.moveToFirst();
        anime.setTitle(c.getString(c.getColumnIndex(AnimeDatabaseEntry.COLUMN_NAME_TITLE)));
        anime.setStatus(WatchingStatus.intToStatus(Integer.parseInt(c.getString(c.getColumnIndex(AnimeDatabaseEntry.COLUMN_NAME_STATUS)))));


        String[] idProjection = {
                IdDatabaseEntry.COLUMN_NAME_MAL,
                IdDatabaseEntry.COLUMN_NAME_ANN
        };
        String idWhere = IdDatabaseEntry.COLUMN_NAME_INTERNAL + "=" + internalIdGetter.getStringId(id);
        c = db.query(IdDatabaseEntry.TABLE_NAME,
                idProjection,
                idWhere,
                null,
                null,
                null,
                null);

        c.moveToFirst();
        Id newId = new Id();
        String malId = c.getString(c.getColumnIndex(IdDatabaseEntry.COLUMN_NAME_MAL));
        String annId = c.getString(c.getColumnIndex(IdDatabaseEntry.COLUMN_NAME_ANN));
        newId.addId(StringIdKey.INTERNAL, id.getString(StringIdKey.INTERNAL));
        newId.addId(StringIdKey.MAL, malId);
        newId.addId(StringIdKey.ANN, annId);
        anime.setId(newId);



        String[] synonymsProjection = {
                AnimeSynonymsRelation.COLUMN_NAME_SYNONYM
        };
        String synonymsWhere = AnimeSynonymsRelation.COLUMN_NAME_ANIME + "=" + internalIdGetter.getStringId(id);
        c = db.query(AnimeSynonymsRelation.TABLE_NAME,
                synonymsProjection,
                synonymsWhere,
                null,
                null,
                null,
                null);
        c.moveToFirst();

        Collection<String> synonyms = new LinkedList<>();
        while (!c.isAfterLast()) {
            synonyms.add(c.getString(c.getColumnIndex(AnimeSynonymsRelation.COLUMN_NAME_SYNONYM)));
            c.moveToNext();
        }

        anime.setSynonyms(synonyms);




        String charactersQuery =
                "SELECT c." + CharacterDatabaseEntry.COLUMN_NAME_NAME + ", p." + PersonDatabseEntry.COLUMN_NAME_NAME + ", v." + CharacterVoiceRelation.COLUMN_NAME_LANGUAGE + " " +
                        "FROM " + AnimeDatabaseEntry.TABLE_NAME + " a " +
                        "INNER JOIN " + AnimeCharactersRelation.TABLE_NAME + " ac " +
                        "ON a." + AnimeDatabaseEntry._ID + "=" + "ac." + AnimeCharactersRelation.COLUMN_NAME_ANIME + " " +
                        "INNER JOIN " + CharacterDatabaseEntry.TABLE_NAME + " c " +
                        "ON c." + CharacterDatabaseEntry._ID + "=ac." + AnimeCharactersRelation.COLUMN_NAME_CHARACTER + " " +
                        "INNER JOIN " + CharacterVoiceRelation.TABLE_NAME + " v " +
                        "ON v." + CharacterVoiceRelation.COLUMN_NAME_CHARACTER + "=c." + CharacterDatabaseEntry._ID + " " +
                        "INNER JOIN " + PersonDatabseEntry.TABLE_NAME + " p " +
                        "ON v." + CharacterVoiceRelation.COLUMN_NAME_PERSON + "=p." + PersonDatabseEntry._ID + " " +
                        "WHERE a." + AnimeDatabaseEntry._ID + "=?;";

        c = db.rawQuery(charactersQuery, new String[] {internalIdGetter.getStringId(id)});

        c.moveToFirst();
        Collection<Person> animeCast = new LinkedList<>();
        while (!c.isAfterLast()) {
            Person p = new Person();
            p.setName(c.getString(c.getColumnIndex("p."+PersonDatabseEntry.COLUMN_NAME_NAME)));
            p.setLanguage(Language.stringToLanguage(c.getString(c.getColumnIndex("v." + CharacterVoiceRelation.COLUMN_NAME_LANGUAGE))));
            p.setRole(c.getString(c.getColumnIndex("c." + CharacterDatabaseEntry.COLUMN_NAME_NAME)));
            animeCast.add(p);
        }





        String staffQuery =
                "SELECT p." + PersonDatabseEntry.COLUMN_NAME_NAME + ", s." + AnimeStaffRelation.COLUMN_NAME_ROLE + " " +
                        "FROM " + AnimeDatabaseEntry.TABLE_NAME + " a " +
                        "INNER JOIN " + AnimeStaffRelation.TABLE_NAME + " s " +
                        "ON a." + AnimeDatabaseEntry._ID + "=s." + AnimeStaffRelation.COLUMN_NAME_ANIME + " " +
                        "INNER JOIN " + PersonDatabseEntry.TABLE_NAME + " p " +
                        "ON p." + PersonDatabseEntry._ID + "=s." + AnimeStaffRelation.COLUMN_NAME_STAFF + " " +
                        "WHERE a." + AnimeDatabaseEntry._ID + "=?;";

        c = db.rawQuery(staffQuery, new String[] {internalIdGetter.getStringId(id)});

        c.moveToFirst();
        Collection<Person> animeStaff = new LinkedList<>();
        while (!c.isAfterLast()) {
            Person p = new Person();
            p.setName(c.getString(c.getColumnIndex("p." + PersonDatabseEntry.COLUMN_NAME_NAME)));
            p.setRole(c.getString(c.getColumnIndex("s." + AnimeStaffRelation.COLUMN_NAME_ROLE)));
            animeStaff.add(p);
        }

        PeopleOfTitle peopleOfTitle = new PeopleOfTitle();
        peopleOfTitle.setCast(animeCast);
        peopleOfTitle.setStaff(animeStaff);
        anime.setPeopleOfTitle(peopleOfTitle);


        c.close();

        return anime;
    }

}