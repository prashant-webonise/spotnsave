package com.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.epicelements.spotnsave.R;
import com.google.android.gms.maps.model.LatLng;

public class GpsGet implements LocationListener {
	Context context;
	protected CheckBox checkbox;
	private Dialog dialog2;

	private LocationManager locationManager;

	public GpsGet(Context con) {
		// TODO Auto-generated constructor stub
		context = con;
		location();
	}

	private void location() {
		// TODO Auto-generated method stub
		SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0
																			// -
																			// for
																			// private
																			// mode

		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		boolean gps_enabled = false;
		try {
			gps_enabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
			// Log.e("MainActivity", ex.getMessage());
		}

		if (!gps_enabled) {
			boolean gpsdialog = pref.getBoolean("gpsdialog", true); // default
																	// is true
			if (gpsdialog == true) {
				dialog2 = createDialog(context);
				dialog2.show();
			}
		}

		// Creating a criteria object to retrieve provider

		Criteria criteria = new Criteria();

		// Getting the name of the best provider

		String provider = locationManager.getBestProvider(criteria, true);

		Location location = null;
		if (provider != null) {

			// Getting Current Location

			location = locationManager.getLastKnownLocation(provider);

		}

		if (location != null) {

			onLocationChanged(location);

		}

		locationManager.requestLocationUpdates(provider, 20000, 0, this);
	}

	@Override
	public void onLocationChanged(Location location) {

		// Getting latitude of the current location

		double latitude = location.getLatitude();

		// Getting longitude of the current location

		double longitude = location.getLongitude();

		System.out.println("lat" + latitude);
		System.out.println("lat" + longitude);
		// Creating a LatLng object for the current location

		LatLng latLng = new LatLng(latitude, longitude);

		// Showing the current location in Google Map

		// mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate,
		// 16));
		// Set zoom
		// mMap.moveCamera(CameraUpdateFactory.zoomTo(15f));

		// save coordinate

		SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0
																			// -
																			// for
																			// private
																			// mode
		Editor editor = pref.edit();

		editor.putFloat("lat", (float) location.getLatitude());
		editor.putFloat("lng", (float) location.getLongitude());
		editor.putFloat("accuracy", (float) location.getAccuracy());
		editor.commit();

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "provider disabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "provider enabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "status changed", Toast.LENGTH_SHORT).show();
	}

	// Alarm Dialog if GPS not enabled
	public Dialog createDialog(final Context context) {
		LayoutInflater inflater = ((FragmentActivity) context)
				.getLayoutInflater();
		View neverShow = inflater.inflate(R.layout.never_show, null);
		checkbox = (CheckBox) neverShow.findViewById(R.id.checkbox);

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(neverShow)
				.setTitle(R.string.location_settings_title)
				.setMessage(R.string.location_instructions)
				.setPositiveButton(R.string.location_settings,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (checkbox.isChecked()) {
									doNotShowAgain(context);
								}
								Intent myIntent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								context.startActivity(myIntent);
							}
						})
				.setNegativeButton(R.string.location_skip,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
								if (checkbox.isChecked()) {
									doNotShowAgain(context);
								}
							}
						});
		// Create the AlertDialog object and return it
		return builder.create();
	}

	private void doNotShowAgain(Context context) {
		// persist shared preference to prevent dialog from showing again.
		// Log.d("MainActivity", "TODO: Persist shared preferences.");
		SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0
																			// -
																			// for
																			// private
																			// mode
		Editor editor = pref.edit();
		editor.putBoolean("gpsdialog", false); // value to store
		editor.commit();

	}

}
