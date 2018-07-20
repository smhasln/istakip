package com.sengelgrup.istakip;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.sengelgrup.istakip.JSON.IstekCevapJSON;
import com.sengelgrup.istakip.JSON.IsteklerJSON;
import com.sengelgrup.istakip.JSON.TaleplerJSON;
import com.tapadoo.alerter.Alerter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Istekler extends AppCompatActivity {

    String dizi_kisi[];
    String dizi_baslik[];
    String dizi_id[];
    String dizi_durum[];

    ListView liste;

    String secilen_istek_id;
    String kullanici_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istekler);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        kullanici_id = preferences.getString("kullanici_id", "N/A");

        liste = findViewById(R.id.istek_liste);

        ImageButton geri = findViewById(R.id.istek_geri);
        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Istekler.this,AnaMenu.class));
                overridePendingTransition(R.anim.saga1,R.anim.saga2);
            }
        });

        doldur();


        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(Istekler.this);
                builder1.setMessage("Talebi iptal edilenlere taşı");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Tamam",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                        secilen_istek_id = dizi_id[position];
                                iptal_gonder();

                            }
                        });

                builder1.setNegativeButton(
                        "Vazgeç",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

    void iptal_gonder()
    {


        final ProgressDialog progress = ProgressDialog.show(Istekler.this, "İşlem yapılıyor", "Lütfen bekleyiniz", true);


        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    Integer hata = jsonresponse.getInt("hata");


                    if (hata == 0)
                    {
                        progress.dismiss();
                        doldur();

                    }
                    else
                    {
                        progress.dismiss();

                        Alerter.create(Istekler.this)
                            .setTitle("Başarılı")
                            .setText("İptal edilenlere taşındı!")
                            .setIcon(R.drawable.ic_check_circle_black_24dp)
                            .setIconColorFilter(0)
                            .setBackgroundColorRes(R.color.colorPrimaryDark)
                            .show();

                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };


        IstekCevapJSON loginrequest = new IstekCevapJSON(secilen_istek_id,responselistener);
        RequestQueue queue = Volley.newRequestQueue(Istekler.this);
        queue.add(loginrequest);
    }

    void doldur()
    {

        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    Integer hata = jsonresponse.getInt("hata");

                    Log.i("yaz",response);

                    if (hata == 0)
                    {

                        JSONArray cek_baslik = jsonresponse.getJSONArray("basliklar");
                        JSONArray cek_kisi = jsonresponse.getJSONArray("kisiler");
                        JSONArray cek_durum = jsonresponse.getJSONArray("durumlar");
                        JSONArray cek_id = jsonresponse.getJSONArray("idler");

                        dizi_baslik = new String[cek_baslik.length()];
                        dizi_kisi = new String[cek_kisi.length()];
                        dizi_durum = new String[cek_durum.length()];
                        dizi_id = new String[cek_id.length()];

                        for (int i = 0; i < cek_id.length(); i++)
                        {
                            dizi_baslik[i] = cek_baslik.get(i).toString();
                            dizi_kisi[i] = cek_kisi.get(i).toString();
                            dizi_durum[i] = cek_durum.get(i).toString();
                            dizi_id[i] = cek_id.get(i).toString();
                        }

                        liste.setAdapter(new CustomIsteklerListeAdapter(Istekler.this,dizi_baslik,dizi_kisi,dizi_durum,dizi_id));
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };


        IsteklerJSON loginrequest = new IsteklerJSON(responselistener);
        RequestQueue queue = Volley.newRequestQueue(Istekler.this);
        queue.add(loginrequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.saga1,R.anim.saga2);
    }
}
