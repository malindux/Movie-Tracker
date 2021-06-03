package com.example.assignment02.databaseHelperClasses;

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
    public static final String TABLE_NAME = "filmInfo" ;
    // Columns in the Events database
    public static final String TITLE = "title" ;
    public static final String YEAR = "year" ;
    public static final String DIRECTOR = "director" ;
    public static final String CAST = "cast_team";
    public static final String RATING = "rating";
    public static final String REVIEW = "review";
    public static final String FAVOURITES = "favourites";
}
