package com.myapplicationdev.android.p10_ps;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyLocationService extends Service {

    LocationCallback mLocationCallback;
    String folderLocation;
    FusedLocationProviderClient client;

    public MyLocationService() {
    }

    boolean started;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("Service", "Service created");
        super.onCreate();
        client = LocationServices.getFusedLocationProviderClient(this);
        String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder";
        File folder = new File(folderLocation);

        if (folder.exists() == false) {
            boolean result = folder.mkdir();
            if (result == false) {
                Toast.makeText(MyLocationService.this, "Folder cant be created in External memory," + "Service exiting", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (checkPermission() == true) {

            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        Location locData = locationResult.getLastLocation();
                        String data = locData.getLatitude() + ", " + locData.getLongitude();
                        Log.d("Service - Loc Changed", data);
                        String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder";
                        File targetFile = new File(folderLocation, "Locations.txt");
                        try {
                            FileWriter writer = new FileWriter(targetFile, true);
                            writer.write(data + "\n");
                            writer.flush();
                            writer.close();
                        } catch (IOException e) {
                            Toast.makeText(MyLocationService.this, "Failed to write!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }
            };

            Log.d("Service", "Service started");
            final LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setSmallestDisplacement(100);

            client = LocationServices.getFusedLocationProviderClient(getApplicationContext());

            folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder";

            Log.i("Location Call back", String.valueOf(mLocationCallback));

            File targetFile = new File(folderLocation, "Locations.txt");
        } else {
            Log.d("Service", "Service is still running");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("Service", "Service exited");
        super.onDestroy();
    }

    private boolean checkPermission(){
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}