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

public class FavouritesList extends AppCompatActivity {

    private static String[] FROM = { _ID, TITLE, YEAR, DIRECTOR, CAST, RATING, REVIEW, FAVOURITES};
    private static String ORDER_BY = TITLE + " DESC";
    private EventsData events;

    private ArrayList<String> favourites = new ArrayList<>(); // This array list will store all the favourite movie titles.

    private CustomListViewAdapter customListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_list);

        events = new EventsData(this);

        Cursor cursor = getEvents();

        ArrayList<String> movieNames = new ArrayList<>();

        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            String fav = cursor.getString(7);
            //Storing all the favourite movie titles
            if(fav.equals("true")){
                movieNames.add(title);
                favourites.add(title);
            }
        }
        cursor.close();

        // Sorting favourite movie titles to display
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
     * Updating favourite movies if there are changes in user favourites
     * Simply setting the favourites column as 'true' or 'false' for checked and unchecked movies and updating the database.
     */
    public void updateFavourites(View view) {

        // This array list will store all movie titles which user checked as their favourites.
        ArrayList<String> selectedFavourites = customListViewAdapter.getSelectedFavourites();

        SQLiteDatabase db = events.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = getEvents();

        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String title = cursor.getString(1);
            if(favourites.contains(title)) {
                if (selectedFavourites.contains(title)) {
                    System.out.println("True");
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
                } else {
                    System.out.println("False");
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
                    values.put(FAVOURITES, "false");
                }
                db.update(TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(id)});
            }
        }

        displayToast(view,"Favourites have been updated");

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