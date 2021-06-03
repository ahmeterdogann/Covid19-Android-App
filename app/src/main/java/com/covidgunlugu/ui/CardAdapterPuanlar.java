package com.covidgunlugu.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.covidgunlugu.R;
import com.covidgunlugu.database.data.dao.Score;
import com.covidgunlugu.database.helper.DBHelper;
import com.covidgunlugu.util.DateTimeUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapterPuanlar extends RecyclerView.Adapter<CardAdapterPuanlar.PuanlarCardTasarımNesneTutucu> {
    private Context context;
    private List<Score> puanlar;

    public CardAdapterPuanlar(Context context, List<Score> puanlar) {
        this.context = context;
        this.puanlar = puanlar;
    }

    public class PuanlarCardTasarımNesneTutucu extends RecyclerView.ViewHolder {
        public TextView tarih;
        public TextView puan;
        public ImageView popUpIcon;

        public PuanlarCardTasarımNesneTutucu(@NonNull View itemView) {
            super(itemView);
            tarih = itemView.findViewById(R.id.puanTarih);
            puan = itemView.findViewById(R.id.puanDeger);
            popUpIcon = itemView.findViewById(R.id.popUpIcon);
        }
    }

    @NonNull
    @Override
    public PuanlarCardTasarımNesneTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.puanlar_card_tasarim, parent, false);
        return new PuanlarCardTasarımNesneTutucu(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull CardAdapterPuanlar.PuanlarCardTasarımNesneTutucu holder, int position) {
        final String tarihGelen = puanlar.get(position).getDate();
        final String puanGelen = puanlar.get(position).getScore();

        holder.tarih.setText(DateTimeUtil.sqliteDateFormatToTRformat(tarihGelen));

        if (Integer.parseInt(puanGelen) > 75) {
            holder.puan.setText(puanGelen);
            holder.puan.setTextColor(0xFFA10808);
        }

        else if (Integer.parseInt(puanGelen) > 40) {
            holder.puan.setText(puanGelen);
            holder.puan.setTextColor(0xFFEC6135);
        }

        else {
            holder.puan.setText(puanGelen);
            holder.puan.setTextColor(0xFF25B12B);
        }

        holder.popUpIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Pop up tıklandı " + puanlar.get(position).getDate(), Toast.LENGTH_SHORT).show();

                PopupMenu popupMenu = new PopupMenu(context, view);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.puanlarim_popup, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle("Detaylar");
                        alert.setMessage(DBHelper.getInstance(context).getSpecDayData(puanlar.get(position).getDate()).toString());
                        alert.setIcon(R.drawable.soru_isareti_siyah);

                        alert.setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        alert.show();

                        return false;
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return puanlar.size();
    }


}
