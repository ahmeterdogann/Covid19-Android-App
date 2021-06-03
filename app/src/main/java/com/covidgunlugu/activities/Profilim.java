package com.covidgunlugu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.covidgunlugu.R;
import com.covidgunlugu.database.data.dao.User;

public class Profilim extends AppCompatActivity {
    private TextView isim;
    private TextView soyisim;
    private TextView dogumTarihi;
    private TextView email;
    private TextView ulke;
    private Button ulkeDegistirButton;
    private Button sifreDegistirButton;
    private Toolbar toolbar;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilim);
        /****************/
        isim = findViewById(R.id.profilimIsim);
        soyisim = findViewById(R.id.profilimSoyisim);
        dogumTarihi = findViewById(R.id.profilimDogumTarihi);
        email = findViewById(R.id.profilimEmail);
        sifreDegistirButton = findViewById(R.id.profilimSifreDegistirButton);
        toolbar = findViewById(R.id.profilimToolbar);
        /****************/

        toolbar.setTitle("Profilim");
        setSupportActionBar(toolbar);

         user = (User) getIntent().getSerializableExtra("userNesnesi");

         isim.setText(user.getIsim());
         soyisim.setText(user.getSoyisim());
         dogumTarihi.setText(user.getDogumTarihi());
         email.setText(user.getEmail());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbars_options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionProfilim :
                startActivity(new Intent(Profilim.this, Profilim.class));
                //USER GÖNDERMEDİĞİN İÇİN HATA VERECEK ŞİMDİLİK

            case R.id.actionCikisYap :
                //ÇIKIŞ İŞLEMLERİ


        }
        return super.onOptionsItemSelected(item);
    }
}