package com.example.semester;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.semester.Database.DatabaseHelper;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class Transaction_History extends AppCompatActivity {
    LinearLayout llDangXuat, llQuetQR, llTrangChu;
    ListView lvLichSuGiaoDich;
    String[] listTramXuatPhat, listKinhDo, listViDo;
    String kinhdo = "", vido = "";
    ArrayList<TransactionHistoryObj> transactionHistoryList = new ArrayList<>();
    TransactionHistoryAdapter transactionHistoryAdapter = null;
    DatabaseHelper databaseHelper;
    int maKH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        getWidget();

        databaseHelper = new DatabaseHelper(this);

        String phoneNumber = "SDT";
        SharedPreferences preferences = getSharedPreferences(phoneNumber, MODE_PRIVATE);
        String sdt = preferences.getString("PhoneNumber", "");

        maKH = databaseHelper.getMaKH(sdt);
        transactionHistoryList = databaseHelper.getTransactionHistory(maKH);
        transactionHistoryAdapter = new TransactionHistoryAdapter(Transaction_History.this,
                R.layout.transaction_history_layout, transactionHistoryList);
        lvLichSuGiaoDich.setAdapter(transactionHistoryAdapter);
        transactionHistoryAdapter.notifyDataSetChanged();

        llTrangChu.setOnClickListener(v -> finish());

        llQuetQR.setOnClickListener(v -> {
            scanCode();
            finish();
        });

        llDangXuat.setOnClickListener(v -> {
            Intent intent = new Intent(Transaction_History.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void getWidget() {
        lvLichSuGiaoDich = findViewById(R.id.lvLichSuGiaoDich);
        llDangXuat = findViewById(R.id.llDangXuat);
        llQuetQR = findViewById(R.id.llQuetQR);
        llTrangChu = findViewById(R.id.llTrangChu);
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(),
            result ->
    {
        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Transaction_History.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                Intent intent = new Intent(Transaction_History.this, ScanQR.class);
                String[] tmp = result.getContents().split("\n");
                String tramxuatphat = tmp[0];
                String diachi = tmp[1];
                String loaixe = tmp[2];
                String biensoxe = tmp[3];
                listTramXuatPhat = getResources().getStringArray(R.array.listStationName);
                listKinhDo = getResources().getStringArray(R.array.longitude);
                listViDo = getResources().getStringArray(R.array.latitude);
                for (int j = 0; j < listTramXuatPhat.length; j++) {
                    if (tramxuatphat.contains(listTramXuatPhat[j])) {
                        kinhdo = listKinhDo[j];
                        vido = listViDo[j];
                        break;
                    }
                }
                double kd = Double.parseDouble(kinhdo);
                double vd = Double.parseDouble(vido);
                Boolean insert = databaseHelper.insetJourney(tramxuatphat, diachi, loaixe, biensoxe,
                        kd, vd, 0, 0, maKH);
                startActivity(intent);
            }).show();
        }
    });
}