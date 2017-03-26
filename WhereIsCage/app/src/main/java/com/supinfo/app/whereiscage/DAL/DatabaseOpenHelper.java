package com.supinfo.app.whereiscage.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE = "scoreboard";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLAYER_NAME = "player";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_MODE = "gamemode";

    public static String[] allColumns = {
            DatabaseOpenHelper.COLUMN_ID,
            DatabaseOpenHelper.COLUMN_PLAYER_NAME,
            DatabaseOpenHelper.COLUMN_SCORE,
            DatabaseOpenHelper.COLUMN_MODE
    };

    private static final String DATABASE_NAME = "whereiscage.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_SCORE
            + " long not null," + COLUMN_PLAYER_NAME + " text not null,"
            + COLUMN_MODE + " long not null);";

    DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
