package com.example.vasil.mapptraveler;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vasil on 12/12/2017.
 */

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://mapptraveler.000webhostapp.com/register.php";

    private Map<String , String> params;

    //construtor que cria a class
    public RegisterRequest(String name,String username , String password ,Response.Listener<String> listener){
    //o post significa que vamos usar o metodo para enviar dados
        super(Method.POST,REGISTER_REQUEST_URL,listener,null);

    //criar um parametro que contem os dados
        params = new HashMap<>();
        params.put("name",name);
        params.put("username",username);
        params.put("password",password);
    }
    //vai buscar os parametros inseridos
    public Map<String,String> getParams(){
        return params;
    }

}
