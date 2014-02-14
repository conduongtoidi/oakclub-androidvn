package com.oakclub.android.view;

import com.oakclub.android.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

public class GPSTracker implements LocationListener {

    private final Context context;
    public boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;
    
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;
    
    public GPSTracker(Context context) {
        this.context = context;
        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        //getLocation();
    }

    public Location getLocation(){
    	location = null;
    	isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled) {
            if(getLocationFromGPS() != null)
                location = getLocationFromGPS();
            else if(getLocationFromNetwork() != null)
                location = getLocationFromNetwork();    
        }
        else showSettingsAlert();
        return location;
    }
    
    private Location getLocationFromNetwork(){
        Location locationNetwork = null;
        try {
            if(locationManager == null)
                locationManager = (LocationManager) context
                        .getSystemService(Context.LOCATION_SERVICE);
            else{
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                Log.d("Network", "Network");
                if (locationManager != null) {
                    locationNetwork = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (locationNetwork != null) {
                        latitude = locationNetwork.getLatitude();
                        longitude = locationNetwork.getLongitude();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationNetwork;
        
    }
    
    private Location getLocationFromGPS(){
        Location locationGPS = null;
        try {
            if(locationManager ==null)
                locationManager = (LocationManager) context
                        .getSystemService(Context.LOCATION_SERVICE);
            else{
//                isGPSEnabled = locationManager
//                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
//                if (isGPSEnabled) {
                    if (locationGPS == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            locationGPS = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (locationGPS != null) {
                                latitude = locationGPS.getLatitude();
                                longitude = locationGPS.getLongitude();
                            }
                        }
                    }
//                }
//                else showSettingsAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationGPS;
    }
    
    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     * */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }
    
    /**
     * Function to get latitude
     * */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     * 
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     * */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(context.getString(R.string.txt_gps_settings_title));
        // Setting Dialog Message
        alertDialog.setMessage(context.getString(R.string.txt_gps_settings_message));
        // On pressing Settings button
        alertDialog.setPositiveButton(context.getString(R.string.txt_settings),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton(context.getString(R.string.txt_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }
    
    public Location getLocation2() {
        try {
            locationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            Log.v("isGPSEnabled", "=" + isGPSEnabled);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Log.v("isNetworkEnabled", "=" + isNetworkEnabled);

            if (isGPSEnabled == false && isNetworkEnabled == false) {
                showSettingsAlert();
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    location=null;
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    location=null;
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }
    
    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}
