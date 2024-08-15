package com.example.semester;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.semester.Database.DatabaseHelper;

public class Register extends AppCompatActivity {
    Button btnDangKy, btnDangNhap;
    LinearLayout llNgaySinh;
    TextView tvNgaySinh;
    EditText eSDT, eTen, eMatKhau, eNhapLaiMatKhau;
    RadioButton rbNam, rbNu, rbKhac;
    CheckBox cbDieuKhoan;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        databaseHelper = new DatabaseHelper(this);

        btnDangKy = findViewById(R.id.btnDangKy);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        eSDT = findViewById(R.id.eNhapSDT);
        eTen = findViewById(R.id.eNhapTen);
        tvNgaySinh = findViewById(R.id.tvNgaySinh);
        eMatKhau = findViewById(R.id.eNhapMatKhau);
        eNhapLaiMatKhau = findViewById(R.id.eNhapMatKhau1);
        rbKhac = findViewById(R.id.rbKhac);
        rbNam = findViewById(R.id.rbNam);
        rbNu = findViewById(R.id.rbNu);
        cbDieuKhoan = findViewById(R.id.cbDieuKhoan);
        llNgaySinh = findViewById(R.id.llNgaySinh);

        llNgaySinh.setOnClickListener(v -> {
            @SuppressLint("SetTextI18n") DatePickerDialog.OnDateSetListener callBack = (view, year, month, dayOfMonth)
                    -> tvNgaySinh.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

            DatePickerDialog pic = new DatePickerDialog(Register.this, callBack, 2024, 5, 22);
            pic.setTitle("My datetime picker");
            pic.show();
        });

        btnDangKy.setOnClickListener(v -> {
            String sdt = eSDT.getText().toString();
            String ten = eTen.getText().toString();
            String ngaysinh = tvNgaySinh.getText().toString();
            String matkhau = eMatKhau.getText().toString();
            String matkhau1 = eNhapLaiMatKhau.getText().toString();
            String gioitinh, tmpNS;
            if (!ngaysinh.isEmpty())
                tmpNS = ngaysinh.split("/")[2];
            else
                tmpNS = "";

            if (rbNam.isChecked())
                gioitinh = "Nam";
            else if (rbNu.isChecked())
                gioitinh = "Nữ";
            else
                gioitinh = "Khác";

            try {
                if (sdt.isEmpty() || ten.isEmpty() || ngaysinh.isEmpty() || matkhau1.isEmpty() || matkhau.isEmpty())
                    Toast.makeText(Register.this, "Vui lòng điên đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                else {
                    if (sdt.length() == 10) {
                        if (ten.length() >= 10) {
                            if (matkhau1.length() > 5) {
                                if (Double.parseDouble(tmpNS) <= 2007) {
                                    if (matkhau1.equals(matkhau)) {
                                        if (cbDieuKhoan.isChecked()) {
                                            Boolean checkUserEmail = databaseHelper.checkPhoneNumber(sdt);
                                            if (!checkUserEmail) {
                                                Boolean insert = databaseHelper.insertData(ten, matkhau, ngaysinh, gioitinh, sdt);
                                                if (insert) {
                                                    Toast.makeText(Register.this, "Đăng ký thành công !", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(intent);
                                                } else
                                                    Toast.makeText(Register.this, "Đăng ký không thành công !", Toast.LENGTH_SHORT).show();
                                            } else
                                                Toast.makeText(Register.this, "Tài khoản đã tồn tại ! Vui lòng đăng nhập !",
                                                        Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(Register.this,
                                                    "Vui lòng xác nhận đồng ý với Điều khoản sử dụng và Quy trình chính sách của chúng tôi !",
                                                    Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(Register.this, "Không đúng mật khẩu !", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(this, "Bạn phải đủ 18 tuổi !", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(this, "Độ dài mật khẩu lớn hơn hoặc bằng 6 ký tự !", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(this, "Bạn cần nhập đủ họ và tên", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Vui lòng nhập lại số điện thoại !", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.getMessage();
            }
        });

        btnDangNhap.setOnClickListener(v ->
        {
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}