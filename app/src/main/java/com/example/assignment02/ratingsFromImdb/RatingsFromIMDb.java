package com.example.assignment02.ratingsFromImdb;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.assignment02.R;
import com.example.assignment02.databaseHelperClasses.EventsData;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.assignment02.databaseHelperClasses.Constants.CAST;
import static com.example.assignment02.databaseHelperClasses.Constants.DIRECTOR;
import static com.example.assignment02.databaseHelperClasses.Constants.FAVOURITES;
import static com.example.assignment02.databaseHelperClasses.Constants.RATING;
import static com.example.assignment02.databaseHelperClasses.Constants.REVIEW;
import static com.example.assignment02.databaseHelperClasses.Constants.TABLE_NAME;
import static com.example.assignment02.databaseHelperClasses.Constants.TITLE;
import static com.example.assignment02.databaseHelperClasses.Constants.YEAR;

public class RatingsFromIMDb extends AppCompatActivity {

    private static String[] FROM = { _ID, TITLE, YEAR, DIRECTOR, CAST, RATING, REVIEW, FAVOURITES};
    private static String ORDER_BY = TITLE + " DESC";
    private EventsData events;

    private ArrayList<String> movieNames = new ArrayList<>(); // Stores all movie titles
    private ArrayList<String> years = new ArrayList<>(); // Stores all movie years

    private static ArrayList<String> checkedMovieNames = new ArrayList<>(); // Stores user selected movie titles

    private CustomListViewAdapterRatingFromImdb customListViewAdapter;

    public static TextView downloading;
    public static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings_from_imdb);

        events = new EventsData(this);
        Cursor cursor = getEvents();

        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            String year = cursor.getString(2);
            // Storing movie titles and years
            movieNames.add(title);
            years.add(year);
        }
        cursor.close();

        ListView listView = findViewById(R.id.listView);

        customListViewAdapter = new CustomListViewAdapterRatingFromImdb(this,
                R.layout.listview_row_layout,
                movieNames,
                years);
        listView.setAdapter(customListViewAdapter);

    }

    /**
     * Action for the Find Rating button
     * This will execute the AsynTask class which will call to the api and
     * get the movie data
     */
    public void findRatings(View view) {

        downloading = findViewById(R.id.downloading);
        progressBar = findViewById(R.id.progressBar);

        downloading.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        /*
        * This array list will hold checked movies names and years combined
        * Purpose of this is we have to pass title+year to the api link to get the movie data from the server
        * So this array list will hold Strings of (Title + Year) format
        */
        ArrayList<String> checkedMovieNamesYears = customListViewAdapter.getSelectedMoviesYear();

        FetchData fetchData= new FetchData(RatingsFromIMDb.this, checkedMovieNamesYears);
        fetchData.execute();

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