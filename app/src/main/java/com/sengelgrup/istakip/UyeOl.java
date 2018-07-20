package com.sengelgrup.istakip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.sengelgrup.istakip.JSON.GirisJSON;
import com.sengelgrup.istakip.JSON.UyeOlJSON;
import com.tapadoo.alerter.Alerter;

import org.json.JSONException;
import org.json.JSONObject;

public class UyeOl extends AppCompatActivity {

    String kadi;
    String sifre;
    String email;
    String adsoyad;

    String token;

    @Override
    protected void onStart() {
        super.onStart();

        EditText txt_uye_adsoyad = findViewById(R.id.txt_adsoyad);
        txt_uye_adsoyad.requestFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uye_ol);


        SharedPreferences token_at = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = token_at.getString("token", "N/A");

        final EditText txt_uye_adsoyad = findViewById(R.id.txt_adsoyad);
        final EditText txt_uye_kadi = findViewById(R.id.txt_uye_kadi);
        final EditText txt_uye_sifre = findViewById(R.id.txt_uye_sifre);
        final EditText txt_uye_email = findViewById(R.id.txt_uye_email);

        Button btn_uye_olustur = findViewById(R.id.btn_uye_tamam);

        ImageButton geri = findViewById(R.id.uye_ol_geri);

        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UyeOl.this,GirisYap.class));
                overridePendingTransition(R.anim.saga1,R.anim.saga2);
            }
        });

        btn_uye_olustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kadi = txt_uye_kadi.getText().toString();
                sifre = txt_uye_sifre.getText().toString();
                email = txt_uye_email.getText().toString();
                adsoyad = txt_uye_adsoyad.getText().toString();

                if (adsoyad.isEmpty() || kadi.isEmpty() || sifre.isEmpty() || email.isEmpty())
                {
                    Alerter.create(UyeOl.this)
                            .setTitle("Giriş Başarısız")
                            .setText("Boş geçilemez!")
                            .setIcon(R.drawable.ic_cancel_black_24dp)
                            .setIconColorFilter(0)
                            .setBackgroundColorRes(android.R.color.holo_red_light)
                            .show();
                }
                else
                {
                    uye_ol();
                }
            }
        });
    }

    void uye_ol()
    {

        final ProgressDialog progress = ProgressDialog.show(UyeOl.this, "Oturum Açma", "Giriş Yapılıyor...", true);

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

                        Intent git = new Intent(UyeOl.this,GirisYap.class);
                        startActivity(git);


                    }
                    else
                    {
                        progress.dismiss();

                        Alerter.create(UyeOl.this)
                                .setTitle("İşlem başarısız")
                                .setText("Sistemde kullanıcı mevcut")
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

        UyeOlJSON loginrequest = new UyeOlJSON(kadi,sifre,email,adsoyad,token,responselistener);
        RequestQueue queue = Volley.newRequestQueue(UyeOl.this);
        queue.add(loginrequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(UyeOl.this,GirisYap.class));
        overridePendingTransition(R.anim.saga1,R.anim.saga2);
    }
}
