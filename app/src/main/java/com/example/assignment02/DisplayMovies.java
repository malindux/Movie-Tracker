package com.example.assignment02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.assignment02.databaseHelperClasses.EventsData;

import java.util.ArrayList;
import java.util.Collections;

import static android.provider.BaseColumns._ID;
import static com.example.assignment02.databaseHelperClasses.Constants.CAST;
import static com.example.assignment02.databaseHelperClasses.Constants.DIRECTOR;
import static com.example.assignment02.databaseHelperClasses.Constants.FAVOURITES;
import static com.example.assignment02.databaseHelperClasses.Constants.RATING;
import static com.example.assignment02.databaseHelperClasses.Constants.REVIEW;
import static com.example.assignment02.databaseHelperClasses.Constants.TABLE_NAME;
import static com.example.assignment02.databaseHelperClasses.Constants.TITLE;
import static com.example.assignment02.databaseHelperClasses.Constants.YEAR;

public class DisplayMovies extends AppCompatActivity {

    private static String[] FROM = { _ID, TITLE, YEAR, DIRECTOR, CAST, RATING, REVIEW, FAVOURITES};
    private static String ORDER_BY = TITLE + " DESC";
    private EventsData events;

    private ArrayList<String> favourites = new ArrayList<>(); // This array list will store all the favourite movie titles.

    private CustomListViewAdapter customListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movies);

        events = new EventsData(this);

        Cursor cursor = getEvents();

        ArrayList<String> movieNames = new ArrayList<>(); // This array list will store all movie titles.

        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            movieNames.add(title);
            /*
            * There can be films which are favourites movies already
            * These movies will be added to the 'favourites' list at the beginning
            */
        }
        cursor.close();

        // Sorting movie titles to display
        Collections.sort(movieNames);

        ListView listView = findViewById(R.id.listView);

        customListViewAdapter = new CustomListViewAdapter(this,
                R.layout.listview_row_layout,
                movieNames,
                favourites);
        listView.setAdapter(customListViewAdapter);

    }

    public void displayToast(View view, String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Adding selected movies as favourites to the data base
     * Simply setting the favourites column as 'true' for checked movies and updating the database.
     */
    public void addToFav(View view) {

        // This array list will store all movie titles which user checked as their favourites.
        ArrayList<String> selectedFavourites = customListViewAdapter.getSelectedFavourites();

        if (selectedFavourites.size() > 0) {

            SQLiteDatabase db = events.getWritableDatabase();
            ContentValues values = new ContentValues();

            Cursor cursor = getEvents();

            while (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                String title = cursor.getString(1);
                for (String favName : selectedFavourites) {
                    if (favName.equals(title)) {
                        String year = cursor.getString(2);
                        String director = cursor.getString(3);
                        String cast = cursor.getString(4);
                        long rating = cursor.getLong(5);
                        String review = cursor.getString(6);
                        values.put(TITLE, title);
                        values.put(YEAR, year);
                        values.put(DIRECTOR, director);
                        values.put(CAST, cast);
                        values.put(RATING, rating);
                        values.put(REVIEW, review);
                        values.put(FAVOURITES, "true");
                        db.update(TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(id)});
                    }
                }
            }
            displayToast(view, "Added to the Favourites");
        }else {
            displayToast(view,"Select a Movie");
        }

    }

    /*
     Perform a managed query. The Activity will
     handle closing and re-querying the cursor
     when needed.
    */
    private Cursor getEvents() {
        SQLiteDatabase db = events.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null, null, ORDER_BY);
        return cursor;
    }

}
