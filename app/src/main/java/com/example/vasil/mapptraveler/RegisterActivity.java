package com.example.vasil.mapptraveler;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //trazer os campos do layout
        final EditText txtNome = (EditText) findViewById(R.id.txtName);
        final EditText txtUsernameme = (EditText) findViewById(R.id.txtUsername);
        final EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
        final Button btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //vamos trazer o que o user registou para fazer o intent
                final String name = txtNome.getText().toString();
                final String user = txtUsernameme.getText().toString();
                final String pass = txtPassword.getText().toString();


                Response.Listener<String> response = new Response.Listener<String>() {
                    //esta e a resposta
                    //teremos entao que converter a resposta em object JSON
                    public void onResponse(String response) {
                        try {
                            Log.i("response",response);
                            JSONObject json = new JSONObject((response));
                            //vai buscar o valor do sucess para aqui
                            //porque o retorno do php e um 'sucess'
                            boolean sucess = json.getBoolean("success");


                            //leva o intent para o loginActivity se for bem sucedido
                            Log.d("sucesso ", String.valueOf(sucess));
                            if (sucess == true) {

                                //criacao do intent
                                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                //começamos o intent
                                startActivity(loginIntent);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Register Failed").setNegativeButton("Retry", null)
                                        .create().show();
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }
                };

                //vai buscar a resposta em JSON
                RegisterRequest request = new RegisterRequest(name, user, pass, response);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(request);

            }
        });
        }

    }

