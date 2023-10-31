package com.sharepay.ug;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sharepay.ug.Model.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SharePay.db";
    private static final int DATABASE_VERSION = 1;

    // SQL statement to create the user table
    private static final String CREATE_TABLE_USER = "CREATE TABLE user (" +
            "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "phone_number TEXT NOT NULL," +
            "first_name TEXT," +
            "last_name TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    //Create User
    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone_number", user.getPhone());
        values.put("first_name", user.getFirstName());
        values.put("last_name", user.getLastName());

        long userId = db.insert("user", null, values);
        db.close();
        return userId;
    }

    @SuppressLint("Range")
    public String getPhoneNumber() {
        SQLiteDatabase db = this.getReadableDatabase();
        String phoneNumber = null;
        String[] columns = { "phone_number" };

        Cursor cursor = db.query("user", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            phoneNumber = cursor.getString(cursor.getColumnIndex("phone_number"));
        }

        cursor.close();
        return phoneNumber;
    }

    @SuppressLint("Range")
    public String getFirstName() {
        SQLiteDatabase db = this.getReadableDatabase();
        String firstName = null;
        String[] columns = { "first_name" };

        Cursor cursor = db.query("user", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            firstName = cursor.getString(cursor.getColumnIndex("first_name"));
        }

        cursor.close();
        return firstName;
    }

    @SuppressLint("Range")
    public String getLastName() {
        SQLiteDatabase db = this.getReadableDatabase();
        String lastName = null;
        String[] columns = { "last_name" };

        Cursor cursor = db.query("user", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            lastName = cursor.getString(cursor.getColumnIndex("last_name"));
        }

        cursor.close();
        return lastName;
    }

    public boolean isUsersTableEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean isEmpty = true;

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM user", null);
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            isEmpty = count == 0;
        }

        if (cursor != null) {
            cursor.close();
        }

        return isEmpty;
    }
}