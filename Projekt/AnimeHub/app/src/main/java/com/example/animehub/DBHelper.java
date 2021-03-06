package com.example.animehub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private Cursor cursor;
    // Database Name
    private static final String DATABASE_NAME = "AnimeHub.db";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Table Names
    private static final String TABLE_ANIME = "MyAnimeList";
    // Common column names
    private static final String KEY_ANIME_ID = "mal_id";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_TITLE = "title";
    private static final String KEY_EPISODES = "episodes";
    private static final String KEY_SYNOPSIS = "synopsis";
    private static final String KEY_AIRING = "airing";
    private static final String KEY_SCORE = "score";
    private static final String KEY_URL = "url";
    // Table Create Statements
    private static final String CREATE_TABLE_ANIME = "CREATE TABLE " + TABLE_ANIME + "(" +
            KEY_ANIME_ID + " TEXT NOT NULL PRIMARY KEY UNIQUE," +
            KEY_IMAGE_URL + " TEXT," + KEY_TITLE + " TEXT," + KEY_EPISODES + " TEXT," +
            KEY_SYNOPSIS + " TEXT," + KEY_AIRING + " TEXT," + KEY_SCORE + " TEXT," + KEY_URL + " TEXT)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ANIME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIME);
        // create new tables
        onCreate(db);
    }

    public Boolean insertAnimeDetails(AnimeObject object) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String animeId = object.getAnimeId();

        contentValues.put(KEY_ANIME_ID, animeId);
        contentValues.put(KEY_IMAGE_URL, object.getImageUrl());
        contentValues.put(KEY_TITLE, object.getTitle());
        contentValues.put(KEY_EPISODES, object.getEpisodes());
        contentValues.put(KEY_SYNOPSIS, object.getSynopsis());
        contentValues.put(KEY_AIRING, object.getAiring());

        String score = isNumeric(object.getScore()) ? object.getScore() + "\u2b50" : object.getScore();
        contentValues.put(KEY_SCORE, score);

        contentValues.put(KEY_URL, object.getUrl());

        long result = db.insert(TABLE_ANIME, null, contentValues);

        // -1 something went wrong
        return result != -1;
    }

    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Boolean detailsExists(String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + TABLE_ANIME + " WHERE " +
                KEY_ANIME_ID + " = ?", new String[]{location});

        // Found a match?
        return cursor.getCount() > 0;
    }

    public ArrayList<AnimeObject> getAnimeList(ArrayList<AnimeObject> objects) {

        SQLiteDatabase db = this.getWritableDatabase();


        String selectAll = "SELECT * FROM " + TABLE_ANIME;

        Cursor cursor = db.rawQuery(selectAll, null);

        while (cursor.moveToNext()) {
            String value_id = cursor.getString(0);
            String value_image = cursor.getString(1);
            String value_title = cursor.getString(2);
            String value_episodes = cursor.getString(3);
            String value_synopsis = cursor.getString(4);
            String value_airing = cursor.getString(5);
            String value_score = cursor.getString(6);
            String value_url = cursor.getString(7);


            AnimeObject object = new AnimeObject();

            object.setAnimeId(value_id);
            object.setImageUrl(value_image);
            object.setTitle(value_title);
            object.setEpisodes(value_episodes);
            object.setSynopsis(value_synopsis);
            object.setAiring(value_airing);
            object.setScore(value_score);
            object.setUrl(value_url);

            objects.add(object);
        }

        cursor.close();
        db.close();

        return objects;
    }

    public boolean isEmpty() {

        SQLiteDatabase db = this.getWritableDatabase();

        String countAll = "SELECT COUNT(*) FROM " + TABLE_ANIME;

        Cursor cursor = db.rawQuery(countAll, null);
        cursor.moveToNext();

        Boolean result = cursor.getString(0).equals("0");
        cursor.close();

        return result;
    }

    public boolean deleteAnimeObject(String animeId) {

        SQLiteDatabase db = this.getWritableDatabase();

        int nrOfRowsDeleted = db.delete(TABLE_ANIME, KEY_ANIME_ID + " = ?",
                new String[]{String.valueOf(animeId)});

        return nrOfRowsDeleted > 0;
    }

    public void deleteAllObjects() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_ANIME);
        db.close();
    }

}
