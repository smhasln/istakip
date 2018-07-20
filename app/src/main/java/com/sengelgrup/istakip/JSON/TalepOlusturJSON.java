package com.sengelgrup.istakip.JSON;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TalepOlusturJSON extends StringRequest {
    private static final String GIRIS_URL = "http://213.142.145.51/plesk-site-preview/sengelgrup.com/android/talep_ekle.php";

    private Map<String, String> params;

    public TalepOlusturJSON(String kullanici_id, String baslik, String aciklama, String onaylayacak, String bilgilendirilecek, String resim_kodu, Response.Listener<String> listener) {
        super(Method.POST, GIRIS_URL, listener, null);
        // İşlemler için kullanılacak veriler hazırlanır.
        params = new HashMap<>();
        params.put("id", kullanici_id);
        params.put("baslik", baslik);
        params.put("aciklama", aciklama);
        params.put("onaylayacak", onaylayacak);
        params.put("bilgilendirilecek", bilgilendirilecek);
        params.put("resim_kodu", resim_kodu);
        params.put("deger","1");

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
