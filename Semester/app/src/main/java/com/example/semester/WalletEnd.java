package com.example.semester;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.semester.Database.DatabaseHelper;

public class WalletEnd extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_end);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        String phoneNumber = "SDT";
        SharedPreferences preferences = getSharedPreferences(phoneNumber, MODE_PRIVATE);
        String sdt = preferences.getString("PhoneNumber", "");

        TextView tvTieuDe = findViewById(R.id.tvTieuDe);
        double result_phoneNumber = databaseHelper.getBankingMoney(databaseHelper.getMaKH(sdt));
        tvTieuDe.setText("Bạn đã nạp " + result_phoneNumber + " điểm thành công !");

        TextView tvTaiKhoanChinh = findViewById(R.id.tvTaiKhoanChinh);
        tvTaiKhoanChinh.setText(databaseHelper.getMoney(databaseHelper.getMaKH(sdt)) + "");

        TextView tvVeTrangChu = findViewById(R.id.tvVeTrangChu);
        tvVeTrangChu.setOnClickListener(v -> finish());
    }
}