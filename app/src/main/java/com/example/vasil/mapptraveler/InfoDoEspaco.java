package com.example.vasil.mapptraveler;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class InfoDoEspaco extends AppCompatActivity implements GestureDetector.OnGestureListener {

    Toolbar t;
    ImageView imagem;
    TextView tv1; //morada
    TextView tv2; //descricao

    private GestureDetectorCompat detector;
    int horario;
    int image;
    String nome;
    String desc;
    String mora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infodoespaco);

        t = (Toolbar) findViewById(R.id.toolbar2);
        imagem = (ImageView) findViewById(R.id.imageView2);
        tv1 = (TextView) findViewById(R.id.tvmorada);
        tv2 = (TextView) findViewById(R.id.tvdescricao);

        Bundle nBundle = getIntent().getExtras();
        if(nBundle != null) {
            t.setTitle(nBundle.getString("nomeLocal"));
            imagem.setImageResource(nBundle.getInt("imagemLocal"));
            tv1.setText(nBundle.getString("morada"));
            tv2.setText(nBundle.getString("descricao"));

            nome = nBundle.getString("nomeLocal");
            desc = nBundle.getString("descricao");
            mora = nBundle.getString("morada");
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

        Intent nIntent = new Intent(this, InfoEspacoHorario.class);
        nIntent.putExtra("nomeLocal", nome);
        nIntent.putExtra("descricao", desc);
        nIntent.putExtra("morada", mora);
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
