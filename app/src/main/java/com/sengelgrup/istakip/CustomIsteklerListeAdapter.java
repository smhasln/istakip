package com.sengelgrup.istakip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomIsteklerListeAdapter extends BaseAdapter {

    String dizi_kisi[];
    String dizi_baslik[];
    String dizi_id[];
    String dizi_durum[];

    LayoutInflater layoutInflater = null;


    public CustomIsteklerListeAdapter(Istekler istekler, String kisiler[], String basliklar[],String durumlar[], String idler[])
    {
        this.dizi_kisi = kisiler;
        this.dizi_baslik = basliklar;
        this.dizi_durum = durumlar;
        this.dizi_id = idler;

        layoutInflater = (LayoutInflater)istekler.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dizi_id.length;
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
        View RowView = layoutInflater.inflate(R.layout.custom_istek_liste,null);

        TextView txt_baslik = RowView.findViewById(R.id.custom_istek_baslik);
        TextView txt_isim = RowView.findViewById(R.id.custom_istek_kisi);

        ImageView durum = RowView.findViewById(R.id.custom_istek_durum);

        txt_baslik.setText(dizi_baslik[position]);
        txt_isim.setText(dizi_kisi[position]);

        if (dizi_durum[position].equals("1"))
        {
            durum.setImageResource(R.drawable.ic_check_circle_black_24dp);
        }
        else
        {
            durum.setImageResource(R.drawable.ic_remove_circle_black_24dp);
        }


        return RowView;
    }
}
