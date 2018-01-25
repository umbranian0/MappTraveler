package com.example.vasil.mapptraveler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMinhaConta extends Fragment {

    //Atributos
    private TextView txtNome;
    private TextView txtUsername;
    private TextView txtDescricao;

    private Button btnGuardarAlteracoes;
    private Button btnAlterarPassword;

    public FragmentMinhaConta() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minha_conta, container, false);
        //Atributos
        txtNome = (TextView) view.findViewById(R.id.txtNome);
        txtUsername = (TextView) view.findViewById(R.id.txtUsername);
        txtDescricao = (TextView) view.findViewById(R.id.txtDescricao);

        btnGuardarAlteracoes = (Button) view.findViewById(R.id.btnGuardar);
        btnAlterarPassword = (Button)view.findViewById(R.id.btnAlterarPassword);

        Bundle bundleRecetor = getArguments();
        String bName = bundleRecetor.getString("name");
        String bUser = bundleRecetor.getString("username");


        txtNome.setText(bName.toString());
        txtUsername.setText(bUser.toString());
     //   txtDescricao.setText(bLocVisitados + bLocAVisitar);

        // Inflate the layout for this fragment
        return view;

    }

}
