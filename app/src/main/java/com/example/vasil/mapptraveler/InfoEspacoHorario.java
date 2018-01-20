package com.example.vasil.mapptraveler;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

public class InfoEspacoHorario extends AppCompatActivity implements GestureDetector.OnGestureListener {

    Toolbar t;
    ImageView imagem;
    private GestureDetectorCompat detector;
    int horario;
    int image;
    String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_espaco_horario);

        t = (Toolbar) findViewById(R.id.toolbar3);
        imagem = (ImageView) findViewById(R.id.imageView3);

        Bundle nBundle = getIntent().getExtras();
        if(nBundle != null) {
            t.setTitle(nBundle.getString("nomeLocal"));
            imagem.setImageResource(nBundle.getInt("horarioLocal"));
            nome = nBundle.getString("nomeLocal");
            image = nBundle.getInt("imagemLocal");
            horario = nBundle.getInt("horarioLocal");
        }

        detector = new GestureDetectorCompat(this,this);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Intent nIntent = new Intent(this, InfoDoEspaco.class);
        nIntent.putExtra("nomeLocal", nome);
        nIntent.putExtra("imagemLocal", image);
        nIntent.putExtra("horarioLocal", horario);
        startActivity(nIntent);
        this.finish();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }
}
