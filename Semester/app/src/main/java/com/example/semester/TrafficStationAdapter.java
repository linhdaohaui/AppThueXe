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

public class TrafficStationAdapter extends ArrayAdapter<TrafficStationObj> {
    Activity context;
    int idLayout;
    ArrayList<TrafficStationObj> myList;

    public TrafficStationAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<TrafficStationObj> objects) {
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

        TrafficStationObj trafficObj = myList.get(position);
        ImageView img_trafficObj = convertView.findViewById(R.id.ivTrafficStaImg);
        img_trafficObj.setImageResource(trafficObj.getImage());

        TextView tvLoaiXe = convertView.findViewById(R.id.tvTramXe);
        tvLoaiXe.setText(trafficObj.getTenTram());

        TextView tvBienSoXe = convertView.findViewById(R.id.tvDiaChiTramXe);
        tvBienSoXe.setText(trafficObj.getDiaChi());

        return convertView;
    }
}
