package com.example.assignment02.ratingsFromImdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.assignment02.R;

import java.util.ArrayList;

public class ShowRatings extends AppCompatActivity {

    public static ArrayList<String> matchedTitles ; // Stores all the IMDb ratings
    public static ArrayList<String> ratings ; // Stores all the IMDb ratings
    public static ArrayList<byte[]> movieCoverBitmaps; // Stores all the movie covers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ratings);

        ListView listView = findViewById (R.id.listView);

        CustomListViewAdapterShowRatings customListViewAdapter = new CustomListViewAdapterShowRatings(this,
                R.layout.listview_row_layout,
                matchedTitles,
                ratings,
                movieCoverBitmaps);
        listView.setAdapter(customListViewAdapter);

        /*
        * Setting action for the movie item click on the list view
        * When user clicks on an item, user will be redirected to another window showing movie cover
        */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movieName = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(ShowRatings.this, ShowImage.class);
                intent.putExtra("movieName", movieName);
                ShowImage.setMovieCoverBitmaps(movieCoverBitmaps.get(position));
                startActivity(intent);
            }
        });

    }

    public static void setRatings(ArrayList<String> ratings) {
        ShowRatings.ratings = ratings;
    }

    public static void setMovieCoverBitmaps(ArrayList<byte[]> movieCoverBitmaps) {
        ShowRatings.movieCoverBitmaps = movieCoverBitmaps;
    }

    public static void setMatchedTitles(ArrayList<String> matchedTitles) {
        ShowRatings.matchedTitles = matchedTitles;
    }
}