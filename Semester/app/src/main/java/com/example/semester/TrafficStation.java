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

public class TrafficStation extends AppCompatActivity {
    LinearLayout llDangXuat, llQuetQR, llTrangChu;
    ListView lvDanhSachTramXe;
    DatabaseHelper databaseHelper;
    String[] listTramXuatPhat, listKinhDo, listViDo;
    String kinhdo = "", vido = "";

    //    Init bike station list
    int[] image = {R.drawable.icon_location};
    ArrayList<TrafficStationObj> trafficStationList = new ArrayList<>();
    TrafficStationAdapter trafficStationAdapterAdapter = null;
    int maKH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_station);
        databaseHelper = new DatabaseHelper(this);

        String phoneNumber = "SDT";
        SharedPreferences preferences = getSharedPreferences(phoneNumber, MODE_PRIVATE);
        String sdt = preferences.getString("PhoneNumber", "");
        maKH = databaseHelper.getMaKH(sdt);

        getWidget();

        //        Add values of list view

        String[] listStation = getResources().getStringArray(R.array.listStationName);
        String[] listAddress = getResources().getStringArray(R.array.listStationAddress);

        for (int i = 0; i < listStation.length; i++) {
            trafficStationList.add(new TrafficStationObj(image[0], listStation[i], listAddress[i]));
        }

        trafficStationAdapterAdapter = new TrafficStationAdapter(TrafficStation.this, R.layout.traffic_station_layout, trafficStationList);
        lvDanhSachTramXe.setAdapter(trafficStationAdapterAdapter);

        llTrangChu.setOnClickListener(v -> finish());

        llQuetQR.setOnClickListener(v -> {
            scanCode();
            finish();
        });

        llDangXuat.setOnClickListener(v -> {
            Intent intent = new Intent(TrafficStation.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void getWidget() {

        lvDanhSachTramXe = findViewById(R.id.lvDanhSachTramXe);
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


    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result ->
    {
        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TrafficStation.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(TrafficStation.this, ScanQR.class);
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
                }
            }).show();
        }
    });
}