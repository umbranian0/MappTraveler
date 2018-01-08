package com.example.vasil.mapptraveler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentoPontosAVisitar extends Fragment {
    //atributos
    ListView lista;
    String[] nomesLocais = {"Castelo", "Palacio", "Igreja"};
    int[] imagens = {R.drawable.castelo,
            R.drawable.palacio,
            R.drawable.igreja};

    public FragmentoPontosAVisitar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_fragmento_pontos_avisitar, container, false);

        lista = (ListView) view.findViewById(R.id.listaFragmentos);

        MyAdapter myAdapter = new MyAdapter(this.getContext(), nomesLocais, imagens);

        return view;
    }

    public interface OnFragmentInteractionListener {
    }

}
