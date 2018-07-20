package com.sengelgrup.istakip;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.sengelgrup.istakip.JSON.BildirimGosterJSON;
import com.sengelgrup.istakip.JSON.BildirimOkuJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Bildirimlerim extends AppCompatActivity {

    String basliklar[];
    String icerikler[];
    String tarihler[];
    String idler[];
    String durumlar[];

    ListView liste;

    String secilen_bildirim_id;
    String k_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bildirimlerim);

        liste = findViewById(R.id.bildirim_listesi);
        liste.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        k_id = preferences.getString("kullanici_id", "N/A");


        ImageButton btn_anasayfa = findViewById(R.id.bildirim_anasayfa);
        ImageButton btn_geri = findViewById(R.id.bildirim_geri);

        btn_anasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Bildirimlerim.this,AnaMenu.class));
                overridePendingTransition(R.anim.saga1, R.anim.saga2);
            }
        });

        btn_geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Bildirimlerim.this,AnaMenu.class));
                overridePendingTransition(R.anim.saga1, R.anim.saga2);
            }
        });

        doldur();

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                String secilen_durum = durumlar[position];

                // OKUNMAMIŞ BİLDİRİM
                if(secilen_durum.equals("1"))
                {
                    secilen_bildirim_id = idler[position];

                    oku();

                    doldur();
                }

            }
        });


    }

    void oku()
    {

        // JSON işlemlerinden dönen success değeri kontrol edilir.
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };

        // Login işlemleri için volley kütüphanesi kullanılır.
        BildirimOkuJSON loginrequest_bildirim = new BildirimOkuJSON(secilen_bildirim_id, listener);
        RequestQueue queue_bildirim = Volley.newRequestQueue(Bildirimlerim.this);
        queue_bildirim.add(loginrequest_bildirim);

    }

    void doldur()
    {
        // JSON işlemlerinden dönen success değeri kontrol edilir.
        Response.Listener<String> responselistener_bildirim = new Response.Listener<String>() {
            @Override
            public void onResponse(String response_bildirim) {
                try
                {
                    JSONObject jsonresponse_bildirim = new JSONObject(response_bildirim);

                        final JSONArray cek_baslik = jsonresponse_bildirim.getJSONArray("baslik");
                        final JSONArray cek_icerik = jsonresponse_bildirim.getJSONArray("icerik");
                        final JSONArray cek_tarih = jsonresponse_bildirim.getJSONArray("tarih");
                        final JSONArray cek_durum = jsonresponse_bildirim.getJSONArray("durum");
                        final JSONArray cek_id = jsonresponse_bildirim.getJSONArray("id");

                        basliklar = new String[cek_baslik.length()];
                        icerikler = new String[cek_icerik.length()];
                        tarihler = new String[cek_tarih.length()];
                        durumlar = new String[cek_durum.length()];
                        idler = new String[cek_id.length()];

                        for (int i = 0; i < basliklar.length; i++)
                        {
                            basliklar[i] = cek_baslik.get(i).toString();
                            icerikler[i] = cek_icerik.get(i).toString();
                            tarihler[i] = cek_tarih.get(i).toString();
                            durumlar[i] = cek_durum.get(i).toString();
                            idler[i] = cek_id.get(i).toString();

                        }

                    liste.setAdapter(new CustomBildirimListeAdapter(Bildirimlerim.this,basliklar,icerikler,durumlar));

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };

        // Login işlemleri için volley kütüphanesi kullanılır.
        BildirimGosterJSON loginrequest_bildirim = new BildirimGosterJSON(k_id, responselistener_bildirim);
        RequestQueue queue_bildirim = Volley.newRequestQueue(Bildirimlerim.this);
        queue_bildirim.add(loginrequest_bildirim);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.saga1, R.anim.saga2);
    }

}
