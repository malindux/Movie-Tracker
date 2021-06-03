package com.example.assignment02.editMovie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment02.R;
import com.example.assignment02.databaseHelperClasses.EventsData;

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

public class UpdateMovie extends AppCompatActivity {

    private static String[] FROM = {_ID, TITLE, YEAR, DIRECTOR, CAST, RATING, REVIEW, FAVOURITES};
    private static String ORDER_BY = TITLE + " DESC";
    private List<String> movieData = new ArrayList<>();
    private EventsData events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_movie);

        events = new EventsData(this);

        // Getting movie data from the edit movies activity
        Intent intent = getIntent();
        movieData = (List<String>) intent.getSerializableExtra("movieData");

        TextView title = findViewById(R.id.titleResult);
        final CheckBox fav = findViewById(R.id.fav);
        EditText titleInput = findViewById(R.id.title);
        EditText yearInput = findViewById(R.id.year);
        EditText directorInput = findViewById(R.id.dir);
        EditText castInput = findViewById(R.id.cast);
        EditText reviewInput = findViewById(R.id.searchInput);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        /*
         * Setting all the current details of movie
         * At the beginning of the activity user will be able to see
         * all the current details of the movie
         */
        title.setText(movieData.get(1) + " - " + movieData.get(2));
        titleInput.setText(movieData.get(1));
        yearInput.setText(movieData.get(2));
        directorInput.setText(movieData.get(3));
        castInput.setText(movieData.get(4));
        reviewInput.setText(movieData.get(6));
        ratingBar.setRating(Integer.parseInt(movieData.get(5)));

        if (movieData.get(7).equals("true")) {
            fav.setChecked(true);
        }else {
            fav.setText("Not Favourite");
        }

        fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    fav.setText("Favourite");
                }else {
                    fav.setText("Not Favourite");
                }
            }
        });

    }

    public void displayToast(View view, String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Data base will be updated with all the updated details which user inputs for the movie
     * User can change any detail of the movie
     */
    public void updateMovie(View view) {

        boolean validated = true;

        CheckBox fav = findViewById(R.id.fav);
        EditText titleInput = findViewById(R.id.title);
        EditText yearInput = findViewById(R.id.year);
        EditText directorInput = findViewById(R.id.dir);
        EditText castInput = findViewById(R.id.cast);
        EditText reviewInput = findViewById(R.id.searchInput);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        // Validating inputs
        if(titleInput.getText().toString().equals("")) {
            titleInput.setError("This field can not be empty");
            validated = false;
        }
        if (yearInput.getText().toString().equals("")){
            yearInput.setError("This field can not be empty");
            validated = false;
        }
        if (!yearInput.getText().toString().equals("") && Integer.parseInt(yearInput.getText().toString()) <= 1895){
            yearInput.setError("Year should be after 1895");
            validated = false;
        }
        if (directorInput.getText().toString().equals("")){
            directorInput.setError("This field can not be empty");
            validated = false;
        }
        if (castInput.getText().toString().equals("")){
            castInput.setError("This field can not be empty");
            validated = false;
        }
        if (reviewInput.getText().toString().equals("")){
            reviewInput.setError("This field can not be empty");
            validated = false;
        }
        if (ratingBar.getRating() > 10 || ratingBar.getRating() < 1) {
            displayToast(view, "Rating should be between 1 and 10");
            validated = false;
        }

        if (validated) {

            SQLiteDatabase db = events.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(TITLE, titleInput.getText().toString());
            values.put(YEAR, yearInput.getText().toString());
            values.put(DIRECTOR, directorInput.getText().toString());
            values.put(CAST, castInput.getText().toString());
            values.put(RATING, ratingBar.getRating());
            values.put(REVIEW, reviewInput.getText().toString());
            if (fav.isChecked()) {
                values.put(FAVOURITES, "true");
            } else values.put(FAVOURITES, "false");

            db.update(TABLE_NAME, values, "_id = ?", new String[]{movieData.get(0)});

            displayToast(view, "Movie Updated");

        }

    }

    public void deleteMovie(View view) {
        SQLiteDatabase db = events.getWritableDatabase();
        db.delete(TABLE_NAME, "_id = ?" , new String[] {movieData.get(0)});
        displayToast(view, "Movie Deleted");
    }

    /**
     * Action for the back button
     * After deleting a movie when user clicks on back button movie list will be updated
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Cursor cursor = getEvents();
        ArrayList<String> movieNames = new ArrayList<>(); // This array list will store all movie titles.
        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            movieNames.add(title);
        }
        cursor.close();
        // Sorting movie titles to display
        Collections.sort(movieNames);
        EditMovies.updateList(movieNames);
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