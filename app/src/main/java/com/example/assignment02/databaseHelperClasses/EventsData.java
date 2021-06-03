package com.example.assignment02.databaseHelperClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.example.assignment02.databaseHelperClasses.Constants.TABLE_NAME;
import static com.example.assignment02.databaseHelperClasses.Constants.TITLE;
import static com.example.assignment02.databaseHelperClasses.Constants.YEAR;
import static com.example.assignment02.databaseHelperClasses.Constants.DIRECTOR;
import static com.example.assignment02.databaseHelperClasses.Constants.CAST;
import static com.example.assignment02.databaseHelperClasses.Constants.RATING;
import static com.example.assignment02.databaseHelperClasses.Constants.REVIEW;
import static com.example.assignment02.databaseHelperClasses.Constants.FAVOURITES;

public class EventsData extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "films.db";
    private static final int DATABASE_VERSION = 1;

    /* Create a helper object for the Events database */
    public EventsData(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME
                + " (" + _ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " TEXT NOT NULL, "
                + YEAR + " TEXT NOT NULL, "
                + DIRECTOR + " TEXT, "
                + CAST + " TEXT, "
                + RATING + " INTEGER, "
                + REVIEW + " TEXT, "
                + FAVOURITES + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

}
