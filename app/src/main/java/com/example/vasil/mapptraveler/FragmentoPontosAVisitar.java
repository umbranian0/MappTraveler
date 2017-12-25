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

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated

     /*   if (mListener != null) {
            mListener.onFragmentInteraction("Fragment Pontos a visistar");
        }
        */


        return view;
    }
    public interface OnFragmentInteractionListener {
    }
}
