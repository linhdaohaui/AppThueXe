package com.example.semester;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TransactionHistoryAdapter extends BaseAdapter {
    Activity context;
    int idLayout;
    ArrayList<TransactionHistoryObj> myList;

    public TransactionHistoryAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<TransactionHistoryObj> objects) {
        this.context = context;
        idLayout = resource;
        this.myList = objects;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myFlater = context.getLayoutInflater();
        convertView = myFlater.inflate(idLayout, null);
        TransactionHistoryObj transactionHistoryObj = myList.get(position);

        TextView tvTenNganHang = convertView.findViewById(R.id.tvTenNganHang);
        tvTenNganHang.setText(transactionHistoryObj.getTenNganHang());

        TextView tvTaiKhoanChinh = convertView.findViewById(R.id.tvTaiKhoanChinh);
        tvTaiKhoanChinh.setText(transactionHistoryObj.getSoDiemDaNop() + "");

        TextView tvTrangThai = convertView.findViewById(R.id.tvTrangThai);
        tvTrangThai.setText("Giao dịch thành công");

        TextView tvThoiGian = convertView.findViewById(R.id.tvThoiGianGiaoDich);
        tvThoiGian.setText(transactionHistoryObj.getThoiGian());

        TextView tvTongDiem = convertView.findViewById(R.id.tvTongDiem);
        tvTongDiem.setText(transactionHistoryObj.getTongSoDiem() + "");

        return convertView;
    }
}
