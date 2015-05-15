package com.epicelements.spotnsave;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
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

public class MyLocfragment extends FragmentActivity implements
		LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	private LocationRequest mLocationRequest;
	private LocationClient mLocationClient;
	boolean mUpdatesRequested = false;
	SharedPreferences mPrefs;
	SharedPreferences.Editor mEditor;
	AppPrefes appPrefes;
	MapView mMapView = null;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.myloc);
		appPrefes = new AppPrefes(this, "sns");
		mMapView = (MapView) findViewById(R.id.bmapview);
		mMapView.setVisibility(View.GONE);
		if (appPrefes.getData("country").equals("China")) {
			mMapView.setVisibility(View.VISIBLE);
			mMapView.getMap().setMyLocationEnabled(true);
			((ViewGroup) mMapView.getParent()).getChildAt(1).setVisibility(
					View.GONE);
		}
		create();
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
	protected void onDestroy() {
		super.onDestroy();
		// execute when activity execution onDestroy mMapView.onDestroy (), to
		// achieve the map lifecycle management
		mMapView.onDestroy();
	}

	@Override
	public void onStop() {
		mLocationClient.disconnect();
		super.onStop();
	}

	@Override
	public void onPause() {
		mMapView.onPause();
		mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED,
				mUpdatesRequested);
		mEditor.commit();
		super.onPause();
	}

	@Override
	public void onStart() {
		super.onStart();
		mLocationClient.connect();
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
		if (servicesConnected()) {
			Location currentLocation = mLocationClient.getLastLocation();
			if (currentLocation == null) {
				return;
			}
			setlat(currentLocation);
			// mLatLng.setText(LocationUtils.getLatLng(this, currentLocation));
		}
	}

	private void setlat(Location currentLocation) {
		// TODO Auto-generated method stub

		double lat = currentLocation.getLatitude();
		double lng = currentLocation.getLongitude();
		LatLng coordinate = new LatLng(lat, lng);
		appPrefes.SaveData("lat", "" + currentLocation.getLatitude());
		appPrefes.SaveData("lng", "" + currentLocation.getLongitude());
		appPrefes.SaveData("accuracy", "" + currentLocation.getAccuracy());
		System.out.println("assdagfadfssdafasdhgfasdghfadshgf"
				+ appPrefes.getData("accuracy"));
		((Mylocation) getSupportFragmentManager().findFragmentById(
				R.id.fragment1)).setmarker(coordinate);
		MyLocationData locData = new MyLocationData.Builder()
		// Set the developers here to get to the direction information,
		// along 0-360-clockwise
				.direction(100).latitude(lat).longitude(lng).build();
		// Set the positioning data
		mMapView.getMap().setMyLocationData(locData);
		locicon(lat, lng);
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
		OverlayOptions option = new MarkerOptions().position(Point)
				.icon(descriptor);
		mMapView.getMap().addOverlay(option);
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
