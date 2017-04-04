package com.example.andrey.client1.entities;


import com.example.andrey.client1.managers.UsersManager;
import com.example.andrey.client1.storage.DateUtil;

public class UserCoords {
    private int id;
    private int userId;
    private double lat;
    private double log;
    private String ts;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserCoords(double lat, double log) {
        DateUtil dateUtil = new DateUtil();
        this.userId = UsersManager.INSTANCE.getUser().getId();
        this.lat = lat;
        this.log = log;
        this.ts = dateUtil.currentDate();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }
}

