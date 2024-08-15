package com.example.semester;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TrafficAdapter extends ArrayAdapter<TrafficObj> {

    Activity context;
    int idLayout;
    ArrayList<TrafficObj> myList;

    public TrafficAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<TrafficObj> objects) {
        super(context, resource, objects);
        this.context = context;
        idLayout = resource;
        this.myList = objects;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myFlater = context.getLayoutInflater();
        convertView = myFlater.inflate(idLayout, null);
        TrafficObj trafficObj = myList.get(position);
        ImageView img_trafficObj = convertView.findViewById(R.id.ivTrafficImg);
        img_trafficObj.setImageResource(trafficObj.getImage());
        TextView tvLoaiXe = convertView.findViewById(R.id.tvLoaiXe);
        tvLoaiXe.setText(trafficObj.getLoaiXe());
        TextView tvBienSoXe = convertView.findViewById(R.id.tvBienSoXe);
        tvBienSoXe.setText(trafficObj.getBienSoXe());
        return convertView;
    }
}
