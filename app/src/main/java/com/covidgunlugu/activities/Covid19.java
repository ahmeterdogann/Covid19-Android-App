package com.covidgunlugu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.covidgunlugu.R;
import com.covidgunlugu.database.data.dao.User;
import com.google.android.material.navigation.NavigationView;

public class Covid19 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private NavigationView covid19NavBar;
    private DrawerLayout covid19Drawer;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid19);

        //Nav bar ve toolbar init
        toolbar = findViewById(R.id.covid19Toolbar);
        toolbar.setTitle("Covid-19");
        setSupportActionBar(toolbar);
        covid19NavBar = findViewById(R.id.covid19NavBar);
        covid19Drawer = findViewById(R.id.covid19Drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, covid19Drawer,
                toolbar, 0, 0);
        covid19Drawer.addDrawerListener(toggle);
        toggle.syncState();
        covid19NavBar.setNavigationItemSelectedListener(this);

        //Kullanıcı nesnesini alma
                user = (User) getIntent().getSerializableExtra("userNesnesi");


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.navMenuAnasayfa:
                Intent intent1 = new Intent(Covid19.this, MainActivity.class);
                intent1.putExtra("userNesnesi", user);
                startActivity(intent1);
                break;
            case R.id.navMenuPuanlarim:
                Intent intent2 = new Intent(Covid19.this, Puanlarim.class);
                intent2.putExtra("userNesnesi", user);
                startActivity(intent2);
                break;
            case R.id.navMenuHaftalıkKonumAnalizi:
                Intent intent3 = new Intent(Covid19.this, HaftalikKonumAnalizi.class);
                intent3.putExtra("userNesnesi", user);
                startActivity(intent3);
                break;
            case R.id.navMenuCovid19:
                Intent intent4 = new Intent(Covid19.this, Covid19.class);
                intent4.putExtra("userNesnesi", user);
                startActivity(intent4);
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbars_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionProfilim :
                Intent intent = new Intent(Covid19.this, Profilim.class);
                intent.putExtra("userNesnesi", user);
                startActivity(intent);
                break;
            //USER GÖNDERMEDİĞİN İÇİN HATA VERECEK ŞİMDİLİK

            case R.id.actionCikisYap :
                user = null;
                Intent intent1 = new Intent(Covid19.this,
                        GirisYap.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent1);
                break;
            //ÇIKIŞ İŞLEMLERİ
        }

        return super.onOptionsItemSelected(item);
    }
}