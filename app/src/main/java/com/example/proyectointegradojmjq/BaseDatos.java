package com.example.proyectointegradojmjq;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {

    private static final String COMMENTS_TABLE_CREATE = "CREATE TABLE conversaciones(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "mensaje TEXT, timeStamperino TEXT, idEmisor INTEGER, idReceptorFK INTEGER , mePertenece INTEGER)";
    private static final String DB_NAME = "Chaterinos.db";
    private static final int DB_VERSION = 1;

    public BaseDatos(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COMMENTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

