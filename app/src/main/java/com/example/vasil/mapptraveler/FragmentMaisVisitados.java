package com.example.vasil.mapptraveler;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vasil.mapptraveler.models.InfoDoEspaco;


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
            R.drawable.palacio,
            R.drawable.castelo,
            R.drawable.igreja};

    int[] horarios = {
            R.drawable.h2,
            R.drawable.h1,
            R.drawable.h3};

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

        //requestLocalsServer(); //vai buscar todos os dados a base de dados (EM DESENVOLVIMENTO) O PROGRAMA ESTÁ COMPLETO FALTA SÓ MESMO SUBSTITUIR OS VECTORES PELOS VECTORES DA BASE DE DADOS

        PopularAdapter popularAdapter = new PopularAdapter(this.getContext(), nomesLocais,imagens,visita,nVisitas);

        lista2.setAdapter(popularAdapter);

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
}
