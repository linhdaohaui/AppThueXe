package com.example.semester;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.semester.Database.DatabaseHelper;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class HomePage extends AppCompatActivity {
    TableRow trNapDiem, trLichSuGiaoDich, trTramXe, trLoaiXe, trChuyenDiCuaToi, trBaoCaoSuCo;
    LinearLayout llDangXuat, llQuetQR, llTrangChu;
    TextView tvUser, tvTaiKhoanChinh;
    ImageView imgThongTinCaNhan;
    DatabaseHelper databaseHelper;
    int maKH = 0;
    String[] listTramXuatPhat, listKinhDo, listViDo;
    String kinhdo = "", vido = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        databaseHelper = new DatabaseHelper(this);
        getWidget();

        String phoneNumber = "SDT";
        SharedPreferences preferences = getSharedPreferences(phoneNumber, MODE_PRIVATE);
        String sdt = preferences.getString("PhoneNumber", "");
        maKH = databaseHelper.getMaKH(sdt);

        tvTaiKhoanChinh.setText(databaseHelper.getMoney(maKH) + "");
        tvUser.setText("Xin chào, " + databaseHelper.getTenKH(sdt));

        imgThongTinCaNhan.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, PersonalInfor.class);
            startActivity(intent);
        });

        trNapDiem.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, Wallet.class);
            startActivity(intent);
        });

        trLichSuGiaoDich.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, Transaction_History.class);
            startActivity(intent);
        });

        trTramXe.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, TrafficStation.class);
            startActivity(intent);
        });

        trLoaiXe.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, Traffic.class);
            startActivity(intent);
        });

        trChuyenDiCuaToi.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, Trips.class);
            startActivity(intent);
        });

        trBaoCaoSuCo.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, Report.class);
            startActivity(intent);
        });

        llQuetQR.setOnClickListener(v -> scanCode());

        llDangXuat.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

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
            AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
            builder.setTitle("Chuyến đi của bạn");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("Bắt đầu chuyến đi", (dialogInterface, i) -> {
                Intent intent = new Intent(HomePage.this, ScanQR.class);
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

    private void getWidget() {
        trNapDiem = findViewById(R.id.trNapDiem);
        trLichSuGiaoDich = findViewById(R.id.trLichSuGiaoDich);
        trTramXe = findViewById(R.id.trTramXe);
        trLoaiXe = findViewById(R.id.trLoaiXe);
        trChuyenDiCuaToi = findViewById(R.id.trChuyenDiCuaToi);
        trBaoCaoSuCo = findViewById(R.id.trBaoCaoSuCo);
        llDangXuat = findViewById(R.id.llDangXuat);
        llQuetQR = findViewById(R.id.llQuetQR);
        llTrangChu = findViewById(R.id.llTrangChu);
        tvUser = findViewById(R.id.tvUser);
        tvTaiKhoanChinh = findViewById(R.id.tvTaiKhoanChinh);
        imgThongTinCaNhan = findViewById(R.id.imgThongTinCaNhan);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        tvTaiKhoanChinh.setText(databaseHelper.getMoney(maKH) + "");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onRestart() {
        super.onRestart();
        tvTaiKhoanChinh.setText(databaseHelper.getMoney(maKH) + "");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onPause() {
        super.onPause();
        tvTaiKhoanChinh.setText(databaseHelper.getMoney(maKH) + "");
    }
}