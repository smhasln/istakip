package com.sengelgrup.istakip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.sengelgrup.istakip.JSON.AnaMenuJSON;
import com.tapadoo.alerter.Alerter;

import org.json.JSONException;
import org.json.JSONObject;

public class AnaMenu extends AppCompatActivity {


    String kullanici_id;
    String yetki;

    String secilen;

    TextView bildirim_kontrol;

    String s1;
    String s2;
    String s3;
    String s4;
    String s5;
    String s6;

    Button btn_onay_bekleyen;
    Button btn_onaylanan;
    Button btn_iptal_edilen;
    Button btn_benden_onay;
    Button btn_benim_onayladiklarim;
    Button btn_kendi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_menu);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        kullanici_id = preferences.getString("kullanici_id", "N/A");
        yetki = preferences.getString("yetki", "N/A");

        Alerter.create(AnaMenu.this)
                .setTitle("Hoşgeldiniz")
                .setText(preferences.getString("adsoyad", "N/A"))
                .setIcon(R.drawable.ic_check_circle_black_24dp)
                .setIconColorFilter(0)
                .setBackgroundColorRes(R.color.colorPrimaryDark)
                .show();

        ImageButton yenile = findViewById(R.id.img_anasayfa_yenile);

        yenile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                veri_cek();
            }
        });

        TextView yonetici2 = findViewById(R.id.txt_yonetici2);
        Button btn_istekler = findViewById(R.id.btn_istekler);

        TextView yonetici1 = findViewById(R.id.txt_yonetici);
        Button btn_uyeler = findViewById(R.id.btn_uye);

        ImageButton geri = findViewById(R.id.anasayfa_geri);

        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnaMenu.this,GirisYap.class));
                overridePendingTransition(R.anim.saga1, R.anim.saga2);
            }
        });


        btn_istekler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnaMenu.this,Istekler.class));
                overridePendingTransition(R.anim.sola1,R.anim.sola2);
            }
        });

        btn_uyeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnaMenu.this,Uyeler.class));
                overridePendingTransition(R.anim.sola1,R.anim.sola2);
            }
        });

        TextView yonetici3 = findViewById(R.id.txt_yonetici13);
        btn_onay_bekleyen = findViewById(R.id.btn_onay_bekleyen);
        btn_onaylanan = findViewById(R.id.btn_onaylanan);
        btn_iptal_edilen = findViewById(R.id.btn_iptal_edilen);
        btn_benden_onay = findViewById(R.id.btn_benden_onay);
        btn_benim_onayladiklarim = findViewById(R.id.btn_benim_onay);
        btn_kendi = findViewById(R.id.btn_kendi_actigim);

        if(yetki.equals("0"))
        {
            btn_uyeler.setVisibility(View.INVISIBLE);
            yonetici1.setVisibility(View.INVISIBLE);

            yonetici2.setVisibility(View.INVISIBLE);
            btn_istekler.setVisibility(View.INVISIBLE);

            yonetici3.setVisibility(View.INVISIBLE);
            btn_onay_bekleyen.setVisibility(View.INVISIBLE);
        }
        else
        {
            btn_uyeler.setVisibility(View.VISIBLE);
            yonetici1.setVisibility(View.VISIBLE);

            yonetici2.setVisibility(View.VISIBLE);
            btn_istekler.setVisibility(View.VISIBLE);

            yonetici3.setVisibility(View.VISIBLE);
            btn_onay_bekleyen.setVisibility(View.VISIBLE);

        }
        ImageButton btn_yeni_talep = findViewById(R.id.btn_yeni_talep);

        btn_onay_bekleyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secilen = "1";
                taleplere_git();
            }
        });

        btn_onaylanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secilen = "2";
                taleplere_git();
            }
        });

        btn_iptal_edilen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secilen = "3";
                taleplere_git();
            }
        });

        btn_benden_onay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secilen = "4";
                taleplere_git();
            }
        });

        btn_benim_onayladiklarim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secilen = "5";
                taleplere_git();
            }
        });

        btn_kendi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secilen = "6";
                taleplere_git();
            }
        });


        ImageButton bildirim = findViewById(R.id.img_btn_bildirim);
        bildirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnaMenu.this,Bildirimlerim.class));
                overridePendingTransition(R.anim.sola1, R.anim.sola2);
            }
        });


        veri_cek();


        btn_yeni_talep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnaMenu.this,TalepOlustur.class));
                overridePendingTransition(R.anim.sola1, R.anim.sola2);
            }
        });


    }

    void taleplere_git()
    {
        Intent gel = new Intent(AnaMenu.this,Talepler.class);
        gel.putExtra("secilen_menu",secilen);
        startActivity(gel);

        overridePendingTransition(R.anim.sola1,R.anim.sola2);
    }


    void veri_cek()
    {

        final ProgressDialog progress = ProgressDialog.show(AnaMenu.this, "Yükleniyor", "Veriler getiriliyor...", true);


        Response.Listener<String> responselistener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    s1 = jsonresponse.getString("sayi1");
                    s2 = jsonresponse.getString("sayi2");
                    s3 = jsonresponse.getString("sayi3");
                    s4 = jsonresponse.getString("sayi4");
                    s5 = jsonresponse.getString("sayi5");
                    s6 = jsonresponse.getString("sayi6");

                    btn_onay_bekleyen.setText("ONAY BEKLEYEN İŞLER" + " ("+ s1 +")");
                    btn_onaylanan.setText("ONAYLANAN İŞLER" + " ("+ s2 +")");
                    btn_iptal_edilen.setText("IPTAL EDİLENLER" + " ("+ s3 +")");
                    btn_benden_onay.setText("SİZDEN ONAY BEKLEYENLER" + " ("+ s4 +")");
                    btn_benim_onayladiklarim.setText("BENİM ONAYLADIKLARIM" + " ("+ s5 +")");
                    btn_kendi.setText("SİZİN OLUŞTURDUKLARINIZ" + " ("+ s6 +")");

                    progress.dismiss();

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        };

        AnaMenuJSON loginrequest = new AnaMenuJSON(kullanici_id,responselistener);
        RequestQueue queue = Volley.newRequestQueue(AnaMenu.this);
        queue.add(loginrequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(AnaMenu.this,GirisYap.class));
        overridePendingTransition(R.anim.saga1, R.anim.saga2);

    }
}
