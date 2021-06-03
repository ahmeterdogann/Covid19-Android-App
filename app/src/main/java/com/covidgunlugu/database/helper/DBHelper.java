package com.covidgunlugu.database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.covidgunlugu.database.data.dao.DayData;
import com.covidgunlugu.database.data.dao.Konum;
import com.covidgunlugu.database.data.dao.Score;
import com.covidgunlugu.util.Covid19Risk;
import com.covidgunlugu.database.data.dao.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "CovidGunlugu.db";
    private static final int DBVERSION = 1;
    private static DBHelper sInstance;

    private DBHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }
    public static synchronized DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                "email VARCHAR PRIMARY KEY," +
                "sifre VARCHAR," +
                "isim VARCHAR, " +
                "soyisim VARCHAR, " +
                "dogumTarihi DATE)"
                );

        MyDB.execSQL("CREATE TABLE IF NOT EXISTS scores (" +
                "puan INTEGER, " +
                "tarih DATE)");

        MyDB.execSQL("CREATE TABLE IF NOT EXISTS locations (" +
                "latitude VARCHAR, " +
                "longitude VARCHAR, " +
                "tarih DATE, " +
                "risk VARCHAR)");

        MyDB.execSQL("CREATE TABLE IF NOT EXISTS daily_data (" +
                "soru1 VARCHAR, " +
                "soru2 VARCHAR, " +
                "soru3 VARCHAR, " +
                "soru4 VARCHAR, " +
                "soru5 VARCHAR, " +
                "soru6 VARCHAR, " +
                "soru7 VARCHAR, " +
                "konumRiski VARCHAR," +
                "tarih DATE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("DROP TABLE IF EXISTS users");
        MyDB.execSQL("DROP TABLE IF EXISTS scores");
        MyDB.execSQL("DROP TABLE IF EXISTS locations");
    }


    public boolean insertUser(String isim, String soyisim, String dogumTarihi, String email, String sifre){
        ContentValues newValues = new ContentValues();
        newValues.put("email",  email);
        newValues.put("sifre", sifre);
        newValues.put("isim", isim);
        newValues.put("soyisim", soyisim);
        newValues.put("dogumTarihi", dogumTarihi);

        return doWorkForExceptionInsert(newValues, "users", "insertUser");
    }

    public boolean insertScore(Score score){
        ContentValues newValues = new ContentValues();
        newValues.put("puan", score.getScore());
        newValues.put("tarih",  score.getDate());

        return doWorkForExceptionInsert(newValues, "scores", "insertScore");
    }

    public boolean insertLocation(Konum location){
        ContentValues newValues = new ContentValues();
        newValues.put("latitude", location.getLatitude());
        newValues.put("longitude",  location.getLongitude());
        newValues.put("tarih",  location.getTarih());
        newValues.put("risk", location.getRisk());

        return doWorkForExceptionInsert(newValues, "locations", "insertUser");
    }

    public boolean insertDayData(DayData dayData){

        ContentValues newValues = new ContentValues();
        newValues.put("soru1", dayData.getSoru1());
        newValues.put("soru2", dayData.getSoru2());
        newValues.put("soru3", dayData.getSoru3());
        newValues.put("soru4", dayData.getSoru4());
        newValues.put("soru5", dayData.getSoru5());
        newValues.put("soru6", dayData.getSoru6());
        newValues.put("soru7", dayData.getSoru7());
        newValues.put("konumRiski", dayData.getKonumRiski());
        newValues.put("tarih", dayData.getTarih());

        return doWorkForExceptionInsert(newValues, "daily_data", "insertDayData");
    }

    public ArrayList<Konum> haftalikKonumlar() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor  cursor = MyDB.rawQuery("SELECT *  FROM locations WHERE tarih BETWEEN datetime('now', '-7 days') AND datetime('now', 'localtime')",null);
        ArrayList<Konum> konumlar = new ArrayList<>();
        String latitude = "", longitude = "", tarih = "", risk = "";

        while(cursor.moveToNext()) {
            latitude = cursor.getString(cursor.getColumnIndex("latitude"));
            longitude = cursor.getString(cursor.getColumnIndex("longitude"));
            tarih = cursor.getString(cursor.getColumnIndex("tarih"));
            risk = cursor.getString(cursor.getColumnIndex("risk"));
            konumlar.add(new Konum(latitude, longitude, tarih, risk));
        }

        return konumlar;
    }

    public long sonYediGunlukOrtalama() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor  cursor = MyDB.rawQuery("SELECT avg(puan) as sonYediGunlukOrtalama FROM scores WHERE tarih BETWEEN datetime('now', '-8 days') AND datetime('now', 'localtime')",null);
        double sonYediGunlukOrtalama = 0;

        while(cursor.moveToNext()) {
            sonYediGunlukOrtalama = cursor.getDouble(cursor.getColumnIndex("sonYediGunlukOrtalama"));
        }

        return Math.round(sonYediGunlukOrtalama);
    }

    public ArrayList<Score> getAllScores() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor  cursor = MyDB.rawQuery("SELECT * FROM scores ORDER BY tarih DESC",null);
        ArrayList<Score> scores = new ArrayList<>();
        String puan, tarih;

        while(cursor.moveToNext()) {
            puan = cursor.getString(cursor.getColumnIndex("puan"));
            tarih = cursor.getString(cursor.getColumnIndex("tarih"));
            scores.add(new Score(puan, tarih));
        }

        return scores;
    }

    public Score getSpecScore(String date) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor  cursor = MyDB.rawQuery("SELECT * FROM scores WHERE tarih = ?", new String[]{date});
        String puan = "", tarih = "";

        while(cursor.moveToNext()) {
            puan = cursor.getString(cursor.getColumnIndex("puan"));
            tarih = cursor.getString(cursor.getColumnIndex("tarih"));
        }

        return new Score(puan, tarih);
    }

    public DayData getSpecDayData(String date) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor  cursor = MyDB.rawQuery("SELECT * FROM daily_data WHERE tarih = ?",new String[]{date});
        ArrayList<DayData> dayData = new ArrayList<>();
        String soru1="", soru2="", soru3="", soru4="", soru5="", soru6="", soru7="", konumRiski="", tarih="";

        while(cursor.moveToNext()) {
            soru1 = cursor.getString(cursor.getColumnIndex("soru1"));
            soru2 = cursor.getString(cursor.getColumnIndex("soru2"));
            soru3 = cursor.getString(cursor.getColumnIndex("soru3"));
            soru4 = cursor.getString(cursor.getColumnIndex("soru4"));
            soru5 = cursor.getString(cursor.getColumnIndex("soru5"));
            soru6 = cursor.getString(cursor.getColumnIndex("soru6"));
            soru7 = cursor.getString(cursor.getColumnIndex("soru7"));
            konumRiski = cursor.getString(cursor.getColumnIndex("konumRiski"));
            tarih = cursor.getString(cursor.getColumnIndex("tarih"));
        }

        return new DayData(soru1, soru2, soru3, soru4, soru5, soru6, soru7, konumRiski,tarih);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Covid19Risk gunlukRisk(String tarih) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT risk, COUNT(*) as sayi FROM (SELECT risk FROM locations WHERE tarih = ?) GROUP BY risk;", new String[] {tarih});

        Map<String, Integer> map = new HashMap<>();

        while(cursor.moveToNext()) {
            map.put(cursor.getString(cursor.getColumnIndex("risk")), cursor.getInt(cursor.getColumnIndex("sayi")));
        }

        if (map.isEmpty()) {
            return Covid19Risk.ORTA;
        }

        int max = Collections.max(map.values());
        List<String> list = map.entrySet().stream()
                .filter(entry -> entry.getValue() == max)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());

        if (list.contains("COK_YUKSEK")) {
            return  Covid19Risk.COK_YUKSEK;
        }

        else if (list.contains("YUKSEK")) {
            return  Covid19Risk.YUKSEK;
        }

        else if (list.contains("ORTA")) {
            return  Covid19Risk.ORTA;
        }

        else {
            return  Covid19Risk.DUSUK;
        }
    }



    public boolean checkEmail(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});

        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public boolean checkEmailPassword(String email, String sifre){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE email = ? AND sifre = ?", new String[] {email,sifre});

        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public User getUser(String userEmail, String userSifre) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE email = ? AND sifre = ?", new String[] {userEmail, userSifre});
        String isim = "", soyisim = "", dogumTarihi = "", sifre = "", email = "";


        while(cursor.moveToNext()) {
            isim = cursor.getString(cursor.getColumnIndex("isim"));
            soyisim = cursor.getString(cursor.getColumnIndex("soyisim"));
            dogumTarihi = cursor.getString(cursor.getColumnIndex("dogumTarihi"));
            email = cursor.getString(cursor.getColumnIndex("email"));
            sifre = cursor.getString(cursor.getColumnIndex("sifre"));
        }

        System.out.println(" isim " + isim + " soyisim " + soyisim);
        return new User(isim, soyisim, dogumTarihi, email, sifre);
    }

    private boolean doWorkForExceptionInsert(ContentValues newValues, String tableName, String methodName) {
        long result = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            result = db.insert(tableName, null, newValues);

        }

        catch (Exception e) {
            System.out.printf("%s methodunda bir sorun oluştu. ", methodName);
            e.printStackTrace();
        }

        return result != -1;
    }
}