package com.example.andrey.client1.activities;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andrey.client1.R;
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

public class MapFragment extends SupportMapFragment {
    private GoogleMap map;
    private UserCoordsManager userCoordsManager = UserCoordsManager.INSTANCE;
    private Location currentLocation = userCoordsManager.getLocation();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                updateUI();
            }
        });
    }

    private void updateUI(){
        if(map==null){
            return;
        }

        LatLng myPoint = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        MarkerOptions myMarker = new MarkerOptions().position(myPoint).title(UsersManager.INSTANCE.getUser().getLogin());
        map.clear();
        map.addMarker(myMarker);

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(myPoint)
                .build();

        int margin = 100;
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
        map.animateCamera(update);
    }
}
