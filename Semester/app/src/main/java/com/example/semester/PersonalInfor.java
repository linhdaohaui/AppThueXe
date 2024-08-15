package com.example.semester;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.semester.Database.DatabaseHelper;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class PersonalInfor extends AppCompatActivity {
    LinearLayout llDangXuat, llQuetQR, llTrangChu;
    EditText eHoTen, eNgaySinh;
    RadioButton rbNam, rbNu, rbKhac;
    TextView tvSoDienThoai;
    Button btnDoiMatKhau, btnThongTinCaNhan;
    DatabaseHelper databaseHelper;
    String[] listTramXuatPhat, listKinhDo, listViDo;
    String kinhdo = "", vido = "";
    int maKH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_infor);
        getWidget();

        databaseHelper = new DatabaseHelper(this);
        String phoneNumber = "SDT";
        SharedPreferences preferences = getSharedPreferences(phoneNumber, MODE_PRIVATE);
        String sdt = preferences.getString("PhoneNumber", "");

        maKH = databaseHelper.getMaKH(sdt);

        tvSoDienThoai.setText(sdt);
        eHoTen.setText(databaseHelper.getTenKH(sdt));
        eNgaySinh.setText(databaseHelper.getNgaySinh(sdt));
        String gioitinh = databaseHelper.getGioiTinh(sdt);
        if (gioitinh.equals("Nam")) rbNam.setChecked(true);
        else if (gioitinh.equals("Nữ")) rbNu.setChecked(true);
        else rbKhac.setChecked(true);


        eNgaySinh.setOnClickListener(v -> {
            @SuppressLint("SetTextI18n") DatePickerDialog.OnDateSetListener callBack =
                    (view, year, month, dayOfMonth) -> eNgaySinh.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            DatePickerDialog pic = new DatePickerDialog(PersonalInfor.this, callBack, 2024, 5, 22);
            pic.setTitle("My datetime picker");
            pic.show();
        });

        btnDoiMatKhau.setOnClickListener(v -> {
            Intent intent = new Intent(PersonalInfor.this, ChangePassword.class);
            startActivity(intent);
        });

        btnThongTinCaNhan.setOnClickListener(v -> {
            if (!eHoTen.getText().toString().isEmpty() || (!rbNam.isChecked() && !rbNu.isChecked()
                    && !rbKhac.isChecked()) || !eNgaySinh.getText().toString().isEmpty()) {
                if (eHoTen.getText().toString().length() >= 10) {
                    if (Double.parseDouble(eNgaySinh.getText().toString().split("/")[2]) < 2007) {
                        String tmp;
                        if (rbNam.isChecked()) tmp = "Nam";
                        else if (rbNu.isChecked()) tmp = "Nữ";
                        else tmp = "Khác";
                        Boolean updateName = databaseHelper.updateHoTen(maKH, eHoTen.getText().toString());
                        Boolean updateSex = databaseHelper.updateGioiTinh(maKH, tmp);
                        Boolean updateBirth = databaseHelper.updateNgaySinh(maKH, eNgaySinh.getText().toString());
                        Toast.makeText(PersonalInfor.this, "Sửa thông tin thành công !", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(PersonalInfor.this, "Bạn phải đủ 18 tuổi !", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(PersonalInfor.this, "Vui lòng nhập đầy đủ họ và tên !", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(PersonalInfor.this, "Vui lòng nhập đầy đủ thông tin !", Toast.LENGTH_SHORT).show();
        });

        llDangXuat.setOnClickListener(v -> {
            Intent intent = new Intent(PersonalInfor.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        llQuetQR.setOnClickListener(v -> {
            scanCode();
            finish();
        });

        llTrangChu.setOnClickListener(v -> {
            Intent intent = new Intent(PersonalInfor.this, HomePage.class);
            startActivity(intent);
            finish();
        });
    }

    private void getWidget() {
        llDangXuat = findViewById(R.id.llDangXuat);
        llQuetQR = findViewById(R.id.llQuetQR);
        llTrangChu = findViewById(R.id.llTrangChu);
        tvSoDienThoai = findViewById(R.id.tvSoDienThoai);
        eHoTen = findViewById(R.id.eHoTen);
        eNgaySinh = findViewById(R.id.eNgaySinh);
        rbNam = findViewById(R.id.rbNam);
        rbNu = findViewById(R.id.rbNu);
        rbKhac = findViewById(R.id.rbKhac);
        btnDoiMatKhau = findViewById(R.id.btnDoiMatKhau);
        btnThongTinCaNhan = findViewById(R.id.btnThongTinCaNhan);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInfor.this);
            builder.setTitle("Chuyến đi của bạn");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("Bắt đầu chuyến đi", (dialogInterface, i) -> {
                Intent intent = new Intent(PersonalInfor.this, ScanQR.class);
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