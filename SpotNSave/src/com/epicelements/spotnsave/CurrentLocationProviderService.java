package com.epicelements.spotnsave;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class CurrentLocationProviderService extends Service
{
//    private ScheduledExecutorService scheduleTaskExecutor;

private static final String TAG = "BOOMBOOMTESTGPS";
private LocationManager mLocationManager = null;
private static final int LOCATION_INTERVAL = 1800000; //30 minutes
private static final float LOCATION_DISTANCE = 10f;


WakeLock mWakeLock2;



private class LocationListener implements android.location.LocationListener{
    Location mLastLocation;
    public LocationListener(String provider)
    {
        Log.e(TAG, "LocationListener " + provider);
        mLastLocation = new Location(provider);
    }
    
    
    
    
    
    
    
    @Override
    public void onLocationChanged(Location location)
    {
        Log.v(TAG, "onLocationChanged: " + location);
        mLastLocation.set(location);
        
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        Editor editor = pref.edit();
    	
        editor.putFloat("lat", (float) location.getLatitude());
        editor.putFloat("lng", (float) location.getLongitude());
        editor.putFloat("accuracy", (float) location.getAccuracy());
        editor.commit();
        
 //       Toast.makeText(CurrentLocationProviderService.this, "Current location is " + location, Toast.LENGTH_LONG).show();

        
    }
    
    
    
    
    
    
    
    
    
    @Override
    public void onProviderDisabled(String provider)
    {
        Log.e(TAG, "onProviderDisabled: " + provider);            
    }
    @Override
    public void onProviderEnabled(String provider)
    {
        Log.e(TAG, "onProviderEnabled: " + provider);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        Log.e(TAG, "onStatusChanged: " + provider);
    }
} 
LocationListener[] mLocationListeners = new LocationListener[] {
        new LocationListener(LocationManager.GPS_PROVIDER),
        new LocationListener(LocationManager.NETWORK_PROVIDER)
};
@Override
public IBinder onBind(Intent arg0)
{
    return null;
}
@Override
public int onStartCommand(Intent intent, int flags, int startId)
{
    Log.e(TAG, "onStartCommand");
    super.onStartCommand(intent, flags, startId);       
    
    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    mWakeLock2 = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    mWakeLock2.acquire();
    
    
//// do every 30 min
//    
//    
//    scheduleTaskExecutor= Executors.newScheduledThreadPool(3);
//
//
//    scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
//      public void run() {	      
//	     
//	        Log.v(TAG, "task executer started every 30 min");
//                                     
//	        Toast.makeText(CurrentLocationProviderService.this, "Current location is " + location, Toast.LENGTH_LONG).show();
//
//        // If you need update UI, simply do this:
//     //   runOnUiThread(new Runnable() {
//      //    public void run() {
//            // update your UI component here.
//        //    myTextView.setText("refreshed");
//         // }
//      //  });
//      }
//    }, 90, 60, TimeUnit.SECONDS);
//
//
//    
    
    return START_STICKY;
}
    
    
    

@Override
public void onCreate()
{
    Log.e(TAG, "onCreate");
    initializeLocationManager();
    try {
        mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                mLocationListeners[1]);
    } catch (java.lang.SecurityException ex) {
        Log.i(TAG, "fail to request location update, ignore", ex);
    } catch (IllegalArgumentException ex) {
        Log.d(TAG, "network provider does not exist, " + ex.getMessage());
    }
    try {
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                mLocationListeners[0]);
    } catch (java.lang.SecurityException ex) {
        Log.i(TAG, "fail to request location update, ignore", ex);
    } catch (IllegalArgumentException ex) {
        Log.d(TAG, "gps provider does not exist " + ex.getMessage());
    }
}
@Override
public void onDestroy()
{
    Log.e(TAG, "onDestroy");
    super.onDestroy();
    if (mLocationManager != null) {
        for (int i = 0; i < mLocationListeners.length; i++) {
            try {
                mLocationManager.removeUpdates(mLocationListeners[i]);
            } catch (Exception ex) {
                Log.i(TAG, "fail to remove location listners, ignore", ex);
            }
        }
    }
    if (mWakeLock2.isHeld())
    {
    mWakeLock2.release();
    }
} 
private void initializeLocationManager() {
    Log.e(TAG, "initializeLocationManager");
    if (mLocationManager == null) {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }
}
}