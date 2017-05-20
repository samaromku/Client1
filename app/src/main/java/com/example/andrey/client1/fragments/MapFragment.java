package com.example.andrey.client1.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andrey.client1.R;
import com.example.andrey.client1.entities.UserCoords;
import com.example.andrey.client1.managers.UserCoordsManager;
import com.example.andrey.client1.managers.UsersManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends SupportMapFragment {
    private GoogleMap map;
    private UserCoordsManager userCoordsManager = UserCoordsManager.INSTANCE;
    private Location currentLocation = userCoordsManager.getLocation();
    private List<UserCoords>userCoordsList = userCoordsManager.getUserCoordsList();
    UsersManager usersManager = UsersManager.INSTANCE;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getMapAsync(googleMap -> {
            map = googleMap;
            updateUI();
        });
    }

    private void updateUI(){
        if(map==null){
            return;
        }
        map.clear();
        for (int i = 0; i < userCoordsList.size(); i++) {
            UserCoords userCoords = userCoordsList.get(i);
            LatLng point = new LatLng(userCoords.getLat(), userCoords.getLog());
            MarkerOptions mark = new MarkerOptions().position(point).title(usersManager.getUserById(userCoords.getUserId()).getLogin());
            map.addMarker(mark);
        }
        LatLng myPoint = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions myMarker = new MarkerOptions()
                .position(myPoint)
                .title(usersManager.getUser().getLogin()+(int)myPoint.latitude+(int)myPoint.longitude);
        map.addMarker(myMarker);

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(myPoint)
                .build();

        int margin = 300;
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
//        map.animateCamera(update);
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                map.moveCamera(update);
            }
        });
    }
}
