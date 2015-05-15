package com.blecomm.utils;


import java.util.Random;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.blecomm.main.BluetoothDeviceActor;
import com.epicelements.spotnsave.R;

public class Utils {

	public final static String ACTION_GATT_CONNECTED = "ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "ACTION_GATT_DISCONNECTED";
	public final static String ACTION_DEVICE_NOT_FOUND = "ACTION_DEVICE_NOT_FOUND";
	public final static String ACTION_CONNECT_FAIL = "ACTION_CONNECT_FAIL";
	public final static String ACTION_EVENT_TRIGGER = "ACTION_EVENT_TRIGGER";
	public final static String ACTION_EVENT_TRIGGER_DOUBLECLICK = "ACTION_EVENT_TRIGGER_DOUBLECLICK";
	public final static String ACTION_EVENT_TRIGGER_UNKNOWNCLICK = "ACTION_EVENT_TRIGGER_UNKNOWNCLICK";
	public final static String ACTION_BATTERY_LEVEL = "ACTION_BATTERY_LEVEL";
	public static BluetoothAdapter btAdapter;
	
	public static int batteryval;

	public static IntentFilter makeIntentFilter() {

		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_GATT_CONNECTED);
		filter.addAction(ACTION_GATT_DISCONNECTED);
		filter.addAction(ACTION_DEVICE_NOT_FOUND);
		filter.addAction(ACTION_CONNECT_FAIL);
		filter.addAction(ACTION_EVENT_TRIGGER);
		filter.addAction(ACTION_EVENT_TRIGGER_DOUBLECLICK);
		filter.addAction(ACTION_EVENT_TRIGGER_UNKNOWNCLICK);
		filter.addAction(ACTION_BATTERY_LEVEL);
		return filter;
	}

	public static BluetoothDeviceActor BDA;

	public static BluetoothDeviceActor getBDA() {
		return BDA;
	}

	public static void setBDA(BluetoothDeviceActor bDA) {
		BDA = bDA;
	}

	public static BluetoothAdapter getBTAdapter() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		return btAdapter;
	}

	/**
	 * Checking whether net connection is available or not.
	 * 
	 * @param nContext
	 * @return true if net connection is avaible otherwise false
	 */
	public static boolean isNetworkAvailable(Context nContext) {
		boolean isNetAvailable = false;
		if (nContext != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) nContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (mConnectivityManager != null) {
				boolean mobileNetwork = false;
				boolean wifiNetwork = false;

				boolean mobileNetworkConnecetd = false;
				boolean wifiNetworkConnecetd = false;

				NetworkInfo mobileInfo = mConnectivityManager
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo wifiInfo = mConnectivityManager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

				if (mobileInfo != null)
					mobileNetwork = mobileInfo.isAvailable();

				if (wifiInfo != null)
					wifiNetwork = wifiInfo.isAvailable();

				if (wifiNetwork == true || mobileNetwork == true) {
					if (mobileInfo != null)
						mobileNetworkConnecetd = mobileInfo
								.isConnectedOrConnecting();
					if (wifiInfo != null)
						wifiNetworkConnecetd = wifiInfo
								.isConnectedOrConnecting();
				}

				isNetAvailable = (mobileNetworkConnecetd || wifiNetworkConnecetd);
			}
		}
		return isNetAvailable;
	}


	public static void showOKAlertMsg(String title, String msg, Context context) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setNeutralButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		dialogBuilder.setTitle(title);
		dialogBuilder.setMessage(msg);
		dialogBuilder.show();
	}

	public static String padIt(String it) {
		String s = new String();
		StringTokenizer Tok = new StringTokenizer(it);
		String t = null;

		while (Tok.hasMoreElements()) {
			t = (String) Tok.nextElement();
			if (!t.equals("0") && !t.equals("00")) {
				if (t.length() == 1) {
					s = s + "0" + t;
				} else {
					s = s + t;
				}
			}
		}
		// SecuRemote.LOG( "it: " + s);
		return s;
	}

	public static void setBatteryValue(int batteryval1)
	{
		batteryval = batteryval1;
	}
	
	public static int getBatteryValue()
	{
		return batteryval;
	}
	/*@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public static void generateNotification(Context context, String title,
			String msg) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);

		
		Random rand = new Random();
		int pickedNumber = rand.nextInt(1000);

			
		Notification n = new Notification.Builder(context)
				.setDefaults(Notification.DEFAULT_SOUND)
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setContentTitle(title)
				.setContentText(msg)
				.setSmallIcon(R.drawable.icon).build();

		notificationManager.notify(pickedNumber, n);
	}*/

}
