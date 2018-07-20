package com.sengelgrup.istakip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomBildirimListeAdapter extends BaseAdapter {

    String dizi_basliklar[];
    String dizi_icerikler[];
    String dizi_durumlar[];

    LayoutInflater layoutInflater = null;


    public CustomBildirimListeAdapter(Bildirimlerim bildirimlerim, String basliklar[], String icerikler[], String durumlar[])
    {
        this.dizi_basliklar = basliklar;
        this.dizi_icerikler = icerikler;
        this.dizi_durumlar = durumlar;

        layoutInflater = (LayoutInflater)bildirimlerim.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dizi_durumlar.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View RowView = layoutInflater.inflate(R.layout.custom_bildirim_liste,null);

        TextView txt_baslik = RowView.findViewById(R.id.txt_bildirim_baslik);
        TextView txt_icerik = RowView.findViewById(R.id.txt_bildirim_icerik);
        ImageView img_durum = RowView.findViewById(R.id.img_bildirim_durum);

        txt_baslik.setText(dizi_basliklar[position]);
        txt_icerik.setText(dizi_icerikler[position]);

        if (dizi_durumlar[position].equals("0"))
        {
            img_durum.setImageResource(R.drawable.pasif);
        }
        else
        {
            img_durum.setImageResource(R.drawable.aktif);
        }


        return RowView;
    }
}
