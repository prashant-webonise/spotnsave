package com.epicelements.spotnsave;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.luttu.AppPrefes;

public class AllSosStopped extends SherlockActivity implements OnClickListener {

	// ---sends an SMS message to another device---

	private AppPrefes appPrefes;

	private void sendSMS(String phoneNumber, String message) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allsosstopped);
		appPrefes = new AppPrefes(this, "sns");
		stop();

		Intent intent1 = new Intent(AllSosStopped.this, Home.class);
		startActivity(intent1);
		finish();
		// appPrefes = new AppPrefes(this, "sns");
		// appPrefes.SaveData("sos", "deactive");
		//
		// Intent intent = new
		// Intent("com.epicelements.spotnsave.START_SERVICE4");
		// AllSosStopped.this.stopService(intent);
		//
		// SharedPreferences pref =
		// getApplicationContext().getSharedPreferences(
		// "MyPref", 0); // 0 - for private mode
		//
		// if (SOSLocationProviderService.myNotificationManager5 != null) {
		// SOSLocationProviderService.myNotificationManager5
		// .cancel(SOSLocationProviderService.NOTIFICATION_IDD);
		// }
		//
		// // send SOS canceled notice
		//
		// String number1 = pref.getString("gnumber1", null); // getting String
		// String number2 = pref.getString("gnumber2", null); // getting String
		// String number3 = pref.getString("gnumber3", null); // getting String
		// String number4 = pref.getString("gnumber4", null); // getting String
		// String number5 = pref.getString("gnumber5", null); // getting String
		// String fullname = pref.getString("fullnameregistration", null); //
		// getting
		// // String
		//
		// if (number1 != null && !number1.equals("") && fullname == null) {
		// sendSMS(number1,
		// "I have canceled the SOS and is no longer in an emergency.");
		// }
		// if (number1 != null && !number1.equals("") && fullname != null) {
		// sendSMS(number1, fullname
		// + " has cancelled the SOS and is no longer in an emergency");
		// }
		// if (number2 != null && !number2.equals("") && fullname == null) {
		// sendSMS(number2,
		// "I have canceled the SOS and is no longer in an emergency.");
		// }
		// if (number2 != null && !number2.equals("") && fullname != null) {
		// sendSMS(number2, fullname
		// + " has cancelled the SOS and is no longer in an emergency");
		// }
		// if (number3 != null && !number3.equals("") && fullname == null) {
		// sendSMS(number3,
		// "I have canceled the SOS and is no longer in an emergency.");
		// }
		// if (number3 != null && !number3.equals("") && fullname != null) {
		// sendSMS(number3, fullname
		// + " has cancelled the SOS and is no longer in an emergency");
		// }
		// if (number4 != null && !number4.equals("") && fullname == null) {
		// sendSMS(number4,
		// "I have canceled the SOS and is no longer in an emergency.");
		// }
		// if (number4 != null && !number4.equals("") && fullname != null) {
		// sendSMS(number4, fullname
		// + " has cancelled the SOS and is no longer in an emergency");
		// }
		// if (number5 != null && !number5.equals("") && fullname == null) {
		// sendSMS(number5,
		// "I have canceled the SOS and is no longer in an emergency.");
		// }
		// if (number5 != null && !number5.equals("") && fullname != null) {
		// sendSMS(number5, fullname
		// + " has cancelled the SOS and is no longer in an emergency");
		// }

	}

	private void stop() {
		// TODO Auto-generated method stub
		appPrefes.SaveData("sos", "deactive");
		if (appPrefes.getData("sos").equals("active")) {
			// bt_cntersos_view.setVisibility(View.VISIBLE);
			// im_cntersos_view.setVisibility(View.GONE);
			// tv1.setText("spotNsave is now Online");
			// tv1.setText("Emergency Alert: Activated");
			// im_cntersos.setVisibility(View.GONE);
		} else {
			// tv1.setText("spotNsave is now Offline");
			// tv1.setText("Emergency Alert: De-Activated");
			// bt_cntersos_view.setVisibility(View.GONE);
			// im_cntersos_view.setVisibility(View.VISIBLE);
			// im_cntersos.setVisibility(View.VISIBLE);
		}
		String fullname = appPrefes.getData("fullnameregistration"); // getting
		// String
		String number1 = appPrefes.getData("gnumber1"); // getting String
		String number2 = appPrefes.getData("gnumber2"); // getting String
		String number3 = appPrefes.getData("gnumber3"); // getting String
		String number4 = appPrefes.getData("gnumber4"); // getting String
		String number5 = appPrefes.getData("gnumber5"); // getting String
		System.out.println("fullnameeeeeeeeeeeee" + fullname);
		Intent intent = new Intent("com.epicelements.spotnsave.START_SERVICE4");
		AllSosStopped.this.stopService(intent);
		if (SOSLocationProviderService.myNotificationManager5 != null) {
			SOSLocationProviderService.myNotificationManager5
					.cancel(SOSLocationProviderService.NOTIFICATION_IDD);
		}
		if (!number1.equals(""))
			sendSMS(number1, fullname
					+ " has cancelled the SOS and is no longer in an emergency");
		if (!number2.equals(""))
			sendSMS(number2, fullname
					+ " has cancelled the SOS and is no longer in an emergency");
		if (!number3.equals(""))
			sendSMS(number3, fullname
					+ " has cancelled the SOS and is no longer in an emergency");
		if (!number4.equals(""))
			sendSMS(number4, fullname
					+ " has cancelled the SOS and is no longer in an emergency");
		if (!number5.equals(""))
			sendSMS(number5, fullname
					+ " has cancelled the SOS and is no longer in an emergency");

		// if (number1 != null && !number1.equals("") && fullname == null) {
		// sendSMS(number1,
		// "I have canceled the SOS and is no longer in an emergency.");
		// }
		// if (number1 != null && !number1.equals("") && fullname != null) {
		// sendSMS(number1, fullname
		// + " has cancelled the SOS and is no longer in an emergency");
		// }
		// if (number2 != null && !number2.equals("") && fullname == null) {
		// sendSMS(number2,
		// "I have canceled the SOS and is no longer in an emergency.");
		// }
		// if (number2 != null && !number2.equals("") && fullname != null) {
		// sendSMS(number2, fullname
		// + " has cancelled the SOS and is no longer in an emergency");
		// }
		// if (number3 != null && !number3.equals("") && fullname == null) {
		// sendSMS(number3,
		// "I have canceled the SOS and is no longer in an emergency.");
		// }
		// if (number3 != null && !number3.equals("") && fullname != null) {
		// sendSMS(number3, fullname
		// + " has cancelled the SOS and is no longer in an emergency");
		// }
		// if (number4 != null && !number4.equals("") && fullname == null) {
		// sendSMS(number4,
		// "I have canceled the SOS and is no longer in an emergency.");
		// }
		// if (number4 != null && !number4.equals("") && fullname != null) {
		// sendSMS(number4, fullname
		// + " has cancelled the SOS and is no longer in an emergency");
		// }
		// if (number5 != null && !number5.equals("") && fullname == null) {
		// sendSMS(number5,
		// "I have canceled the SOS and is no longer in an emergency.");
		// }
		// if (number5 != null && !number5.equals("") && fullname != null) {
		// sendSMS(number5, fullname
		// + " has cancelled the SOS and is no longer in an emergency");
		// }

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		super.onResume();
		// Toast.makeText(AllSosStopped.this,
		// "Please enter old and new passwords!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onBackPressed() {
		// Intent intent1 = new Intent(getApplicationContext(),
		// MapViewMain.class);
		// intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// intent1.putExtra("EXIT", true);
		// startActivity(intent1);
		// finish();

	}

}
