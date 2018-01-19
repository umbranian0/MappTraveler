package com.example.vasil.mapptraveler;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentoPontosAVisitar extends Fragment {
    //atributos
    ViewGroup v;
    ListView lista;
    Toolbar t;

    String[] nomesLocais = {"Castelo", "Palacio", "Igreja"};
    int[] imagens = {R.drawable.castelo,
            R.drawable.palacio,
            R.drawable.igreja};
    int[] horarios = {R.drawable.h1,
            R.drawable.h2,
            R.drawable.h3};

    public FragmentoPontosAVisitar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_fragmento_pontos_avisitar, container, false);

        lista = (ListView) view.findViewById(R.id.listaFragmentos);

        t = (Toolbar) view.findViewById(R.id.toolbar);
        v = container;
        MyAdapter myAdapter = new MyAdapter(this.getContext(), nomesLocais, imagens);

        lista.setAdapter(myAdapter);



        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent nIntent = new Intent(getActivity(), InfoDoEspaco.class);
                nIntent.putExtra("nomeLocal", nomesLocais[i]);
                nIntent.putExtra("imagemLocal", imagens[i]);
                nIntent.putExtra("horarioLocal", horarios[i]);
                startActivity(nIntent);
            }
        });

        return view;
    }

    public interface OnFragmentInteractionListener {
    }

}
