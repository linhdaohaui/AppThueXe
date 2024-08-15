package com.example.semester;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.semester.Database.DatabaseHelper;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ScanQR extends AppCompatActivity {
    LinearLayout llDangXuat, llQuetQR, llTrangChu;
    Button btnBatDau, btnNapDiem;
    TextView tvTaiKhoanChinh, tvTenTramXe, tvDiaChiTramXe, tvLoaiXe, tvBienSoXe;
    DatabaseHelper databaseHelper;
    String[] listTramXuatPhat, listKinhDo, listViDo;
    String kinhdo = "", vido = "";
    int maKH = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        getWidget();

        databaseHelper = new DatabaseHelper(this);

        String phoneNumber = "SDT";
        SharedPreferences preferences = getSharedPreferences(phoneNumber, MODE_PRIVATE);
        String sdt = preferences.getString("PhoneNumber", "");
        maKH = databaseHelper.getMaKH(sdt);

        String tramxuatphat = databaseHelper.getTramXuatPhat(maKH);
        String diachi = databaseHelper.getDiaChi(maKH);
        String loaixe = databaseHelper.getLoaiXe(maKH);
        String biensoxe = databaseHelper.getBienSoXe(maKH);

        tvTenTramXe.setText(tramxuatphat);
        tvDiaChiTramXe.setText(diachi);
        tvLoaiXe.setText(loaixe);
        tvBienSoXe.setText(biensoxe);

        tvTaiKhoanChinh.setText(databaseHelper.getMoney(maKH) + "");

        listTramXuatPhat = getResources().getStringArray(R.array.listStationName);
        listKinhDo = getResources().getStringArray(R.array.longitude);
        listViDo = getResources().getStringArray(R.array.latitude);
        for (int i = 0; i < listTramXuatPhat.length; i++) {
            if (tramxuatphat.contains(listTramXuatPhat[i])) {
                kinhdo = listKinhDo[i];
                vido = listViDo[i];
                break;
            }
        }

        btnBatDau.setOnClickListener(v -> {
            if (Double.parseDouble(tvTaiKhoanChinh.getText().toString()) >= 20000) {
                Intent intent = new Intent(ScanQR.this, Journey.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ScanQR.this, "Bạn không đủ điểm để khởi tạo chuyến đi !",
                        Toast.LENGTH_SHORT).show();
                Toast.makeText(ScanQR.this, "Vui lòng nạp thêm điểm !",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnNapDiem.setOnClickListener(v -> {
            Intent intent = new Intent(ScanQR.this, Wallet.class);
            startActivity(intent);
        });

        llTrangChu.setOnClickListener(v -> finish());

        llQuetQR.setOnClickListener(v -> {
            scanCode();
            finish();
        });

        llDangXuat.setOnClickListener(v -> {
            Intent intent = new Intent(ScanQR.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void getWidget() {
        btnBatDau = findViewById(R.id.btnBatDau);
        btnNapDiem = findViewById(R.id.btnNapDiem);
        llDangXuat = findViewById(R.id.llDangXuat);
        llQuetQR = findViewById(R.id.llQuetQR);
        llTrangChu = findViewById(R.id.llTrangChu);
        tvTaiKhoanChinh = findViewById(R.id.tvTaiKhoanChinh);
        tvTenTramXe = findViewById(R.id.tvTenTramXe);
        tvDiaChiTramXe = findViewById(R.id.tvDiaChiTramXe);
        tvLoaiXe = findViewById(R.id.tvLoaiXe);
        tvBienSoXe = findViewById(R.id.tvBienSoXe);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ScanQR.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String[] tmp = result.getContents().split("\n");
                    String tramxuatphat = tmp[0];
                    String diachi = tmp[1];
                    String loaixe = tmp[2];
                    String biensoxe = tmp[3];
                    double kd = Double.parseDouble(kinhdo);
                    double vd = Double.parseDouble(vido);
                    Boolean insert = databaseHelper.insetJourney(tramxuatphat, diachi, loaixe,
                            biensoxe, kd, vd, 0, 0, maKH);
                }
            }).show();
        }
    });
}
