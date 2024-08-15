package com.example.semester;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.semester.Database.DatabaseHelper;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Calendar;

public class EndJourney extends AppCompatActivity {
    LinearLayout llDangXuat, llQuetQR, llTrangChu;
    Button btnTrangChu;
    TextView tvTaiKhoanChinh, tvTramXuatPhat, tvDiaChi, tvLoaiXe, tvBienSoXe, tvThoiGianDi, tvTongTien;
    DatabaseHelper databaseHelper;
    String[] listTramXuatPhat, listKinhDo, listViDo;
    String kinhdo = "", vido = "";
    int maKH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_journey);
        getWidget();

        databaseHelper = new DatabaseHelper(this);

        String phoneNumber = "SDT";
        SharedPreferences preferences = getSharedPreferences(phoneNumber, MODE_PRIVATE);
        String sdt = preferences.getString("PhoneNumber", "");
        maKH = databaseHelper.getMaKH(sdt);

        double tien = databaseHelper.getTongTien(maKH);
        tvTramXuatPhat.setText(databaseHelper.getTramXuatPhat(maKH));
        tvDiaChi.setText(databaseHelper.getDiaChi(maKH));
        tvLoaiXe.setText(databaseHelper.getLoaiXe(maKH));
        tvBienSoXe.setText(databaseHelper.getBienSoXe(maKH));
        tvThoiGianDi.setText(databaseHelper.getTongThoiGian(maKH) + "");
        tvTongTien.setText(tien + "");

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // Use HOUR_OF_DAY for 24-hour format
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Month starts from 0
        int year = calendar.get(Calendar.YEAR);

        String trangThai = "Thanh toán thành công";
        String thoiGian = String.format("%02d:%02d:%02d - %02d/%02d/%04d", hour, minute, second,
                dayOfMonth, month, year);
        double sotien = Math.floor(-1 * databaseHelper.getTongTien(maKH));
        double tongTien = Math.floor(databaseHelper.getMoney(maKH) - tien);

        TransactionHistoryObj transactionHistoryObj = new TransactionHistoryObj("TNGo",
                sotien, trangThai, thoiGian, tongTien, maKH);
        Boolean insert = databaseHelper.insetTransactionHistory(transactionHistoryObj);

        tvTaiKhoanChinh.setText(databaseHelper.getMoney(maKH) + "");

        btnTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llQuetQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
                finish();
            }
        });

        llDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndJourney.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getWidget() {
        btnTrangChu = findViewById(R.id.btnTrangChu);
        llDangXuat = findViewById(R.id.llDangXuat);
        llQuetQR = findViewById(R.id.llQuetQR);
        llTrangChu = findViewById(R.id.llTrangChu);
        tvTaiKhoanChinh = findViewById(R.id.tvTaiKhoanChinh);
        tvTramXuatPhat = findViewById(R.id.tvTramXuatPhat);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        tvLoaiXe = findViewById(R.id.tvLoaiXe);
        tvBienSoXe = findViewById(R.id.tvBienSoXe);
        tvThoiGianDi = findViewById(R.id.tvThoiGianDi);
        tvTongTien = findViewById(R.id.tvTongTien);
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result ->
    {
        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EndJourney.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(EndJourney.this, ScanQR.class);
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
                    Boolean insert = databaseHelper.insetJourney(tramxuatphat, diachi, loaixe,
                            biensoxe, kd, vd, 0, 0, maKH);
                    startActivity(intent);
                }
            }).show();
        }
    });
}

