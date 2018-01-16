package com.example.vasil.mapptraveler;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vasil on 20/12/2017.
 */

public class LoginRequest extends StringRequest{

    private static final String LOGIN_REQUEST_URL = "https://maapptraveler.000webhostapp.com/login.php";

    private Map<String , String> params;

    //construtor que cria a class
    public LoginRequest(String username , String password ,Response.Listener<String> listener){
        //o post significa que vamos usar o metodo para enviar dados
        super(Request.Method.POST,LOGIN_REQUEST_URL,listener,null);

        //criar um parametro que contem os dados
        params = new HashMap<>();
        params.put("username",username);
        params.put("password",password);
    }
    //vai buscar os parametros inseridos
    public Map<String,String> getParams(){
        return params;
    }


}
