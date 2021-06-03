package com.example.assignment02;

import androidx.appcompat.app.AppCompatActivity;

import static android.provider.BaseColumns._ID;
import static com.example.assignment02.databaseHelperClasses.Constants.FAVOURITES;
import static com.example.assignment02.databaseHelperClasses.Constants.TABLE_NAME;
import static com.example.assignment02.databaseHelperClasses.Constants.TITLE;
import static com.example.assignment02.databaseHelperClasses.Constants.YEAR;
import static com.example.assignment02.databaseHelperClasses.Constants.DIRECTOR;
import static com.example.assignment02.databaseHelperClasses.Constants.CAST;
import static com.example.assignment02.databaseHelperClasses.Constants.RATING;
import static com.example.assignment02.databaseHelperClasses.Constants.REVIEW;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assignment02.databaseHelperClasses.EventsData;

public class RegisterMovie extends AppCompatActivity {

    private static String[] FROM = { _ID, TITLE, YEAR, DIRECTOR, CAST, RATING, REVIEW, FAVOURITES};
    private static String ORDER_BY = TITLE + " DESC";
    private EventsData events;
    private boolean dataExisting = false; // To check if entered data already exists in the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);
        events = new EventsData(this);
    }

    public void displayToast(View view, String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Adding new movie record to the database from user inputs
     * Validating user inputs (year and rating)
     */
    public void addRecord(View view) {

        boolean validated = true;

        Cursor cursor = getEvents();

        EditText titleInput = findViewById(R.id.title);
        EditText yearInput = findViewById(R.id.year);
        EditText directorInput = findViewById(R.id.dir);
        EditText castInput = findViewById(R.id.cast);
        EditText ratingInput = findViewById(R.id.rating);
        EditText reviewInput = findViewById(R.id.searchInput);

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
        if (ratingInput.getText().toString().equals("")){
            ratingInput.setError("This field can not be empty");
            validated = false;
        }
        if (!ratingInput.getText().toString().equals("") && (Integer.parseInt(ratingInput.getText().toString()) > 10 || Integer.parseInt(ratingInput.getText().toString()) < 1)) {
            ratingInput.setError("Rating should be between 1 and 10");
            validated = false;
        }
        if (reviewInput.getText().toString().equals("")){
            reviewInput.setError("This field can not be empty");
            validated = false;
        }

        if (validated) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(1);
                String year = cursor.getString(2);
                if (titleInput.getText().toString().equals(title) && yearInput.getText().toString().equals(year)) {
                    dataExisting = true;
                }
            }
            cursor.close();

            if (!dataExisting) {
                addEvent(titleInput.getText().toString(),
                        yearInput.getText().toString(),
                        directorInput.getText().toString(),
                        castInput.getText().toString(),
                        Integer.parseInt(ratingInput.getText().toString()),
                        reviewInput.getText().toString());

                displayToast(view, "Your film has been successfully saved");

            } else displayToast(view, "Movie already existing in the database");
        }

    }

    // Insert a new record into the Events data source
    private void addEvent(String title, String year, String dir, String cast, Integer rating, String review) {
        SQLiteDatabase db = events.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, title);
        values.put(YEAR, year);
        values.put(DIRECTOR, dir);
        values.put(CAST, cast);
        values.put(RATING, rating);
        values.put(REVIEW, review);
        values.put(FAVOURITES,"false");
        db.insertOrThrow(TABLE_NAME, null, values);
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