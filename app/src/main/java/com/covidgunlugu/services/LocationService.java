package com.covidgunlugu.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.covidgunlugu.R;
import com.covidgunlugu.database.data.dao.Konum;
import com.covidgunlugu.database.data.dao.User;
import com.covidgunlugu.database.helper.DBHelper;
import com.covidgunlugu.util.Covid19RandomRiskGenerator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocationService extends Service implements LocationListener {
    private Handler handler;
    private String konumSaglayici = "gps";
    private DBHelper DB;
    private User user;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.covidgunlugu.services";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.cikisyap)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        handler = new Handler();
        handler.postDelayed(runnable, 5000);

        //DB init
        DB = DBHelper.getInstance(this);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location konum = locationManager.getLastKnownLocation(konumSaglayici);
        Toast.makeText(this, "Konum Takibi Başlatıldı", Toast.LENGTH_SHORT).show();

        if (konum != null) {
            onLocationChanged(konum);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = sdf.format(new Date());
            Toast.makeText(LocationService.this,
                    "Enlem : " + konum.getLatitude() +
                            "\nBoylam : " + konum.getLongitude() +
                            "\nTarih - Saat : " + currentDate +
                            "\nRİSK : " + Covid19RandomRiskGenerator.getRisk(), Toast.LENGTH_SHORT).show();

            Konum konumDao = new Konum(konum.getLatitude()+"", konum.getLongitude()+"", currentDate, Covid19RandomRiskGenerator.getRisk().toString());

            DB.insertLocation(konumDao);

        }
        else {
            Toast.makeText(this, "Servis Çalışıyor Ancak Konum Alınamıyor", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Konum Takibi Sonlandırıldı", Toast.LENGTH_SHORT).show();
        Log.i("SERVİS BİLGİSİ", "KAPATILDI");
        handler.removeCallbacks(runnable);
    }

    private final Runnable runnable = new Runnable() {
        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location konum = locationManager.getLastKnownLocation(konumSaglayici);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, LocationService.this);

            if (konum != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String currentDate = sdf.format(new Date());
                onLocationChanged(konum);

                Toast.makeText(LocationService.this,
                         "Enlem : " + konum.getLatitude() +
                             "\nBoylam : " + konum.getLongitude() +
                             "\nTarih - Saat : " + currentDate +
                             "\nRİSK : " + Covid19RandomRiskGenerator.getRisk(), Toast.LENGTH_SHORT).show();

                Log.i("SERVIS", "Calisiyor");
                Konum konumDao = new Konum(konum.getLatitude()+"", konum.getLongitude()+"", currentDate, Covid19RandomRiskGenerator.getRisk().toString());
                DB.insertLocation(konumDao);
            }
            else {
                Toast.makeText(LocationService.this, "Servis Çalışıyor --> Konum alınamıyor", Toast.LENGTH_SHORT).show();
                Log.i("SERVIS", "Calisiyor");
            }
             LocationService.this.handler.postDelayed(runnable, 5000);
        }
    };

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double enlem = location.getLatitude();
        double boylam = location.getAltitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
