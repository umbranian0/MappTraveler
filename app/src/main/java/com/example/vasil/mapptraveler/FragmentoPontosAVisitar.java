package com.example.vasil.mapptraveler;


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
import com.example.vasil.mapptraveler.ServerRequests.LocaisRequest;
import com.example.vasil.mapptraveler.models.InfoDoEspaco;
import com.example.vasil.mapptraveler.models.MyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentoPontosAVisitar extends Fragment {
    //atributos
    ViewGroup v;
    ListView lista;
    Toolbar t;

/*
    Context x;
    */

    String[] nomesLocais = {"Castelo", "Palacio", "Igreja"};
    String[] descricao = {"Descrição Castelo", "Descrição Palacio", "Descrição Igreja"};;
    String[] morada = {"Morada Castelo", "Morada Palacio", "Morada Igreja"};

    int[] imagens = {
            R.drawable.castelo,
            R.drawable.igreja,
            R.drawable.palacio
    };
    int[] horarios = {
            R.drawable.h1,
            R.drawable.h3,
            R.drawable.h2
    };

    int[] visita = {
            1,
            0,
            1};



    public FragmentoPontosAVisitar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_fragmento_pontos_avisitar, container, false);

        //pedir request ao SERVIDOR
        requestLocalsServer();

        lista = (ListView) view.findViewById(R.id.listaFragmentos);

        t = (Toolbar) view.findViewById(R.id.toolbar);
        v = container;

        //x = this.getContext();

        //requestLocalsServer(); //vai buscar todos os dados a base de dados (EM DESENVOLVIMENTO) O PROGRAMA ESTÁ COMPLETO FALTA SÓ MESMO SUBSTITUIR OS VECTORES PELOS VECTORES DA BASE DE DADOS

        MyAdapter myAdapter = new MyAdapter(this.getContext(), nomesLocais, imagens, visita);

        lista.setAdapter(myAdapter);



        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent nIntent = new Intent(getActivity(), InfoDoEspaco.class);
                nIntent.putExtra("nomeLocal", nomesLocais[i]);
                nIntent.putExtra("descricao", descricao[i]);
                nIntent.putExtra("morada", morada[i]);
                nIntent.putExtra("imagemLocal", imagens[i]);
                nIntent.putExtra("horarioLocal", horarios[i]);
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


public void requestLocalsServer(){

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

                       //este log escreve todos os objetos que o JSONarray tem
                        Log.i("NOME",  nomesLocais[i]);
                        Log.i("DESCRICAO", descricao[i]);
                        Log.i("MORADA", morada[i]);
                       //agora tens que atribuir os dados que veem do SERVER para o array que tens aqui
                    }
                    Log.i("RequestServer"," recebeste os dados");

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        };

        //vai buscar a resposta em JSON
        LocaisRequest request = new LocaisRequest( response);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);

    }

}
