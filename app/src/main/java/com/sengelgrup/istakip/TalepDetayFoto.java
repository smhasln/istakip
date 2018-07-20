package com.sengelgrup.istakip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Picasso;

public class TalepDetayFoto extends AppCompatActivity {

    PhotoViewAttacher photoViewAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talep_detay_foto);

        Intent git = getIntent();
        String url = git.getStringExtra("tam_resim");

        PhotoView resim = findViewById(R.id.tamresim);

        Picasso.get().load(url).into(resim);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.saga1,R.anim.saga2);
    }
}
