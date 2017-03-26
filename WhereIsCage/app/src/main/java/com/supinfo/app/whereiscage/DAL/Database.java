package com.supinfo.app.whereiscage.DAL;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Database {

    private static Database _instance;

    private DatabaseOpenHelper db;
    private SQLiteDatabase database;

    private Database(Context context){
        db = new DatabaseOpenHelper(context);
    }

    @Override
    public void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public static Database Init(Context context){
        _instance = new Database(context);
        _instance.open();
        return _instance;
    }

    public static Database Intance(){
        return _instance;
    }

    public void open() {
        try {
            database = db.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
