package com.covidgunlugu.util;

import java.util.Date;

public class Utility {
    public static boolean checkIfTheTimeHasPassed(long timeInMillis) {
        long nowTime = new Date().getTime();
        return nowTime > timeInMillis;
    }
}
