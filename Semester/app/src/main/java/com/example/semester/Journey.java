package com.example.semester;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.semester.Database.DatabaseHelper;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Journey extends AppCompatActivity {
    LinearLayout llBaoCaoSuCo, llTraXe, llTrangChu;
    TextView eThoiGianDi, tvGiaCuocHienTai, tvXeDaMoKhoa;
    long lStartTime, lSystemTime = 0L;
    Handler handler = new Handler();
    boolean isRun;
    DatabaseHelper databaseHelper;
    boolean test = true;
    String[] listTramXuatPhat, listKinhDo, listViDo;
    String kinhdo = "", vido = "";
    private LocationRequest locationRequest;
    double latitude = 0, longitude = 0;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);

        databaseHelper = new DatabaseHelper(this);

        String phoneNumber = "SDT";
        SharedPreferences preferences = getSharedPreferences(phoneNumber, MODE_PRIVATE);
        String sdt = preferences.getString("PhoneNumber", "");
        int maKH = databaseHelper.getMaKH(sdt);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        getCurrentLocation();

        Runnable runnable = new Runnable() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void run() {
                lSystemTime = SystemClock.uptimeMillis() - lStartTime;
                long lUpdateTime = lSystemTime;
                long secs = lUpdateTime / 1000;
                long mins = secs / 60;
                secs = secs % 60;
                long milliseconds = lUpdateTime % 1000;
                eThoiGianDi.setText(mins + ":" + String.format("%02d", secs) + ":" +
                        String.format("%03d", milliseconds));

                double tien;
                double time = mins + (double) secs / 60;
                if (databaseHelper.getLoaiXe(maKH).contains("Xe đạp cơ"))
                    tien = Math.floor((time * 10000) / 60);
                else
                    tien = Math.floor((time * 20000) / 60);

                if (databaseHelper.getMoney(maKH) - tien < 5000 && test) {
                    Toast.makeText(Journey.this, "Vui lòng nạp thêm tiền !",
                            Toast.LENGTH_SHORT).show();
                    test = false;
                }
                if (databaseHelper.getMoney(maKH) - tien <= 0) {
                    Intent intent = new Intent(Journey.this, EndJourney.class);
                    if (secs >= 30)
                        mins++;
                    if (databaseHelper.updateTongThoiGian(mins, maKH))
                        Toast.makeText(Journey.this, "Đã cập nhật thời gian !",
                                Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Journey.this, "Có lỗi khi cập nhật thời gian !",
                                Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    return;
                }
                handler.postDelayed(this, 0);
            }
        };

        getWidget();

        if (databaseHelper.getLoaiXe(maKH).contains("Xe đạp điện"))
            tvGiaCuocHienTai.setText("20.000 đồng / 60 phút");
        else
            tvGiaCuocHienTai.setText("10.000 đồng / 60 phút");

        tvXeDaMoKhoa.setText("Xe " + databaseHelper.getBienSoXe(maKH));

        isRun = true;
        lStartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);

        llTrangChu.setOnClickListener(v -> finish());

        llBaoCaoSuCo.setOnClickListener(v -> {
            Intent intent = new Intent(Journey.this, Report.class);
            startActivity(intent);
            finish();
        });

        llTraXe.setOnClickListener(v -> {
            listTramXuatPhat = getResources().getStringArray(R.array.listStationName);
            listKinhDo = getResources().getStringArray(R.array.longitude);
            listViDo = getResources().getStringArray(R.array.latitude);
            for (int j = 0; j < listTramXuatPhat.length; j++) {
                if (listTramXuatPhat[j].contains(databaseHelper.getTramXuatPhat(maKH))) {
                    kinhdo = listKinhDo[j];
                    vido = listViDo[j];
                    break;
                }
            }
            double kd = Double.parseDouble(kinhdo);
            double vd = Double.parseDouble(vido);

            if (Math.abs(kd - longitude) <= 0.00001 && Math.abs(vd - latitude) <= 0.00001) {
                if (!isRun)
                    return;
                isRun = false;
                handler.removeCallbacks(runnable);

                String[] tmp = eThoiGianDi.getText().toString().split(":");
                double minute = Integer.parseInt(tmp[0]) + (double) Integer.parseInt(tmp[1]) / 60;
                if (databaseHelper.updateTongThoiGian(minute, maKH))
                    Toast.makeText(Journey.this, "Đã cập nhật thời gian !",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Journey.this, "Có lỗi khi cập nhật thời gian !",
                            Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Journey.this, EndJourney.class);
                Toast.makeText(this, "Cảm ơn vì chuyến đi !", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            } else
                Toast.makeText(this, "Vui lòng trả xe đúng trạm !",
                        Toast.LENGTH_SHORT).show();
        });
    }

    private void getWidget() {
        llTrangChu = findViewById(R.id.llTrangChu);
        llBaoCaoSuCo = findViewById(R.id.llBaoCaoSuCo);
        llTraXe = findViewById(R.id.llTraXe);
        eThoiGianDi = findViewById(R.id.eThoiGianDi);
        tvGiaCuocHienTai = findViewById(R.id.tvGiaCuocHienTai);
        tvXeDaMoKhoa = findViewById(R.id.tvXeDaMoKhoa);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled())
                    getCurrentLocation();
                else
                    turnOnGPS();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2)
            if (resultCode == Activity.RESULT_OK)
                getCurrentLocation();
    }

    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(Journey.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(Journey.this)
                            .requestLocationUpdates(locationRequest,
                                    new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    LocationServices.getFusedLocationProviderClient(Journey.this)
                                            .removeLocationUpdates(this);
                                    if (locationResult != null && locationResult.getLocations().size() > 0) {
                                        int index = locationResult.getLocations().size() - 1;
                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                        longitude = locationResult.getLocations().get(index).getLongitude();
                                    }
                                }
                            }, Looper.getMainLooper());
                } else
                    turnOnGPS();
            } else
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(Journey.this, "GPS is already tured on",
                            Toast.LENGTH_SHORT).show();
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException
                                        .startResolutionForResult(Journey.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;
        if (locationManager == null)
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;
    }
}