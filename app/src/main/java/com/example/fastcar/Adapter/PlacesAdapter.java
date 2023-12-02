package com.example.fastcar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fastcar.Model.Places.Place;
import com.example.fastcar.R;

import java.util.ArrayList;
import java.util.List;

public class PlacesAdapter extends BaseAdapter {
    List<Place.Predictions> predictionsList = new ArrayList<>();
    Context context;

    public PlacesAdapter(Context context) {
        this.context = context;
    }

    public void setPredictions(List<Place.Predictions> predictions) {
        this.predictionsList.clear();
        this.predictionsList.addAll(predictions);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return predictionsList.size();
    }

    @Override
    public Object getItem(int i) {
        return predictionsList.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View v = LayoutInflater.from(context).inflate(R.layout.layout_item1_diadiem, viewGroup, false);
        TextView txtShortAddress = v.findViewById(R.id.txtShortAddress);
        TextView txtLongAddress = v.findViewById(R.id.txtLongAddress);

        txtShortAddress.setText(predictionsList.get(i).getStructured_formatting().getMain_text());
        txtLongAddress.setText(predictionsList.get(i).getStructured_formatting().getSecondary_text());
        return v;
    }
}
