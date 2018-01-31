package com.example.vasil.mapptraveler.ServerRequests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kaytsak on 26/01/2018.
 */

public class LocaisRequest extends StringRequest {
    private static final String LOCAL_REQUEST_URL = "https://maapptraveler.000webhostapp.com/listarLocais.php";

    private Map<String , String> params;

    public LocaisRequest(Response.Listener<String> listener ) {
        super(Request.Method.POST, LOCAL_REQUEST_URL, listener, null);

        //criar um parametro que contem os dados
        params = new HashMap<>();

    }
    //vai buscar os parametros inseridos
    public Map<String,String> getParams(){
        return params;
    }
}
