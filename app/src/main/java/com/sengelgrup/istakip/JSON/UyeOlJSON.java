package com.sengelgrup.istakip.JSON;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class UyeOlJSON extends StringRequest {
    private static final String GIRIS_URL = "http://213.142.145.51/plesk-site-preview/sengelgrup.com/android/uye_ol.php";

    private Map<String, String> params;

    public UyeOlJSON(String kullanici_adi, String sifre, String email, String adsoyad, String token, Response.Listener<String> listener) {
        super(Method.POST, GIRIS_URL, listener, null);
        // İşlemler için kullanılacak veriler hazırlanır.
        params = new HashMap<>();
        params.put("kadi", kullanici_adi);
        params.put("sifre", sifre);
        params.put("email",email);
        params.put("adsoyad",adsoyad);
        params.put("token",token);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
