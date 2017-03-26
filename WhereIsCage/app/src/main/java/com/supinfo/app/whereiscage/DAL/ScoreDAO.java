package com.supinfo.app.whereiscage.DAL;


import android.content.ContentValues;
import android.database.Cursor;

import com.supinfo.app.whereiscage.Utils.Gamemode;
import com.supinfo.app.whereiscage.model.Scoreboard;

import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    public static Scoreboard createScore(String pseudo, float score, long gamemode) {
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_PLAYER_NAME, pseudo);
        values.put(DatabaseOpenHelper.COLUMN_SCORE, score);
        values.put(DatabaseOpenHelper.COLUMN_MODE, gamemode);

        long insertId = Database.Intance().getDatabase().insert(DatabaseOpenHelper.TABLE, null, values);
        Cursor cursor = Database.Intance().getDatabase().query(DatabaseOpenHelper.TABLE,
                DatabaseOpenHelper.allColumns, DatabaseOpenHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Scoreboard scoreboard = cursorToScoreboard(cursor);
        cursor.close();
        return scoreboard;
    }

    private static Scoreboard cursorToScoreboard(Cursor cursor) {
        Scoreboard score = new Scoreboard();
        score.setId(cursor.getLong(0));
        score.setScore(cursor.getLong(1));
        score.setScore(cursor.getLong(2));
        score.setGamemode(cursor.getLong(3));
        return score;
    }

    public static List<Scoreboard> getAllScoreboard(Gamemode gamemode) {
        List<Scoreboard> quotes = new ArrayList<Scoreboard>();

        Cursor cursor = Database.Intance().getDatabase().query(DatabaseOpenHelper.TABLE,
                DatabaseOpenHelper.allColumns, DatabaseOpenHelper.COLUMN_MODE + "=?", new String[] { String.valueOf(gamemode.value()) }, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Scoreboard quote = cursorToScoreboard(cursor);
            quotes.add(quote);
            cursor.moveToNext();
        }
        cursor.close();
        return quotes;
    }

}
