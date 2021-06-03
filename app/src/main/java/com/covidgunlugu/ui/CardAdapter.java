package com.covidgunlugu.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.covidgunlugu.R;

import java.util.ArrayList;

public class CardAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<CardModel> modelArrayList;
    private SharedPreferences sharedPreferences;

    public CardAdapter(Context context, ArrayList<CardModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        sharedPreferences = this.context.getSharedPreferences("com.covidgunlugu.ui", Context.MODE_PRIVATE);
    }




    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull  View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull  ViewGroup container, int position) {

        //
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, container, false);

        //
        ImageView soruFoto = view.findViewById(R.id.soruFoto);
        TextView soruTitle = view.findViewById(R.id.soruTitle);
        TextView soru = view.findViewById(R.id.soru);
        Switch soruSwitch = view.findViewById(R.id.soruswitch);

        //
        CardModel model = modelArrayList.get(position);
        String soruTitleStr = model.getBaslik();
        String soruStr = model.getSoru();
        //Switch soruSwitchButton = model.getSoruSwitch();
        int image = model.getImage();

        //
        soruFoto.setImageResource(image);
        soruTitle.setText(soruTitleStr);
        soru.setText(soruStr);
        soruSwitch.setChecked(model.getCevap());

        //

        soruSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i("SWITCH TRIGGERED", "soru-" + (position + 1) + "enabled");
                    String soru = "soru" + (position + 1);

                    sharedPreferences.edit().putBoolean(soru, true).apply();
                }

                else {
                    Log.i("SWITCH TRIGGERED", "soru-" + (position + 1) + "disebled");
                    String soru = "soru" + (position + 1);

                    sharedPreferences.edit().putBoolean(soru, false).apply();
                }
            }
        });


        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull  ViewGroup container, int position, @NonNull  Object object) {
        container.removeView((View)object);
    }
}
