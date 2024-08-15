package com.example.semester;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.Calendar;

public class Wallet extends AppCompatActivity {
    LinearLayout llDangXuat, llQuetQR, llTrangChu;
    Button btnNapDiem;
    Spinner spNganHang;
    EditText eSoTienNap, eTaiKhoan, eMatKhau;
    TextView tvTaiKhoanChinh;
    ArrayAdapter<String> adapterNganHang = null;
    String[] listNganHang, listTramXuatPhat, listKinhDo, listViDo;
    String nganHang = "", kinhdo = "", vido = "";
    DatabaseHelper databaseHelper;
    int maKH = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        getWidget();

        databaseHelper = new DatabaseHelper(this);

        String phoneNumber = "SDT";
        SharedPreferences preferences = getSharedPreferences(phoneNumber, MODE_PRIVATE);
        String sdt = preferences.getString("PhoneNumber", "");
        maKH = databaseHelper.getMaKH(sdt);

        tvTaiKhoanChinh.setText(databaseHelper.getMoney(maKH) + "");

        listNganHang = getResources().getStringArray(R.array.listBanking);
        adapterNganHang = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listNganHang);
        adapterNganHang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNganHang.setAdapter(adapterNganHang);
        spNganHang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nganHang = listNganHang[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnNapDiem.setOnClickListener(v -> {
            if (check()) {
                double soTien = Integer.parseInt(eSoTienNap.getText().toString());
                if (soTien >= 20000) {
                    if (eTaiKhoan.getText().toString().length() >= 10) {
                        if (eMatKhau.getText().toString().length() > 5) {
                            String trangThai = "Thanh toán thành công";
                            Calendar calendar = Calendar.getInstance();
                            int hour = calendar.get(Calendar.HOUR_OF_DAY); // Use HOUR_OF_DAY for 24-hour format
                            int minute = calendar.get(Calendar.MINUTE);
                            int second = calendar.get(Calendar.SECOND);
                            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                            int month = calendar.get(Calendar.MONTH) + 1; // Month starts from 0
                            int year = calendar.get(Calendar.YEAR);
                            @SuppressLint("DefaultLocale") String thoiGian = String.format
                                    ("%02d:%02d:%02d - %02d/%02d/%04d", hour, minute, second, dayOfMonth, month, year);
                            double tongTien = databaseHelper.getMoney(maKH) + soTien;
                            TransactionHistoryObj transactionHistoryObj = new
                                    TransactionHistoryObj(nganHang, soTien, trangThai, thoiGian, tongTien, maKH);
                            Boolean insert = databaseHelper.insetTransactionHistory(transactionHistoryObj);
                            if (insert) {
                                Toast.makeText(Wallet.this, "Nạp tiền thành công !", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Wallet.this, WalletEnd.class);
                                startActivity(intent);
                                finish();
                            } else
                                Toast.makeText(Wallet.this, "Nạp tiền không thành công !", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(this, "Mật khẩu không hợp lệ !", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Số tài khoản không hợp lệ !", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Số tiền bạn nộp phải lớn hơn 20.000 VNĐ !", Toast.LENGTH_SHORT).show();
            }
        });
        llTrangChu.setOnClickListener(v -> {
            Intent intent = new Intent(Wallet.this, HomePage.class);
            startActivity(intent);
        });

        llQuetQR.setOnClickListener(v -> scanCode());

        llDangXuat.setOnClickListener(v -> {
            Intent intent = new Intent(Wallet.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void getWidget() {
        btnNapDiem = findViewById(R.id.btnNapDiem);
        llDangXuat = findViewById(R.id.llDangXuat);
        llQuetQR = findViewById(R.id.llQuetQR);
        llTrangChu = findViewById(R.id.llTrangChu);
        spNganHang = findViewById(R.id.spNganHang);
        eSoTienNap = findViewById(R.id.eSoTienNap);
        eTaiKhoan = findViewById(R.id.eTaiKhoan);
        eMatKhau = findViewById(R.id.eMatKhau);
        tvTaiKhoanChinh = findViewById(R.id.tvTaiKhoanChinh);
    }

    private boolean check() {
        if (eSoTienNap.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số tiền nạp !", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (eTaiKhoan.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số tài khoản !", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (eMatKhau.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Wallet.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                Intent intent = new Intent(Wallet.this, ScanQR.class);
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
                Boolean insert = databaseHelper.insetJourney(tramxuatphat, diachi, loaixe, biensoxe, kd, vd, 0, 0, maKH);
                startActivity(intent);
            }).show();
        }
    });
}