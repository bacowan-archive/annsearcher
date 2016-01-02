package moe.cowan.b.annsearcher.backend.database.internalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Callback;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Ids.IncrementalIdCalculator;
import moe.cowan.b.annsearcher.backend.Ids.NewIdCalculator;
import moe.cowan.b.annsearcher.backend.Ids.StringIdGetter;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.Ids.StringIdSetter;
import moe.cowan.b.annsearcher.backend.Language;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.backend.Person;
import moe.cowan.b.annsearcher.backend.WatchingStatus;
import moe.cowan.b.annsearcher.backend.database.InternalDatabaseProxy;

import static moe.cowan.b.annsearcher.backend.database.internalDatabase.SqlDatabaseContract.*;

/**
 * Created by user on 02/09/2015.
 */
public class SqliteDatabaseProxy implements InternalDatabaseProxy {

    private static final String GET_CHARACTERS_OF_ANIME_QUERY = "SELECT DISTINCT c." + CharacterDatabaseEntry.COLUMN_NAME_NAME + " AS cName, p." + PersonDatabseEntry._ID + ", p." + PersonDatabseEntry.COLUMN_NAME_NAME + " AS pName, v." + CharacterVoiceRelation.COLUMN_NAME_LANGUAGE + " " +
            "FROM " + AnimeDatabaseEntry.TABLE_NAME + " a " +
            "INNER JOIN " + AnimeCharactersRelation.TABLE_NAME + " ac " +
            "ON a." + AnimeDatabaseEntry.COLUMN_NAME_ID + "=" + "ac." + AnimeCharactersRelation.COLUMN_NAME_ANIME + " " +
            "INNER JOIN " + CharacterDatabaseEntry.TABLE_NAME + " c " +
            "ON c." + CharacterDatabaseEntry._ID + "=ac." + AnimeCharactersRelation.COLUMN_NAME_CHARACTER + " " +
            "INNER JOIN " + CharacterVoiceRelation.TABLE_NAME + " v " +
            "ON v." + CharacterVoiceRelation.COLUMN_NAME_CHARACTER + "=c." + CharacterDatabaseEntry._ID + " " +
            "INNER JOIN " + PersonDatabseEntry.TABLE_NAME + " p " +
            "ON v." + CharacterVoiceRelation.COLUMN_NAME_PERSON + "=p." + PersonDatabseEntry._ID + " " +
            "WHERE a." + AnimeDatabaseEntry.COLUMN_NAME_ID + "=?;";
    private static final String GET_ALL_CAST_PERSON_NAME = "pName";
    private static final String GET_ALL_CAST_PERSON_LANGUAGE = "v." + CharacterVoiceRelation.COLUMN_NAME_LANGUAGE;
    private static final String GET_ALL_CAST_CHARACTER_NAME = "cName";
    private static final String GET_ALL_CAST_PERSON_ID = "p._id";

    private static final String GET_STAFF_OF_ANIME_QUERY = "SELECT DISTINCT p.*, s." + AnimeStaffRelation.COLUMN_NAME_ROLE + " " +
            "FROM " + AnimeDatabaseEntry.TABLE_NAME + " a " +
            "INNER JOIN " + AnimeStaffRelation.TABLE_NAME + " s " +
            "ON a." + AnimeDatabaseEntry.COLUMN_NAME_ID + "=s." + AnimeStaffRelation.COLUMN_NAME_ANIME + " " +
            "INNER JOIN " + PersonDatabseEntry.TABLE_NAME + " p " +
            "ON p." + PersonDatabseEntry._ID + "=s." + AnimeStaffRelation.COLUMN_NAME_STAFF + " " +
            "WHERE a." + AnimeDatabaseEntry.COLUMN_NAME_ID + "=?;";
    private static final String GET_ALL_STAFF_PERSON_NAME = "p." + PersonDatabseEntry.COLUMN_NAME_NAME;
    private static final String GET_ALL_STAFF_PERSON_ROLE = "s." + AnimeStaffRelation.COLUMN_NAME_ROLE;
    private static final String GET_ALL_STAFF_PERSON_ID = "p._id";

    private static final String CROSSREFERENCE_STAFF_QUERY = "SELECT DISTINCT a." + AnimeDatabaseEntry.COLUMN_NAME_TITLE + ", c." + CharacterDatabaseEntry.COLUMN_NAME_NAME + " " +
            "FROM " + AnimeDatabaseEntry.TABLE_NAME + " a " +
            "INNER JOIN " + AnimeCharactersRelation.TABLE_NAME + " acr " +
            "ON a." + AnimeDatabaseEntry.COLUMN_NAME_ID + "=acr." + AnimeCharactersRelation.COLUMN_NAME_ANIME + " " +
            "INNER JOIN " + CharacterDatabaseEntry.TABLE_NAME + " c " +
            "ON c." + CharacterDatabaseEntry._ID + "=acr." + AnimeCharactersRelation.COLUMN_NAME_CHARACTER + " " +
            "INNER JOIN " + CharacterVoiceRelation.TABLE_NAME + " cvr " +
            "ON c." + CharacterDatabaseEntry._ID + "=cvr." + CharacterVoiceRelation.COLUMN_NAME_CHARACTER + " " +
            "INNER JOIN " + PersonDatabseEntry.TABLE_NAME + " p " +
            "ON p." + PersonDatabseEntry._ID + "=cvr." + CharacterVoiceRelation.COLUMN_NAME_PERSON + " " +
            "WHERE p." + PersonDatabseEntry._ID + "=?;";
    private static final String CROSSREFERENCE_TITLE_COLUMN = "a." + AnimeDatabaseEntry.COLUMN_NAME_TITLE;
    private static final String CROSSREFERENCE_NAME_COLUMN = "c." + CharacterDatabaseEntry.COLUMN_NAME_NAME;

    private static final String GET_INTERNAL_ID_FROM_MAL = "SELECT " + IdDatabaseEntry.COLUMN_NAME_INTERNAL + " FROM " + IdDatabaseEntry.TABLE_NAME + " WHERE " + IdDatabaseEntry.TABLE_NAME + "." + IdDatabaseEntry.COLUMN_NAME_MAL + " =?";

    private static final String LARGEST_INTERNAL_ID_COLUMN = "MAX(" + IdDatabaseEntry.COLUMN_NAME_INTERNAL + ")";
    private static final String GET_LARGEST_INTERNAL_ID_QUERY = "SELECT " + LARGEST_INTERNAL_ID_COLUMN + " FROM " + IdDatabaseEntry.TABLE_NAME + ";";

    private SqliteDBHelper helper;
    private static final StringIdGetter internalIdGetter = new StringIdGetter(StringIdKey.INTERNAL);
    private static final StringIdSetter internalIdSetter = new StringIdSetter(StringIdKey.INTERNAL);
    private static final StringIdGetter annIdGetter = new StringIdGetter(StringIdKey.ANN);
    private static final StringIdGetter malIdGetter = new StringIdGetter(StringIdKey.MAL);

    private NewIdCalculator idCalculator;

    public SqliteDatabaseProxy(SqliteDBHelper helper) {
        this.helper = helper;
        this.idCalculator = new IncrementalIdCalculator(new LargestIdCallback());
    }

    public SqliteDatabaseProxy(Context context, String dbName) {
        helper = new SqliteDBHelper(context, dbName);
        idCalculator = new IncrementalIdCalculator(new LargestIdCallback());
    }

    public SqliteDatabaseProxy(Context context, String dbName, NewIdCalculator idCalculator) {
        helper = new SqliteDBHelper(context, dbName);
        this.idCalculator = idCalculator;
    }

    public void close() {
        helper.close();
    }

    @Override
    public void addAnime(Anime anime) {
        SQLiteDatabase db = helper.getWritableDatabase();
        calculateInternalAnimeId(anime);
        long animeId = insertAnimeValues(db, anime);
        insertSynonyms(db, anime, animeId);
        insertIdValues(db, anime);
        insertPeopleValues(db, anime, animeId);
    }

    private void calculateInternalAnimeId(Anime anime) {
        if (internalIdGetter.getStringId(anime.getId()).equals(""))
            internalIdSetter.setString(anime.getId(), idCalculator.calculateNewId());
    }

    private void calculateInternalPersonId(Person person) {
        if (internalIdGetter.getStringId(person.getId()).equals(""))
            internalIdSetter.setString(person.getId(), idCalculator.calculateNewId());
    }

    private long insertAnimeValues(SQLiteDatabase db, Anime anime) {
        ContentValues values = new ContentValues();
        values.put(AnimeDatabaseEntry.COLUMN_NAME_ID, internalIdGetter.getStringId(anime.getId()));
        values.put(AnimeDatabaseEntry.COLUMN_NAME_TITLE, anime.getTitle());
        values.put(AnimeDatabaseEntry.COLUMN_NAME_STATUS, anime.getStatus().getStatusInt());
        return db.insertWithOnConflict(AnimeDatabaseEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void insertSynonyms(SQLiteDatabase db, Anime anime, long animeId) {
        for (String synonym : anime.getSynonyms())
            insertSingleSynonym(db, synonym, animeId);
    }

    private void insertSingleSynonym(SQLiteDatabase db, String synonym, long animeId) {
        ContentValues values = new ContentValues();
        values.put(AnimeSynonymsRelation.COLUMN_NAME_ANIME, animeId);
        values.put(AnimeSynonymsRelation.COLUMN_NAME_SYNONYM, synonym);
        db.insertWithOnConflict(AnimeSynonymsRelation.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void insertIdValues(SQLiteDatabase db, Anime anime) {
        ContentValues values = new ContentValues();
        values.put(IdDatabaseEntry.COLUMN_NAME_INTERNAL, internalIdGetter.getStringId(anime.getId()));
        values.put(IdDatabaseEntry.COLUMN_NAME_ANN, annIdGetter.getStringId(anime.getId()));
        values.put(IdDatabaseEntry.COLUMN_NAME_MAL, malIdGetter.getStringId(anime.getId()));
        db.insertWithOnConflict(IdDatabaseEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
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
        long characterId = db.insertWithOnConflict(CharacterDatabaseEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        insertAnimeCharacterRelation(db, characterId, animeId);
        insertCharacterVoiceRelation(db, person, characterId, personId);
    }

    private void insertAnimeCharacterRelation(SQLiteDatabase db, long characterId, long animeId) {
        ContentValues values = new ContentValues();
        values.put(AnimeCharactersRelation.COLUMN_NAME_ANIME, animeId);
        values.put(AnimeCharactersRelation.COLUMN_NAME_CHARACTER, characterId);
        db.insertWithOnConflict(AnimeCharactersRelation.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void insertCharacterVoiceRelation(SQLiteDatabase db, Person person, long characterId, long personId) {
        ContentValues values = new ContentValues();
        values.put(CharacterVoiceRelation.COLUMN_NAME_CHARACTER, characterId);
        values.put(CharacterVoiceRelation.COLUMN_NAME_PERSON, personId);
        values.put(CharacterVoiceRelation.COLUMN_NAME_LANGUAGE, person.getLanguage().toString());
        db.insertWithOnConflict(CharacterVoiceRelation.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void insertStaffValues(SQLiteDatabase db, Anime anime, long animeId) {
        for (Person p : anime.getPeopleOfTitle().getStaff()) {
            long personId = insertSinglePersonValues(db, p);
            insertSingleStaffRelationValues(db, p, animeId, personId);
        }
    }

    private long insertSinglePersonValues(SQLiteDatabase db, Person person) {
        calculateInternalPersonId(person);
        ContentValues values = new ContentValues();
        values.put(PersonDatabseEntry._ID, internalIdGetter.getStringId(person.getId()));
        values.put(PersonDatabseEntry.COLUMN_NAME_NAME, person.getName());
        return db.insertWithOnConflict(PersonDatabseEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void insertSingleStaffRelationValues(SQLiteDatabase db, Person person, long animeId, long personId) {
        ContentValues values = new ContentValues();
        values.put(AnimeStaffRelation.COLUMN_NAME_STAFF, personId);
        values.put(AnimeStaffRelation.COLUMN_NAME_ANIME, animeId);
        values.put(AnimeStaffRelation.COLUMN_NAME_ROLE, person.getRole());
        db.insertWithOnConflict(AnimeStaffRelation.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public Anime getAnime(Id id) {
        if (validateId(id))
            return getAnimeWithoutValidation(id);
        return null;
    }

    private Anime getAnimeWithoutValidation(Id id) {
        Anime anime = new Anime();
        SQLiteDatabase db = helper.getReadableDatabase();

        retrieveTitleAndStatus(id, anime, db);
        retrieveId(id, anime, db);
        retrieveSynonyms(id, anime, db);
        retrievePeopleOfTitle(id, anime, db);

        return anime;
    }

    private void retrievePeopleOfTitle(Id id, Anime anime, SQLiteDatabase db) {
        Collection<Person> animeCast = retrieveCast(id, db);
        Collection<Person> animeStaff = retrieveStaff(id, db);

        anime.setPeopleOfTitle(new PeopleOfTitle(animeStaff, animeCast));
    }

    @NonNull
    private Collection<Person> retrieveStaff(Id id, SQLiteDatabase db) {
        Cursor c = db.rawQuery(GET_STAFF_OF_ANIME_QUERY, new String[]{internalIdGetter.getStringId(id)});
        Collection<Person> animeStaff = animeStaffFromCursor(c);
        c.close();

        return animeStaff;
    }

    @NonNull
    private Collection<Person> animeStaffFromCursor(Cursor c) {
        c.moveToFirst();
        Collection<Person> animeStaff = new LinkedList<>();
        while (!c.isAfterLast()) {
            animeStaff.add(animeStaffFromCursorNode(c));
            c.moveToNext();
        }
        return animeStaff;
    }

    private Person animeStaffFromCursorNode(Cursor c) {
        Person p = new Person();
        p.setName(c.getString(c.getColumnIndex(GET_ALL_STAFF_PERSON_NAME)));
        p.setRole(c.getString(c.getColumnIndex(GET_ALL_STAFF_PERSON_ROLE)));
        p.setId(new Id(c.getString(c.getColumnIndex(GET_ALL_STAFF_PERSON_ID))));
        return p;
    }

    @NonNull
    private Collection<Person> retrieveCast(Id id, SQLiteDatabase db) {
        Cursor c = db.rawQuery(GET_CHARACTERS_OF_ANIME_QUERY, new String[] {internalIdGetter.getStringId(id)});
        Collection<Person> animeCast = animeCastFromCursor(c);
        c.close();

        return animeCast;
    }

    @NonNull
    private Collection<Person> animeCastFromCursor(Cursor c) {
        c.moveToFirst();
        Collection<Person> animeCast = new LinkedList<>();
        while (!c.isAfterLast()) {
            animeCast.add(animeCastFromCursorNode(c));
            c.moveToNext();
        }
        return animeCast;
    }

    private Person animeCastFromCursorNode(Cursor c) {
        Person p = new Person();
        p.setName(c.getString(c.getColumnIndex(GET_ALL_CAST_PERSON_NAME)));
        p.setLanguage(Language.stringToLanguage(c.getString(c.getColumnIndex(GET_ALL_CAST_PERSON_LANGUAGE))));
        p.setRole(c.getString(c.getColumnIndex(GET_ALL_CAST_CHARACTER_NAME)));
        p.setId(new Id(c.getString(c.getColumnIndex(GET_ALL_CAST_PERSON_ID))));
        return p;
    }

    private void retrieveSynonyms(Id id, Anime anime, SQLiteDatabase db) {
        Collection<String> synonyms = getSynonyms(id, db);
        anime.setSynonyms(synonyms);
    }

    private Collection<String> getSynonyms(Id id, SQLiteDatabase db) {
        String[] synonymsProjection = {
                AnimeSynonymsRelation.COLUMN_NAME_SYNONYM
        };
        String synonymsWhere = AnimeSynonymsRelation.COLUMN_NAME_ANIME + "=" + internalIdGetter.getStringId(id);
        Cursor c = db.query(AnimeSynonymsRelation.TABLE_NAME,
                synonymsProjection,
                synonymsWhere,
                null,
                null,
                null,
                null);

        return animeSynonymsFromCursor(c);
    }

    private Collection<String> animeSynonymsFromCursor(Cursor c) {
        c.moveToFirst();

        Collection<String> synonyms = new LinkedList<>();
        while (!c.isAfterLast()) {
            synonyms.add(animeSynonymsFromCursorNode(c));
            c.moveToNext();
        }
        c.close();
        return synonyms;
    }

    private String animeSynonymsFromCursorNode(Cursor c) {
        return c.getString(c.getColumnIndex(AnimeSynonymsRelation.COLUMN_NAME_SYNONYM));
    }

    private void retrieveId(Id id, Anime anime, SQLiteDatabase db) {
        String[] idProjection = {
                IdDatabaseEntry.COLUMN_NAME_MAL,
                IdDatabaseEntry.COLUMN_NAME_ANN
        };
        String idWhere = IdDatabaseEntry.COLUMN_NAME_INTERNAL + "=" + internalIdGetter.getStringId(id);
        Cursor c = db.query(IdDatabaseEntry.TABLE_NAME,
                idProjection,
                idWhere,
                null,
                null,
                null,
                null);

        animeIdFromCursor(id, anime, c);

        c.close();
    }

    private void animeIdFromCursor(Id id, Anime anime, Cursor c) {
        c.moveToFirst();
        Id newId = new Id();
        String malId = c.getString(c.getColumnIndex(IdDatabaseEntry.COLUMN_NAME_MAL));
        String annId = c.getString(c.getColumnIndex(IdDatabaseEntry.COLUMN_NAME_ANN));
        newId.addId(StringIdKey.INTERNAL, id.getString(StringIdKey.INTERNAL));
        newId.addId(StringIdKey.MAL, malId);
        newId.addId(StringIdKey.ANN, annId);
        anime.setId(newId);
    }

    private void retrieveTitleAndStatus(Id id, Anime anime, SQLiteDatabase db) {
        String[] animeProjection = {
                AnimeDatabaseEntry.COLUMN_NAME_ID,
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

        titleAndStatusFromCursor(anime, c);

        c.close();
    }

    private void titleAndStatusFromCursor(Anime anime, Cursor c) {
        c.moveToFirst();
        anime.setTitle(c.getString(c.getColumnIndex(AnimeDatabaseEntry.COLUMN_NAME_TITLE)));
        anime.setStatus(WatchingStatus.intToStatus(Integer.parseInt(c.getString(c.getColumnIndex(AnimeDatabaseEntry.COLUMN_NAME_STATUS)))));
    }

    @Override
    public Map<String, Collection<String>> crossReferenceActor(Person person) {
        if (validateId(person.getId()))
            return crossReferenceActorWithoutValidation(person);
        return new HashMap<>();
    }

    private Map<String, Collection<String>> crossReferenceActorWithoutValidation(Person person) {
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.rawQuery(CROSSREFERENCE_STAFF_QUERY, new String[]{internalIdGetter.getStringId(person.getId())});
        Map<String, Collection<String>> characters = getCrossreferencesFromCursor(c);
        c.close();

        return characters;
    }

    private Map<String, Collection<String>> getCrossreferencesFromCursor(Cursor c) {
        Map<String, Collection<String>> characters = new HashMap<>();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String animeTitle = crossreferencedTitleFromCursorNode(c);
            String characterName = crossreferencedCharacterFromCursorNode(c);
            addAnimeCharacterPairToMap(animeTitle, characterName, characters);
            c.moveToNext();
        }
        return characters;
    }

    private String crossreferencedTitleFromCursorNode(Cursor c) {
        return c.getString(c.getColumnIndex(CROSSREFERENCE_TITLE_COLUMN));
    }

    private String crossreferencedCharacterFromCursorNode(Cursor c) {
        return c.getString(c.getColumnIndex(CROSSREFERENCE_NAME_COLUMN));
    }

    private void addAnimeCharacterPairToMap(String anime, String character, Map<String, Collection<String>> allCharacters) {
        if (allCharacters.containsKey(anime))
            allCharacters.get(anime).add(character);
        else {
            Collection<String> charactersOfAnime = new ArrayList<>();
            charactersOfAnime.add(character);
            allCharacters.put(anime, charactersOfAnime);
        }
    }

    @Override
    public Collection<String> getAnimeSynonyms(Id id) {
        if (validateId(id))
            return getSynonyms(id, helper.getReadableDatabase());
        return new ArrayList<>();
    }

    @Override
    public PeopleOfTitle getPeopleOfTitle(Id id) {
        if (validateId(id))
            return getPeopleOfTitleWithoutValidation(id);
        return new PeopleOfTitle();
    }

    private PeopleOfTitle getPeopleOfTitleWithoutValidation(Id id) {
        PeopleOfTitle peopleOfTitle = new PeopleOfTitle();
        SQLiteDatabase db = helper.getReadableDatabase();
        peopleOfTitle.setCast(retrieveCast(id, db));
        peopleOfTitle.setStaff(retrieveStaff(id, db));
        return peopleOfTitle;
    }

    private boolean validateId(Id id) {
        return id != null && !internalIdGetter.getStringId(id).isEmpty();
    }

    @Override
    public void getInternalIdsFromMalIds(Id id) {
        StringIdGetter malIdGetter = new StringIdGetter(StringIdKey.MAL);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(GET_INTERNAL_ID_FROM_MAL, new String[]{malIdGetter.getStringId(id)});
        setInternalIdFromCursor(c, id);
        c.close();
    }

    private void setInternalIdFromCursor(Cursor c, Id id) {
        StringIdSetter internalIdSetter = new StringIdSetter(StringIdKey.INTERNAL);
        c.moveToFirst();
        try {
            internalIdSetter.setString(id, Integer.toString(c.getInt(c.getColumnIndex(IdDatabaseEntry.COLUMN_NAME_INTERNAL))));
        } catch (CursorIndexOutOfBoundsException e) {}
    }

    public int getLargestInternalId() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(GET_LARGEST_INTERNAL_ID_QUERY, null);
        int id = idFromCursor(c);
        c.close();

        return id;
    }

    private int idFromCursor(Cursor c) {
        c.moveToFirst();
        return c.getInt(c.getColumnIndex(LARGEST_INTERNAL_ID_COLUMN));
    }

    private class LargestIdCallback implements Callback<Integer> {
        @Override
        public Integer call() {
            return getLargestInternalId();
        }
    }

}