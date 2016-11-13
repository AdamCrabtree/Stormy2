package com.example.user.stormy2;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class LocationFinder implements LocationListener {
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onLocationChanged(Location location) {
        int latitude = (int) (location.getLatitude());
        int longitude = (int) (location.getLongitude());

    }
}