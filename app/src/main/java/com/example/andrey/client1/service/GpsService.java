package com.example.andrey.client1.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.andrey.client1.entities.UserCoords;
import com.example.andrey.client1.managers.UserCoordsManager;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.storage.JsonParser;
import com.example.andrey.client1.storage.MyLocation;

public class GpsService extends IntentService {
    private static final String TAG = "GpsService";
    UserCoordsManager userCoordsManager = UserCoordsManager.INSTANCE;
    JsonParser parser = new JsonParser();

    public static Intent newIntent(Context context){
        return new Intent(context, GpsService.class);
    }

    public GpsService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        if(!isNetworkWorks()){
//            return;
//        }

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                    MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                        @Override
                        public void gotLocation(final Location location) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(20000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    UserCoords userCoords = new UserCoords(location.getLatitude(), location.getLongitude());
                                    userCoordsManager.addUserCoords(userCoords);
                                    userCoordsManager.setUserCoords(userCoords);
                                    userCoordsManager.setLocation(location);
                                    Client.INSTANCE.sendMessage(parser.requestToServer(new Request(userCoords, Request.ADD_COORDS)));
                                }
                            }).start();
                        }
                    };
                    MyLocation myLocation = new MyLocation();
                    myLocation.getLocation(GpsService.this, locationResult);
            }
        });

    }



    private boolean isNetworkWorks(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean networkAvaliable = cm.getActiveNetworkInfo() !=null;
        boolean networkConnected = networkAvaliable && cm.getActiveNetworkInfo().isConnected();
        return networkConnected;
    }
}

