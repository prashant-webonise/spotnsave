package com.luttu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.example.luttulibrary.R;

public final class GPSTracker implements LocationListener {

	private final Context mContext;

	// flag for GPS status
	public boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetLocation = false;

	Location location; // location
	double latitude; // latitude
	double longitude; // longitude

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 20 * 1; // 1 minute

	// Declaring a Location Manager
	protected LocationManager locationManager;

	private float accuracy;

	private CheckBox checkbox;

	public GPSTracker(Context context) {
		this.mContext = context;
		getLocation();
	}

	public GPSTracker(Context context, boolean b) {
		this.mContext = context;
		getLocations();
	}

	/**
	 * Function to get the user's current location
	 * 
	 * @return
	 */
	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			Log.v("isGPSEnabled", "=" + isGPSEnabled);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			Log.v("isNetworkEnabled", "=" + isNetworkEnabled);

			if (isGPSEnabled == false) {
				showSettingsAlert();
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
							accuracy = location.getAccuracy();
							System.out.println("latitude" + latitude);
							System.out.println("longitude" + longitude);
							System.out.println("accuracy" + accuracy);
							if (latitude == 0.0) {
								// showSettingsAlert();
							}
						} else {
							// showSettingsAlert();
						}
					} else {
						// showSettingsAlert();
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
								accuracy = location.getAccuracy();
								System.out.println("latitude" + latitude);
								System.out.println("longitude" + longitude);
								System.out.println("accuracy" + accuracy);
								if (latitude == 0.0) {
									// showSettingsAlert();
								} else {
									// showSettingsAlert();
								}
							} else {
								// showSettingsAlert();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	public Location getLocations() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			Log.v("isGPSEnabled", "=" + isGPSEnabled);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			Log.v("isNetworkEnabled", "=" + isNetworkEnabled);

			if (isGPSEnabled == false) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
							accuracy = location.getAccuracy();
							System.out.println("latitude" + latitude);
							System.out.println("longitude" + longitude);
							System.out.println("accuracy" + accuracy);
							if (latitude == 0.0) {
								// showSettingsAlert();
							}
						} else {
							// showSettingsAlert();
						}
					} else {
						// showSettingsAlert();
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
								accuracy = location.getAccuracy();
								System.out.println("latitude" + latitude);
								System.out.println("longitude" + longitude);
								System.out.println("accuracy" + accuracy);
								if (latitude == 0.0) {
									// showSettingsAlert();
								} else {
									// showSettingsAlert();
								}
							} else {
								// showSettingsAlert();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	/**
	 * Stop using GPS listener Calling this function will stop using GPS in your
	 * app
	 * */
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
	}

	public boolean getgps() {
		return isGPSEnabled;
	}

	/**
	 * Function to get latitude
	 * */
	public double getLatitude() {
		if (location != null) {
			// latitude = location.getLatitude();
		}

		// return latitude
		return latitude;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude() {
		if (location != null) {
			// longitude = location.getLongitude();
		}

		// return longitude
		return longitude;
	}

	public float getAccuracy() {
		return accuracy;
	}

	/**
	 * Function to check GPS/wifi enabled
	 * 
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	/**
	 * Function to show settings alert dialog On pressing Settings button will
	 * lauch Settings Options
	 * */
	public void showSettingsAlert() {

		SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0);
		boolean gpsdialog = pref.getBoolean("gpsdialog", true); // default is
																// true
		if (gpsdialog == true) {
			Dialog dialog2 = createDialog();
			dialog2.show();
		}
		//
		// AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
		//
		// // Setting Dialog Title
		// alertDialog.setTitle("GPS is settings");
		//
		// // Setting Dialog Message
		// alertDialog
		// .setMessage("GPS is not enabled. Do you want to go to settings menu?");
		//
		// // On pressing Settings button
		// alertDialog.setPositiveButton("Settings",
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int which) {
		// Intent intent = new Intent(
		// Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		// mContext.startActivity(intent);
		// }
		// });
		//
		// // on pressing cancel button
		// alertDialog.setNegativeButton("Cancel",
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.cancel();
		// }
		// });
		//
		// // Showing Alert Message
		// alertDialog.show();
	}

	public Dialog createDialog() {
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		View neverShow = inflater.inflate(R.layout.never_show, null);
		checkbox = (CheckBox) neverShow.findViewById(R.id.checkbox);

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setView(neverShow)
				.setTitle(R.string.location_settings_title)
				.setMessage(R.string.location_instructions)
				.setPositiveButton(R.string.location_settings,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (checkbox.isChecked()) {
									doNotShowAgain();
								}
								Intent myIntent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								mContext.startActivity(myIntent);
							}
						})
				.setNegativeButton(R.string.location_skip,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
								if (checkbox.isChecked()) {
									doNotShowAgain();
								}
							}
						});
		// Create the AlertDialog object and return it
		return builder.create();
	}

	private void doNotShowAgain() {
		// persist shared preference to prevent dialog from showing again.
		// Log.d("MainActivity", "TODO: Persist shared preferences.");
		SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0); // 0
																				// -
																				// for
																				// private
																				// mode
		Editor editor = pref.edit();
		editor.putBoolean("gpsdialog", false); // value to store
		editor.commit();

	}

	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		accuracy = location.getAccuracy();
		System.out.println("latitude" + latitude);
		System.out.println("longitude" + longitude);
		System.out.println("accuracy" + accuracy);
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}