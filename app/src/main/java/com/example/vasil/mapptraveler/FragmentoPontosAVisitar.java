package com.example.vasil.mapptraveler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentoPontosAVisitar extends Fragment {


    public FragmentoPontosAVisitar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_fragmento_pontos_avisitar, container, false);




        return view;
    }
    public interface OnFragmentInteractionListener {
    }
}
