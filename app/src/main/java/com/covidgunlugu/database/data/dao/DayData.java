package com.covidgunlugu.database.data.dao;

import android.content.Context;

import com.covidgunlugu.CovidGunlugu;
import com.covidgunlugu.database.helper.DBHelper;
import com.covidgunlugu.util.*;

public class DayData {
    String soru1, soru2, soru3, soru4, soru5, soru6, soru7, konumRiski, tarih;

    public DayData(String soru1, String soru2, String soru3, String soru4,
                   String soru5, String soru6, String sour7, String konumRiski, String tarih) {
        this.soru1 = soru1;
        this.soru2 = soru2;
        this.soru3 = soru3;
        this.soru4 = soru4;
        this.soru5 = soru5;
        this.soru6 = soru6;
        this.soru7 = sour7;
        this.konumRiski = konumRiski;
        this.tarih = tarih;
    }

    public String getSoru1() {
        return soru1;
    }

    public void setSoru1(String soru1) {
        this.soru1 = soru1;
    }

    public String getSoru2() {
        return soru2;
    }

    public void setSoru2(String soru2) {
        this.soru2 = soru2;
    }

    public String getSoru3() {
        return soru3;
    }

    public void setSoru3(String soru3) {
        this.soru3 = soru3;
    }

    public String getSoru4() {
        return soru4;
    }

    public void setSoru4(String soru4) {
        this.soru4 = soru4;
    }

    public String getSoru5() {
        return soru5;
    }

    public void setSoru5(String soru5) {
        this.soru5 = soru5;
    }

    public String getSoru6() {
        return soru6;
    }

    public void setSoru6(String soru6) {
        this.soru6 = soru6;
    }

    public String getSoru7() {
        return soru7;
    }

    public void setSoru7(String soru7) {
        this.soru7 = soru7;
    }

    public String getKonumRiski() {
        return konumRiski;
    }

    public void setKonumRiski(String konumRiski) {
        this.konumRiski = konumRiski;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    private String getAlertMessageSoru(int soruSıra, String cevap) {
        String [] puanlar = {"", "+10", "+15", "+10", "+5", "+10", "+10", "+15"};

        if (cevap.equals("HAYIR"))
            return "Hayır " + "-";
        else
            return "Evet " + puanlar[soruSıra];
    }

    private String getAlertMessageKonum(String konumRiski) {
        String [] puanlar = {"-", "+5", "+10", "+20", "+25"};

        switch (konumRiski) {
            case "DUSUK":
                return "Düşük " + puanlar[1];
            case "ORTA":
                return "Orta " + puanlar[2];
            case "YUKSEK":
                return "Yüksek " + puanlar[3];
            case "COK_YUKSEK":
                return "Çok Yüksek " + puanlar[4];
            default:
                return puanlar[0];

        }
    }

    @Override
    public String toString() {
        return  "Soru-1 : " + getAlertMessageSoru(1, soru1) + "\n" +
                "Soru-2 : " + getAlertMessageSoru(2, soru2) + "\n" +
                "Soru-3 : " + getAlertMessageSoru(3, soru3) + "\n" +
                "Soru-4 : " + getAlertMessageSoru(4, soru4) + "\n" +
                "Soru-5 : " + getAlertMessageSoru(5, soru5) + "\n" +
                "Soru-6 : " + getAlertMessageSoru(6, soru6) + "\n" +
                "Soru-7 : " + getAlertMessageSoru(7, soru7) + "\n" +
                "Konum Riski : " + getAlertMessageKonum(konumRiski) + "\n" +
                "Toplam = " + DBHelper.getInstance(CovidGunlugu.getAppContext()).getSpecScore(tarih).getScore();
    }
}
