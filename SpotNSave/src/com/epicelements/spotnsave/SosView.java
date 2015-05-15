package com.epicelements.spotnsave;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.luttu.AppPrefes;
import com.luttu.PictureOrentation;
import com.makeramen.RoundedImageView;
import com.util.ImageSmallerAction;
import com.util.LocationUtils;

public class SosView extends FragmentActivity implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	AppPrefes appPrefes;
	private LocationRequest mLocationRequest;
	private LocationClient mLocationClient;
	boolean mUpdatesRequested = false;
	SharedPreferences mPrefs;
	SharedPreferences.Editor mEditor;
	final Activity activity = this;
	MapView mMapView = null;

	// final MyCount counter = new MyCount(12000,1000);
	// this means that we count from 11.000 to 0 with 1000ms per second...so it
	// will be displayed...12...11...10...etc
	MyCount counter1 = new MyCount(6000, 1000);// instantiate counter

	private PowerManager.WakeLock wl;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.sosview);
		create();

		appPrefes = new AppPrefes(this, "sns");
		mMapView = (MapView) findViewById(R.id.bmapview);
		mMapView.setVisibility(View.GONE);
		if (appPrefes.getData("country").equals("China")) {
			mMapView.setVisibility(View.VISIBLE);
			mMapView.getMap().setMyLocationEnabled(true);
			((ViewGroup) mMapView.getParent()).getChildAt(1).setVisibility(
					View.GONE);
		}
		// check if logged in when band press, otherwise do not send SOS

		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				"MyPref", 0); // 0 - for private mode

		Boolean loggedin = pref.getBoolean("loggedIn", false); // getting
																// boolean

		if (loggedin == false) {

			// wake lock stuff
			// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
			// |
			this.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
							| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
							| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
					// WindowManager.LayoutParams.FLAG_FULLSCREEN |
					WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
							| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
							| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My wakelock");
			wl.acquire();

			// user is not logged in redirect him to Login Activity
			// Intent i = new Intent(getBaseContext(), Main.class);
			// Closing all the Activities
			// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			// getBaseContext().startActivity(i);

			finish();
		}

	}

	ImageSmallerAction imageSmallerAction = new ImageSmallerAction();

	private void locicon(double lat, double lng) {
		// TODO Auto-generated method stub
		final View cluster = LayoutInflater.from(this).inflate(
				R.layout.roundede_place, null);
		RoundedImageView im_profile_round = (RoundedImageView) cluster
				.findViewById(R.id.im_profile_round);
		im_profile_round.setBorderWidth(0);
		im_profile_round.setOval(false);

		float scale = getResources().getDisplayMetrics().density;
		int imageSizeWidthPX = (int) ((80 * scale) + 0.5);
		int imageSizeHeightPX = (int) ((80 * scale) + 0.5);
		if (!appPrefes.getData("ggcmain").equals("")) {
			if (appPrefes.getData("ggcmain").equals("cam")) {
				im_profile_round.setVisibility(View.VISIBLE);
				PictureOrentation orentation = new PictureOrentation();
				orentation.change(appPrefes.getData("gimagemain"),
						im_profile_round, this);
			} else if (appPrefes.getData("ggcmain").equals("gallery")) {
				im_profile_round.setVisibility(View.VISIBLE);
				Bitmap bitmap = imageSmallerAction
						.decodeSampledBitmapFromGallery(
								appPrefes.getData("gimagemain"), 100, 100);
				im_profile_round.setImageBitmap(ImageSmallerAction
						.getCircleBitmap(bitmap, imageSizeWidthPX,
								imageSizeHeightPX));
			}
		}
		cluster.measure(
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		cluster.layout(0, 0, cluster.getMeasuredWidth(),
				cluster.getMeasuredHeight());
		Bitmap clusterBitmap = Bitmap.createBitmap(cluster.getMeasuredWidth(),
				cluster.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(clusterBitmap);
		cluster.draw(canvas);
		com.baidu.mapapi.model.LatLng Point = new com.baidu.mapapi.model.LatLng(
		// 39.963175, 116.400244);
				lat, lng);
		BitmapDescriptor Bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.marker);
		BitmapDescriptor descriptor = BitmapDescriptorFactory
				.fromBitmap(clusterBitmap);
		OverlayOptions option = new MarkerOptions().position(Point).icon(
				descriptor);
		mMapView.getMap().addOverlay(option);
	}

	@Override
	public void onResume() {
		super.onResume();

		mMapView.onResume();
		if (mPrefs.contains(LocationUtils.KEY_UPDATES_REQUESTED)) {
			mUpdatesRequested = mPrefs.getBoolean(
					LocationUtils.KEY_UPDATES_REQUESTED, false);
		} else {
			mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
			mEditor.commit();
		}
		// wake lock stuff
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
		// |
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
				// WindowManager.LayoutParams.FLAG_FULLSCREEN |
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My wakelock");
		wl.acquire();

		// getting cooardinates from sharedPref

		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				"MyPref", 0); // 0 - for private mode
		// Editor editor = pref.edit();

		// double lat = (double)pref.getFloat("lat", 0);
		// double lng = (double)pref.getFloat("lng", 0);
		// GPSTracker gpsTracker = new GPSTracker(activity);
		// double lat = gpsTracker.getLatitude();
		// double lng = gpsTracker.getLongitude();

		// LatLng coordinate = new LatLng(lat, lng);

		// start address resolutino from coordianates

		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// stop counter
				counter1.cancel();
				finish();
			}
		});

		counter1.start();// counter start as soon as app is opened...you can put
							// this in a OnClickListener also.
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// execute when activity execution onDestroy mMapView.onDestroy (), to
		// achieve the map lifecycle management
		mMapView.onDestroy();
	}

	public class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			appPrefes.SaveData("sos", "active");
			TextView scor = (TextView) findViewById(R.id.textView1);
			TextView initiate = (TextView) findViewById(R.id.textView2);
			TextView sent = (TextView) findViewById(R.id.textView3);
			Button button = (Button) findViewById(R.id.button1);
			button.setVisibility(View.GONE);
			initiate.setText("");
			sent.setText("sent!!!");
			scor.setText("SOS");// this is the message displayed at the finish
								// of the counting...can be Game Over ..etc..
			Animation anim = new AlphaAnimation(0.0f, 1.0f);
			anim.setDuration(200); // You can manage the time of the blink with
									// this parameter
			anim.setStartOffset(20);
			anim.setRepeatMode(Animation.REVERSE);
			anim.setRepeatCount(Animation.INFINITE);
			scor.startAnimation(anim);

			SharedPreferences pref = getApplicationContext()
					.getSharedPreferences("MyPref", 0); // 0 - for private mode

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
			// String addresstext = pref.getString("addressText", null); //
			// getting String
			String number1 = appPrefes.getData("gnumber1"); // getting String
			String number2 = appPrefes.getData("gnumber2"); // getting String
			String number3 = appPrefes.getData("gnumber3"); // getting String
			String number4 = appPrefes.getData("gnumber4"); // getting String
			String number5 = appPrefes.getData("gnumber5"); // getting String
			String fullname = appPrefes.getData("fullnameregistration"); // getting
																			// String
																			// double
																			// accuracy
																			// =
																			// (double)
																			// pref.getFloat("accuracy",
																			// 0);
			// String accuracyString = String.valueOf(accuracy).split("\\.")[0];
			// String fullname = pref.getString("fullnameregistration", null);
			// // getting
			// String
			// getting cooardinates from sharedPref

			// double lat = (double) pref.getFloat("lat", 0);
			// double lng = (double) pref.getFloat("lng", 0);

			// GPSTracker gpsTracker = new GPSTracker(activity);
			// double lat = gpsTracker.getLatitude();
			// double lng = gpsTracker.getLongitude();

			String lat = appPrefes.getData("lat");
			String lng = appPrefes.getData("lng");
			String accuracyString = appPrefes.getData("accuracy");
			String linktomaps = "http://maps.google.com/maps?q=" + lat + ","
					+ lng;

			if (number1 != null && !number1.equals("") && fullname == null) {
				sendSMS(number1,
						"I am in an emergency and I need help. Location "
								+ linktomaps + " Accuracy: " + accuracyString
								+ "m.");
			}
			if (number1 != null && !number1.equals("") && fullname != null) {
				sendSMS(number1, fullname
						+ " is in an emergency and needs help. Location "
						+ linktomaps + " Accuracy: " + accuracyString + "m.");
			}
			if (number2 != null && !number2.equals("") && fullname == null) {
				sendSMS(number2,
						"I am in an emergency, and I need help. Location "
								+ linktomaps + " Accuracy: " + accuracyString
								+ "m.");
			}
			if (number2 != null && !number2.equals("") && fullname != null) {
				sendSMS(number2, fullname
						+ " is in an emergency and needs help. Location "
						+ linktomaps + " Accuracy: " + accuracyString + "m.");
			}
			if (number3 != null && !number3.equals("") && fullname == null) {
				sendSMS(number3,
						"I am in an emergency, and I need help. Location "
								+ linktomaps + " Accuracy: " + accuracyString
								+ "m.");
			}
			if (number3 != null && !number3.equals("") && fullname != null) {
				sendSMS(number3, fullname
						+ " is in an emergency and needs help. Location "
						+ linktomaps + " Accuracy: " + accuracyString + "m.");
			}
			if (number4 != null && !number4.equals("") && fullname == null) {
				sendSMS(number4,
						"I am in an emergency, and I need help. Location "
								+ linktomaps + " Accuracy: " + accuracyString
								+ "m.");
			}
			if (number4 != null && !number4.equals("") && fullname != null) {
				sendSMS(number4, fullname
						+ " is in an emergency and needs help. Location "
						+ linktomaps + " Accuracy: " + accuracyString + "m.");
			}
			if (number5 != null && !number5.equals("") && fullname == null) {
				sendSMS(number5,
						"I am in an emergency, and I need help. Location "
								+ linktomaps + " Accuracy: " + accuracyString
								+ "m.");
			}
			if (number5 != null && !number5.equals("") && fullname != null) {
				sendSMS(number5, fullname
						+ " is in an emergency and needs help. Location "
						+ linktomaps + " Accuracy: " + accuracyString + "m.");
			}

			// start Service to resend location every 2 minutes
			appPrefes.SaveIntData("remainsecond", 110);
			Intent intent = new Intent(
					"com.epicelements.spotnsave.START_SERVICE4");
			getApplicationContext().startService(intent);

			counter1.cancel();
			timer();
		}

		private void timer() {
			// TODO Auto-generated method stub
			new CountDownTimer(7000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					finish();
				}
			}.start();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			TextView scor = (TextView) findViewById(R.id.textView1);
			scor.setText(String.valueOf(millisUntilFinished / 1000));
			// here we update the text with seconds until 0.
			// Get instance of Vibrator from current Context
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

			// This example will cause the phone to vibrate "SOS" in Morse Code
			// In Morse Code, "s" = "dot-dot-dot", "o" = "dash-dash-dash"
			// There are pauses to separate dots/dashes, letters, and words
			// The following numbers represent millisecond lengths
			int dot = 200; // Length of a Morse Code "dot" in milliseconds
			// int dash = 500; // Length of a Morse Code "dash" in milliseconds
			// int short_gap = 200; // Length of Gap Between dots/dashes
			// int medium_gap = 500; // Length of Gap Between Letters
			// int long_gap = 1000; // Length of Gap Between Words
			long[] pattern = { 800, // Start after 0.5 second
					// dot, short_gap, dot, short_gap, dot, // s
					dot
			// medium_gap,
			// dash, short_gap, dash, short_gap, dash, // o
			// medium_gap,
			// dot, short_gap, dot, short_gap, dot, // s
			// long_gap
			};

			// Only perform this pattern one time (-1 means "do not repeat")
			v.vibrate(pattern, -1);

		}

	}

	@Override
	public void onBackPressed() {
		// stop counter
		counter1.cancel();
		finish();
	}

	private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {
		Context mContext;

		public ReverseGeocodingTask(Context context) {
			super();
			mContext = context;
		}

		// Finding address using reverse geocoding
		protected String doInBackground(LatLng... params) {
			Geocoder geocoder = new Geocoder(mContext);
			double latitude = params[0].latitude;
			double longitude = params[0].longitude;

			List<Address> addresses = null;
			String addressText = "";

			try {
				addresses = geocoder.getFromLocation(latitude, longitude, 1);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);

				// addressText = String.format("%s, %s, %s",
				// address.getMaxAddressLineIndex() > 0 ?
				// address.getAddressLine(0) : "",
				// address.getLocality(),
				// address.getCountryName()
				// );
				addressText = String.format(
						"%s, %s",
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "", address.getLocality());
			}

			return addressText;
		}

		@Override
		protected void onPostExecute(String addressText) {
			// Setting the title for the marker.
			// This will be displayed on taping the marker
			// markerOptions.title(addressText);
			SharedPreferences pref = getApplicationContext()
					.getSharedPreferences("MyPref", 0); // 0 - for private mode
			Editor editor = pref.edit();
			editor.putString("addressText", addressText); // Storing string
			editor.commit(); // commit changes

			// double accuracy = (double) pref.getFloat("accuracy", 0);
			// String accuracyString = String.valueOf(accuracy);
			// String accuracyFinal = accuracyString.split("\\.")[0];

			String accuracyString = appPrefes.getData("accuracy");

			if (addressText != null && addressText != "" && addressText != " ") {
				Toast.makeText(
						SosView.this,
						"Detected Address: " + addressText
								+ " with an acccuracy of about "
								+ accuracyString + " meter", Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	// ---sends an SMS message to another device---

	private void sendSMS(String phoneNumber, String message) {
		try {
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phoneNumber, null, message, null, null);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	protected void onStop() {

		mLocationClient.disconnect();
		// releasing the wakelock
		if (wl != null && wl.isHeld() == true) {
			try {
				wl.release();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

		}
		super.onStop();

	}

	protected void onPause() {
		// releasing the wakelock

		mMapView.onPause();
		mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED,
				mUpdatesRequested);
		mEditor.commit();
		if (wl != null && wl.isHeld() == true) {
			try {
				wl.release();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

		}
		super.onPause();

	}

	private void create() {
		mLocationRequest = LocationRequest.create();
		mLocationRequest
				.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest
				.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
		mPrefs = getSharedPreferences(LocationUtils.SHARED_PREFERENCES,
				Context.MODE_PRIVATE);
		mEditor = mPrefs.edit();
		mUpdatesRequested = false;
		mLocationClient = new LocationClient(this, this, this);
	}

	@Override
	public void onStart() {
		super.onStart();
		mLocationClient.connect();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

		// Choose what to do based on the request code
		switch (requestCode) {

		// If the request code matches the code sent in onConnectionFailed
		case LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST:

			switch (resultCode) {
			// If Google Play services resolved the problem
			case Activity.RESULT_OK:

			default:

				break;
			}

			// If any other request code was received
		default:

			break;
		}
	}

	private boolean servicesConnected() {

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {

			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Display an error dialog
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
					this, 0);
			if (dialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(dialog);
				errorFragment.show(getSupportFragmentManager(),
						LocationUtils.APPTAG);
			}
			return false;
		}
	}

	public void getLocation() {
		try {
			if (servicesConnected()) {
				Location currentLocation = mLocationClient.getLastLocation();
				if (currentLocation == null) {
					return;
				}
				setlat(currentLocation);
				// mLatLng.setText(LocationUtils.getLatLng(this,
				// currentLocation));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void setlat(Location currentLocation) {
		// TODO Auto-generated method stub
		double lat = currentLocation.getLatitude();
		double lng = currentLocation.getLongitude();
		LatLng coordinate = new LatLng(lat, lng);
		new ReverseGeocodingTask(getBaseContext()).execute(coordinate);
		appPrefes.SaveData("lat", "" + currentLocation.getLatitude());
		appPrefes.SaveData("lng", "" + currentLocation.getLongitude());
		appPrefes.SaveData("accuracy", "" + currentLocation.getAccuracy());
		System.out.println("assdagfadfssdafasdhgfasdghfadshgf"
				+ appPrefes.getData("accuracy"));
		((ViewAddressDialog) getSupportFragmentManager().findFragmentById(
				R.id.fragment1)).setmarker(coordinate);
		locicon(lat, lng);
	}

	@Override
	public void onConnected(Bundle bundle) {
	}

	@Override
	public void onDisconnected() {
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

		if (connectionResult.hasResolution()) {
			try {

				connectionResult.startResolutionForResult(this,
						LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

			} catch (IntentSender.SendIntentException e) {

				e.printStackTrace();
			}
		} else {
			showErrorDialog(connectionResult.getErrorCode());
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// mLatLng.setText(LocationUtils.getLatLng(this, location));
		appPrefes.SaveData("lat", "" + location.getLatitude());
		appPrefes.SaveData("lng", "" + location.getLongitude());
		appPrefes.SaveData("accuracy", "" + location.getAccuracy());
	}

	private void showErrorDialog(int errorCode) {

		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
				this, LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

		if (errorDialog != null) {
			ErrorDialogFragment errorFragment = new ErrorDialogFragment();

			errorFragment.setDialog(errorDialog);

			errorFragment.show(getSupportFragmentManager(),
					LocationUtils.APPTAG);
		}
	}

	public static class ErrorDialogFragment extends DialogFragment {

		private Dialog mDialog;

		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}
}
