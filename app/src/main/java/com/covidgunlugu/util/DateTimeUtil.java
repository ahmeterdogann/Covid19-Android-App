package com.covidgunlugu.util;

public class DateTimeUtil {
    public static long forMainActivityCountDownTimer(String zaman) {
        String [] hms = zaman.split(":");
        long oneDay = 24 * 3_600_000;

        long dayEnd = oneDay - Integer.parseInt(hms[0]) * 3_600_000 -
                Integer.parseInt(hms[1]) * 60000 -
                Integer.parseInt(hms[2]) * 1000;

        return dayEnd;

    }

    public static String sqliteDateFormatToTRformat(String date) {
        String [] trTarih = date.split("-");

        return String.format("%s/%s/%s", trTarih[2], trTarih[1], trTarih[0]);
    }
}
