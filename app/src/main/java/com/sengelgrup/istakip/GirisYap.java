package com.sengelgrup.istakip;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.sengelgrup.istakip.JSON.GirisJSON;
import com.tapadoo.alerter.Alerter;

import org.json.JSONException;
import org.json.JSONObject;

public class GirisYap extends AppCompatActivity {


    String kadi;
    String sifre;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_yap);

        SharedPreferences token_at = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = token_at.getString("token", "N/A");

        final EditText txt_kadi = findViewById(R.id.txt_kadi);
        final EditText txt_sifre = findViewById(R.id.txt_sifre);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        txt_kadi.setText(preferences.getString("kadi", ""));
        txt_sifre.setText(preferences.getString("pw", ""));

        ImageButton btn_hakkinda = findViewById(R.id.hakkinda);

        btn_hakkinda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(GirisYap.this);
                builder1.setTitle("Hakkımızda");
                builder1.setMessage("Bu uygulama Sengel Grup'ta iş akışını hızlandırmak için Tekmar Selçuk tarafından geliştirilmiştir.\nTüm hakları saklıdır.\nUygulamada yaşanan sorunlar için\n0535 597 01 95\nnumaralı telefondan bize ulaşabilirsiniz.");
                builder1.setCancelable(true);

                builder1.setNegativeButton(
                        "Kapat",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });


        ImageButton btn_uye = findViewById(R.id.imgbtn_uye);

        Button oturum_ac = findViewById(R.id.btn_giris);


        btn_uye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GirisYap.this,UyeOl.class));
                overridePendingTransition(R.anim.sola1, R.anim.sola2);
            }
        });

        oturum_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kadi = txt_kadi.getText().toString();
                sifre = txt_sifre.getText().toString();

                if (kadi.isEmpty() || sifre.isEmpty())
                {
                    Alerter.create(GirisYap.this)
                            .setTitle("Giriş Başarısız")
                            .setText("Boş geçilemez!")
                            .setIcon(R.drawable.ic_cancel_black_24dp)
                            .setIconColorFilter(0)
                            .setBackgroundColorRes(android.R.color.holo_red_light)
                            .show();
                }
                else
                {

                    giris_yap();
                }

            }
        });
    }

    void giris_yap()
    {

        final ProgressDialog progress = ProgressDialog.show(GirisYap.this, "Oturum Açma", "Giriş Yapılıyor...", true);

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

                        String cek_id = jsonresponse.getString("id");
                        String cek_yetki = jsonresponse.getString("yetki");
                        String cek_adsoyad = jsonresponse.getString("adsoyad");

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString("kadi", kadi);
                        editor.putString("pw", sifre);
                        editor.putString("adsoyad",cek_adsoyad);
                        editor.putString("kullanici_id", cek_id);
                        editor.putString("yetki", cek_yetki);

                        editor.commit();

                        Intent git = new Intent(GirisYap.this,AnaMenu.class);
                        startActivity(git);
                        overridePendingTransition(R.anim.sola1, R.anim.sola2);

                    }
                    else
                    {
                        progress.dismiss();

                        Alerter.create(GirisYap.this)
                                .setTitle("Giriş Başarısız")
                                .setText("Kullanıcı adı veya şifre hatalı!")
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

        GirisJSON loginrequest = new GirisJSON(kadi,sifre,token,responselistener);
        RequestQueue queue = Volley.newRequestQueue(GirisYap.this);
        queue.add(loginrequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);

    }
}
