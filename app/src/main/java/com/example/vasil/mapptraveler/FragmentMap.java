package com.example.vasil.mapptraveler;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.vasil.mapptraveler.ServerRequests.LocationRequest;
import com.example.vasil.mapptraveler.models.CustomWindowAdapter;
import com.example.vasil.mapptraveler.models.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.location.places.Places.GeoDataApi;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMap extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener{
    //api key da google
    private GoogleMap nMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int PLACE_PICKER_REQUEST = 1;

    private static final String TAG = "MapFragment";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    //2 coordenadas random do mundo
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40,-168),new LatLng(71,136));
    private static final float DEFAULT_ZOOM = 15f;

    //atrubutos
    //atributos do googleapi
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleApiClient mGoogleApiClient;
    private Boolean mLocationPermissionGranted = false;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private PlaceInfo mPlace;
    private Marker mMarker;
    //widgets

    private RadioGroup tipoMapa;
    private ImageView imgLoc ;
    private ImageView btnPlacePicker;
    private ImageView btnInfo;
    private AutoCompleteTextView mSearchText;


  //  private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    public FragmentMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //criar uma view
        View view = inflater.inflate(R.layout.fragment_fragment_map, container, false);
        getLocationPermission();
        //Vamos trazer os RADIO group
        tipoMapa = (RadioGroup) view.findViewById(R.id.tiposMapa);
        mSearchText = (AutoCompleteTextView) view.findViewById(R.id.inputSearch);
        btnPlacePicker = (ImageView)view.findViewById(R.id.placePicker);
        imgLoc = (ImageView)view.findViewById(R.id.imgLoc);
        btnInfo = (ImageView)view.findViewById(R.id.imgInfo);

        //pede as localizacoes que tem de adicionar do servidor
       // requestLocationsServer();
        return view;

    }
    //traz o mapa para o fragmento
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //esta funçao serve para mudar o tipo de mapa
        changeMapType(view);

    }
    //---------------------Search
    //criar um init para a pesquisa
    private void init(){
        Log.d(TAG,"initializing search etc...");
        //google api client
        mGoogleApiClient = new GoogleApiClient
                .Builder(this.getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this.getActivity(), this)
                .build();

        mSearchText.setOnItemClickListener(mAutocompleteClickListener);

        //listener for fused location client
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this.getContext(), mGoogleApiClient , LAT_LNG_BOUNDS  , null);

        //adaptamos o search input para um place autocomplete adapter
        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        //para fazer pesquisas de espaços
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    //metodo para pesquisar
                    geoLocate();

                }
                return false;
            }
        });

        //para recebentrar nvis«a loc do utilizador
        imgLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"clickou no icon de sua localizacao");
                getDeviceLocation();
            }
        });

        //para adicionar um sitio
        btnPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(getActivity()),PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    Log.e(TAG," onClick btnPlacepicker"+e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG," onClick btnPlacepicker, not available"+e.getMessage());
                }
            }
        });
        //botao para informação detalhada do espaco
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "on click btnInfo");

                try {

                     if(mMarker.isInfoWindowShown()){
                         //se ja houver uma janela
                        mMarker.hideInfoWindow();
                     }else{
                         //display window
                         Log.d(TAG, " displaying window");
                         mMarker.showInfoWindow();
                     }

                }//caso de erro
                catch (NullPointerException e){
                    Log.e(TAG, "on click null pointer " + e.getMessage());
                }
            }
        });

        //esconde teclado depois de pesquisar
        esconderTeclado(getContext());

    }

    //result for place picker btn
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                //Place é o objeto que vamos buscar
                Place place = PlacePicker.getPlace(this.getContext(), data);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient,place.getId());

                placeResult.setResultCallback(placeDetailCallback);

            }
        }
    }
//para pesquisar
    private void geoLocate() {
       Log.d(TAG,"geo locate search ") ;

       String searchString =mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(this.getContext());
        List<Address> list = new ArrayList<>();

        try{
        list = geocoder.getFromLocationName(searchString,1);
        }catch(IOException e){
            Log.e(TAG, "geolocate error : " + e.getMessage());
        }
        if(list.size()>0){
            Address adress = list.get(0);

            Log.i(TAG,"location found : " + adress.toString());
            //move a camera para a pesquisa
            moveCamera(new LatLng(adress.getLatitude(),adress.getLongitude()),DEFAULT_ZOOM,adress.getAddressLine(0));

        }
    }

    //---------------------MAPA

    //trazer a current location
    private void getDeviceLocation() {
        Log.d(TAG, "getting the device current location");
        //inializar mfused
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getContext());

        //security exception
        try {
            if (mLocationPermissionGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "found location");
                            Location currentLocation = (Location) task.getResult();

                            //mover a camera para este resultado
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM,
                                    "Minha Localizacao");

                        } else {
                            Log.d(TAG, "unable to found location");
                            Toast.makeText(getContext(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.d(TAG, "Security error : " + e.getMessage());
        }

    }



    //localiza o no mapa
    public void initMap() {
        //trazer o fragmento mapa para o mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().
                findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(FragmentMap.this);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        Toast.makeText(FragmentMap.this.getActivity(), "A iniciar", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Map is Ready");
        nMap = map;

        if (mLocationPermissionGranted) {
           getDeviceLocation();

            //propriedades do mapa
            if (ActivityCompat.checkSelfPermission(this.getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this.getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            nMap.setMyLocationEnabled(true);
            nMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }else{
            //vamos buscar as permissoes de localizacao
            getLocationPermission();
        }

    }


    //metodo para mover a camera
    private void moveCamera(LatLng latLng, float zoom , String title) {
        Log.d(TAG, "moving the camera to : lat " + latLng.latitude + " | long : " + latLng.longitude);

        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        //pedir localizacao dos espacos a visitar
        requestLocationsServer();

        nMap.clear();
        //So cria marcador se for outra localização sem ser a minha
        if(!title.equals("Minha Localizacao")){
            //adicionar marker de uma pesquisa
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            nMap.addMarker(options);
        }
        //vai esconder a caixa de dialogo sempre que fizer uma pesquisa
        esconderTeclado(getContext());

    }

    //metodo para mover a camera
    private void moveCamera(LatLng latLng, float zoom , PlaceInfo placeInfo) {
        Log.d(TAG, "moving the camera to : lat " + latLng.latitude + " | long : " + latLng.longitude);

        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        requestLocationsServer();
        nMap.clear();
        //informaçao detalhada dos sitios
        nMap.setInfoWindowAdapter(new CustomWindowAdapter(this.getContext()));

        if(placeInfo != null){
            try{
                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                        "Website: " + placeInfo.getWebsite() + "\n" +
                        "Price Rating: " + placeInfo.getRating() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.getName())
                        .snippet(snippet);
                mMarker = nMap.addMarker(options);

            }catch (NullPointerException e){
                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage() );
            }
        }else{
            nMap.addMarker(new MarkerOptions().position(latLng));
        }


        //vai esconder a caixa de dialogo sempre que fizer uma pesquisa
        esconderTeclado(getContext());

    }
    //funçao para mudar o tipo de mapa
    public void changeMapType(View v) {

        tipoMapa.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){

                    // Other supported types include: MAP_TYPE_NORMAL,
                    // MAP_TYPE_TERRAIN, MAP_TYPE_HYBRID and MAP_TYPE_NONE
                    case R.id.btnNormal:
                        nMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case R.id.btnHibrid:
                        nMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case R.id.btnTerreno:
                        nMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case R.id.btnSatelite:
                        nMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    default:
                        nMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                }
            }
        });
    }


    //-------ver permissoes do android
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String [] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION };

        if(ContextCompat.checkSelfPermission(this.getContext(),
                FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this.getContext(),
                    COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                initMap();
            }
            else{
                ActivityCompat.requestPermissions((Activity)this.getContext(),
                        permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions((Activity)this.getContext(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionGranted = false;

        //vamos ver o request code
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    //verifica todas as permissoes
                    for(int i = 0 ; i< grantResults.length;i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG,"permissions garanted");
                    mLocationPermissionGranted = true;
                    //inicializa o mapa mas sem coordenadas
                    initMap();
                }
            }
        }
    }

    private void esconderTeclado(Context ctx){
        Log.d("teclado","esconder teclado ");

        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /* ------------------------------------------
        Google places API autocomplete sugestions
        -----------------------------------------*/
     private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
            esconderTeclado(getContext());

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            //precisamos do objeto da localizacao
                    PendingResult< PlaceBuffer> placeResult = GeoDataApi.getPlaceById(mGoogleApiClient,placeId);

                    placeResult.setResultCallback(placeDetailCallback);

        }
    };

    // traz a informação sobre o sitio pesquisado
     private ResultCallback<PlaceBuffer> placeDetailCallback = new ResultCallback<PlaceBuffer>() {
         @Override
         public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, " place query did not complete " + places.getStatus().toString());
                places.release();
                return;
            }
            //traz o sitio , so é executado se for uma pesquisa valida
            final Place place = places.get(0);

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                mPlace.setAttributions(place.getAttributions().toString());
                mPlace.setId(place.getId().toString());
                mPlace.setLatLng(place.getLatLng());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setRating(place.getRating());
                mPlace.setWebsite(place.getWebsiteUri());

                Log.d(TAG, " place : " + mPlace.toString());

            }catch(NullPointerException e)
            {
                Log.e(TAG,"error : " + e.getMessage());
            }
            //mover a camera
             moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                     place.getViewport().getCenter().longitude), DEFAULT_ZOOM , mPlace);

             places.release();
         }
     };

     //--------------------------------------------------------
    //Setting locations from the server

    public void requestLocationsServer(){

        Response.Listener<String> response = new Response.Listener<String>() {
            //esta e a resposta
            //teremos entao que converter a resposta em object JSON
            public void onResponse(String response) {
                try {
                    Log.i("resposta localizacoes",response);

                    JSONArray json = new JSONArray((response));

                    for(int i = 0 ; i < json.length(); i++){
                        JSONObject objeto = (JSONObject) json.get(i);

                        String nome = objeto.getString("nome");
                        double latitude = objeto.getDouble("lat");
                        double longitude = objeto.getDouble("lng");

                        drawCircle(new LatLng(latitude,longitude),nome);
                        Log.i("drawCircle", "drawed circle "+i);
                    }
                      //  Log.i("RequestServer"," recebeste os dados");

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        };

        //vai buscar a resposta em JSON
        LocationRequest request = new LocationRequest( response);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);

    }
    //--------------------------------------------------------
    //fazer circulos das localizacoes que temos na BDD
    private void drawCircle(LatLng point, String nome){
        Log.i("drawingCircle", "Desenhar ciruclos");
        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(200);

        // Border color of the circle
        circleOptions.strokeColor(Color.RED);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
       nMap.addCircle(circleOptions);


        //adicionar marker
        MarkerOptions mark = new MarkerOptions()
                .position(point)
                .title(nome);

        nMap.addMarker(mark);
    }
}
