package com.example.assignment02.ratingsFromImdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.assignment02.R;

import java.util.ArrayList;

public class ShowImage extends AppCompatActivity {

    private String movieName;
    private static byte[] movieCoverBitmaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        Intent intent = getIntent();
        movieName = intent.getStringExtra("movieName");

        Bitmap movieCover = BitmapFactory.decodeByteArray(movieCoverBitmaps, 0, movieCoverBitmaps.length);

        TextView name = findViewById(R.id.name);
        ImageView cover = findViewById(R.id.cover);

        name.setText(movieName);
        cover.setImageBitmap(movieCover);

    }

    public static void setMovieCoverBitmaps(byte[] movieCoverBitmaps) {
        ShowImage.movieCoverBitmaps = movieCoverBitmaps;
    }

}