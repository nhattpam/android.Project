package com.example.myprojectprm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;
    private String loggedInUsername; // Variable to store the username of the logged-in user

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Retrieve the passed username from the arguments
//        loggedInUsername = getArguments().getString("username");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(AppConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", "");
        Log.d("map", loggedInUsername);

        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        // Set up the map and add markers, etc.

        // Get the address (latitude and longitude) from your data source
        double latitude = 47.613028; // Example latitude
        double longitude = -122.342064; // Example longitude
        LatLng location = new LatLng(latitude, longitude);

        // Add a marker at the location with a title and snippet
        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title("Amazon Shop")
                .snippet("Marker Snippet"));

        // Move the camera to the marker location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
    }

}
