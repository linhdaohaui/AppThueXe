package com.example.semester;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.semester.Database.DatabaseHelper;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import java.util.ArrayList;

public class Traffic extends AppCompatActivity {
    LinearLayout llDangXuat, llQuetQR, llTrangChu;
    ListView lvDanhSachXe;
    DatabaseHelper databaseHelper;
    String[] listTramXuatPhat, listKinhDo, listViDo;
    String kinhdo = "", vido = "";
    int maKH = 0;

    int[] image = {R.drawable.icon_bikeee, R.drawable.icon_bike};
    ArrayList<TrafficObj> trafficList = new ArrayList<>();
    TrafficAdapter trafficAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
        databaseHelper = new DatabaseHelper(this);

        String phoneNumber = "SDT";
        SharedPreferences preferences = getSharedPreferences(phoneNumber, MODE_PRIVATE);
        String sdt = preferences.getString("PhoneNumber", "");
        maKH = databaseHelper.getMaKH(sdt);

        getWidget();

        String[] listTraffic = getResources().getStringArray(R.array.listTraffic);
        String[] listLicensePlate = getResources().getStringArray(R.array.listLicensePlate);

        for (int i = 0; i < 10; i++) {
            trafficList.add(new TrafficObj(image[0], listTraffic[0], listLicensePlate[i]));
            trafficList.add(new TrafficObj(image[1], listTraffic[1], listLicensePlate[i]));
        }

        trafficAdapter = new TrafficAdapter(Traffic.this, R.layout.traffic_layout, trafficList);
        lvDanhSachXe.setAdapter(trafficAdapter);

        llTrangChu.setOnClickListener(v -> finish());

        llQuetQR.setOnClickListener(v -> {
            scanCode();
            finish();
        });

        llDangXuat.setOnClickListener(v -> {
            Intent intent = new Intent(Traffic.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void getWidget() {
        lvDanhSachXe = findViewById(R.id.lvDanhSachXe);
        llDangXuat = findViewById(R.id.llDangXuat);
        llQuetQR = findViewById(R.id.llQuetQR);
        llTrangChu = findViewById(R.id.llTrangChu);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(Traffic.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Traffic.this, ScanQR.class);
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