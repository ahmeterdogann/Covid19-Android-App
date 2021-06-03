package com.covidgunlugu.database.data.dao;

import java.util.logging.SimpleFormatter;

public class Score {
    String score;
    String date;

    public Score(String score, String date) {
        this.score = score;
        this.date = date;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        String [] trTarih = date.split("-");

        return String.format("%s/%s/%s tarihli bulaş riski puanı %s", trTarih[2], trTarih[1], trTarih[0], score);
    }
}
