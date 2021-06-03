package com.covidgunlugu.ui;

import android.widget.Switch;

public class CardModel {
    String baslik, soru;
    Switch soruSwitch;
    boolean cevap;
    int image;

    public CardModel(String baslik, String soru, Switch soruSwitch, int image, boolean cevap) {
        this.baslik = baslik;
        this.soru = soru;
        this.soruSwitch = soruSwitch;
        this.image = image;
        this.cevap = cevap;
    }

    public boolean getCevap() {
        return cevap;
    }

    public void setCevap(boolean cevap) {
        this.cevap = cevap;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getSoru() {
        return soru;
    }

    public void setSoru(String soru) {
        this.soru = soru;
    }

    public Switch getSoruSwitch() {
        return soruSwitch;
    }

    public void setSoruSwitch(Switch soruSwitch) {
        this.soruSwitch = soruSwitch;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
