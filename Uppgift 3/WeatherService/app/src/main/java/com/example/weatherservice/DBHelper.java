package com.example.weatherservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private Cursor cursor;
    // Database Name
    private static final String DATABASE_NAME = "Weather.db";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Table Names
    private static final String TABLE_WEATHER = "weather";
    // Common column names
    private static final String KEY_LOCATION = "location";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_TIME = "time";
    private static final String KEY_STATUS = "status";
    private static final String KEY_TEMP = "temperature";
    private static final String KEY_TEMP_MIN = "tempMin";
    private static final String KEY_TEMP_MAX = "tempMax";
    private static final String KEY_IMAGE = "image";
    // Table Create Statements
    private static final String CREATE_TABLE_WEATHER = "CREATE TABLE " + TABLE_WEATHER + "(" +
            "entry_id INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LOCATION + " TEXT NOT NULL UNIQUE," +
            KEY_COUNTRY + " TEXT," + KEY_TIME + " TEXT," + KEY_STATUS + " TEXT," +
            KEY_TEMP + " TEXT," + KEY_TEMP_MIN + " TEXT," + KEY_TEMP_MAX + " TEXT," +
            KEY_IMAGE + " TEXT)";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        // create new tables
        onCreate(db);
    }

    public Boolean insertWeatherDetails(WeatherEntity weather) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String location = weather.getLocation();

        contentValues.put(KEY_LOCATION, location);
        contentValues.put(KEY_COUNTRY, weather.getCountry());
        contentValues.put(KEY_TIME, weather.getTime());
        contentValues.put(KEY_STATUS, weather.getStatus());
        contentValues.put(KEY_TEMP, weather.getTemp());
        contentValues.put(KEY_TEMP_MIN, weather.getTempMin());
        contentValues.put(KEY_TEMP_MAX, weather.getTempMax());
        contentValues.put(KEY_IMAGE, weather.getWeatherImage());

        long result = -1;

        if (detailsExists(location)) {
            result = db.update(TABLE_WEATHER, contentValues, KEY_LOCATION + " = ?",
                    new String[]{location});
        } else {
            result = db.insert(TABLE_WEATHER, null, contentValues);
        }

        // -1 something went wrong
        return result == -1 ? false : true;
    }

    public Boolean detailsExists(String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + TABLE_WEATHER + " WHERE " +
                KEY_LOCATION + " = ?", new String[]{location});

        // Found a match?
        return cursor.getCount() > 0 ? true : false;
    }

    public ArrayList<WeatherEntity> getWeatherList(ArrayList<WeatherEntity> valueEntitys) {

        SQLiteDatabase db = this.getWritableDatabase();


        String selectAll = "SELECT " + KEY_LOCATION + ", " + KEY_COUNTRY+ ", " + KEY_STATUS + ", " +
                KEY_IMAGE + " FROM " + TABLE_WEATHER;

        Cursor cursor = db.rawQuery(selectAll, null);

        while (cursor.moveToNext()) {
            String value_location = cursor.getString(0);
            String value_country = cursor.getString(1);
            String value_status = cursor.getString(2);
            String value_image = cursor.getString(3);

            WeatherEntity localEntity = new WeatherEntity();
            localEntity.setLocation(value_location);
            localEntity.setCountry(value_country);
            localEntity.setStatus(value_status);
            localEntity.setWeatherImage(value_image);
            valueEntitys.add(localEntity);
        }

        cursor.close();
        db.close();

        return valueEntitys;
    }

    public String getLatestEntry() {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectAll = "SELECT * FROM " + TABLE_WEATHER + " WHERE entry_id = (SELECT MAX(entry_id) FROM " + TABLE_WEATHER + ")";
        cursor = db.rawQuery(selectAll, null);

        String value = "";
        if (cursor.moveToFirst()) {
            value = cursor.getString(cursor.getColumnIndex(KEY_LOCATION));
                   // + ", " + cursor.getString(cursor.getColumnIndex(KEY_COUNTRY));
        }

        return value;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+ TABLE_WEATHER);
        db.close();
    }
}
