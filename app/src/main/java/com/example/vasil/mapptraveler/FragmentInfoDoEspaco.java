package com.example.vasil.mapptraveler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentInfoDoEspaco extends Fragment {

    TextView t;
    ImageView imagem;

    public FragmentInfoDoEspaco() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_info_do_espaco, container, false);

            t = (TextView) view.findViewById(R.id.txtName);
            imagem = (ImageView) view.findViewById(R.id.imgEspaco);

            Bundle nBundle = getActivity().getIntent().getExtras();
            if(nBundle != null) {
                t.setText(nBundle.getString("nomeLocal"));
                imagem.setImageResource(nBundle.getInt("imagemLocal"));
            }


        // Inflate the layout for this fragment
        return view;
    }

}
