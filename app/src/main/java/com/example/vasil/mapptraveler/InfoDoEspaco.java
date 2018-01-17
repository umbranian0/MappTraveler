package com.example.vasil.mapptraveler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

public class InfoDoEspaco extends AppCompatActivity {

    Toolbar t;
    ImageView imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infodoespaco);

        t = (Toolbar) findViewById(R.id.toolbar2);
        imagem = (ImageView) findViewById(R.id.imageView2);

        Bundle nBundle = getIntent().getExtras();
        if(nBundle != null) {
            t.setTitle(nBundle.getString("nomeLocal"));
            imagem.setImageResource(nBundle.getInt("imagemLocal"));
        }

    }
}
