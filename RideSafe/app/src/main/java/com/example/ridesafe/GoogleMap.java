package com.example.ridesafe;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class GoogleMap extends Fragment implements OnMapReadyCallback {
    private com.google.android.gms.maps.GoogleMap mMap;
    SupportMapFragment mapFragment;
    //private SearchView pick,destination;
    EditText mSearchtext;

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 15f;
    Button sm;

    EditText mSearchText;
    LatLng myLocation,destination;

    private Boolean mLocationPermissionsGranted = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_google_map,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mSearchText=view.findViewById(R.id.input_search);
        sm=view.findViewById(R.id.searchMap);
        getLocationPermission();

        sm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoLocate();
            }
        });


        /*pick=(SearchView)view.findViewById(R.id.pickup_location);
        destination=view.findViewById(R.id.destination_location);*/

        /*search = view.findViewById(R.id.sv_location);
        EditText editText = search.findViewById(androidx.appcompat.R.id.search_bar);
        editText.setTextColor(getResources().getColor(R.color.colorAccent));*/

        /*pick.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        String location=pick.getQuery().toString();
                        List<Address> addressList =null;
                        if(location!=null || !location.equals("")){
                            Geocoder geocoder=new Geocoder(getContext());
                            try {
                                addressList = geocoder.getFromLocationName(location, 1);
                            }catch(IOException e){
                                e.printStackTrace();
                            }
                        }
                        Address address=addressList.get(0);
                        LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                }
        );
        //mapFragment.getMapAsync(this);*/
    }


    @Override
    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.map));

            if (!success) {
                Log.e("Map log", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Map log", "Can't find style. Error: ", e);
        }

        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;

            }
            mMap.setMyLocationEnabled(true);
            init();
        }

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(29.941996699999997, 76.8144186);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    private void init(){
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH||actionId==EditorInfo.IME_ACTION_DONE|| event.getAction()==KeyEvent.ACTION_DOWN || event.getAction()==KeyEvent.KEYCODE_ENTER){
                    geoLocate();
                }

                return false;
            }
        });
        hideSoftKeyboard();
    }

    private void geoLocate(){
        Log.d(TAG,"inside geolocate");
        String searchString=mSearchText.getText().toString();

        Geocoder geocoder=new Geocoder(getContext());
        List<Address> list=new ArrayList<>();

        try{
            list=geocoder.getFromLocationName(searchString,1);
        }catch(IOException e) {

        }
        if(list.size()>0){
            Address address=list.get(0);
            Log.e(TAG,"geolocation found"+address.toString());
            destination=new LatLng(address.getLatitude(),address.getLongitude());
            moveCamera(destination,DEFAULT_ZOOM,address.getAddressLine(0));

            mMap.addPolyline(new PolylineOptions().add(
                    myLocation,
                    destination
                    )
                    .width(10)
                    .color(Color.WHITE)
            );
        }
    }

    private void getDeviceLocation(){
        Log.d(TAG,"Getting device location");
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getContext());

        try{

            Task location=mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){



                        Log.d(TAG,"Successfull");
                        Location currentLocation=(Location) task.getResult();
                        //LatLng latLng=new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM));
                        try {
                            myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            moveCamera(myLocation, DEFAULT_ZOOM, "My Location");
                        }catch(Exception e){
                            Toast.makeText(getContext(),"Please enable your location service",Toast.LENGTH_LONG).show();
                        }
                        //moveCamera(new LatLng(24,36),DEFAULT_ZOOM);
                    }
                    else{
                        Log.d(TAG,"failed to location");
                        //Toast.makeText(this,"Map is ready",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch(SecurityException e){
            Log.e(TAG,"security exception");
        }
    }


    private void moveCamera(LatLng latlng, float zoom,String title){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));

        if(!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latlng)
                    .title(title);
            mMap.addMarker(options);
        }
        hideSoftKeyboard();
    }

    private void initMap(){
        Log.d(TAG,"onMapReady: Initializing");
        mapFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment==null){
            FragmentManager fm=getChildFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            mapFragment=SupportMapFragment.newInstance();
            ft.replace(R.id.map,mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

    }

    private void getLocationPermission(){
        Log.d(TAG,"onMapReady: getting permission");
        String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(getContext().getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getContext().getApplicationContext(),COURSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted=true;
                initMap();
            }else{
                requestPermissions(permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            requestPermissions(permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"onMapReady: called");

        mLocationPermissionsGranted=false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0){
                    for(int i=0;i<grantResults.length;i++){
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted=false;
                            Log.d(TAG,"onMapReady: Permisiion failed");
                            return;
                        }
                    }
                    Log.d(TAG,"onMapReady: Granted");
                    mLocationPermissionsGranted=true;
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard(){
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


}
