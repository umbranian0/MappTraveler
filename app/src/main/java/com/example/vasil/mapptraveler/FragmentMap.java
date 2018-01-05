package com.example.vasil.mapptraveler;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMap extends Fragment implements OnMapReadyCallback {
    //api key da google
    private GoogleMap nMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static String TAG = "MapFragment";
    private static String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    //atrubutos
    private Boolean mLocationPermissionGranted = false;
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient = null;
    private RadioGroup tipoMapa;

    //widgets
    private EditText mSearchText;

    public FragmentMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //criar uma view
        View view = inflater.inflate(R.layout.fragment_fragment_map, container, false);
        //Vamos trazer os RADIO group
        tipoMapa = (RadioGroup) view.findViewById(R.id.tiposMapa);
        mSearchText = (EditText)view.findViewById(R.id.inputSearch);
        //iniucia o mapa
        initMap();

        return view;

    }
    //traz o mapa para o fragmento
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //esta funçao serve para mudar o tipo de mapa
        changeMapType(view);
        getLocationPermission();
        init();

    }
    //---------------------Search
    //criar um init para a pesquisa
    private void init(){
        Log.d(TAG,"initializing search etc...");

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
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);

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

    //metodo para mover a camera
    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "movind the camera to : tat " + latLng.latitude + " | long : " + latLng.longitude);
        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
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
        Toast.makeText(FragmentMap.this.getActivity(), "MAPA esta pronto ", Toast.LENGTH_SHORT).show();
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
            Log.i("permission","parmission"+mLocationPermissionGranted);
            nMap.setMyLocationEnabled(true);
            nMap.getUiSettings().setMyLocationButtonEnabled(true);

            init();
        }else{
            //vamos buscar as permissoes de localizacao
            getLocationPermission();
        }


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
        String [] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION };
        if(ContextCompat.checkSelfPermission(this.getContext(),
                FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getContext(),
                    COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
            }else{
                ActivityCompat.requestPermissions((Activity)this.getContext(),
                        permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        //vamos ver o request code
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    //verifica todas as permissoes
                    for(int i = 0 ; i< grantResults.length;i++){
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;

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


}
