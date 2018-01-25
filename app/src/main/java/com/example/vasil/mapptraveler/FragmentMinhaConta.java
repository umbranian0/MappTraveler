package com.example.vasil.mapptraveler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
        String bUser = bundleRecetor.getString("username");
        String bName = bundleRecetor.getString("name");


        try {
        txtNome.setText(bName);
         }catch(NullPointerException e){
            Log.d("Erro nome", "erro" + e.getMessage());
        }
        try {
            txtUsername.setText(bUser);
        }catch(NullPointerException e){
            Log.d("Erro user", "erro" + e.getMessage());
        }
        //   txtDescricao.setText(bLocVisitados + bLocAVisitar);

        // Inflate the layout for this fragment
        return view;

    }

}
