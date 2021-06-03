package com.example.assignment02.ratingsFromImdb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FetchData extends AsyncTask<Void,Void,Void> {

    private Context context;

    private final String API_KEY = "k_66y9zak6";

    private ArrayList<String> findList; // Holds the movie data which selects to find the IMDb ratings
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> idList = new ArrayList<>(); // Stores all the IMDb id for each movie
    private ArrayList<String> ratings = new ArrayList<>(); // Stores IMDb ratings for movies
    private ArrayList<byte[]> movieCoverBitmaps = new ArrayList<>(); // Stores all the movie covers which retrieves from the server

    public FetchData(Context context, ArrayList<String> findList) {
        this.context = context;
        this.findList = findList;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            for (String movie : findList) {
                String baseURL = "https://imdb-api.com/en/API/SearchTitle/" + API_KEY + "/" + movie;

                Uri builtURI = Uri.parse(baseURL);
                URL requestURL = new URL(builtURI.toString());
                HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();

                InputStream inputStream = conn.getInputStream();

                StringBuilder data = new StringBuilder();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data.append(line);
                }

                JSONObject returnedData = new JSONObject(data.toString());
                JSONArray results = returnedData.getJSONArray("results");
                JSONObject resultData;
                String imgUrlString;
                ArrayList<String> coverImgUrls = new ArrayList<>();

                for (int i = 0; i<results.length(); i++){
                    resultData = results.getJSONObject(i);
                    String id = resultData.getString("id");
                    String title = resultData.getString("title");
                    imgUrlString = resultData.getString("image");
                    titleList.add(title);
                    idList.add(id);
                    coverImgUrls.add(imgUrlString);
                }

                for (String url : coverImgUrls) {
                    URL imgUrl = new URL(url);
                    HttpURLConnection conn1 = (HttpURLConnection) imgUrl.openConnection();
                    InputStream is = conn1.getInputStream();
                    Bitmap bitmapImg = BitmapFactory.decodeStream(is);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmapImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    movieCoverBitmaps.add(byteArray);
                }

            }

            for (String movieId : idList){
                String baseURLID = "https://imdb-api.com/en/API/UserRatings/" + API_KEY + "/" + movieId;
                Uri builtURIID = Uri.parse(baseURLID);
                URL requestURLID = new URL(builtURIID.toString());
                HttpURLConnection conn = (HttpURLConnection) requestURLID.openConnection();
                InputStream inputStream = conn.getInputStream();

                String ratingData = "";

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    ratingData = ratingData + line;
                }

                JSONObject returnedRatingData = new JSONObject(ratingData);
                String rating = returnedRatingData.getString("totalRating");

                ratings.add(rating);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        ShowRatings.setMatchedTitles(this.titleList);
        ShowRatings.setRatings(this.ratings);
        ShowRatings.setMovieCoverBitmaps(this.movieCoverBitmaps);

        TextView downloading = RatingsFromIMDb.downloading;
        ProgressBar progressBar = RatingsFromIMDb.progressBar;

        downloading.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(context, ShowRatings.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

}
