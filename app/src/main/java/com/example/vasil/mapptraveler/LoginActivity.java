package com.example.vasil.mapptraveler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {


    // UI references.
    private EditText txtUsername ;
    private EditText txtPassword;
    private Button btnLogin;
    private Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //criar um intent para o botao de registo
        btnLogin = (Button)findViewById(R.id.btnLogin);

        // Set up the login form
        btnLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //set up the references do XML
                txtUsername = (EditText) findViewById(R.id.txtUsername);
                txtPassword = (EditText) findViewById(R.id.txtPassword);

                //set up references to String
                final String username = txtUsername.getText().toString();
                final String password = txtPassword.getText().toString();

                //primeiro precisamos de um responseListener
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            //precisamos de saber se foi bem sucedido
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                //trazemos o que o JSON file responde
                                String name = jsonResponse.getString("name");

                                //criacao do intent
                                Intent loginIntent = new Intent(LoginActivity.this,NavigationActivity.class);
                                //passamos parametross que queremos
                                loginIntent.putExtra("name",name);
                                loginIntent.putExtra("username",username);
                                //começamos o intent
                                LoginActivity.this.startActivity(loginIntent);
                            }

                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("Login Failed").setNegativeButton("Retry", null)
                                    .create().show();
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                //criamos um request de login
                LoginRequest loginRequest = new LoginRequest(username, password , responseListener );
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
        //criar um intent para o botao de registo
        btnRegister = (Button)findViewById(R.id.btnRegister);

        //listener para Registo de utilizador que cria o intent
        btnRegister.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //criacao do intent
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                //começamos o intent
                LoginActivity.this.startActivity(registerIntent);
            }
        });


    }



}

