package com.activities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class LeaderboardDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "leaderboard.db";
    private static final int DATABASE_VERSION = 1;

    public LeaderboardDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE leaderboard (id INTEGER PRIMARY KEY, name TEXT, score INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

class LeaderboardEntry {
    private long id;
    private String name;
    private int score;

    public LeaderboardEntry(long id, String name, int score) {
        this.name = name;
        this.score = score;
    }

    public LeaderboardEntry(String name, int score) {
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }
}
