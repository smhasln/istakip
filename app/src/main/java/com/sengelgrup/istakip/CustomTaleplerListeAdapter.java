package com.sengelgrup.istakip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class CustomTaleplerListeAdapter extends BaseAdapter {

    String dizi_idler[];
    String dizi_basliklar[];
    String dizi_tarihler[];

    LayoutInflater layoutInflater = null;


    public CustomTaleplerListeAdapter(Talepler talepler, String basliklar[], String tarihler[], String idler[])
    {
        this.dizi_basliklar = basliklar;
        this.dizi_tarihler = tarihler;
        this.dizi_idler = idler;

        layoutInflater = (LayoutInflater)talepler.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dizi_idler.length;
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
        View RowView = layoutInflater.inflate(R.layout.custom_talepler_liste,null);


        TextView txt_baslik = RowView.findViewById(R.id.txt_custom_talep_baslik);
        TextView txt_tarih = RowView.findViewById(R.id.txt_custom_talep_tarih);

        txt_baslik.setText(dizi_basliklar[position]);
        txt_tarih.setText(dizi_tarihler[position]);

        return RowView;
    }
}
