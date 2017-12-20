package com.example.vasil.mapptraveler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class UserAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        //vamos buscar os dados da outra atividade para esta
        Intent intent = getIntent();
        String nome = intent.getStringExtra("name");
        String username = intent.getStringExtra("username");

        String message = nome + "bem vondo ao MAPP TRAVELER";



    }


}
