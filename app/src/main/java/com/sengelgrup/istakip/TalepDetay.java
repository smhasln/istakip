package com.sengelgrup.istakip;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.sengelgrup.istakip.JSON.IptalJSON;
import com.sengelgrup.istakip.JSON.TalepDetayJSON;
import com.sengelgrup.istakip.JSON.TalepDetaySorJSON;
import com.sengelgrup.istakip.JSON.TalepIslemJSON;
import com.sengelgrup.istakip.JSON.TaleplerJSON;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.squareup.picasso.Picasso;
import com.tapadoo.alerter.Alerter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import ss.com.bannerslider.Slider;

public class TalepDetay extends AppCompatActivity {

    String resim_dizi[];
    String bilgilendirilecekler[];
    String onaylayacaklar[];

    String kullanici_id;
    String menu_sorgu;
    String talep_id;

    TextView txt_aciklama;
    TextView txt_isim;
    TextView txt_tarih;

    TextView resim_sayi;
    ImageView resim;

    String kisi_durum = "2";

    ImageButton resim_ileri;
    ImageButton resim_geri;

    Integer resim_say = 0;

    String resim_url_bas = "http://213.142.145.51/plesk-site-preview/sengelgrup.com/android/resimler/";

    Spinner spinner1;
    Spinner spinner2;

    String yetki;
    String adsoyad;

    Button talep_islem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talep_detay);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        kullanici_id = preferences.getString("kullanici_id", "N/A");
        yetki = preferences.getString("yetki", "N/A");
        adsoyad = preferences.getString("adsoyad", "N/A");

        Intent git = getIntent();
        menu_sorgu = git.getStringExtra("secilen_menu");
        talep_id = git.getStringExtra("secilen_talep_id");

        talep_islem = findViewById(R.id.button_talep_detay_islem);

        button_sorgu();

        talep_islem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                islem_yap();

            }
        });

        ImageButton img_iptal = findViewById(R.id.img_iptal);

        img_iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mesaj_icerigi;
                // ADMIN
                if (yetki.equals("1"))
                {
                    mesaj_icerigi = "Talebi iptal edilenlere taşı.";
                }
                // KULLANICI
                else
                {
                    mesaj_icerigi = "Yöneticiye iptal isteği gönder.";
                }

                AlertDialog.Builder builder1 = new AlertDialog.Builder(TalepDetay.this);
                builder1.setMessage(mesaj_icerigi);
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Tamam",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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

        ImageButton img_yorum = findViewById(R.id.img_yorum);

        img_yorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent git = new Intent(TalepDetay.this,Yorumlar.class);
                git.putExtra("secilen_talep_id",talep_id);
                startActivity(git);

                overridePendingTransition(R.anim.sola1,R.anim.sola2);
            }
        });

        spinner1 = findViewById(R.id.spinner_1);
        spinner2 = findViewById(R.id.spinner_2);

        resim_ileri = findViewById(R.id.resim_ileri);
        resim_geri = findViewById(R.id.resim_geri);

        resim_ileri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (resim_say < resim_dizi.length-1)
                {
                    resim_say++;
                }
                Picasso.get().load(resim_dizi[resim_say]).error(R.drawable.ic_error_outline_black_24dp).into(resim);
                resim_sayi.setText((resim_say + 1) + "/" + resim_dizi.length);
            }
        });

        resim_geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (resim_say > 0)
                {
                    resim_say--;
                }
                Picasso.get().load(resim_dizi[resim_say]).error(R.drawable.ic_error_outline_black_24dp).into(resim);
                resim_sayi.setText((resim_say + 1) + "/" + resim_dizi.length);
            }
        });

        resim_sayi = findViewById(R.id.talepdetayresimsay);

        resim = findViewById(R.id.talepdetayresim);

        resim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent git = new Intent(TalepDetay.this,TalepDetayFoto.class);
                git.putExtra("tam_resim",resim_dizi[resim_say]);
                startActivity(git);

                overridePendingTransition(R.anim.sola1,R.anim.sola2);
            }
        });

        txt_aciklama = findViewById(R.id.talepdetayaciklama);
        txt_isim = findViewById(R.id.talepdetayisim);
        txt_tarih = findViewById(R.id.talepdetaytarih);

        doldur();

        button_sorgu();

    }

    void button_sorgu()
    {

        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    Integer hata = jsonresponse.getInt("hata");

                    if (hata == 1)
                    {
                        talep_islem.setVisibility(View.GONE);
                    }
                    else
                    {
                        talep_islem.setVisibility(View.VISIBLE);
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        TalepDetaySorJSON loginrequest = new TalepDetaySorJSON(talep_id,kullanici_id,responselistener);
        RequestQueue queue = Volley.newRequestQueue(TalepDetay.this);
        queue.add(loginrequest);
    }

    void iptal_gonder()
    {

        final ProgressDialog progress = ProgressDialog.show(TalepDetay.this, "İşlem yapılıyor", "Lütfen bekleyiniz", true);
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

                        Alerter.create(TalepDetay.this)
                                .setTitle("Uyarı")
                                .setText("İşlemi zaten gerçekleştirdiniz.")
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

        IptalJSON loginrequest = new IptalJSON(adsoyad,talep_id,yetki,responselistener);
        RequestQueue queue = Volley.newRequestQueue(TalepDetay.this);
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

                    if (hata == 0)
                    {
                        JSONArray cek_onaylayacak = jsonresponse.getJSONArray("onay");
                        JSONArray cek_onayladimi = jsonresponse.getJSONArray("onayladilarmi");
                        JSONArray cek_bilgilendirilecek = jsonresponse.getJSONArray("bilgi");
                        JSONArray cek_bilgilendimi = jsonresponse.getJSONArray("bilgilendimi");
                        JSONArray cek_resim = jsonresponse.getJSONArray("resim");

                        if (cek_onaylayacak.length() < 0 )
                        {
                            spinner1.setVisibility(View.GONE);
                        }
                        if (cek_bilgilendirilecek.length() < 0 )
                        {
                            spinner2.setVisibility(View.GONE);
                        }

                        kisi_durum = jsonresponse.getString("kisi_durum");

                        if (kisi_durum.equals("yok")) // İLGİLİ OLMAYAN KİŞİLER
                        {
                            talep_islem.setVisibility(View.INVISIBLE);
                        }
                        else if (kisi_durum.equals("0")) // ONAYLAYACAKLAR
                        {
                            talep_islem.setText("ONAYLADIM");
                        }
                        else if (kisi_durum.equals("1")) // BİLGİLENDİRİLECEKLER
                        {
                            talep_islem.setText("BİLGİLENDİRİLDİM");
                        }

                        String cek_aciklama = jsonresponse.getString("aciklama");
                        String cek_isim = jsonresponse.getString("ad");
                        String cek_tarih = jsonresponse.getString("tarih");

                        txt_aciklama.setText(cek_aciklama);
                        txt_isim.setText(cek_isim);
                        txt_tarih.setText(cek_tarih);

                        onaylayacaklar = new String[cek_onaylayacak.length()];
                        bilgilendirilecekler = new String[cek_bilgilendirilecek.length()];
                        resim_dizi = new String[cek_resim.length()];


                        for (int i = 0; i < cek_resim.length(); i++)
                        {
                            resim_dizi[i] = resim_url_bas + cek_resim.get(i).toString() + ".jpg";
                        }
                        for (int k = 0; k < cek_onaylayacak.length(); k++)
                        {
                            onaylayacaklar[k] = cek_onaylayacak.get(k).toString() + " - " + cek_onayladimi.get(k).toString();

                        }
                        for (int m = 0; m < cek_bilgilendirilecek.length(); m++)
                        {
                            bilgilendirilecekler[m] = cek_bilgilendirilecek.get(m).toString() + " - " + cek_bilgilendimi.get(m).toString();
                        }

                        Picasso.get().load(resim_dizi[0]).error(R.drawable.ic_error_outline_black_24dp).into(resim);
                        resim_sayi.setText("1/"+resim_dizi.length);

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                (TalepDetay.this, R.layout.custom_talep_spinner,
                                        onaylayacaklar); //selected item will look like a spinner set from XML
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        spinner1.setAdapter(spinnerArrayAdapter);


                        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>
                                (TalepDetay.this, R.layout.custom_talep_spinner,
                                        bilgilendirilecekler); //selected item will look like a spinner set from XML
                        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        spinner2.setAdapter(spinnerArrayAdapter2);
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        TalepDetayJSON loginrequest = new TalepDetayJSON(talep_id,kullanici_id,responselistener);
        RequestQueue queue = Volley.newRequestQueue(TalepDetay.this);
        queue.add(loginrequest);

    }


    void islem_yap()
    {

        final ProgressDialog progress = ProgressDialog.show(TalepDetay.this, "İşlem yapılıyor", "Lütfen bekleyiniz", true);
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


                        talep_islem.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        progress.dismiss();

                        Alerter.create(TalepDetay.this)
                                .setTitle("Uyarı")
                                .setText("İşlemi zaten gerçekleştirdiniz.")
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

        TalepIslemJSON loginrequest = new TalepIslemJSON(talep_id,kullanici_id,kisi_durum,responselistener);
        RequestQueue queue = Volley.newRequestQueue(TalepDetay.this);
        queue.add(loginrequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.saga1,R.anim.saga2);
    }
}


