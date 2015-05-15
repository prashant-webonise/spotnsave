package com.epicelements.spotnsave;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class BatteryNetworkService extends Service {
	 
	BroadcastReceiver bReceiver = new BatteryNetworkReceiver();
    
    @Override
    public void onCreate() {
        super.onCreate();
//        // REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC      
        final IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION); //"android.net.conn.CONNECTIVITY_CHANGE"
        filter.addAction("android.intent.action.BATTERY_LOW"); //"android.net.conn.CONNECTIVITY_CHANGE"
        filter.addAction("android.intent.action.BATTERY_OKAY");

        registerReceiver(bReceiver, filter);
        
        
    }

    @Override
    public void onStart(Intent intent, int startId) {
    //    boolean screenOn = intent.getBooleanExtra("sosstate", false);
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//      String sosstart = pref.getString("sos", null); // getting String

    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
	    return Service.START_STICKY;
	  }

    @Override
    public void onDestroy() {
        unregisterReceiver(bReceiver);
        Toast.makeText(this, "Battery low SOS disabled", Toast.LENGTH_SHORT).show();
    }
}