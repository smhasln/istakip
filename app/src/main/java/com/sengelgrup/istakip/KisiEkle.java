package com.sengelgrup.istakip;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.sengelgrup.istakip.JSON.TalepOlusturBilgiJSON;
import com.sengelgrup.istakip.JSON.TalepOlusturJSON;
import com.tapadoo.alerter.Alerter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KisiEkle extends AppCompatActivity {

    ArrayList<String> ana_kisiler = new ArrayList<>();
    ArrayList<String> ana_idler = new ArrayList<>();

    ArrayList<String> secilen_onay_idler = new ArrayList<>();
    ArrayList<String> secilen_bilgi_idler = new ArrayList<>();

    ListView liste;

    ArrayAdapter<String> arrayAdapter;

    String baslik;
    String aciklama;
    String kullanici_id;
    String gonderilecek_resim_kodu;

    String onay_idleri = "";
    String bilgi_idleri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisi_ekle);

        final Intent gonder = getIntent();
        baslik = gonder.getStringExtra("baslik");
        aciklama = gonder.getStringExtra("aciklama");
        kullanici_id = gonder.getStringExtra("kullanici_id");
        gonderilecek_resim_kodu = gonder.getStringExtra("gonderilecek_resim_kodu");

        liste = findViewById(R.id.kisi_listesi);

        listele();

        final TextView txt_onay = findViewById(R.id.txt_onaylar);
        final TextView txt_bilgi = findViewById(R.id.txt_bilgiler);

        Button kisi_sec = findViewById(R.id.btn_kisi_secimi_yap);

        kisi_sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < secilen_onay_idler.size(); i++)
                {
                    onay_idleri = onay_idleri + secilen_onay_idler.get(i) + ",";
                }
                for (int k = 0; k < secilen_bilgi_idler.size(); k++)
                {
                    bilgi_idleri = bilgi_idleri + secilen_bilgi_idler.get(k) + ",";
                }

                if(onay_idleri.equals(""))
                {
                    Alerter.create(KisiEkle.this)
                            .setTitle("Hata")
                            .setText("Onaylayacak kişi seçmek zorunludur.")
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


        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(KisiEkle.this);
                builder1.setMessage(ana_kisiler.get(position));
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Onaylayacak",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                txt_onay.setText(txt_onay.getText() + ana_kisiler.get(position) + ", ");
                                secilen_onay_idler.add(ana_idler.get(position));

                                ana_kisiler.remove(position);
                                ana_idler.remove(position);

                                arrayAdapter.notifyDataSetChanged();

                            }
                        });

                builder1.setNegativeButton(
                        "Bilgilenecek",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                txt_bilgi.setText(txt_bilgi.getText() + ana_kisiler.get(position) + ", ");
                                secilen_bilgi_idler.add(ana_idler.get(position));

                                ana_kisiler.remove(position);
                                ana_idler.remove(position);

                                arrayAdapter.notifyDataSetChanged();

                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();



            }
        });
    }


    void listele()
    {

        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    JSONArray cek_isim = jsonresponse.getJSONArray("isimler");
                    JSONArray cek_id = jsonresponse.getJSONArray("idler");

                    for (int i = 0; i < cek_id.length(); i++)
                    {
                        ana_kisiler.add(i,cek_isim.get(i).toString());
                        ana_idler.add(i,cek_id.get(i).toString());
                    }

                    arrayAdapter = new ArrayAdapter<String>(KisiEkle.this, android.R.layout.simple_list_item_1,ana_kisiler);

                    liste.setAdapter(arrayAdapter);


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };


        TalepOlusturBilgiJSON loginrequest = new TalepOlusturBilgiJSON(kullanici_id,responselistener);
        RequestQueue queue = Volley.newRequestQueue(KisiEkle.this);
        queue.add(loginrequest);
    }


    void gonder()
    {

        final ProgressDialog progress = ProgressDialog.show(KisiEkle.this, "Ekleme işlemi", "Yükleniyor...", true);

        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try
                {
                    JSONObject jsonresponse = new JSONObject(response);
                    Integer cek_hata = jsonresponse.getInt("hata");

                    if(cek_hata == 0)
                    {
                        progress.dismiss();
                        Intent git = new Intent(KisiEkle.this, AnaMenu.class);
                        startActivity(git);
                    }
                    else
                    {
                        progress.dismiss();
                        Alerter.create(KisiEkle.this)
                                .setTitle("İşlem Başarısız")
                                .setText("Daha sonra tekrar deneyin.")
                                .setIcon(R.drawable.ic_cancel_black_24dp)
                                .setIconColorFilter(0)
                                .setBackgroundColorRes(android.R.color.holo_red_light)
                                .show();
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        };

        TalepOlusturJSON loginrequest = new TalepOlusturJSON(kullanici_id,baslik,aciklama,onay_idleri,
                bilgi_idleri,gonderilecek_resim_kodu,responselistener);
        RequestQueue queue = Volley.newRequestQueue(KisiEkle.this);
        queue.add(loginrequest);

    }

}
