package com.example.lifecycle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table users(email Text primary key, password Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String email, String password) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);

        long result = db.insert("users", null, contentValues);

        // -1 something went wrong
        return result == -1 ? false : true;
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from users WHERE  email = ?", new String[]{email});

        // Found a match?
        return cursor.getCount() > 0 ? true : false;
    }

    public Boolean checkValidLogin(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from users WHERE  email = ? AND password = ?", new String[]{email, password});

        // Found a match?
        return cursor.getCount() > 0 ? true : false;
    }
}
