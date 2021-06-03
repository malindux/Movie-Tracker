package com.example.assignment02.editMovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.assignment02.R;

import java.util.ArrayList;

public class CustomListViewAdapterEditMovies extends ArrayAdapter<String> {
    private ArrayList<String> names;

    public CustomListViewAdapterEditMovies(Context context, int resource, ArrayList<String> names) {
        super(context, resource, names);
        this.names = new ArrayList<String>();
        this.names.addAll(names);
    }

    private class ViewHolder {
        TextView name;

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
            convertView = vi.inflate(R.layout.activity_edit_movies_listview_row_layout, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String name = names.get(position);
        holder.name.setText(position+1 + ".  " +  name);

        return convertView;

    }

}
