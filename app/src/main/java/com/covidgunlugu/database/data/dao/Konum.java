package com.covidgunlugu.database.data.dao;

public class Konum {
    String latitude, longitude, tarih, risk;

    public Konum(String latitude, String longitude, String tarih, String risk) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.tarih = tarih;
        this.risk = risk;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }
}
