package com.sengelgrup.istakip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.sengelgrup.istakip.JSON.UyelerJSON;
import com.sengelgrup.istakip.JSON.YorumGonderJSON;
import com.sengelgrup.istakip.JSON.YorumlarJSON;
import com.tapadoo.alerter.Alerter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Yorumlar extends AppCompatActivity {


    String dizi_isimler[];
    String dizi_tarihler[];
    String dizi_yorumlar[];

    ListView yorum_liste;

    String kullanici_id;
    String talep_id;

    EditText txt_yorum;

    String yorum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yorumlar);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        kullanici_id = preferences.getString("kullanici_id", "N/A");

        Intent gel = getIntent();
        talep_id = gel.getStringExtra("secilen_talep_id");

        yorum_liste = findViewById(R.id.yorum_liste);

        txt_yorum = findViewById(R.id.txt_yorum);

        final Button yorum_at = findViewById(R.id.btn_yorum_gonder);

        yorum_at.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                yorum = txt_yorum.getText().toString();

                if (yorum.isEmpty())
                {

                    Alerter.create(Yorumlar.this)
                            .setTitle("İşlem Başarısız")
                            .setText("Alanlar boş geçilemez!")
                            .setIcon(R.drawable.ic_cancel_black_24dp)
                            .setIconColorFilter(0)
                            .setBackgroundColorRes(android.R.color.holo_red_light)
                            .show();
                }
                else
                {
                    gonder();
                    txt_yorum.setText("");
                }
            }
        });

        doldur();
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

                    if (hata == 0)
                    {

                        JSONArray cek_isim = jsonresponse.getJSONArray("isimler");
                        JSONArray cek_tarih = jsonresponse.getJSONArray("tarihler");
                        JSONArray cek_yorum = jsonresponse.getJSONArray("yorumlar");

                        dizi_isimler = new String[cek_isim.length()];
                        dizi_tarihler = new String[cek_tarih.length()];
                        dizi_yorumlar = new String[cek_yorum.length()];

                        for (int i = 0; i < cek_yorum.length(); i++)
                        {
                            dizi_isimler[i] = cek_isim.get(i).toString();
                            dizi_tarihler[i] = cek_tarih.get(i).toString();
                            dizi_yorumlar[i] = cek_yorum.get(i).toString();
                        }

                        yorum_liste.setAdapter(new CustomYorumListeAdapter(Yorumlar.this,dizi_yorumlar,dizi_tarihler,dizi_isimler));
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        YorumlarJSON loginrequest = new YorumlarJSON(talep_id,responselistener);
        RequestQueue queue = Volley.newRequestQueue(Yorumlar.this);
        queue.add(loginrequest);
    }

    void gonder()
    {
        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    Integer hata = jsonresponse.getInt("hata");

                    if (hata == 0)
                    {
                        doldur();

                        Alerter.create(Yorumlar.this)
                                .setTitle("Başarılı")
                                .setText("Yorum eklendi!")
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

        YorumGonderJSON loginrequest = new YorumGonderJSON(talep_id,kullanici_id,yorum,responselistener);
        RequestQueue queue = Volley.newRequestQueue(Yorumlar.this);
        queue.add(loginrequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.saga1,R.anim.saga2);
    }
}
