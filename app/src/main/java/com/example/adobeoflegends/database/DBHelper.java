package com.example.adobeoflegends.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "GameTable";
    private static final String TABLE_COLUMN_ID = "ID";
    private static final String TABLE_COLUMN_EMAIL = "EMAIL";
    private static final String TABLE_COLUMN_POINTS = "POINTS";
    private static final String DATABASE_NAME = "MY_DB";
    private static final String LOG_TAG = "MY_LOGS";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TABLE_COLUMN_EMAIL + " TEXT,"
                + TABLE_COLUMN_POINTS + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addUser(SQLiteDatabase db, String email, int points){
        ContentValues cv = new ContentValues();
        cv.put(TABLE_COLUMN_EMAIL, email);
        cv.put(TABLE_COLUMN_POINTS, points);
        long rowID = db.insert(TABLE_NAME, null, cv);
        Log.d(LOG_TAG, "Inserted, ID = " + rowID);
    }

    public int getPoints(SQLiteDatabase db, String email) {
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex(TABLE_COLUMN_ID);
            int emailColIndex = c.getColumnIndex(TABLE_COLUMN_EMAIL);
            int pointsColIndex = c.getColumnIndex(TABLE_COLUMN_POINTS);
            do {
                String _email = c.getString(emailColIndex);
                int points = c.getInt(pointsColIndex);
                Log.d(LOG_TAG, "ID = " + c.getInt(idColIndex) + ", email = " + _email + ", points = " + points);
                if (email.equals(_email)) return points;
            } while (c.moveToNext());
        }
        return -1;
    }

    public void setPoints(SQLiteDatabase db, String email, int points){
        db.execSQL("UPDATE " + TABLE_NAME
        + " SET " + TABLE_COLUMN_POINTS + "=" + points
        + " WHERE " + TABLE_COLUMN_EMAIL + "='" + email + "';");
    }

    public void showInfo(SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex(TABLE_COLUMN_ID);
            int emailColIndex = c.getColumnIndex(TABLE_COLUMN_EMAIL);
            int pointsColIndex = c.getColumnIndex(TABLE_COLUMN_POINTS);
            do {
                String _email = c.getString(emailColIndex);
                int points = c.getInt(pointsColIndex);
                Log.d(LOG_TAG, "ID = " + c.getInt(idColIndex) + ", email = " + _email + ", points = " + points);
            } while (c.moveToNext());
        }
    }

    public int getRowsCount(SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        int count = 0;
        if (c.moveToFirst()) {
            count++;
            } while (c.moveToNext());
        return count;
    }

}
