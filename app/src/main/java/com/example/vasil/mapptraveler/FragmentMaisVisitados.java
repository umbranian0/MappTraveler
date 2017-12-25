package com.example.vasil.mapptraveler;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMaisVisitados extends Fragment {


    private Spinner spinner;

    public FragmentMaisVisitados() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_mais_visitados, container, false);

        spinner = (Spinner)v.findViewById(R.id.spinner);

        //trazemos o array de recursos
        Resources res = getResources();
        String[] tipos_de_espacos = res.getStringArray(R.array.tipos_de_espacos);

        //damos set up no spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,tipos_de_espacos);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        // Inflate the layout for this fragment
        return v;

    }

    public interface OnFragmentInteractionListener {
    }
}
