package com.covidgunlugu.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.covidgunlugu.database.data.dao.DayData;
import com.covidgunlugu.database.data.dao.Score;
import com.covidgunlugu.database.helper.DBHelper;
import com.covidgunlugu.util.Covid19Risk;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {
    private DBHelper DB;
    private SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        DB = DBHelper.getInstance(context);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);
        Date dateBefore1Day = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(dateBefore1Day);

        sharedPreferences = context.getSharedPreferences("com.covidgunlugu.services", Context.MODE_PRIVATE);
        System.out.println("ALARM TETİKLENDİ");
        Toast.makeText(context, "ALARM TETİKLENDİ", Toast.LENGTH_LONG).show();

        int puan = 0;
        boolean [] soru = new boolean[8];
        String [] sorular = new String[8];

        soru[1] = sharedPreferences.getBoolean("soru1", false);
        soru[2] = sharedPreferences.getBoolean("soru2", false);
        soru[3] = sharedPreferences.getBoolean("soru3", false);
        soru[4] = sharedPreferences.getBoolean("soru4", false);
        soru[5] = sharedPreferences.getBoolean("soru5", false);
        soru[6] = sharedPreferences.getBoolean("soru6", false);
        soru[7] = sharedPreferences.getBoolean("soru7", false);

        for (int i = 1; i <= 7; i++)
            if (soru[i])
                sorular[i] = "EVET";
            else
                sorular[i] = "HAYIR";



        if (soru[1])
            puan += 10;

        if (soru[2])
            puan += 15;

        if (soru[3])
            puan += 10;

        if (soru[4])
            puan += 5;

        if (soru[5])
            puan += 10;

        if (soru[6])
            puan += 10;

        if (soru[7])
            puan += 15;

        Covid19Risk risk = DB.gunlukRisk(date);

        if (risk == Covid19Risk.COK_YUKSEK)
            puan += 25;
        else if (risk == Covid19Risk.YUKSEK)
            puan += 20;
        else if (risk == Covid19Risk.ORTA)
            puan += 10;
        else if (risk == Covid19Risk.DUSUK)
            puan += 5;
        else {
            //..
        }

        Score score = new Score(puan+"", date);

        DB.insertScore(score);

        DayData dayData = new DayData(sorular[1], sorular[2], sorular[3], sorular[4], sorular[5],
                sorular[6], sorular[7], risk.toString(), date);

        DB.insertDayData(dayData);

    }
}