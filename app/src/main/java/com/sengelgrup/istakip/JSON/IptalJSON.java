package com.sengelgrup.istakip.JSON;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class IptalJSON extends StringRequest {
    private static final String GIRIS_URL = "http://213.142.145.51/plesk-site-preview/sengelgrup.com/android/iptal_et.php";

    private Map<String, String> params;

    public IptalJSON(String adsoyad, String talep_id, String yetki, Response.Listener<String> listener) {
        super(Method.POST, GIRIS_URL, listener, null);
        // İşlemler için kullanılacak veriler hazırlanır.
        params = new HashMap<>();
        params.put("adsoyad", adsoyad);
        params.put("talep_id", talep_id);
        params.put("yetki", yetki);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
