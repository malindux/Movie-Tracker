package com.example.assignment02.ratingsFromImdb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.assignment02.R;

import java.util.ArrayList;

public class CustomListViewAdapterShowRatings extends ArrayAdapter<String> {
    private ArrayList<String> names;
    private ArrayList<String> ratings;
    private ArrayList<byte[]> movieCoverBitmaps;

    public CustomListViewAdapterShowRatings(Context context, int resource, ArrayList<String> names, ArrayList<String> ratings, ArrayList<byte[]> movieCoverBitmaps) {
        super(context, resource, names);
        this.names = new ArrayList<String>();
        this.names.addAll(names);
        this.ratings = new ArrayList<String>();
        this.ratings.addAll(ratings);
        this.movieCoverBitmaps = new ArrayList<byte[]>();
        this.movieCoverBitmaps.addAll(movieCoverBitmaps);
    }

    private class ViewHolder {
        TextView name;
        TextView rating;
        ImageView cover;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.activity_show_ratings_listview_row_layout, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.rating = (TextView) convertView.findViewById(R.id.rating);
            holder.cover = (ImageView) convertView.findViewById(R.id.cover);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String name = names.get(position);
        String rating = ratings.get(position);
        Bitmap movieCoverImg = BitmapFactory.decodeByteArray(movieCoverBitmaps.get(position), 0, movieCoverBitmaps.get(position).length);

        holder.name.setText(name);
        holder.rating.setText("Rating - " + rating);
        holder.cover.setImageBitmap(movieCoverImg);

        return convertView;

    }

}
