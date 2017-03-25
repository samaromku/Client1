package com.example.andrey.client1.managers;

import android.location.Location;

import com.example.andrey.client1.entities.UserCoords;

import java.util.ArrayList;
import java.util.List;

public class UserCoordsManager {
    private UserCoords userCoords;
    private List<UserCoords>userCoordsList;
    private Location location = new Location("newProvider");

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public static final UserCoordsManager INSTANCE = new UserCoordsManager();

    private UserCoordsManager(){
        userCoordsList = new ArrayList<>();
    }

    public List<UserCoords> getUserCoordsList() {
        return userCoordsList;
    }

    public UserCoords getUserCoords() {
        return userCoords;
    }

    public void setUserCoords(UserCoords userCoords) {
        this.userCoords = userCoords;
    }

    public void addUserCoords(UserCoords userCoords){
        userCoordsList.add(userCoords);
    }


}
