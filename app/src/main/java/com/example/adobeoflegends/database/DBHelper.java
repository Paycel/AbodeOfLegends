package com.example.adobeoflegends.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.adobeoflegends.objects.Card;
import com.example.adobeoflegends.objects.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "GameTable";
    private static final String TABLE_COLUMN_ID = "ID";
    private static final String TABLE_COLUMN_EMAIL = "EMAIL";
    private static final String TABLE_COLUMN_POINTS = "POINTS";
    private static final String TABLE_COLUMN_OBJECT = "OBJECT";
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
                + TABLE_COLUMN_OBJECT + " TEXT,"
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
        Player player = new Player();
        player.setDeck(new Card().getNewDeck());
        player.setHealthPoints(20);
        player.setManaPoints(20);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String temp = gson.toJson(player);
        cv.put(TABLE_COLUMN_OBJECT, temp);
        long rowID = db.insert(TABLE_NAME, null, cv);
        Log.d(LOG_TAG, "Inserted, ID = " + rowID);
    }

    public void updateCard(SQLiteDatabase db, String email, String name, int dHP, int dDP, int cost){
        setPoints(db, email, getPoints(db, email) - cost);
        Player player = getPlayer(db, email);
        List<Card> list = player.getDeck();
        for (Card card: list)
            if (card.getName().equals(name)){
                card.setHealthPoints(card.getHealthPoints() + dHP);
                card.setDamagePoints(card.getDamagePoints() + dDP);
                break;
            }
        player.setDeck(list);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String temp = gson.toJson(player);
        ContentValues cv = new ContentValues();
        cv.put(TABLE_COLUMN_OBJECT, temp);
        db.update(TABLE_NAME, cv, TABLE_COLUMN_EMAIL + "='" + email + "'", null);
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
        c.close();
        return -1;
    }

    public void setPoints(SQLiteDatabase db, String email, int points){
        db.execSQL("UPDATE " + TABLE_NAME
        + " SET " + TABLE_COLUMN_POINTS + "=" + points
        + " WHERE " + TABLE_COLUMN_EMAIL + "='" + email + "';");
    }

    public void addACH(SQLiteDatabase db, String email, String achieve){
        Player player = getPlayer(db, email);
        player.addAchievement(achieve);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String object = gson.toJson(player);
        ContentValues cv = new ContentValues();
        cv.put(TABLE_COLUMN_OBJECT, object);
        db.update(TABLE_NAME, cv,TABLE_COLUMN_EMAIL + "='" + email + "'", null);
    }

    public void copyBattles(SQLiteDatabase db, String from, String to){
        Player player_from = getPlayer(db, from);
        Player player_to = new Player();
        ArrayList<String> copied = new ArrayList<>(player_from.getAchievements());
        for (int i = 0; i < copied.size(); i++) player_to.addAchievement(copied.get(i));
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String object = gson.toJson(player_to);
        ContentValues cv = new ContentValues();
        cv.put(TABLE_COLUMN_OBJECT, object);
        db.update(TABLE_NAME, cv,TABLE_COLUMN_EMAIL + "='" + to + "'", null);
    }

    public Player getPlayer(SQLiteDatabase db, String email){
        String query = "SELECT " + TABLE_COLUMN_OBJECT + " FROM " + TABLE_NAME + " WHERE " + TABLE_COLUMN_EMAIL + "='" + email + "';";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String object = c.getString(0);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        c.close();
        return gson.fromJson(object, Player.class);
    }

    public void showInfo(SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex(TABLE_COLUMN_ID);
            int emailColIndex = c.getColumnIndex(TABLE_COLUMN_EMAIL);
            int pointsColIndex = c.getColumnIndex(TABLE_COLUMN_POINTS);
            int objectIndex = c.getColumnIndex(TABLE_COLUMN_OBJECT);
            do {
                String _email = c.getString(emailColIndex);
                int points = c.getInt(pointsColIndex);
                // Log.d(LOG_TAG, "ID = " + c.getInt(idColIndex) + ", email = " + _email + ", points = " + points + ", object = " + c.getString(objectIndex));
            } while (c.moveToNext());
        }
        c.close();
    }

    public int getRowsCount(SQLiteDatabase db){
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }

}
