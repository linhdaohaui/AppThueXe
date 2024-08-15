package com.example.semester;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.semester.Database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    Button btnDangNhap, btnDangKy, btnBaoCaoSuCo;
    EditText eSoDienThoai, eMatKhau;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);

        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnDangKy = findViewById(R.id.btnDangKy);
        btnBaoCaoSuCo = findViewById(R.id.btnBaoCaoSuCo);
        eSoDienThoai = findViewById(R.id.eNhapSDT);
        eMatKhau = findViewById(R.id.eMatKhau);

        btnDangNhap.setOnClickListener(v -> {
            String sdt = eSoDienThoai.getText().toString();
            String matKhau = eMatKhau.getText().toString();

            if(sdt.isEmpty() || matKhau.isEmpty())
                Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin !", Toast.LENGTH_SHORT).show();
            else{
                Boolean checkCredentials = databaseHelper.checkPassword(sdt, matKhau);
                if(checkCredentials){
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                    Intent intent  = new Intent(MainActivity.this, HomePage.class);
                    saveLoginState();
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Tài khoản hoặc mật khẩu không chính xác !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDangKy.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        });

        btnBaoCaoSuCo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Report.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveLoginState();
    }

    public void saveLoginState() {
        SharedPreferences preferences = getSharedPreferences("SDT", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PhoneNumber", eSoDienThoai.getText().toString());
        editor.commit();
    }
}