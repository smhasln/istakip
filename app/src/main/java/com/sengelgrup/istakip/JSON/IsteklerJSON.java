package com.sengelgrup.istakip.JSON;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class IsteklerJSON extends StringRequest {
    private static final String GIRIS_URL = "http://213.142.145.51/plesk-site-preview/sengelgrup.com/android/istekler.php";

    public IsteklerJSON(Response.Listener<String> listener) {
        super(Method.GET, GIRIS_URL, listener, null);
    }

}
