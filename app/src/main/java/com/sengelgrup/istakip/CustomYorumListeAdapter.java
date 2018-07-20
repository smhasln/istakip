package com.sengelgrup.istakip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomYorumListeAdapter extends BaseAdapter {

    String dizi_yorum[];
    String dizi_tarih[];
    String dizi_isim[];

    LayoutInflater layoutInflater = null;


    public CustomYorumListeAdapter(Yorumlar yorumlar, String yorum[], String tarih[], String isim[])
    {
        this.dizi_isim = isim;
        this.dizi_tarih = tarih;
        this.dizi_yorum = yorum;

        layoutInflater = (LayoutInflater)yorumlar.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dizi_isim.length;
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
        View RowView = layoutInflater.inflate(R.layout.custom_yorum_liste,null);

        TextView txt_isim = RowView.findViewById(R.id.custom_yorum_isim);
        TextView txt_tarih = RowView.findViewById(R.id.custom_yorum_tarih);
        TextView txt_yorum = RowView.findViewById(R.id.custom_yorum);

        txt_isim.setText(dizi_isim[position]);
        txt_tarih.setText(dizi_tarih[position]);
        txt_yorum.setText(dizi_yorum[position]);


        return RowView;
    }
}
