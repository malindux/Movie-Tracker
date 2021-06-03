package com.example.assignment02.ratingsFromImdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.assignment02.R;

import java.util.ArrayList;

public class CustomListViewAdapterRatingFromImdb extends ArrayAdapter<String> {
    private ArrayList<String> names;

    private ArrayList<String> selectedMoviesYear = new ArrayList<>();
    private ArrayList<String> selectedMovies = new ArrayList<>();
    private ArrayList<String> years;

    private CheckBox selected = null;

    public CustomListViewAdapterRatingFromImdb(Context context, int resource, ArrayList<String> names, ArrayList<String> years) {
        super(context, resource, names);
        this.names = new ArrayList<>();
        this.names.addAll(names);
        this.years = new ArrayList<>();
        this.years.addAll(years);
    }

    private class ViewHolder {
        TextView name;
        CheckBox checkBox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.listview_row_layout, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);

            final ViewHolder finalHolder = holder;
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CheckBox checkedCheckBox = (CheckBox) buttonView;
                    if (isChecked){
                        if (selected != null){
                            selected.setChecked(false);
                        }
                        finalHolder.checkBox.setChecked(true);
                        selected = finalHolder.checkBox;
                        selectedMoviesYear.add(checkedCheckBox.getTag().toString() + " " + years.get(position));
                        selectedMovies.add(checkedCheckBox.getTag().toString());
                    }else {
                        selectedMoviesYear.remove(checkedCheckBox.getTag().toString() + " " + years.get(position));
                        selectedMovies.remove(checkedCheckBox.getTag().toString());
                    }
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String name = names.get(position);
        holder.name.setText("  " + (position+1) + ".  " + name);
        holder.checkBox.setTag(name);
        holder.checkBox.setText("");

        return convertView;

    }

    public ArrayList<String> getSelectedMoviesYear() {
        return selectedMoviesYear;
    }

}
