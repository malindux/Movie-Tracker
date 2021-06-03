package com.example.assignment02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.assignment02.editMovie.EditMovies;
import com.example.assignment02.ratingsFromImdb.RatingsFromIMDb;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Action for the Register Movie button
     */
    public void regMovie(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterMovie.class);
        startActivity(intent);
    }

    /**
     * Action for the Display Movies button
     */
    public void displayMovie(View view) {
        Intent intent = new Intent(MainActivity.this, DisplayMovies.class);
        startActivity(intent);
    }

    /**
     * Action for the Favourites Movies button
     */
    public void favList(View view) {
        Intent intent = new Intent(MainActivity.this, FavouritesList.class);
        startActivity(intent);
    }

    /**
     * Action for the Edit Movies button
     */
    public void editMovies(View view) {
        Intent intent = new Intent(MainActivity.this, EditMovies.class);
        startActivity(intent);
    }

    /**
     * Action for the Search Movies button
     */
    public void searchMovie(View view) {
        Intent intent = new Intent(MainActivity.this, SearchMovie.class);
        startActivity(intent);
    }

    /**
     * Action for the Ratings From IMDB button
     */
    public void ratingsFromImdb(View view) {
        Intent intent = new Intent(MainActivity.this, RatingsFromIMDb.class);
        startActivity(intent);
    }

}