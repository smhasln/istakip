package com.sengelgrup.istakip;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.sengelgrup.istakip.JSON.TalepOlusturBilgiJSON;
import com.sengelgrup.istakip.JSON.TalepOlusturJSON;
import com.google.android.gms.common.util.ArrayUtils;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.tapadoo.alerter.Alerter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class TalepOlustur extends AppCompatActivity {

    String gonderilecek_resim_kodu = "";
    String[] foto_dizi = new String[5];
    Integer foto_sayisi;

    private String[] islemler = {
            "Bir fotoğraf çek",
            "Galeriden yükle",
            "Resimleri temizle"};

    String resim_kodu = "";

    String baslik;
    String aciklama;

    String kullanici_id;
    Button btn_belge;
    private int PICK_IMAGE_REQUEST = 1;

    String onay_idleri;
    String bilgi_idleri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talep_olustur);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        kullanici_id = preferences.getString("kullanici_id", "N/A");

        final EditText txt_baslik = findViewById(R.id.txt_talep_baslik);
        final EditText txt_aciklama = findViewById(R.id.txt_talep_aciklama);

        btn_belge = findViewById(R.id.btn_belge);

        Button btn_tamam = findViewById(R.id.btn_talep_olustur);

        ImageButton btn_geri = findViewById(R.id.btn_talep_geri);

        btn_geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               startActivity(new Intent(TalepOlustur.this,AnaMenu.class));
               overridePendingTransition(R.anim.saga1,R.anim.saga2);
            }
        });

        foto_sayisi = 0;


        btn_belge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (foto_sayisi < 5)
                {

                    // Filtrele
                    final AlertDialog.Builder builder = new AlertDialog.Builder(TalepOlustur.this);
                    View mView = getLayoutInflater().inflate(R.layout.callspinner, null);
                    builder.setTitle("Belge Yükle");

                    final Spinner spinner = (Spinner) mView.findViewById(R.id.spinner);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(TalepOlustur.this, android.R.layout.simple_spinner_dropdown_item, islemler);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);


                    //Hangi opsiyon seçildiyse tespit edilir işlemler yapılır.
                    builder.setPositiveButton("Seç", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String secilen = islemler[spinner.getSelectedItemPosition()].toString();
                            if(secilen.equals("Bir fotoğraf çek"))
                            {

                                RxImagePicker.with(TalepOlustur.this).requestImage(Sources.CAMERA).subscribe(new Consumer<Uri>() {
                                    @Override
                                    public void accept(@NonNull Uri uri) throws Exception {

                                        final InputStream imageStream = getContentResolver().openInputStream(uri);
                                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                                        Bitmap btmp = Bitmap.createScaledBitmap(selectedImage, 400, 500, false);
                                        resim_kodu = encodeImage(btmp);

                                        gonderilecek_resim_kodu = gonderilecek_resim_kodu + resim_kodu + ",";

                                        foto_sayisi++;

                                        btn_belge.setText(foto_sayisi + " FOTOĞRAF EKLENDİ");

                                    }
                                });


                            }
                            else if (secilen.equals("Galeriden yükle"))
                            {

                                showFileChooser();
                            }
                            else if (secilen.equals("Resimleri temizle"))
                            {

                                foto_sayisi = 0;
                                gonderilecek_resim_kodu = "";
                                resim_kodu = "";

                                btn_belge.setText("BELGE EKLE ("+foto_sayisi+")");
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
                else
                {
                    Alerter.create(TalepOlustur.this)
                            .setTitle("Uyarı")
                            .setText("Maksimum belge sayısına ulaşıldı!")
                            .setIcon(R.drawable.ic_cancel_black_24dp)
                            .setIconColorFilter(0)
                            .setBackgroundColorRes(android.R.color.holo_red_light)
                            .show();
                }



            }
        });

        btn_tamam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                baslik = txt_baslik.getText().toString();
                aciklama = txt_aciklama.getText().toString();

                if(baslik.isEmpty() || aciklama.isEmpty())
                {
                    Alerter.create(TalepOlustur.this)
                            .setTitle("İşlem Başarısız")
                            .setText("Alanlar boş geçilemez!")
                            .setIcon(R.drawable.ic_cancel_black_24dp)
                            .setIconColorFilter(0)
                            .setBackgroundColorRes(android.R.color.holo_red_light)
                            .show();
                }
                else
                {
                    Intent gonder = new Intent(TalepOlustur.this,KisiEkle.class);
                    gonder.putExtra("baslik",baslik);
                    gonder.putExtra("aciklama",baslik);
                    gonder.putExtra("gonderilecek_resim_kodu",gonderilecek_resim_kodu);
                    gonder.putExtra("kullanici_id",kullanici_id);
                    startActivity(gonder);

                }
            }
        });

    }

    private String encodeImage(Bitmap bm)
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,80,baos);


        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView

                Bitmap btmp = Bitmap.createScaledBitmap(bitmap, 400, 500, false);
                resim_kodu = encodeImage(btmp);

                gonderilecek_resim_kodu = gonderilecek_resim_kodu + resim_kodu + ",";

                foto_sayisi++;

                btn_belge.setText(foto_sayisi + " FOTOĞRAF EKLENDİ");

                //imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.saga1,R.anim.saga2);
    }
}
