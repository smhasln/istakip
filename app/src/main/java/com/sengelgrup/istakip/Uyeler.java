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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.sengelgrup.istakip.JSON.UyeAtamaJSON;
import com.sengelgrup.istakip.JSON.UyelerJSON;
import com.tapadoo.alerter.Alerter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Uyeler extends AppCompatActivity {

    ListView liste;

    private String[] filtre_dizi = {
            "Aktif Üyeler",
            "Pasif Üyeler",
            "Tümü"};

    private String[] islemler = {
            "Bildirim gönder",
            "Aktif üye yap",
            "Pasif üye yap",
            "Yönetici yetkisi ver",
            "Yönetici yetkisini geri al"
    };

    String dizi_isimler[];
    String dizi_kadiler[];
    String dizi_sifreler[];
    String dizi_durumlar[];
    String dizi_idler[];

    String tutulan_isim;
    String tutulan_id;
    String onay_durum;
    String deger = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uyeler);

        liste = findViewById(R.id.uye_listesi);

        doldur();

        ImageButton geri = findViewById(R.id.uyeler_geri);
        ImageButton anasayfa = findViewById(R.id.uye_anasayfa);

        final ImageButton filtre = findViewById(R.id.img_btn_filtrele);

        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Uyeler.this,AnaMenu.class));
                overridePendingTransition(R.anim.saga1,R.anim.saga2);
            }
        });

        anasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Uyeler.this,AnaMenu.class));
                overridePendingTransition(R.anim.saga1,R.anim.saga2);
            }
        });

        filtre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Filtrele
                final AlertDialog.Builder builder = new AlertDialog.Builder(Uyeler.this);
                View mView = getLayoutInflater().inflate(R.layout.callspinner, null);
                builder.setTitle("Filtrele");

                final Spinner spinner = (Spinner) mView.findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Uyeler.this, android.R.layout.simple_spinner_dropdown_item, filtre_dizi);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                //Hangi opsiyon seçildiyse tespit edilir işlemler yapılır.
                builder.setPositiveButton("Seç", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String secilen = filtre_dizi[spinner.getSelectedItemPosition()].toString();

                        if(secilen.equals("Aktif Üyeler"))
                        {
                            deger = "2";
                        }
                        else if (secilen.equals("Pasif Üyeler"))
                        {
                            deger = "1";
                        }
                        else if (secilen.equals("Tümü"))
                        {
                            deger = "0";
                        }
                        doldur();

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

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                tutulan_isim = dizi_isimler[position];
                tutulan_id = dizi_idler[position];


                // Filtrele
                final AlertDialog.Builder builder = new AlertDialog.Builder(Uyeler.this);
                View mView = getLayoutInflater().inflate(R.layout.callspinner, null);
                builder.setTitle("Üye İşlemleri");

                final Spinner spinner = (Spinner) mView.findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Uyeler.this, android.R.layout.simple_spinner_dropdown_item, islemler);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                //Hangi opsiyon seçildiyse tespit edilir işlemler yapılır.
                builder.setPositiveButton("Seç", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String secilen = islemler[spinner.getSelectedItemPosition()].toString();
                        if(secilen.equals("Bildirim gönder"))
                        {
                            Intent git = new Intent(Uyeler.this,BildirimGonder.class);
                            git.putExtra("secilen_isim",tutulan_isim);
                            git.putExtra("secilen_id",tutulan_id);
                            startActivity(git);
                            overridePendingTransition(R.anim.sola1,R.anim.sola2);
                        }
                        else if (secilen.equals("Aktif üye yap"))
                        {
                            onay_durum = "1";
                            atama_yap();
                        }
                        else if (secilen.equals("Pasif üye yap"))
                        {
                            onay_durum = "0";
                            atama_yap();
                        }
                        else if (secilen.equals("Yönetici yetkisi ver"))
                        {
                            onay_durum = "3";
                            atama_yap();
                        }
                        else if (secilen.equals("Yönetici yetkisini geri al"))
                        {
                            onay_durum = "4";
                            atama_yap();
                        }

                        doldur();

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
    }

    void atama_yap()
    {
        final ProgressDialog progress = ProgressDialog.show(Uyeler.this, "İşleniyor", "Lütfen bekleyin...", true);

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

                        Alerter.create(Uyeler.this)
                                .setTitle("İşlem başarısız")
                                .setText("Lütfen daha sonra tekrar deneyin.")
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

        UyeAtamaJSON loginrequest = new UyeAtamaJSON(onay_durum,tutulan_id,responselistener);
        RequestQueue queue = Volley.newRequestQueue(Uyeler.this);
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

                        JSONArray cek_isim = jsonresponse.getJSONArray("isimler");
                        JSONArray cek_kadi = jsonresponse.getJSONArray("kadiler");
                        JSONArray cek_sifre = jsonresponse.getJSONArray("sifreler");
                        JSONArray cek_durum = jsonresponse.getJSONArray("durumlar");
                        JSONArray cek_id = jsonresponse.getJSONArray("idler");

                        dizi_isimler = new String[cek_isim.length()];
                        dizi_kadiler = new String[cek_kadi.length()];
                        dizi_sifreler = new String[cek_sifre.length()];
                        dizi_durumlar = new String[cek_durum.length()];
                        dizi_idler = new String[cek_id.length()];

                        for (int i = 0; i < cek_sifre.length(); i++)
                        {
                            dizi_isimler[i] = cek_isim.get(i).toString();
                            dizi_kadiler[i] = cek_kadi.get(i).toString();
                            dizi_sifreler[i] = cek_sifre.get(i).toString();
                            dizi_durumlar[i] = cek_durum.get(i).toString();
                            dizi_idler[i] = cek_id.get(i).toString();
                        }

                        liste.setAdapter(new CustomUyeListeAdapter(Uyeler.this,dizi_isimler,dizi_kadiler,dizi_sifreler,dizi_durumlar));
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        UyelerJSON loginrequest = new UyelerJSON(deger,responselistener);
        RequestQueue queue = Volley.newRequestQueue(Uyeler.this);
        queue.add(loginrequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Uyeler.this,AnaMenu.class));
        overridePendingTransition(R.anim.saga1,R.anim.saga2);
    }
}
