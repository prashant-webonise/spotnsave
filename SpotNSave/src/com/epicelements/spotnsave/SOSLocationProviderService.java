package com.epicelements.spotnsave;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.os.SystemClock;
import android.os.PowerManager.WakeLock;
import android.telephony.SmsManager;
import android.util.Log;
import com.luttu.AppPrefes;

public class SOSLocationProviderService extends Service {
	private ScheduledExecutorService scheduleTaskExecutor;
	private AppPrefes appPrefes;

	private static final String TAG = "BOOMBOOMTESTGPSSOS";
	private LocationManager mLocationManager = null;
	private static final int LOCATION_INTERVAL = 3000; // 3 seconds
	private static final float LOCATION_DISTANCE = 10f;

	public static final int NOTIFICATION_IDD = 5;
	public static NotificationManager myNotificationManager5;

	WakeLock mWakeLock;

	// ---sends an SMS message to another device---

	private void sendSMS(String phoneNumber, String message) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}

	private class LocationListener implements android.location.LocationListener {
		Location mLastLocation;

		public LocationListener(String provider) {
			Log.e(TAG, "LocationListener " + provider);
			mLastLocation = new Location(provider);
		}

		@Override
		public void onLocationChanged(Location location) {
			Log.v(TAG, "onLocationChanged: " + location);
			mLastLocation.set(location);

			SharedPreferences pref = getApplicationContext()
					.getSharedPreferences("MyPref", 0); // 0 - for private mode

			Editor editor = pref.edit();

			editor.putFloat("lat", (float) location.getLatitude());
			editor.putFloat("lng", (float) location.getLongitude());
			editor.putFloat("accuracy", (float) location.getAccuracy());
			// appPrefes.SaveData("accuracy", "" + location.getAccuracy());
			// appPrefes.SaveData("lat", "" + location.getLatitude());
			// appPrefes.SaveData("lng", "" + location.getLongitude());
			// System.out.println("serviiiiiiiiiiiiiceeee"+appPrefes.getData("accuracy"));
			Log.v(TAG, "onLocationChanged: " + location.getAccuracy());
			editor.commit();

			// Toast.makeText(CurrentLocationProviderService.this,
			// "Current location is " + location, Toast.LENGTH_LONG).show();

		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.e(TAG, "onProviderDisabled: " + provider);
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.e(TAG, "onProviderEnabled: " + provider);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.e(TAG, "onStatusChanged: " + provider);
		}
	}

	LocationListener[] mLocationListeners = new LocationListener[] {
			new LocationListener(LocationManager.GPS_PROVIDER),
			new LocationListener(LocationManager.NETWORK_PROVIDER) };

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand");
		super.onStartCommand(intent, flags, startId);

		appPrefes = new AppPrefes(getApplicationContext(), "sns");
		MyTimerTask myTimerTask = new MyTimerTask();
		Timer timer = new Timer();
		timer.schedule(myTimerTask, 1000, 1000);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
		mWakeLock.acquire();

		// // do every 30 min

		scheduleTaskExecutor = Executors.newScheduledThreadPool(3);

		scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {

			public void run() {
				Log.e(TAG, "task executer started every 2 min");

				// SharedPreferences pref = getApplicationContext()
				// .getSharedPreferences("MyPref", 0); // 0 - for private
				// mode

				// double accuracy = (double) pref.getFloat("accuracy", 0);
				// String accuracyString =
				// String.valueOf(accuracy).split("\\.")[0];
				// getting cooardinates from sharedPref

				// double lat = (double) pref.getFloat("lat", 0);
				// double lng = (double) pref.getFloat("lng", 0);

				// GPSTracker gpsTracker = new
				// GPSTracker(getApplicationContext());
				// double lat = gpsTracker.getLatitude();
				// double lng = gpsTracker.getLongitude();
				appPrefes = new AppPrefes(getApplicationContext(), "sns");
				appPrefes.SaveIntData("remainsecond", 120);
				String lat = appPrefes.getData("lat");
				String lng = appPrefes.getData("lng");
				String accuracyString = appPrefes.getData("accuracy");

				// float accuracy = gpsTracker.getAccuracy();
				// String accuracyString =
				// String.valueOf(accuracy).split("\\.")[0];
				String number1 = appPrefes.getData("gnumber1"); // getting
																// String
				String number2 = appPrefes.getData("gnumber2"); // getting
																// String
				String number3 = appPrefes.getData("gnumber3"); // getting
																// String
				String number4 = appPrefes.getData("gnumber4"); // getting
																// String
				String number5 = appPrefes.getData("gnumber5"); // getting
																// String
				String fullname = appPrefes.getData("fullnameregistration"); // getting
																				// String

				// Toast.makeText(SOSLocationProviderService.this,
				// "Current location is " + lat + lng + "accur " +
				// accuracyString + "meter.", Toast.LENGTH_LONG).show();
				Log.e(TAG, "Current location is " + lat + lng + "accur "
						+ accuracyString + "meter.");

				// send SOS

				// String number1 = pref.getString("gnumber1", null); // getting
				// // String
				// String number2 = pref.getString("gnumber2", null); // getting
				// // String
				// String number3 = pref.getString("gnumber3", null); // getting
				// // String
				// String number4 = pref.getString("gnumber4", null); // getting
				// // String
				// String number5 = pref.getString("gnumber5", null); // getting
				// // String
				// String fullname = pref.getString("fullnameregistration",
				// null); // getting
				// String

				// getting cooardinates from sharedPref

				String linktomaps = "http://maps.google.com/maps?q=" + lat
						+ "," + lng;

				if (number1 != null && !number1.equals("") && fullname == null) {
					sendSMS(number1,
							"I am in an emergency and I need help. Location "
									+ linktomaps + " Accuracy: "
									+ accuracyString + "m.");
				}
				if (number1 != null && !number1.equals("") && fullname != null) {
					sendSMS(number1, fullname
							+ " is in an emergency and needs help. Location "
							+ linktomaps + " Accuracy: " + accuracyString
							+ "m.");
				}
				if (number2 != null && !number2.equals("") && fullname == null) {
					sendSMS(number2,
							"I am in an emergency, and I need help. Location "
									+ linktomaps + " Accuracy: "
									+ accuracyString + "m.");
				}
				if (number2 != null && !number2.equals("") && fullname != null) {
					sendSMS(number2, fullname
							+ " is in an emergency and needs help. Location "
							+ linktomaps + " Accuracy: " + accuracyString
							+ "m.");
				}
				if (number3 != null && !number3.equals("") && fullname == null) {
					sendSMS(number3,
							"I am in an emergency, and I need help. Location "
									+ linktomaps + " Accuracy: "
									+ accuracyString + "m.");
				}
				if (number3 != null && !number3.equals("") && fullname != null) {
					sendSMS(number3, fullname
							+ " is in an emergency and needs help. Location "
							+ linktomaps + " Accuracy: " + accuracyString
							+ "m.");
				}
				if (number4 != null && !number4.equals("") && fullname == null) {
					sendSMS(number4,
							"I am in an emergency, and I need help. Location "
									+ linktomaps + " Accuracy: "
									+ accuracyString + "m.");
				}
				if (number4 != null && !number4.equals("") && fullname != null) {
					sendSMS(number4, fullname
							+ " is in an emergency and needs help. Location "
							+ linktomaps + " Accuracy: " + accuracyString
							+ "m.");
				}
				if (number5 != null && !number5.equals("") && fullname == null) {
					sendSMS(number5,
							"I am in an emergency, and I need help. Location "
									+ linktomaps + " Accuracy: "
									+ accuracyString + "m.");
				}
				if (number5 != null && !number5.equals("") && fullname != null) {
					sendSMS(number5, fullname
							+ " is in an emergency and needs help. Location "
							+ linktomaps + " Accuracy: " + accuracyString
							+ "m.");
				}

			}
		}, 110, 120, TimeUnit.SECONDS);

		return START_NOT_STICKY;
	}

	@Override
	public void onCreate() {
		Log.e(TAG, "onCreate");
		initializeLocationManager();
		try {
			mLocationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL,
					LOCATION_DISTANCE, mLocationListeners[1]);
		} catch (java.lang.SecurityException ex) {
			Log.i(TAG, "fail to request location update, ignore", ex);
		} catch (IllegalArgumentException ex) {
			Log.d(TAG, "network provider does not exist, " + ex.getMessage());
		}
		try {
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, LOCATION_INTERVAL,
					LOCATION_DISTANCE, mLocationListeners[0]);
		} catch (java.lang.SecurityException ex) {
			Log.i(TAG, "fail to request location update, ignore", ex);
		} catch (IllegalArgumentException ex) {
			Log.d(TAG, "gps provider does not exist " + ex.getMessage());
		}

		myNotificationManager5 = (NotificationManager) getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);

		CharSequence NotificationTicket = "SpotNSave SOS every 2 min";
		CharSequence NotificationTitle = "SOS will be sent every 2 min!";
		CharSequence NotificationContent = "To cancel all SOS please tap here";

		Notification notification = new Notification(R.drawable.pnggg,
				NotificationTicket, 0);
		Intent notificationIntent = new Intent(getApplicationContext(),
				AllSosStopped.class);
		notificationIntent.putExtra("cancel", "cancel");
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, notificationIntent, 0);
		notification.setLatestEventInfo(getApplicationContext(),
				NotificationTitle, NotificationContent, contentIntent);
		// notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		myNotificationManager5.notify(NOTIFICATION_IDD, notification);
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_SOUND;

	}

	@Override
	public void onDestroy() {
		if (scheduleTaskExecutor != null) {
			scheduleTaskExecutor.shutdownNow();
		}

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

		if (mWakeLock.isHeld()) {
			mWakeLock.release();
		}

	}

	private void initializeLocationManager() {
		Log.e(TAG, "initializeLocationManager");
		if (mLocationManager == null) {
			mLocationManager = (LocationManager) getApplicationContext()
					.getSystemService(Context.LOCATION_SERVICE);
		}
	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			int remtime = appPrefes.getIntData("remainsecond");
			if (remtime == 0) {
				remtime = 110;
			}
			remtime--;
			System.out.println("kittunna service remainsecond time" + remtime);
			appPrefes.SaveIntData("remainsecond", remtime);
		}
	}

}