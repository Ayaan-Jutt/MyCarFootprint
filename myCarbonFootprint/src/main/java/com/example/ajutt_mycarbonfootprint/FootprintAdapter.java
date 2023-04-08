package com.example.ajutt_mycarbonfootprint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FootprintAdapter extends ArrayAdapter<Travel> {
    public FootprintAdapter(Context context, ArrayList<Travel> travels) { super(context, 0, travels); }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.activity_listview, parent, false);

        } else {
            view = convertView;
        }

        Travel travel = getItem(position);
        TextView TVFuel = view.findViewById(R.id.TVFuel);
        TextView TVFootprint = view.findViewById(R.id.TVFootprint);
        TextView TVStation = view.findViewById(R.id.TVstation);
        TVFuel.setText(travel.getFuelType());
        TVFootprint.setText(String.valueOf(travel.getFootprint()));
        TVStation.setText(travel.getName());
        return view;
    }
}
