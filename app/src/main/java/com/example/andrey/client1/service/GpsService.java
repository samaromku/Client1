package com.example.andrey.client1.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.example.andrey.client1.entities.UserCoords;
import com.example.andrey.client1.managers.UserCoordsManager;
import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.network.Client;
import com.example.andrey.client1.network.Request;
import com.example.andrey.client1.storage.ConverterMessages;
import com.example.andrey.client1.storage.JsonParser;
import com.example.andrey.client1.storage.MyLocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GpsService extends IntentService {
    private static final String TAG = "GpsService";
    UserCoordsManager userCoordsManager = UserCoordsManager.INSTANCE;
    private static final int INTERVAL = 1000*300;
    private Geocoder geocoder;
    private ConverterMessages converter = new ConverterMessages();


    public static Intent newIntent(Context context){
        return new Intent(context, GpsService.class);
    }

    public GpsService() {
        super(TAG);
    }

    public static void setServiceAlarm(Context context, boolean isOn){
        Intent i = GpsService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(isOn){
            am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), GpsService.INTERVAL, pi);
        }else{
            am.cancel(pi);
            pi.cancel();
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
            MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                @Override
                public void gotLocation(final Location location) {
                    if (UsersManager.INSTANCE.getUser() != null) {
                        UserCoords userCoords = new UserCoords(location.getLatitude(), location.getLongitude());
                        if(userCoordsManager.getUserCoords()!=null &&
                                userCoords.getLat()==userCoordsManager.getUserCoords().getLat()&&
                                userCoords.getLog()==userCoordsManager.getUserCoords().getLog()){
                            return;
                        }
                        userCoordsManager.addUserCoords(userCoords);
                        userCoordsManager.setUserCoords(userCoords);
                        userCoordsManager.setLocation(location);
                        try {
                            geocoder = new Geocoder(GpsService.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            for (int i = 0; i < addresses.size(); i++) {
                                System.out.println(addresses.get(0).getAddressLine(1));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        converter.authMessage(new Request(userCoords, Request.ADD_COORDS));
                    }
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

