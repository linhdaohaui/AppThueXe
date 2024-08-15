package com.example.semester;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.regex.Pattern;

public class Report extends AppCompatActivity {
    EditText eVanDeCuaToi, eViTri;
    Button btnGoiHotline, btnGui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        eVanDeCuaToi = findViewById(R.id.eVanDeCuaToi);
        eViTri = findViewById(R.id.eViTri);
        btnGoiHotline = findViewById(R.id.btnGoiHotline);
        btnGui = findViewById(R.id.btnGui);

        btnGoiHotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callintent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
                        "1900633548"));
                startActivity(callintent);
                finish();
            }
        });

        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!eVanDeCuaToi.getText().toString().isEmpty() ||
                        !eViTri.getText().toString().isEmpty()) {

                    String vande = eVanDeCuaToi.getText().toString();
                    String vitr = eViTri.getText().toString();
                    String gmail = "info@vantaiso.com.vn";

                    Intent intentSend = new Intent(Intent.ACTION_SEND);
                    intentSend.putExtra(Intent.EXTRA_EMAIL, new String[]{gmail});
                    intentSend.putExtra(Intent.EXTRA_SUBJECT, "Báo cáo sự cố");
                    intentSend.putExtra(Intent.EXTRA_TEXT, vande + "\n" + vitr);
                    intentSend.setType("message/rfc822");
                    startActivity(Intent.createChooser(intentSend, "Choose gmail client: "));
                    finish();
                } else
                    Toast.makeText(Report.this, "Vui lòng nhập đầy đủ thông tin !",
                            Toast.LENGTH_SHORT).show();

            }
        });
    }
}