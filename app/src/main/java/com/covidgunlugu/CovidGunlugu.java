package com.covidgunlugu;

import android.app.Application;
import android.content.Context;

public class CovidGunlugu extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        CovidGunlugu.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return CovidGunlugu.context;
    }
}