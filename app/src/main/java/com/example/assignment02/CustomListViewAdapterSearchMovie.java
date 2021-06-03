package com.example.assignment02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomListViewAdapterSearchMovie extends ArrayAdapter<String> {
    private ArrayList<String> name;
    private ArrayList<List<String>> directors;
    private ArrayList<List<String>> castMembers;

    public CustomListViewAdapterSearchMovie(Context context, int resource, ArrayList<String> name, ArrayList<List<String>> directors, ArrayList<List<String>> cast) {
        super(context, resource, name);
        this.name = name;
        this.directors = directors;
        this.castMembers = cast;
    }

    private class ViewHolder {
        TextView name;
        TextView dir;
        TextView cast;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.activity_search_movie_listview_layout, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.dir = (TextView) convertView.findViewById(R.id.directors);
            holder.cast = (TextView) convertView.findViewById(R.id.cast);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(name.get(position));
        holder.dir.setText("Directors : " + directors.get(position).toString().replace("[","").replace("]",""));
        holder.cast.setText("Cast : " + castMembers.get(position).toString().replace("[","").replace("]",""));

        return convertView;

    }

}
