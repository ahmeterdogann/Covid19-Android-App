package com.covidgunlugu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.covidgunlugu.R;
import com.covidgunlugu.database.data.dao.Score;
import com.covidgunlugu.database.data.dao.User;
import com.covidgunlugu.database.helper.DBHelper;
import com.covidgunlugu.ui.CardAdapterPuanlar;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Puanlarim extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private NavigationView puanlarimNavBar;
    private DrawerLayout puanlarimDrawer;
    private User user;
    private RecyclerView recyclerView;
    private ArrayList<Score> puanlar;
    private DBHelper DB;
    private CardAdapterPuanlar cardAdapterPuanlar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puanlarim);

        //Nav bar ve toolbar init
        toolbar = findViewById(R.id.puanlarimToolbar);
        toolbar.setTitle("Puanlarım");
        setSupportActionBar(toolbar);
        puanlarimNavBar = findViewById(R.id.puanlarimNavBar);
        puanlarimDrawer = findViewById(R.id.puanlarimDrawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, puanlarimDrawer,
                toolbar, 0, 0);
        puanlarimDrawer.addDrawerListener(toggle);
        toggle.syncState();
        puanlarimNavBar.setNavigationItemSelectedListener(this);

        //Kullanıcı nesnesini alma
        user = (User) getIntent().getSerializableExtra("userNesnesi");
        DB = DBHelper.getInstance(this);

        puanlar = DB.getAllScores();
        //REC GELECEK
        recyclerView = this.findViewById(R.id.puanlarRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cardAdapterPuanlar = new CardAdapterPuanlar(this, puanlar);
        recyclerView.setAdapter(cardAdapterPuanlar);



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.navMenuAnasayfa:
                Intent intent1 = new Intent(Puanlarim.this, MainActivity.class);
                intent1.putExtra("userNesnesi", user);
                startActivity(intent1);
                break;
            case R.id.navMenuPuanlarim:
                Intent intent2 = new Intent(Puanlarim.this, Puanlarim.class);
                intent2.putExtra("userNesnesi", user);
                startActivity(intent2);
                break;
            case R.id.navMenuHaftalıkKonumAnalizi:
                Intent intent3 = new Intent(Puanlarim.this, HaftalikKonumAnalizi.class);
                intent3.putExtra("userNesnesi", user);
                startActivity(intent3);
                break;
            case R.id.navMenuCovid19:
                Intent intent4 = new Intent(Puanlarim.this, Covid19.class);
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

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionProfilim :
                Intent intent = new Intent(Puanlarim.this, Profilim.class);
                intent.putExtra("userNesnesi", user);
                startActivity(intent);
                break;
            //USER GÖNDERMEDİĞİN İÇİN HATA VERECEK ŞİMDİLİK

            case R.id.actionCikisYap :
                user = null;
                Intent intent1 = new Intent(Puanlarim.this,
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