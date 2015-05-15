package com.blecomm.utils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.blecomm.dbhelper.DBHelper;
import com.blecomm.main.BluetoothDeviceActor;

@SuppressLint("NewApi")
public class ScanDevices {

	private BluetoothAdapter mBluetoothAdapter;
	private ArrayList<String> scanDeviceNameList;
	private Context context;
	public ScanInterface scaninterface;
	public boolean isDeviceConnected = false;
	public DBHelper dbhelper;
	private Timer timer;
	private ScheduleTask scheduleTask;
	protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	String uuid = "";
	int transmissionpower = 0;
	String devicename = null;
	String comp_spe = "";

	@SuppressLint("NewApi")
	public ScanDevices(Context mcontext, BluetoothDeviceActor bluetoothDeviceActor, DBHelper db) {
		final BluetoothManager bluetoothManager = (BluetoothManager) mcontext
				.getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		scanDeviceNameList = new ArrayList<String>();
		context = mcontext;
		dbhelper = db;
		scanLEDevice(true);
		scaninterface = bluetoothDeviceActor;

	}

	public ScanDevices(Context mcontext) {
		final BluetoothManager bluetoothManager = (BluetoothManager) mcontext
				.getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		scanDeviceNameList = new ArrayList<String>();
		context = mcontext;
		Log.e("from BG", "BGGG");
		scanLEDevice(true);
		scaninterface = Utils.BDA;
	}

	// Scan the LE Devices.
	@SuppressLint("NewApi")
	private void scanLEDevice(boolean enable) {

		if (enable) {
			scanDeviceNameList.clear();
			timer = new Timer();
			scheduleTask = new ScheduleTask();
			timer.schedule(scheduleTask, 10000, 50000);
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}

	}

	class ScheduleTask extends TimerTask {
		@Override
		public void run() {

			if (!isDeviceConnected) {
				broadcastUpdate(Utils.ACTION_DEVICE_NOT_FOUND);
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
			}
			scheduleTask.cancel();
			timer.cancel();
		}
	}

	private void broadcastUpdate(final String action) {

		final Intent intent = new Intent(action);
		context.sendBroadcast(intent);
	}

	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
			if (scanDeviceNameList != null && scanDeviceNameList.contains(device.getName())) {
			} else {

				// Log.e("onLeScan", "onLeScan device == " + device.getName());

				scanDeviceNameList.add(device.getName());
				StringBuffer sb = new StringBuffer();
				for (byte b : scanRecord) {
					sb.append(String.format("%02X", b));
				}
				System.out.println(sb.toString());
				String s = sb.toString();
				decodescan(s);

				// Log.e("onLeScan", "uuid == " + uuid);

				// 08-25 13:11:28.792: E/onLeScan(7718): uuid ==
				// FB349B5F800000800010000000F00000
				// 08-25 13:17:08.122: E/onLeScan(7718): uuid ==
				// FB349B5F800000800010000002F00000

				// if (device.getName().equalsIgnoreCase("Smart Buzzer")
				// || uuid.equalsIgnoreCase("6F0CDD5A6896E1978744A4C5FD180FD3"))
				// {
				try {
					if (device.getName().equalsIgnoreCase("SpotNSave")
							|| (uuid != null && (uuid.equalsIgnoreCase("FB349B5F800000800010000000F00000")
									|| uuid.equalsIgnoreCase("FB349B5F800000800010000002F00000") || uuid
										.equalsIgnoreCase("FB349B5F800000800010000002180000")))) {
						Log.e("scanRecord", "device matched == ");
						isDeviceConnected = true;
						if (scheduleTask != null)
							scheduleTask.cancel();
						if (timer != null)
							timer.cancel();
						mBluetoothAdapter.stopLeScan(mLeScanCallback);

						scaninterface.connectedDevice(device);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

	};

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public void decodescan(String data) {
		// byte[] s = data.getBytes();
		int datalength = 0;
		int length;
		int type;
		int i = 0;
		int j = 2;
		while (datalength != data.length()) {
			length = Integer.parseInt(data.substring(i, i + j), 16);
			type = Integer.parseInt(data.substring((i + j), i + j + 2), 16);
			switch (type) {
			case 1:
				break;
			case 2:
				uuid = data.substring((i + j + 2), i + j + (length * 2));
				break;
			case 3:
				uuid = data.substring((i + j + 2), i + j + (length * 2));
				break;
			case 4:
				uuid = data.substring((i + j + 2), i + j + (length * 2));
				break;
			case 5:
				uuid = data.substring((i + j + 2), i + j + (length * 2));
				break;
			case 6:
				uuid = data.substring((i + j + 2), i + j + (length * 2));
				break;
			case 7:
				uuid = data.substring((i + j + 2), i + j + (length * 2));
				break;
			case 9:
				devicename = hextoString(data.substring((i + j + 2), i + j + (length * 2)));
				break;
			case 10: // for the transmission power
				// transmissionpower = data.substring((i+j+2),i + j +(length *
				// 2));
				transmissionpower = Integer.parseInt(data.substring((i + j + 2), i + j + (length * 2)), 16);
				byte x = (byte) transmissionpower;
				transmissionpower = x;
				// short sc = (short)transmissionpower;
				// System.out.print(sc);
				break;
			case 255:
				comp_spe = data.substring((i + j + 2), i + j + (length * 2));
				/*
				 * String lsb = "" , msb =""; if(comp_spe != null) {
				 * company_name = comp_spe.substring(0,4); msb =
				 * company_name.substring(0,2); lsb =
				 * company_name.substring(2,company_name.length()); company_name
				 * = getcompanyIdentifier(lsb+msb); }
				 */
				/*
				 * String st = String.valueOf(comp_spe); System.out.print(st);
				 */
				break;
			}
			// i = i + j + 2 + length;
			i = i + j + (length * 2);
			datalength = i;
			if (length == 0)
				break;
		}
	}

	public String hextoString(String hex) {

		StringBuilder output = new StringBuilder();
		for (int j = 0; j < hex.length(); j += 2) {
			String str = hex.substring(j, j + 2);
			output.append((char) Integer.parseInt(str, 16));
		}
		return output.toString();
	}

}
