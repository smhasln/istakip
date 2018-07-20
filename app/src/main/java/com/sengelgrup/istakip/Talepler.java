package com.sengelgrup.istakip;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.sengelgrup.istakip.JSON.MailGonderJSON;
import com.sengelgrup.istakip.JSON.TaleplerJSON;
import com.sengelgrup.istakip.JSON.UyelerJSON;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.tapadoo.alerter.Alerter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class Talepler extends AppCompatActivity {


    private String[] islemler = {
            "Talep detayına git",
            "Mail gönder"
    };

    String dizi_id[];
    String dizi_baslik[];
    String dizi_tarih[];

    ListView liste;

    String sorgu;

    String kullanici_id;

    String kullanicilar[];
    String idler[];

    String secilen_kisi_id;
    String secilen_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talepler);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        kullanici_id = preferences.getString("kullanici_id", "N/A");

        Intent gel = getIntent();
        sorgu = gel.getStringExtra("secilen_menu");

        TextView txt_baslik = findViewById(R.id.txt_talepler_title);


        if (sorgu.equals("1"))
        {
            txt_baslik.setText("ONAY BEKLEYENLER");
        }
        else if (sorgu.equals("2"))
        {
            txt_baslik.setText("ONAYLANANLAR");
        }
        else if (sorgu.equals("3"))
        {
            txt_baslik.setText("İPTAL EDİLENLER");
        }
        else if (sorgu.equals("4"))
        {
            txt_baslik.setText("ONAYIMI BEKLEYENLER");
        }
        else if (sorgu.equals("5"))
        {
            txt_baslik.setText("ONAYLADIKLARIM");
        }
        else if (sorgu.equals("6"))
        {
            txt_baslik.setText("OLUŞTURDUKLARIM");
        }


        ImageButton geri = findViewById(R.id.talepler_geri);
        ImageButton anasayfa = findViewById(R.id.talepler_anasayfa);

        liste = findViewById(R.id.talepler_liste);

        doldur();

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                secilen_id = dizi_id[position];
                Log.i("yaz",secilen_id);

                // Filtrele
                final AlertDialog.Builder builder = new AlertDialog.Builder(Talepler.this);
                View mView = getLayoutInflater().inflate(R.layout.callspinner, null);
                builder.setTitle("Talep işlemleri");

                final Spinner spinner = (Spinner) mView.findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Talepler.this, android.R.layout.simple_spinner_dropdown_item, islemler);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);


                //Hangi opsiyon seçildiyse tespit edilir işlemler yapılır.
                builder.setPositiveButton("Seç", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String secilen = islemler[spinner.getSelectedItemPosition()].toString();
                        if(secilen.equals("Talep detayına git"))
                        {

                            Intent git = new Intent(Talepler.this,TalepDetay.class);
                            git.putExtra("secilen_talep_id",secilen_id);
                            git.putExtra("secilen_menu",sorgu);
                            startActivity(git);

                            overridePendingTransition(R.anim.sola1,R.anim.sola2);

                        }
                        else if (secilen.equals("Mail gönder"))
                        {
                            mail_gonder();
                        }

                    }
                });
                builder.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Dialog penceresi oluşturulur.
                builder.setView(mView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Talepler.this,AnaMenu.class));
                overridePendingTransition(R.anim.saga1,R.anim.saga2);
            }
        });

        anasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Talepler.this,AnaMenu.class));
                overridePendingTransition(R.anim.saga1,R.anim.saga2);
            }
        });
    }


    void mail_gonder()
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
                        JSONArray cek_id = jsonresponse.getJSONArray("idler");

                        kullanicilar = new String[cek_isim.length()];
                        idler = new String[cek_id.length()];

                        for (int i = 0; i < cek_isim.length(); i++)
                        {
                            kullanicilar[i] = cek_isim.get(i).toString();
                            idler[i] = cek_id.get(i).toString();
                        }


                        // Filtrele
                        final AlertDialog.Builder builder = new AlertDialog.Builder(Talepler.this);
                        View mView = getLayoutInflater().inflate(R.layout.callspinner, null);
                        builder.setTitle("Bir kişiye mail gönder");

                        final Spinner spinner = (Spinner) mView.findViewById(R.id.spinner);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Talepler.this, android.R.layout.simple_spinner_dropdown_item, kullanicilar);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);


                        //Hangi opsiyon seçildiyse tespit edilir işlemler yapılır.
                        builder.setPositiveButton("Gönder", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                secilen_kisi_id = idler[spinner.getSelectedItemPosition()];

                                gonder();
                            }
                        });
                        builder.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        // Dialog penceresi oluşturulur.
                        builder.setView(mView);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        UyelerJSON loginrequest = new UyelerJSON("2",responselistener);
        RequestQueue queue = Volley.newRequestQueue(Talepler.this);
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

                    Alerter.create(Talepler.this)
                            .setTitle("Başarılı")
                            .setText("Mail gönderildi!")
                            .setIcon(R.drawable.ic_check_circle_black_24dp)
                            .setIconColorFilter(0)
                            .setBackgroundColorRes(R.color.colorPrimaryDark)
                            .show();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        MailGonderJSON loginrequest = new MailGonderJSON(secilen_kisi_id,secilen_id,responselistener);
        RequestQueue queue = Volley.newRequestQueue(Talepler.this);
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

                        JSONArray cek_baslik = jsonresponse.getJSONArray("baslik");
                        JSONArray cek_tarih = jsonresponse.getJSONArray("tarih");
                        JSONArray cek_id = jsonresponse.getJSONArray("id");

                        dizi_baslik = new String[cek_baslik.length()];
                        dizi_tarih = new String[cek_tarih.length()];
                        dizi_id = new String[cek_id.length()];

                        for (int i = 0; i < cek_tarih.length(); i++)
                        {
                            dizi_baslik[i] = cek_baslik.get(i).toString();
                            dizi_tarih[i] = cek_tarih.get(i).toString();
                            dizi_id[i] = cek_id.get(i).toString();
                        }

                        liste.setAdapter(new CustomTaleplerListeAdapter(Talepler.this,dizi_baslik,dizi_tarih,dizi_id));
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };


        TaleplerJSON loginrequest = new TaleplerJSON(sorgu,kullanici_id,responselistener);
        RequestQueue queue = Volley.newRequestQueue(Talepler.this);
        queue.add(loginrequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.saga1,R.anim.saga2);
    }
}
