package com.example.semester;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.semester.Database.DatabaseHelper;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ChangePassword extends AppCompatActivity {
    LinearLayout llDangXuat, llQuetQR, llTrangChu;
    EditText eMatKhauMoi, eMatKhauMoi1;
    Button btnDoiMatKhau;
    DatabaseHelper databaseHelper;
    String[] listTramXuatPhat, listKinhDo, listViDo;
    String kinhdo = "", vido = "";
    int maKH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        databaseHelper = new DatabaseHelper(this);

        getWidget();

        btnDoiMatKhau.setOnClickListener(v -> {
            String phoneNumber = "SDT";
            SharedPreferences preferences = getSharedPreferences(phoneNumber, MODE_PRIVATE);
            String sdt = preferences.getString("PhoneNumber", "");
            maKH = databaseHelper.getMaKH(sdt);

            String matkhaumoi = eMatKhauMoi.getText().toString();
            String matkhaumoi1 = eMatKhauMoi1.getText().toString();

            if (matkhaumoi.length() >= 6) {
                if (matkhaumoi.equals(matkhaumoi1)) {
                    Intent intent = new Intent(ChangePassword.this, HomePage.class);
                    Boolean update = databaseHelper.updateMatKhau(maKH, matkhaumoi);
                    Toast.makeText(this, "Đổi mật khẩu thành công !", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else
                    Toast.makeText(this, "Mật khẩu xác nhận không trùng khớp !", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Độ dài mật khẩu lớn hơn 6 ký tự !", Toast.LENGTH_SHORT).show();
        });

        llDangXuat.setOnClickListener(v -> {
            Intent intent = new Intent(ChangePassword.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        llQuetQR.setOnClickListener(v -> scanCode());

        llTrangChu.setOnClickListener(v -> {
            Intent intent = new Intent(ChangePassword.this, HomePage.class);
            startActivity(intent);
            finish();
        });
    }

    private void getWidget() {
        llDangXuat = findViewById(R.id.llDangXuat);
        llQuetQR = findViewById(R.id.llQuetQR);
        llTrangChu = findViewById(R.id.llTrangChu);
        eMatKhauMoi = findViewById(R.id.eMatKhauMoi);
        eMatKhauMoi1 = findViewById(R.id.eMatKhauMoi1);
        btnDoiMatKhau = findViewById(R.id.btnDoiMatKhau);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
            builder.setTitle("Chuyến đi của bạn");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("Bắt đầu chuyến đi", (dialogInterface, i) -> {
                Intent intent = new Intent(ChangePassword.this, ScanQR.class);
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