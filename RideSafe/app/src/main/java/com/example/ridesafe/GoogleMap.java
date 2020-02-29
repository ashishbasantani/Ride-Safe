package com.example.ridesafe;

import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.zip.Inflater;

public class GoogleMap extends Fragment implements OnMapReadyCallback {
    private com.google.android.gms.maps.GoogleMap mMap;
    SupportMapFragment mapFragment;
    private SearchView pick,destination;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_google_map,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment==null){
            FragmentManager fm=getChildFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            mapFragment=SupportMapFragment.newInstance();
            ft.replace(R.id.map,mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        pick=(SearchView)view.findViewById(R.id.pickup_location);
        destination=view.findViewById(R.id.destination_location);

        /*search = view.findViewById(R.id.sv_location);
        EditText editText = search.findViewById(androidx.appcompat.R.id.search_bar);
        editText.setTextColor(getResources().getColor(R.color.colorAccent));*/

        pick.setOnQueryTextListener(
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
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(29.941996699999997, 76.8144186);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
