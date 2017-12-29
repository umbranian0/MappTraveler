package com.example.vasil.mapptraveler;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMap extends Fragment implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static String TAG = "MapFragment";
    private static String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermissionGranted = false;
    //api key da google
    private GoogleMap nMap;
  //  private FusedLocationProviderClient mFusedLocationProviderClient = null;
    private RadioGroup tipoMapa;
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
        return view;

    }

    //traz o mapa para o fragmento
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //iniucia o mapa
         initMap();


        //esta funçao serve para mudar o tipo de mapa
        changeMapType(view);
    }

    //localiza o no mapa
    public void initMap(){
        //trazer o fragmento mapa para o mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(FragmentMap.this);
    }
    @Override
    public void onMapReady(GoogleMap map) {
      nMap = map ;

        //vamos buscar as permissoes de localizacao
        getLocationPermission();

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
            if(ContextCompat.checkSelfPermission(this.getContext(),COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
            }else{
                ActivityCompat.requestPermissions(this.getActivity(),permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        //vamos ver o request code
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 ) {
                    //verifica todas as permissoes
                    for(int i = 0 ; i< grantResults.length;i++){
                        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    //inicializa o mapa mas sem coordenadas
                    initMap();
                }
            }
        }
    }




}
