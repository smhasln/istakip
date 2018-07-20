package com.sengelgrup.istakip.JSON;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class TalepIslemJSON extends StringRequest {
    private static final String GIRIS_URL = "http://213.142.145.51/plesk-site-preview/sengelgrup.com/android/talep_islem.php";

    private Map<String, String> params;

    public TalepIslemJSON(String talep_id, String kullanici_id, String durum, Response.Listener<String> listener) {
        super(Method.POST, GIRIS_URL, listener, null);
        // İşlemler için kullanılacak veriler hazırlanır.
        params = new HashMap<>();
        params.put("talep_id", talep_id);
        params.put("k_id", kullanici_id);
        params.put("durum", durum);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
