package com.example.andrey.client1.network;

public class CheckNetwork {
    private boolean isNetworkInsideOrOutside;

    public boolean isNetworkInsideOrOutside() {
        return isNetworkInsideOrOutside;
    }

    public void setNetworkInsideOrOutside(boolean networkInsideOrOutside) {
        isNetworkInsideOrOutside = networkInsideOrOutside;
    }

    public static final CheckNetwork instance = new CheckNetwork();
    private CheckNetwork(){}
}
