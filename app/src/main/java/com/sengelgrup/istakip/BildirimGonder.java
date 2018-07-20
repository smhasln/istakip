package com.sengelgrup.istakip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.sengelgrup.istakip.JSON.BildirimJSON;
import com.sengelgrup.istakip.JSON.TalepOlusturBilgiJSON;
import com.tapadoo.alerter.Alerter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BildirimGonder extends AppCompatActivity {

    String adsoyad;
    String id;


    String baslik;
    String icerik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bildirim_gonder);

        Intent git = getIntent();
        adsoyad = git.getStringExtra("secilen_isim");
        id = git.getStringExtra("secilen_id");

        TextView isim = findViewById(R.id.txt_bildirim_adsoyad);
        isim.setText(adsoyad);

        final EditText txt_baslik = findViewById(R.id.txt_bildirim_baslik);
        final EditText txt_aciklama = findViewById(R.id.txt_bildirim_aciklama);

        ImageButton geri = findViewById(R.id.bildirim_gonder_geri);
        Button btn_tamam = findViewById(R.id.btn_bildirim_gonder);

        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BildirimGonder.this,Uyeler.class));
                overridePendingTransition(R.anim.saga1,R.anim.saga2);
            }
        });

        btn_tamam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                baslik = txt_baslik.getText().toString();
                icerik = txt_aciklama.getText().toString();

                if (baslik.isEmpty() || icerik.isEmpty())
                {
                    Alerter.create(BildirimGonder.this)
                            .setTitle("Hata")
                            .setText("Başlık veya içerik boş geçilemez!")
                            .setIcon(R.drawable.ic_cancel_black_24dp)
                            .setIconColorFilter(0)
                            .setBackgroundColorRes(android.R.color.holo_red_light)
                            .show();
                }
                else
                {
                    gonder();
                }
            }
        });


    }

    void gonder()
    {

        final ProgressDialog progress = ProgressDialog.show(BildirimGonder.this, "Ekleme işlemi", "Yükleniyor...", true);

        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    Integer cek_hata = jsonresponse.getInt("hata");

                    if (cek_hata == 0)
                    {
                        progress.dismiss();
                        startActivity(new Intent(BildirimGonder.this,Uyeler.class));
                    }
                    else
                    {
                        progress.dismiss();

                        Alerter.create(BildirimGonder.this)
                                .setTitle("İşlem Başarısız")
                                .setText("Daha sonra tekrar deneyin.")
                                .setIcon(R.drawable.ic_cancel_black_24dp)
                                .setIconColorFilter(0)
                                .setBackgroundColorRes(android.R.color.holo_red_light)
                                .show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        BildirimJSON loginrequest = new BildirimJSON(id,baslik,icerik,responselistener);

        RequestQueue queue = Volley.newRequestQueue(BildirimGonder.this);
        queue.add(loginrequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(BildirimGonder.this,Uyeler.class));
        overridePendingTransition(R.anim.saga1,R.anim.saga2);
    }
}
