package com.sengelgrup.istakip;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CustomUyeListeAdapter extends BaseAdapter {

    String dizi_isimler[];
    String dizi_kadiler[];
    String dizi_sifreler[];
    String dizi_durumlar[];

    LayoutInflater layoutInflater = null;


    public CustomUyeListeAdapter(Uyeler uyeler, String isimler[], String kadiler[], String sifreler[], String durumlar[])
    {
        this.dizi_isimler = isimler;
        this.dizi_kadiler = kadiler;
        this.dizi_sifreler = sifreler;
        this.dizi_durumlar = durumlar;

        layoutInflater = (LayoutInflater)uyeler.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View RowView = layoutInflater.inflate(R.layout.custom_uye_liste,null);

        TextView txt_adsoyad = RowView.findViewById(R.id.txt_uye_adi);
        TextView txt_kadi = RowView.findViewById(R.id.txt_uye_bilgisi);
        TextView txt_sifre = RowView.findViewById(R.id.txt_uye_bilgisi2);
        TextView txt_durum = RowView.findViewById(R.id.txt_uye_onay_durum);

        txt_adsoyad.setText(dizi_isimler[position]);
        txt_kadi.setText("Kullanıcı adı : " +dizi_kadiler[position]);
        txt_sifre.setText("Şifre : " + dizi_sifreler[position]);

        if (dizi_durumlar[position].equals("0"))
        {
            txt_durum.setText("Onay Bekliyor");
            txt_durum.setBackgroundResource(R.drawable.ana_corner);
        }
        else
        {
            txt_durum.setText("Onaylı");
            txt_durum.setBackgroundResource(R.drawable.bildirim_corner);
        }


        return RowView;
    }
}
