package com.example.assignment02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListViewAdapter extends ArrayAdapter<String> {
    private ArrayList<String> names;
    private ArrayList<String> favourites;

    private ArrayList<String> selectedFavourites = new ArrayList<>();

    public CustomListViewAdapter(Context context, int resource, ArrayList<String> names, ArrayList<String> favourites) {
        super(context, resource, names);
        this.names = new ArrayList<String>();
        this.names.addAll(names);
        this.favourites = new ArrayList<String>();
        this.favourites.addAll(favourites);
    }

    private class ViewHolder {
        TextView name;
        CheckBox checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.listview_row_layout, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CheckBox checkedCheckBox = (CheckBox) buttonView;
                    if (isChecked){
                        selectedFavourites.add(checkedCheckBox.getTag().toString());
                    }else selectedFavourites.remove(checkedCheckBox.getTag().toString());
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String name = names.get(position);
        holder.name.setText(name);
        holder.checkBox.setTag(name);

        for (String favourite : favourites) {
            if (holder.checkBox.getTag().equals(favourite)) {
                holder.checkBox.setChecked(true);
            }
        }

        return convertView;

    }

    public ArrayList<String> getSelectedFavourites() {
        return selectedFavourites;
    }

}
