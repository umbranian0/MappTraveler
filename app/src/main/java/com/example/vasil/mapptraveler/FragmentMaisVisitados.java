package com.example.vasil.mapptraveler;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.vasil.mapptraveler.ServerRequests.PopularRequest;
import com.example.vasil.mapptraveler.models.InfoDoEspaco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMaisVisitados extends Fragment {

    ViewGroup v2;
    ListView lista2;
    Toolbar t;

    Context x;

    String[] nomesLocais = {"Palacio", "Castelo", "Igreja"};
    String[] descricao = {"Descrição Palacio","Descrição Castelo",  "Descrição Igreja"};
    String[] morada = {"Morada Palacio", "Morada Castelo", "Morada Igreja"};

    int[] imagens = {
            R.drawable.igreja,
            R.drawable.castelo,
            R.drawable.palacio};

    int[] horarios = {
            R.drawable.h3,
            R.drawable.h1,
            R.drawable.h2};

    int[] visita = {0, 1, 1};
    int[] nVisitas = {937, 203, 194};


    public FragmentMaisVisitados() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_mais_visitados, container, false);

        lista2 = (ListView) view.findViewById(R.id.listaFragmentos2);
        v2 = container;

        x = this.getContext();

        requestLocais(); //vai buscar todos os dados a base de dados (EM DESENVOLVIMENTO) O PROGRAMA ESTÁ COMPLETO FALTA SÓ MESMO SUBSTITUIR OS VECTORES PELOS VECTORES DA BASE DE DADOS

        lista2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nIntent = new Intent(getActivity(), InfoDoEspaco.class);
                nIntent.putExtra("nomeLocal", nomesLocais[position]);
                nIntent.putExtra("descricao", descricao[position]);
                nIntent.putExtra("morada", morada[position]);
                nIntent.putExtra("imagemLocal", imagens[position]);
                nIntent.putExtra("horarioLocal", horarios[position]);
                startActivity(nIntent);
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
    }

    public void requestLocais(){

        Response.Listener<String> response = new Response.Listener<String>() {
            //esta e a resposta
            //teremos entao que converter a resposta em object JSON

            public void onResponse(String response) {
                try {

                    JSONArray json = new JSONArray((response));



                    for(int i = 0 ; i < json.length(); i++){
                        JSONObject objeto = (JSONObject) json.get(i);

                        nomesLocais[i] = objeto.getString("nome");
                        descricao[i] = objeto.getString("descricao");
                        morada[i] = objeto.getString("endereco");
                        nVisitas[i] = objeto.getInt("nVisitas");

                        //este log escreve todos os objetos que o JSONarray tem
                        Log.i("NOME",  nomesLocais[i]);
                        Log.i("DESCRICAO", descricao[i]);
                        Log.i("MORADA", morada[i]);
                        Log.i("NVISITAS", "" + nVisitas[i]);

                    }
                    Log.i("RequestServer"," recebeste os dados");

                    PopularAdapter popularAdapter = new PopularAdapter(x, nomesLocais,imagens,visita,nVisitas);

                    lista2.setAdapter(popularAdapter);

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        };

        //vai buscar a resposta em JSON
        PopularRequest request = new PopularRequest( response);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);

    }


}
