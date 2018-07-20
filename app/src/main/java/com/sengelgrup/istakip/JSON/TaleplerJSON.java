package com.sengelgrup.istakip.JSON;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class TaleplerJSON extends StringRequest {
    private static final String GIRIS_URL = "http://213.142.145.51/plesk-site-preview/sengelgrup.com/android/talep_cek.php";

    private Map<String, String> params;

    public TaleplerJSON(String sorgu,String id, Response.Listener<String> listener) {
        super(Method.POST, GIRIS_URL, listener, null);
        // İşlemler için kullanılacak veriler hazırlanır.
        params = new HashMap<>();
        params.put("sorgu", sorgu);
        params.put("per_id", id);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
