package com.epicelements.spotnsave;

import com.luttu.AppPrefes;
import com.luttu.GPSTracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.SmsManager;

public class BatteryNetworkReceiver extends BroadcastReceiver {
	public static final int NOTIFICATION_IDD = 3;
	private NotificationManager myNotificationManager2;
	private AppPrefes appPrefes;

	// ---sends an SMS message to another device---

	private void sendSMS(String phoneNumber, String message) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {

		boolean batteryLow = intent.getAction().equals(
				Intent.ACTION_BATTERY_LOW);
		boolean batteryok = intent.getAction().equals(
				Intent.ACTION_BATTERY_OKAY);

		SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0
																			// -
																			// for
																			// private
																			// mode
		Editor editor = pref.edit();

		appPrefes = new AppPrefes(context, "sns");
		boolean batterylowsmssent = pref.getBoolean("batterylowsmssent", false);

		if (batteryLow == true && batterylowsmssent == false) {

			editor.putBoolean("batterylowsmssent", true);
			editor.commit();

			myNotificationManager2 = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			CharSequence NotificationTicket = "SpotNSave Battery Warning";
			CharSequence NotificationTitle = "Battery is getting low! SoS sent";
			CharSequence NotificationContent = "Charge your battery soon!";

			Notification notification = new Notification(R.drawable.pnggg,
					NotificationTicket, 0);
			Intent notificationIntent = new Intent(context, Home.class);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
					notificationIntent, 0);
			notification.setLatestEventInfo(context, NotificationTitle,
					NotificationContent, contentIntent);
			// notification.flags |= Notification.FLAG_ONGOING_EVENT;
			myNotificationManager2.notify(NOTIFICATION_IDD, notification);
			notification.defaults |= Notification.DEFAULT_LIGHTS;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notification.defaults |= Notification.DEFAULT_SOUND;

			// String number1 = pref.getString("gnumber1", null); // getting
			// String
			// String number2 = pref.getString("gnumber2", null); // getting
			// String
			// String number3 = pref.getString("gnumber3", null); // getting
			// String
			// String number4 = pref.getString("gnumber4", null); // getting
			// String
			// String number5 = pref.getString("gnumber5", null); // getting
			// String
			String number1 = appPrefes.getData("gnumber1"); // getting String
			String number2 = appPrefes.getData("gnumber2"); // getting String
			String number3 = appPrefes.getData("gnumber3"); // getting String
			String number4 = appPrefes.getData("gnumber4"); // getting String
			String number5 = appPrefes.getData("gnumber5"); // getting String
			String fullname = appPrefes.getData("fullnameregistration"); // getting
																			// String
//			double accuracy = (double) pref.getFloat("accuracy", 0);
			// String fullname = pref.getString("fullnameregistration", null);
			// // getting String
			// getting cooardinates from sharedPref

//			double lat = (double) pref.getFloat("lat", 0);
//			double lng = (double) pref.getFloat("lng", 0);
			GPSTracker gpsTracker = new GPSTracker(context);
			double lat = gpsTracker.getLatitude();
			double lng = gpsTracker.getLongitude(); 
			
			String accuracyString = String.valueOf(gpsTracker.getAccuracy()).split("\\.")[0];
			String linktomaps = "http://maps.google.com/maps?q=" + lat + ","
					+ lng;

			if (number1 != null && !number1.equals("") && fullname == null) {
				sendSMS(number1, "My battery is low. Last known location is "
						+ linktomaps + " Accuracy: " + accuracyString + "m.");
			}
			if (number1 != null && !number1.equals("") && fullname != null) {
				sendSMS(number1, fullname
						+ "'s battery is low. Last know location is "
						+ linktomaps + " Accuracy: " + accuracyString + "m.");
			}
			if (number2 != null && !number2.equals("") && fullname == null) {
				sendSMS(number2, "My battery is low. Last known location is "
						+ linktomaps + " Accuracy: " + accuracyString + "m.");
			}
			if (number2 != null && !number2.equals("") && fullname != null) {
				sendSMS(number2, fullname
						+ "'s battery is low. Last know location is "
						+ linktomaps + " Accuracy: " + accuracyString + "m.");
			}
			if (number3 != null && !number3.equals("") && fullname == null) {
				sendSMS(number3, "My battery is low. Last known location is "
						+ linktomaps + " Accuracy: " + accuracyString + "m.");
			}
			if (number3 != null && !number3.equals("") && fullname != null) {
				sendSMS(number3, fullname
						+ "'s battery is low. Last know location is "
						+ linktomaps + " Accuracy: " + accuracyString + "m.");
			}
			if (number4 != null && !number4.equals("") && fullname == null) {
				sendSMS(number4, "My battery is low. Last known location is "
						+ linktomaps + " Accuracy: " + accuracyString + "m.");
			}
			if (number4 != null && !number4.equals("") && fullname != null) {
				sendSMS(number4, fullname
						+ "'s battery is low. Last know location is "
						+ linktomaps + " Accuracy: " + accuracyString + "m.");
			}
			if (number5 != null && !number5.equals("") && fullname == null) {
				sendSMS(number5, "My battery is low. Last known location is "
						+ linktomaps + " Accuracy: " + accuracyString + "m.");
			}
			if (number5 != null && !number5.equals("") && fullname != null) {
				sendSMS(number5, fullname
						+ "'s battery is low. Last know location is "
						+ linktomaps + " Accuracy: " + accuracyString + "m.");
			}

		}

		if (batteryLow == false) {
			if (myNotificationManager2 != null) {

				myNotificationManager2.cancel(3);
			}

		}

		if (batteryok == true) {
			if (myNotificationManager2 != null) {

				myNotificationManager2.cancel(3);
			}

			editor.putBoolean("batterylowsmssent", false);
			editor.commit();

		}

	}
}
