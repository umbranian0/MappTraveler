package com.example.vasil.mapptraveler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity

        //Note : OnFragmentInteractionListener of all the fragments
        implements
        /*Why do we need it ?
        Basically, I use to set the toolbar title when i toggle between different fragments.*/
        FragmentoPontosAVisitar.OnFragmentInteractionListener,
        FragmentMaisVisitados.OnFragmentInteractionListener,

        NavigationView.OnNavigationItemSelectedListener {

            //bundle estatico que partilha informação sobre a conta logada
        public static Bundle dadosConta = new Bundle();

        //atributos
        TextView mName;
        TextView mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //teste
        //vamos buscar os dados da outra atividade para esta

        Intent intent = getIntent();
        String nome = intent.getStringExtra("name");
        String username = intent.getStringExtra("username");

        dadosConta.putString("name",nome);
        dadosConta.putString("username",username);

       // String message = nome + "bem vondo ao MAPP TRAVELER";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //acesso ao menu de navehação
        ////Connect the views of navigation bar
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mName   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.textNome);
        mUsername   = (TextView)navigationView.getHeaderView(0).findViewById(R.id.txtUsername);

        mName.setText("Bem Vindo " + nome);
        mUsername.setText(username);

        //NOTE:  Checks first item in the navigation drawer initially
        navigationView.setCheckedItem(R.id.fragmentPontosAVisitar);

        //NOTE:  Open fragment1 initially.
        setTitle("Pontos a Visitar");
        FragmentoPontosAVisitar fragmento = new FragmentoPontosAVisitar();
        FragmentTransaction fragmentoTransaction = getSupportFragmentManager().beginTransaction();
        fragmentoTransaction.replace(R.id.frame,fragmento,"fragment a visitar");
        fragmentoTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //NOTE: creating fragment object
     //   Fragment fragment = null;

        if (id == R.id.fragmentPontosAVisitar) {
      //      fragment = new FragmentoPontosAVisitar();
            // Handle the camera action
            setTitle("Pontos A Visitar");
            FragmentoPontosAVisitar fragmento = new FragmentoPontosAVisitar();
            FragmentTransaction fragmentoTransaction = getSupportFragmentManager().beginTransaction();
            fragmentoTransaction.replace(R.id.frame,fragmento,"fragment a visitar");
            fragmentoTransaction.commit();


        }
        else if (id == R.id.fragmentMaisVisitados) {
        //    fragment = new FragmentMaisVisitados();
            setTitle("Pontos mais Visitados");
            FragmentMaisVisitados fragmento = new FragmentMaisVisitados();
            FragmentTransaction fragmentoTransaction = getSupportFragmentManager().beginTransaction();
            fragmentoTransaction.replace(R.id.frame,fragmento,"fragment mais visitados");
            fragmentoTransaction.commit();
        }
        else if (id == R.id.fragmentMap) {

            //primeiro precisamos de um responseListener
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONArray jsonResponse = new JSONArray(response);
                        Log.d("JSON RESP", jsonResponse.toString());

                        //bundle total
                        Bundle bundleTot = new Bundle();

                       for(int i = 0; i < jsonResponse.length();i++){
                           JSONObject resposta = (JSONObject)jsonResponse.get(i);
                           String nome = resposta.getString("nome");
                           String lng= resposta.getString("lng");
                           String lat= resposta.getString("lat");


                           Bundle bundle = new Bundle();
                           bundle.putString("nomeLoc", nome );
                           bundle.putString("nomeLoc", lng );
                           bundle.putString("nomeLoc", lat );

                           bundleTot.putBundle("b"+i , bundle);

                        }


                            //criação da transação do fragmento
                            //    fragment = new FragmentMap();
                            setTitle(" MAPA ");
                            FragmentMap fragmento = new FragmentMap();

                                    //envia o bundle com os diversos obj
                                    fragmento.setArguments(bundleTot);

                            FragmentTransaction fragmentoTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentoTransaction.replace(R.id.frame,fragmento,"fragment Map");
                            fragmentoTransaction.commit();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };

            //     //criamos um request de login
            LocationRequest locationRequest = new LocationRequest( responseListener );
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(locationRequest);
        }


        else if (id == R.id.fragmentMinhaConta) {
            //    fragment = new FragmentMap();
            setTitle(" Minha conta ");
            FragmentMinhaConta fragmento = new FragmentMinhaConta();
            fragmento.setArguments(dadosConta);
            FragmentTransaction fragmentoTransaction = getSupportFragmentManager().beginTransaction();
            fragmentoTransaction.replace(R.id.frame,fragmento,"fragment minha conta");
            fragmentoTransaction.commit();
        }

        else if(id == R.id.logOut){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        //NOTE:  Closing the drawer after selecting
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
