package com.covidgunlugu.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.covidgunlugu.R;
import com.covidgunlugu.database.data.dao.User;
import com.covidgunlugu.database.helper.DBHelper;

public class GirisYap extends AppCompatActivity {
    private TextView email;
    private TextView sifre;
    private Button girisYapButton;
    private Button girisYapkayitOlButton;
    private Button sifremiUnuttumButton;
    private DBHelper DB;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_yap);

        /****************/
        email = findViewById(R.id.girisYapEmail);
        sifre = findViewById(R.id.girisYapSifre);
        girisYapButton = findViewById(R.id.girisYapButton);
        girisYapkayitOlButton = findViewById(R.id.girisYapKayitOlButton);
        sifremiUnuttumButton = findViewById(R.id.sifremiUnuttumButton);
        DB = DBHelper.getInstance(this);
        sharedPreferences = this.getSharedPreferences("com.covidgunlugu.activities", Context.MODE_PRIVATE);
        /****************/
        email.setText(sharedPreferences.getString("email", ""));
        email.setText("ahmett.erdogan.23@gmail.com");
        sifre.setText("zxasqw12");

        girisYapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String userSifre = sifre.getText().toString();

                if (userEmail.equals("") || userSifre.equals(""))
                    Toast.makeText(GirisYap.this, "Lütfen email ve şifrenizi giriniz!", Toast.LENGTH_SHORT).show();

                else {
                    if (DB.checkEmailPassword(userEmail, userSifre)) {
                        sharedPreferences.edit().putString("email", userEmail).apply();
                        Toast.makeText(GirisYap.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                        User user = DB.getUser(userEmail, userSifre);
                        Intent intent = new Intent(GirisYap.this, MainActivity.class);
                        intent.putExtra("userNesnesi", user);
                        finish();
                        startActivity(intent);
                    }

                    else {
                        Toast.makeText(GirisYap.this, "Email ya da şifreniz yanlış!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        girisYapkayitOlButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sharedPreferences.getBoolean("isActivated", false)) {
                    Toast.makeText(GirisYap.this, "Uygulamaya kayıt olunmuş. Şifrenizi unuttuysanız şifremi unuttum butonuna tıklayınız.", Toast.LENGTH_SHORT).show();
                }

                else {
                    startActivity(new Intent(GirisYap.this, KayitOl.class));
                }

            }
        });



    }
}