package com.covidgunlugu.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.covidgunlugu.database.data.dao.DayData;
import com.covidgunlugu.database.data.dao.Score;
import com.covidgunlugu.database.helper.DBHelper;
import com.covidgunlugu.services.AlarmReceiver;
import com.covidgunlugu.ui.CardAdapter;
import com.covidgunlugu.ui.CardModel;
import com.covidgunlugu.services.LocationService;
import com.covidgunlugu.R;
import com.covidgunlugu.database.data.dao.User;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private NavigationView anasayfaNavBar;
    private DrawerLayout anasayfaDrawer;
    private User user;
    private int izinKontrol;
    private ViewPager viewPager;
    private ArrayList<CardModel> cardModelArrayList;
    private CardAdapter cardAdapter;
    private Switch soruSwitch;
    private Switch konumIzni;
    private SharedPreferences sharedPreferences;
    private DBHelper DB;
    private TextView bulasRiskiText;
    private ImageView bulasRiskiIcon;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB = DBHelper.getInstance(this);

        /*Score score1 = new Score("90", "2021-06-01");
        Score score2 = new Score("60", "2021-06-02");
        Score score3 = new Score("40", "2021-06-03");
        Score score4 = new Score("80", "2021-05-31");
        Score score5 = new Score("50", "2021-05-30");
        Score score6 = new Score("65", "2021-05-29");
        Score score7 = new Score("25", "2021-05-28");
        Score score8 = new Score("70", "2021-05-27");

        DB.insertScore(score1);
        DB.insertScore(score2);
        DB.insertScore(score3);
        DB.insertScore(score4);
        DB.insertScore(score5);
        DB.insertScore(score6);
        DB.insertScore(score7);
        DB.insertScore(score8);

        DB.insertDayData(new DayData("EVET", "EVET", "HAYIR", "HAYIR",
                "EVET", "EVET", "HAYIR", "YUKSEK", "2021-05-29"));*/


        //Nav bar ve toolbar init
        toolbar = findViewById(R.id.anasayfaToolbar);
        toolbar.setTitle("Anasayfa");
        setSupportActionBar(toolbar);
        anasayfaNavBar = findViewById(R.id.anasayfaNavBar);
        anasayfaDrawer = findViewById(R.id.anasayfaDrawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, anasayfaDrawer,
                toolbar, 0, 0);
        anasayfaDrawer.addDrawerListener(toggle);
        toggle.syncState();
        anasayfaNavBar.setNavigationItemSelectedListener(this);

        //Bula?? riski init
        bulasRiskiText = findViewById(R.id.bulasRiskiText);
        bulasRiskiIcon = findViewById(R.id.bulasRiskiIcon);
        long sonYediGunlukOrt = DB.sonYediGunlukOrtalama();
        System.out.println("HAFTALIK ORTALAMA : " + sonYediGunlukOrt);
        if (sonYediGunlukOrt > 75) {
            bulasRiskiText.setText("Bula?? Riskiniz Y??ksek");
            bulasRiskiIcon.setImageResource(R.drawable.riskyuksek);
        }

        else if (sonYediGunlukOrt > 40) {
            bulasRiskiText.setText("Bula?? Riskiniz Normal");
            bulasRiskiIcon.setImageResource(R.drawable.risknormal);
        }

        else {
            bulasRiskiText.setText("Bula?? Riskiniz D??????k");
            bulasRiskiIcon.setImageResource(R.drawable.riskdusuk);
        }

        //Sorular Cards
        sharedPreferences = this.getSharedPreferences("com.covidgunlugu.activities", Context.MODE_PRIVATE);
        viewPager = findViewById(R.id.viewPager);
        loadCards();


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //Kullan??c?? nesnesini alma
        user = (User) getIntent().getSerializableExtra("userNesnesi");


        //alarm manager
        if (sharedPreferences.getBoolean("isFirst", true)) {
            sharedPreferences.edit().putBoolean("isFirst", false).apply();

            Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

        }
        else{
            //..
        }

        //Konum Takibi
        konumIzni = findViewById(R.id.konumIzni);
        konumIzni.setChecked(isMyServiceRunning(LocationService.class));

        konumIzni.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    izinKontrol = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                    if (izinKontrol != PackageManager.PERMISSION_GRANTED) {
                        //Daha ??nce izin verilmemi??
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                    }

                    else {
                        Toast.makeText(MainActivity.this, "??zin Verilmi?? Servis Ba??lat??l??yor", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, LocationService.class);
                        intent.putExtra("userNesnesi", user);
                        startForegroundService(intent);

                    }
                }

                else {
                    stopService(new Intent(MainActivity.this, LocationService.class));

                }
            }
        });

    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void loadCards() {
        //init list
        cardModelArrayList = new ArrayList<>();
        Log.i("Y??klendi", "soru1");
        //add items to list
        cardModelArrayList.add(new CardModel(
                "Soru - 1",
                "G??n i??erisinde ate??iniz 37.5 dereceyi ge??ti\nmi?",
                soruSwitch,
                R.drawable.soru1,
                sharedPreferences.getBoolean("soru1", false)
        ));
        Log.i("Y??klendi", "soru1");

        cardModelArrayList.add(new CardModel(
                "Soru - 2",
                "G??n i??erisinde tat ve koku kayb?? ya??ad??n??z\nm???",
                soruSwitch,
                R.drawable.soru2,
                sharedPreferences.getBoolean("soru2", false)
        ));
        Log.i("Y??klendi", "soru2");

        cardModelArrayList.add(new CardModel(
                "Soru - 3",
                "G??n i??erisinde kuru ??ks??r??k ataklar??\nya??ad??n??z m???",
                soruSwitch,
                R.drawable.soru3,
                sharedPreferences.getBoolean("soru3", false)
        ));
        Log.i("Y??klendi", "soru3");

        cardModelArrayList.add(new CardModel(
                "Soru - 4",
                "G??n i??erisinde a????r?? yorgun hissetti??iniz\nanlar oldu mu?",
                soruSwitch,
                R.drawable.soru4,
                sharedPreferences.getBoolean("soru4", false)
        ));
        Log.i("Y??klendi", "soru4");

        cardModelArrayList.add(new CardModel(
                "Soru - 5",
                "G??n i??erisinde market,restoran,toplu ta????ma\ngibi kalabal??k ortamlarda bulundunuz mu?",
                soruSwitch,
                R.drawable.soru5,
                sharedPreferences.getBoolean("soru5", false)
        ));
        Log.i("Y??klendi", "soru5");

        cardModelArrayList.add(new CardModel(
                "Soru - 6",
                "G??n i??erisinde riskli bir alanda maskesiz\nbulundunuz mu?",
                soruSwitch,
                R.drawable.soru6,
                sharedPreferences.getBoolean("soru6", false)
        ));
        Log.i("Y??klendi", "soru7");

        cardModelArrayList.add(new CardModel(
                "Soru - 7",
                "Son 14 g??n i??erisinde temasta\nbulundu??unuz birinin testi pozitif ????kt?? m???",
                soruSwitch,
                R.drawable.soru7,
                sharedPreferences.getBoolean("soru7", false)
        ));
        Log.i("Y??klendi", "soru7");

        cardAdapter = new CardAdapter(this, cardModelArrayList);

        viewPager.setAdapter(cardAdapter);
        viewPager.setPadding(100, 0 ,100, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "??zin Verildi Servis Ba??lat??l??yor", Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, LocationService.class));
            } else {
                konumIzni.setChecked(false);
                Toast.makeText(MainActivity.this, "??zin Verilmedi Servis Ba??lat??lam??yor", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.navMenuAnasayfa:
                Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                intent1.putExtra("userNesnesi", user);
                startActivity(intent1);
                break;
            case R.id.navMenuPuanlarim:
                Intent intent2 = new Intent(MainActivity.this, Puanlarim.class);
                intent2.putExtra("userNesnesi", user);
                startActivity(intent2);
                break;
            case R.id.navMenuHaftal??kKonumAnalizi:
                Intent intent3 = new Intent(MainActivity.this, HaftalikKonumAnalizi.class);
                intent3.putExtra("userNesnesi", user);
                startActivity(intent3);
                break;
            case R.id.navMenuCovid19:
                Intent intent4 = new Intent(MainActivity.this, Covid19.class);
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
                Intent intent = new Intent(MainActivity.this, Profilim.class);
                intent.putExtra("userNesnesi", user);
                startActivity(intent);
                break;
                //USER G??NDERMED??????N ??????N HATA VERECEK ????MD??L??K

            case R.id.actionCikisYap :
                user = null;
                Intent intent1 = new Intent(MainActivity.this,
                        GirisYap.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent1);
                break;
                //??IKI?? ????LEMLER??
        }

        return super.onOptionsItemSelected(item);
    }



}