package io.cleaninsights.demo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import io.cleaninsights.sdk.piwik.thresholds.BaseThreshold;

/**
 * Created by n8fr8 on 4/5/17.
 */

public class GeoFenceThreshold extends BaseThreshold {

    private LocationManager locationManager;
    private Context context;

    private Location locationNear;
    private float distanceLimit;

    /**
    * A custom Threshold example for geofencing
     */
    public GeoFenceThreshold(boolean required, Context context, Location locationNear, float distanceLimit)
    {
        super (required);

        this.context = context;
        this.locationNear = locationNear;
        this.distanceLimit = distanceLimit;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public boolean allowMeasurement() {

        if (checkLocationPermission()) {

            //get the last good current location
            Location locationNow = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            //check the distance between this location, and the user provided one
            float distanceNow = locationNow.distanceTo(locationNear);

            //if within distance, then allow measurement
            if (distanceNow <= distanceLimit)
                return true;

        }

        return false;
    }

    private boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_COARSE_LOCATION";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}
