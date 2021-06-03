package com.example.assignment02;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.assignment02.databaseHelperClasses.EventsData;

import java.util.ArrayList;
import java.util.Arrays;
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

public class SearchMovie extends AppCompatActivity {

    private static String[] FROM = { _ID, TITLE, YEAR, DIRECTOR, CAST, RATING, REVIEW, FAVOURITES};
    private static String ORDER_BY = TITLE + " DESC";
    private EventsData events;

    private ArrayList<String> searchedMoviesTitles = new ArrayList<>(); // Stores movie titles
    private ArrayList<List<String>> searchedMoviesDirectors = new ArrayList<>(); // Stores movie director names
    private ArrayList<List<String>> searchedMoviesCast = new ArrayList<>(); // Store movie cast

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        events = new EventsData(this);

    }

    public void displayToast(View view, String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Searching giver key word by user in all movie titles, director names and cast names
     * Simply looping through cursor object and checking if the given key word is in the title,
     * director names or cast member names
     */
    public void lookUp(View view) {

        searchedMoviesTitles.clear();
        searchedMoviesDirectors.clear();
        searchedMoviesCast.clear();

        boolean found = false; // To check if any result were found or not

        EditText searchInput = findViewById(R.id.searchInput);
        String searchFor =  searchInput.getText().toString().toLowerCase();

        Cursor cursor = getEvents();

        /*
        * Validating input
        * Input can not be empty
        */
        if (searchFor.equals("")){
            searchInput.setError("This field can not be empty");
        }else {
            while (cursor.moveToNext()) {
                String title = cursor.getString(1);
                String dir = cursor.getString(3);
                String cast = cursor.getString(4);
                if (searchedMoviesTitles.contains(title)) {
                    continue;
                }
                if (title.toLowerCase().contains(searchFor) || dir.toLowerCase().contains(searchFor) || cast.toLowerCase().contains(searchFor)) {
                    searchedMoviesTitles.add(title);
                    searchedMoviesDirectors.add(new ArrayList<>(Arrays.asList(dir.split(","))));
                    searchedMoviesCast.add(new ArrayList<>(Arrays.asList(cast.split(","))));
                    found = true;
                }
            }
            cursor.close();
        }

        if (found){
            displayToast(view, "Total of " + searchedMoviesTitles.size() + " matches found");
        }else displayToast(view, "No Matches Found");

        ListView listView = findViewById(R.id.listView);
        CustomListViewAdapterSearchMovie customListViewAdapter = new CustomListViewAdapterSearchMovie(this,
                R.layout.activity_search_movie_listview_layout,
                searchedMoviesTitles,
                searchedMoviesDirectors,
                searchedMoviesCast);
        listView.setAdapter(customListViewAdapter);

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