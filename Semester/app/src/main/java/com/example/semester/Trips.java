package com.example.semester;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.semester.Database.DatabaseHelper;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class Trips extends AppCompatActivity {
    Button btnQuetQR, btnKhoiTao, btnNapDiem;
    LinearLayout llDangXuat, llQuetQR, llTrangChu;
    Spinner spTramXuatPhat, spLoaiXe, spBienSoXe;
    TextView tvTaiKhoanChinh, tvDiaChi;
    ArrayAdapter<String> adapterTramXuatPhat = null;
    String[] listTramXuatPhat, listDiaChi, listKinhDo, listViDo, listLoaiXe, listBienSoXe;
    ArrayAdapter<String> adapterLoaiXe = null;
    ArrayAdapter<String> adapterBienSoXe = null;
    String tramxuatphat = "", loaixe = "", biensoxe = "", diachi = "", kinhdo = "", vido = "";
    DatabaseHelper databaseHelper;
    int maKH = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        getWidget();

        databaseHelper = new DatabaseHelper(this);
        String phoneNumber = "SDT";
        SharedPreferences preferences = getSharedPreferences(phoneNumber, MODE_PRIVATE);
        String sdt = preferences.getString("PhoneNumber", "");
        maKH = databaseHelper.getMaKH(sdt);
        tvTaiKhoanChinh.setText(databaseHelper.getMoney(maKH) + "");

        listDiaChi = getResources().getStringArray(R.array.listStationAddress);
        listTramXuatPhat = getResources().getStringArray(R.array.listStationName);
        listKinhDo = getResources().getStringArray(R.array.longitude);
        listViDo = getResources().getStringArray(R.array.latitude);
        listLoaiXe = getResources().getStringArray(R.array.listTraffic);
        listBienSoXe = getResources().getStringArray(R.array.listLicensePlate);

        adapterTramXuatPhat = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                listTramXuatPhat);
        adapterTramXuatPhat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTramXuatPhat.setAdapter(adapterTramXuatPhat);
        spTramXuatPhat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tramxuatphat = listTramXuatPhat[position];
                for (int i = 0; i < listTramXuatPhat.length; i++) {
                    if (tramxuatphat.contains(listTramXuatPhat[i])) {
                        diachi = listDiaChi[i];
                        tvDiaChi.setText(listDiaChi[i]);
                        kinhdo = listKinhDo[i];
                        vido = listViDo[i];
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        adapterBienSoXe = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                listBienSoXe);
        adapterBienSoXe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBienSoXe.setAdapter(adapterBienSoXe);

        spBienSoXe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                biensoxe = listBienSoXe[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        adapterLoaiXe = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                listLoaiXe);
        adapterLoaiXe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLoaiXe.setAdapter(adapterLoaiXe);
        spLoaiXe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loaixe = listLoaiXe[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnNapDiem.setOnClickListener(v -> {
            Intent intent = new Intent(Trips.this, Wallet.class);
            startActivity(intent);
        });

        btnKhoiTao.setOnClickListener(v -> {
            if (Double.parseDouble(tvTaiKhoanChinh.getText().toString()) >= 20000) {
                Intent intent = new Intent(Trips.this, Journey.class);
                double kd = Double.parseDouble(kinhdo);
                double vd = Double.parseDouble(vido);
                Boolean insert = databaseHelper.insetJourney(tramxuatphat, diachi, loaixe, biensoxe,
                        kd, vd, 0, 0, maKH);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Bạn không đủ điểm để khởi tạo chuyến đi !",
                        Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Vui lòng nạp thêm điểm !", Toast.LENGTH_SHORT).show();
            }
        });

        btnQuetQR.setOnClickListener(v -> scanCode());

        llTrangChu.setOnClickListener(v -> finish());

        llQuetQR.setOnClickListener(v -> scanCode());

        llDangXuat.setOnClickListener(v -> {
            Intent intent = new Intent(Trips.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void getWidget() {
        btnNapDiem = findViewById(R.id.btnNapDiem);
        btnKhoiTao = findViewById(R.id.btnKhoiTao);
        btnQuetQR = findViewById(R.id.btnQuetQR);
        llDangXuat = findViewById(R.id.llDangXuat);
        llQuetQR = findViewById(R.id.llQuetQR);
        llTrangChu = findViewById(R.id.llTrangChu);
        spTramXuatPhat = findViewById(R.id.spTramXuatPhat);
        spLoaiXe = findViewById(R.id.spLoaiXe);
        spBienSoXe = findViewById(R.id.spBienSoXe);
        tvTaiKhoanChinh = findViewById(R.id.tvTaiKhoanChinh);
        tvDiaChi = findViewById(R.id.tvDiaChi);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(Trips.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                Intent intent = new Intent(Trips.this, ScanQR.class);
                String[] tmp = result.getContents().split("\n");
                String tramxuatphat = tmp[0];
                String diachi = tmp[1];
                String loaixe = tmp[2];
                String biensoxe = tmp[3];
                double kd = Double.parseDouble(kinhdo);
                double vd = Double.parseDouble(vido);
                Boolean insert = databaseHelper.insetJourney(tramxuatphat, diachi, loaixe, biensoxe,
                        kd, vd, 0, 0, maKH);
                startActivity(intent);
            }).show();
        }
    });
}