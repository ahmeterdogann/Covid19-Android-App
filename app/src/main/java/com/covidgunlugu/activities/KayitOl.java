package com.covidgunlugu.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.covidgunlugu.R;
import com.covidgunlugu.database.helper.DBHelper;


public class KayitOl extends AppCompatActivity {
    /****************/
    private EditText isim;
    private EditText soyIsim;
    private EditText dogumTarihi;
    private EditText email;
    private EditText sifre;
    private Button kayitOl;
    private DBHelper DB;
    private SharedPreferences sharedPreferences;
    /****************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);

        /****************/
        isim = findViewById(R.id.kayitIsim);
        soyIsim = findViewById(R.id.kayitSoyisim);
        dogumTarihi = findViewById(R.id.kayitDogumTarihi);
        email = findViewById(R.id.kayitEmail);
        sifre = findViewById(R.id.kayitSifre);
        kayitOl = findViewById(R.id.kayitOlButton);
        DB = DBHelper.getInstance(this);
        sharedPreferences = this.getSharedPreferences("com.covidgunlugu.activities", MODE_PRIVATE);
        //////
        isim.setText("Ahmet");
        soyIsim.setText("Erdogan");
        dogumTarihi.setText("1997-04-07");
        email.setText("ahmett.erdogan.23@gmail.com");
        sifre.setText("zxasqw12");
        //////


        kayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userIsim = isim.getText().toString();
                String userSoyisim = soyIsim.getText().toString();
                String userDogumTarihi = dogumTarihi.getText().toString();
                String userEmail = email.getText().toString();
                String userSifre = sifre.getText().toString();



                if(userIsim.equals("") ||
                        userSoyisim.equals("") ||
                        userDogumTarihi.equals("") ||
                        userEmail.equals("") ||
                        userSifre.equals("")) {
                    Toast.makeText(KayitOl.this, "Lütfen tüm alanları doldurunuz!", Toast.LENGTH_SHORT).show();
                }

                else {
                    boolean checkuser = DB.checkEmail(userEmail);
                    if(checkuser==false) {
                        boolean insert = DB.insertUser(userIsim, userSoyisim, userDogumTarihi, userEmail, userSifre);
                        if(insert==true) {
                            Toast.makeText(KayitOl.this, "Kayıt olma işlemi başarılı. Hoşgeldiniz :)", Toast.LENGTH_SHORT).show();
                            sharedPreferences.edit().putBoolean("isActivated", true).apply();
                            Intent intent = new Intent(getApplicationContext(),GirisYap.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(KayitOl.this, "Kayıt olma işlemi başarısız!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(KayitOl.this, "Bu mail adresine sahip bir kullanıcı bulunmakta!", Toast.LENGTH_SHORT).show();
                    }
                }

                }

        });



    }
}