package com.example.assignment02.editMovie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.assignment02.R;
import com.example.assignment02.databaseHelperClasses.EventsData;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.example.assignment02.databaseHelperClasses.Constants.CAST;
import static com.example.assignment02.databaseHelperClasses.Constants.DIRECTOR;
import static com.example.assignment02.databaseHelperClasses.Constants.FAVOURITES;
import static com.example.assignment02.databaseHelperClasses.Constants.RATING;
import static com.example.assignment02.databaseHelperClasses.Constants.REVIEW;
import static com.example.assignment02.databaseHelperClasses.Constants.TABLE_NAME;
import static com.example.assignment02.databaseHelperClasses.Constants.TITLE;
import static com.example.assignment02.databaseHelperClasses.Constants.YEAR;

public class EditMovies extends AppCompatActivity {

    private static String[] FROM = { _ID, TITLE, YEAR, DIRECTOR, CAST, RATING, REVIEW, FAVOURITES};
    private static String ORDER_BY = TITLE + " DESC";
    private EventsData events;

    private static ListView listView;
    private static Context context;

    /**
     * This activity will show all the stored movie titles in a list view
     * When user clicks on an item in the list view, user will be redirected to another page to edit the movie
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movies);

        context = this;
        events = new EventsData(this);
        Cursor cursor = getEvents();
        ArrayList<String> movieNames = new ArrayList<>(); // This array list will store all movie titles.

        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            movieNames.add(title);
        }
        cursor.close();

        // Sorting movie titles to display
        Collections.sort(movieNames);

        listView = findViewById(R.id.listView);
        CustomListViewAdapterEditMovies customListViewAdapter = new CustomListViewAdapterEditMovies(this,
                R.layout.listview_row_layout,
                movieNames);
        listView.setAdapter(customListViewAdapter);

        /*
        * Setting a Listener for movie item clicks on the list view
        * User will redirect to another window to update the movie
        * with all the data of the clicked movie
        */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = getEvents();
                List<String> movieData = new ArrayList<>(); // Holds the data of the clicked movie
                while (cursor.moveToNext()) {
                    long movieId = cursor.getLong(0);
                    String title = cursor.getString(1);
                    if (parent.getItemAtPosition(position).equals(title)) {
                        String year = cursor.getString(2);
                        String director = cursor.getString(3);
                        String cast = cursor.getString(4);
                        long rating = cursor.getLong(5);
                        String review = cursor.getString(6);
                        String fav = cursor.getString(7);
                        movieData.add(String.valueOf(movieId));
                        movieData.add(title);
                        movieData.add(year);
                        movieData.add(director);
                        movieData.add(cast);
                        movieData.add(String.valueOf(rating));
                        movieData.add(review);
                        movieData.add(fav);
                        updateMovie(movieData);
                    }
                }
            }
        });

    }

    // Starting UpdateMovie activity
    private void updateMovie(List<String> movieData) {
        Intent intent = new Intent(EditMovies.this, UpdateMovie.class);
        intent.putExtra("movieData", (Serializable) movieData);
        startActivity(intent);
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

    // Updates list if user deletes a movie
    public static void updateList(ArrayList<String> movieNames) {
        CustomListViewAdapterEditMovies customListViewAdapter = new CustomListViewAdapterEditMovies(context,
                R.layout.listview_row_layout,
                movieNames);
        listView.setAdapter(customListViewAdapter);
    }

}