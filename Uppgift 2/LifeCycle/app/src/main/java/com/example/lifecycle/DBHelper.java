package com.example.lifecycle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private Cursor cursor;
    // Database Name
    private static final String DATABASE_NAME = "Login.db";
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_USER_DETAILS = "userdetails";

    // Common column names
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NAME = "name";
    private static final String KEY_HOBBIES = "hobbies";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_GENDER = "gender";

    // Table Create Statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "(" +
            KEY_EMAIL + " TEXT PRIMARY KEY," + KEY_PASSWORD + " TEXT)";

    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER_DETAILS + "(" +
            KEY_EMAIL + " TEXT PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_HOBBIES + " TEXT,"
            + KEY_COUNTRY + " TEXT," + KEY_GENDER + " TEXT)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // Delete database
        //context.deleteDatabase("Login.db");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETAILS);

        // create new tables
        onCreate(db);
    }

    /* * * * For Login * * * */

    public Boolean insertUserData(String email, String password) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_EMAIL, email);
        contentValues.put(KEY_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, contentValues);

        // -1 something went wrong
        return result == -1 ? false : true;
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                KEY_EMAIL + " = ?", new String[]{email});

        // Found a match?
        return cursor.getCount() > 0 ? true : false;
    }

    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_EMAIL +
                " = ? AND " + KEY_PASSWORD + " = ?", new String[]{email, password});

        // Found a match?
        return cursor.getCount() > 0 ? true : false;
    }


    /* * * * For Form Details * * * */

    public Boolean insertUserDetails(UserDetailsEntity user) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String email = user.getEmail();

        contentValues.put(KEY_EMAIL, email);
        contentValues.put(KEY_NAME, user.getName());
        contentValues.put(KEY_HOBBIES, user.getHobbies());
        contentValues.put(KEY_COUNTRY, user.getCountry());
        contentValues.put(KEY_GENDER, user.getGender());

        long result = -1;

        if (detailsExists(email)) {
            result = db.update(TABLE_USER_DETAILS, contentValues, KEY_EMAIL + " = ?",
                    new String[]{email});
        } else {
            result = db.insert(TABLE_USER_DETAILS, null, contentValues);
        }

        // -1 something went wrong
        return result == -1 ? false : true;
    }

    public Boolean detailsExists(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + TABLE_USER_DETAILS + " WHERE " +
                KEY_EMAIL + " = ?", new String[]{email});

        // Found a match?
        return cursor.getCount() > 0 ? true : false;
    }

    public UserDetailsEntity getUserDetails(String email) {
        UserDetailsEntity details = new UserDetailsEntity(email);
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("SELECT  * FROM " + TABLE_USER_DETAILS + " WHERE "
                + KEY_EMAIL + " = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            System.out.println(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            details.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            details.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            details.setHobbies(cursor.getString(cursor.getColumnIndex(KEY_HOBBIES)));
            details.setGender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));
            details.setCountry(cursor.getString(cursor.getColumnIndex(KEY_COUNTRY)));
        }

        return details;
    }

    public boolean deleteUserDetails(String email) {

        SQLiteDatabase db = this.getWritableDatabase();

        int nrOfRowsDeleted = db.delete(TABLE_USER_DETAILS, KEY_EMAIL + " = ?",
                new String[]{String.valueOf(email)});

        return nrOfRowsDeleted > 0;
    }

}
