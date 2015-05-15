package com.epicelements.spotnsave;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.luttu.AppPrefes;
import com.luttu.GPSTracker;

public class SetUppage extends Activity {
	private ImageView im_l;
	private ImageView im_b;
	private AppPrefes appPrefes;
	private boolean btclick = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setuppage);
		im_l = (ImageView) findViewById(R.id.im_l);
		im_b = (ImageView) findViewById(R.id.im_b);
		appPrefes = new AppPrefes(this, "sns");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		boolean b = new GPSTracker(this, true).getgps();
		System.out.println("bbbbbbbbbbb" + b);
		checkblutoth();
		if (btclick) {
			btclick = false;
			if (im_l.getTag() != null || !b) {
				im_l.setTag(null);
				im_l.setImageResource(R.drawable.ic_off);
			} else {
				im_l.setTag("notnulll");
				im_l.setImageResource(R.drawable.ic_on);
			}
		}
	}

	public void Bloototh(View view) {
		// TODO Auto-generated method stub
		bluetooth();
		// if (im_b.getTag() != null) {
		// im_b.setTag(null);
		// im_b.setImageResource(R.drawable.ic_off);
		// } else {
		// im_b.setTag("notnulll");
		// im_b.setImageResource(R.drawable.ic_on);
		// }
	}

	private void onblutoth() {
		// TODO Auto-generated method stub
		final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			Toast.makeText(getApplicationContext(),
					"Device does not support Bluetooth", Toast.LENGTH_SHORT)
					.show();
		} else {
			if (!mBluetoothAdapter.isEnabled()) {
				mBluetoothAdapter.enable();
			} else {
				mBluetoothAdapter.disable();
			}
		}
	}
	private void checkblutoth() {
		// TODO Auto-generated method stub
		final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			Toast.makeText(getApplicationContext(),
					"Device does not support Bluetooth", Toast.LENGTH_SHORT)
					.show();
		} else {
			if (!mBluetoothAdapter.isEnabled()) {
				im_b.setImageResource(R.drawable.ic_off);
			} else {
				im_b.setImageResource(R.drawable.ic_on);
			}
		}
	}

	public void Locations(View view) {
		// TODO Auto-generated method stub
	}

	public void Blootoths(View view) {
		// TODO Auto-generated method stub
		// bluetooth();
	}

	public void Back(View view) {
		// TODO Auto-generated method stub
		finish();
	}

	public void Location(View view) {
		// TODO Auto-generated method stub
		boolean b = new GPSTracker(this, true).getgps();
		if (!b) {
			btclick = true;
			Intent myIntent = new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(myIntent);
		} else {
			btclick = false;
			if (im_l.getTag() != null) {
				im_l.setTag(null);
				im_l.setImageResource(R.drawable.ic_off);
			} else {
				im_l.setTag("notnulll");
				im_l.setImageResource(R.drawable.ic_on);
			}
		}

	}

	public void Continu(View view) {
		// TODO Auto-generated method stub
		System.out.println("gettag" + im_l.getTag());
		if (im_l.getTag() == null) {
			Toast.makeText(SetUppage.this,
					"You must turn on Location services to continue",
					Toast.LENGTH_LONG).show();
		} else {
			appPrefes.SaveData("setup", "cmlte");
			Intent intent = new Intent(SetUppage.this, Home.class);
			startActivity(intent);
			finish();
		}
	}

	private void bluetooth() {
		// TODO Auto-generated method stub
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Bluetooth Settings");
		alertDialog
				.setMessage("Are you sure you want to change Bluetooth settings "
						+ "?");
		alertDialog.setNegativeButton("Cancel", null);
		alertDialog.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						onblutoth();
						if (im_b.getTag() != null) {
							im_b.setTag(null);
							im_b.setImageResource(R.drawable.ic_off);
						} else {
							im_b.setTag("notnulll");
							im_b.setImageResource(R.drawable.ic_on);
						}
					}
				});
		alertDialog.show();
	}
}
