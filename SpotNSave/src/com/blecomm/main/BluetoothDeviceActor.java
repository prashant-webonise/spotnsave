package com.blecomm.main;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.belcomm.scanservice.ScanBGService;
import com.blecomm.dbhelper.DBHelper;
import com.blecomm.model.CharacteristicsModel;
import com.blecomm.model.OperationModel;
import com.blecomm.utils.BDACommand;
import com.blecomm.utils.ScanDevices;
import com.blecomm.utils.ScanInterface;
import com.blecomm.utils.Utils;
import com.epicelements.spotnsave.SosView;
import com.epicelements.spotnsave.SpotNSaveApp;
import com.luttu.AppPrefes;

@SuppressLint("NewApi")
public class BluetoothDeviceActor implements Runnable, ScanInterface {

	private final static String TAG = BluetoothDeviceActor.class
			.getSimpleName();
	private Context mContext;
	private DBHelper dbhelper;
	private SpotNSaveApp appStorage;
	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	public int deviceId = 0;
	public BluetoothGatt mBluetoothGatt;
	public String commandname;
	private BluetoothDevice mDevice;
	public String deviceMacAddress;
	private boolean isConnected = false;
	private Thread thread;
	private AppPrefes appPrefes;

	public ArrayList<BDACommand> bdaCommandQueue;
	private Queue<BluetoothGattDescriptor> descriptorWriteQueue = new LinkedList<BluetoothGattDescriptor>();
	private Timer failTimer;
	private ConnectionFailerTask failerTask;

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public BluetoothGatt getmBluetoothGatt() {
		return mBluetoothGatt;
	}

	public void setmBluetoothGatt(BluetoothGatt mBluetoothGatt) {
		this.mBluetoothGatt = mBluetoothGatt;
	}

	public BluetoothDevice getmDevice() {
		return mDevice;
	}

	public void setmDevice(BluetoothDevice mDevice) {
		this.mDevice = mDevice;
	}

	public String getDeviceMacAddress() {
		return deviceMacAddress;
	}

	public void setDeviceMacAddress(String deviceMacAddress) {
		this.deviceMacAddress = deviceMacAddress;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	@SuppressLint("NewApi")
	public void initialization(Context context) {

		mContext = context;
		appStorage = (SpotNSaveApp) context.getApplicationContext();
		dbhelper = appStorage.getDbhelper();
		mBluetoothManager = (BluetoothManager) context
				.getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		bdaCommandQueue = new ArrayList<BDACommand>();
	}

	public void setContext(Context context) {
		mContext = context;
	}

	public void scanDeviceObject() {

		@SuppressWarnings("unused")
		ScanDevices scan = new ScanDevices(mContext, this, dbhelper);
	}

	public void scanBGDeviceObject(Context context) {

		mContext = context;
		ScanDevices scan = new ScanDevices(mContext);
	}

	@Override
	public void connectedDevice(BluetoothDevice device) {

		mDevice = device;
		setDeviceMacAddress(device.getAddress());
		startThread();
	}

	@Override
	public void run() {
		connectDevice(mDevice);
	}

	public void startThread() {
		thread = new Thread(this);
		thread.start();
	}

	public void stopThread() {
		if (thread != null) {
			final Thread tempThread = thread;
			thread = null;
			tempThread.interrupt();
		}
	}

	/**
	 * Connects to the GATT server hosted on the Bluetooth LE device.
	 * 
	 * @param device
	 *            The device address of the destination device.
	 * 
	 * @return Return true if the connection is initiated successfully. The
	 *         connection result is reported asynchronously through the
	 *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 *         callback.
	 */
	public boolean connectDevice(final BluetoothDevice device) {

		if (device == null) {
			return false;
		}

		failTimer = new Timer();
		failerTask = new ConnectionFailerTask();
		failTimer.schedule(failerTask, 15000, 50000);

		try {

			if (mContext != null) {

				mBluetoothGatt = device.connectGatt(mContext, true,
						mGattCallback);

				setmBluetoothGatt(mBluetoothGatt);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	// Implements callback methods for GATT events that the app cares about. For
	// example,
	// connection change and services discovered.
	public final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			super.onConnectionStateChange(gatt, status, newState);

			String intentAction;

			if (newState == BluetoothProfile.STATE_CONNECTED) {
				intentAction = Utils.ACTION_GATT_CONNECTED;
				try {
					setConnected(true);
					if (failerTask != null && failTimer != null) {
						failerTask.cancel();
						failTimer.cancel();
					}
					Log.e(TAG, "Connect from GATT server.");

					broadcastUpdate(intentAction);

					stopThread();
					// Utils.generateNotification(mContext,"BDA Connect","Stop Service");
					// mContext.stopService(new
					// Intent(mContext,ScanBGService.class));

					if (getmBluetoothGatt() != null) {
						boolean discover = getmBluetoothGatt()
								.discoverServices();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				Log.e(TAG, "Disconnected from GATT server.");
				setConnected(false);
				intentAction = Utils.ACTION_GATT_DISCONNECTED;
				getmBluetoothGatt().close();
				setmBluetoothGatt(null);

				ScanBGService.serviceBDA.isConnected = false;
				Utils.BDA.isConnected = false;

				if (bdaCommandQueue != null && bdaCommandQueue.size() > 0)
					bdaCommandQueue.clear();
				if (descriptorWriteQueue != null
						&& descriptorWriteQueue.size() > 0)
					descriptorWriteQueue.clear();

				commandname = "";
				// Utils.generateNotification(mContext,"BDA disconnect","Start Service");
				// mContext.startService(new
				// Intent(mContext,ScanBGService.class));
				broadcastUpdate(intentAction);

			}

		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			super.onServicesDiscovered(gatt, status);

			discoverServices(gatt.getServices());

		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			super.onCharacteristicRead(gatt, characteristic, status);
			Log.e(TAG, "onCharacteristicRead called");
			byte[] data = characteristic.getValue();
			if (data != null) {

				int type = Integer.parseInt(String.format("%02X", data[0])
						.trim(), 16);

				Log.e(TAG, "type = " + type);
				if (characteristic.getUuid().toString()
						.equals("00002a19-0000-1000-8000-00805f9b34fb")) {

					Utils.setBatteryValue(type);
					broadcastUpdate(Utils.ACTION_BATTERY_LEVEL);
				}
			}
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			super.onCharacteristicWrite(gatt, characteristic, status);

			Log.e(TAG, "onCharacteristicWrite called");
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			// TODO Auto-generated method stub
			super.onCharacteristicChanged(gatt, characteristic);

			Log.e(TAG, "onCharacteristicChanged called");

			byte[] data = characteristic.getValue();
			if (data != null) {

				int type = Integer.parseInt(String.format("%02X", data[0])
						.trim(), 16);
				Log.e(TAG, "type = " + type);

				if (characteristic.getUuid().toString()
						.equals("0000f800-0000-1000-8000-00805f9b34fb")
						&& type == 2) {
					String intentAction = Utils.ACTION_EVENT_TRIGGER_DOUBLECLICK;
					/*
					 * Utils.generateNotification(mContext, "SpotNSave",
					 * "Double Click");
					 */
					Intent intent = new Intent(mContext, SosView.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
					// sendSOS();
					broadcastUpdate(intentAction);
				} else if (characteristic.getUuid().toString()
						.equals("0000f800-0000-1000-8000-00805f9b34fb")
						&& type == 1) {
					String intentAction = Utils.ACTION_EVENT_TRIGGER;
					broadcastUpdate(intentAction);
					/*
					 * Utils.generateNotification(mContext, "SpotNSave",
					 * "Event Trigger");
					 */
					sendSOS();
				} else if (characteristic.getUuid().toString()
						.equals("00002a19-0000-1000-8000-00805f9b34fb")) {

					/*
					 * String intentAction = Utils.ACTION_BATTERY_LEVEL;
					 * broadcastUpdate(intentAction);
					 */

					Utils.setBatteryValue(type);
					broadcastUpdate(Utils.ACTION_BATTERY_LEVEL);
				}

			}

			// Log.e(TAG, "UUID = " + characteristic.getUuid());
			// if (characteristic.getUuid().toString()
			// .equals("00002afd-0000-1000-8000-00805f9b34fb")) {
			// String intentAction = Utils.ACTION_EVENT_TRIGGER;
			// broadcastUpdate(intentAction);
			// }

		}

		private void sendSOS() {
			// TODO Auto-generated method stub
			/*
			 * Intent intent = new Intent(
			 * "com.epicelements.spotnsave.START_SERVICE4");
			 * mContext.startService(intent);
			 */

			appPrefes = new AppPrefes(mContext, "sns");
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

		}

		private void sendSMS(String phoneNumber, String message) {
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phoneNumber, null, message, null, null);
		}

		@Override
		public void onDescriptorRead(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			// TODO Auto-generated method stub
			super.onDescriptorRead(gatt, descriptor, status);
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			super.onDescriptorWrite(gatt, descriptor, status);

			descriptorWriteQueue.remove(); // pop the item that we just

			// finishing writing
			// if there is more to write, do it!
			if (descriptorWriteQueue.size() > 0)
				writeGattDescriptor(descriptorWriteQueue.element());
			else {

				if (commandname != null && !commandname.equalsIgnoreCase("")) {
					// performCommand(commandname, dayAgo);
					performCommand("batteryCommand", 0);
				}

			}

		}

	};

	private void discoverServices(List<BluetoothGattService> list) {

		for (int i = 0; i < list.size(); i++) {

			final int serviceId = dbhelper.getServiceIdFromUUIDAndDeviceId(list
					.get(i).getUuid(), getDeviceId());

			Log.e(TAG, "Service id = " + serviceId);

			if (serviceId == -1) {
				continue;
			}

			Hashtable<String, CharacteristicsModel> charHas = dbhelper
					.getCharacteristicsOfService(serviceId, getDeviceId());

			ArrayList<BluetoothGattCharacteristic> gattCharacteristics = (ArrayList<BluetoothGattCharacteristic>) list
					.get(i).getCharacteristics();

			Log.e(TAG,
					"gattCharacteristics size = " + gattCharacteristics.size());

			for (int j = 0; j < gattCharacteristics.size(); j++) {

				Object name = gattCharacteristics.get(j).getUuid().toString()
						.trim();

				Log.e(TAG, "name  = " + name);

				if (charHas != null && charHas.containsKey(name)) {

					Log.e(TAG, "name  matched");

					CharacteristicsModel model = charHas
							.get(gattCharacteristics.get(j).getUuid()
									.toString().trim());
					Log.e(TAG, "model isObservable = " + model.isObservable());

					if (model.isObservable() == 1) {

						boolean isNotify = getmBluetoothGatt()
								.setCharacteristicNotification(
										gattCharacteristics.get(j), true);

						ArrayList<BluetoothGattDescriptor> gattdescriptor = (ArrayList<BluetoothGattDescriptor>) gattCharacteristics
								.get(j).getDescriptors();

						Log.e(TAG, "gattdescriptor = " + gattdescriptor.size());

						for (int k = 0; k < gattdescriptor.size(); k++) {

							BluetoothGattDescriptor descriptor = gattdescriptor
									.get(k);
							descriptorWriteQueue.add(descriptor);

						}
					}
				}
			}
		}

		if (descriptorWriteQueue.size() > 0) {
			writeGattDescriptor(descriptorWriteQueue.element());
		} else {
			// perform conmmand;
		}

	}

	// Write gatt descriptor
	public void writeGattDescriptor(BluetoothGattDescriptor d) {
		d.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		getmBluetoothGatt().writeDescriptor(d);
	}

	class ConnectionFailerTask extends TimerTask {
		@Override
		public void run() {
			broadcastUpdate(Utils.ACTION_CONNECT_FAIL);
			failerTask.cancel();
			failTimer.cancel();
			// mContext.startService(new Intent(mContext,ScanBGService.class));
		}
	}

	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);
		mContext.sendBroadcast(intent);
	}

	@Override
	public void addDevice(BluetoothDevice device) {
		// TODO Auto-generated method stub

	}

	// device is ready to for communition
	public void deviceIsReadyForCommunication(String name, int dayago) {

		Log.e(TAG, "commandname in deviceIsReadyForCommunication = " + name);
		commandname = name;

		if (bdaCommandQueue != null && bdaCommandQueue.size() > 0) {
			bdaCommandQueue.remove(0);
		}

		if (name != null && !name.equalsIgnoreCase("")) {
			checkAndPerformCommand(name, dayago);
		}
	}

	// check if device is null or its bluetooth gatt is null.
	public void checkAndPerformCommand(String command, int dayago) {

		if (mDevice == null && deviceMacAddress != null) {
			mDevice = mBluetoothAdapter.getRemoteDevice(deviceMacAddress);
		} else {

			if (mDevice == null && deviceMacAddress == null) {
				ScanDevices scan = new ScanDevices(mContext, this, dbhelper);
				return;
			}
		}
		// Log.e(TAG, "mBluetoothGatt == " + getmBluetoothGatt());
		if (getmBluetoothGatt() == null) {
			ScanDevices scan = new ScanDevices(mContext, this, dbhelper);
		} else {
			performCommand(command, dayago);
		}
	}

	public void performCommand(String command, int dayago) {

		Log.e(TAG, "Command Name in performCommand ====== " + command
				+ " =======  " + bdaCommandQueue);

		if (bdaCommandQueue == null || bdaCommandQueue.size() == 0) {
			BDACommand bdc;
			bdc = new BDACommand(mContext, command);
			bdaCommandQueue.add(bdc);

			if (bdaCommandQueue.size() == 1)
				processCommand(bdc, dayago);
		}
	}

	private void processCommand(BDACommand bdc, int dayago) {
		Log.e("processCommand()", " " + bdc.operationSequenceList.size());
		if (bdc.operationSequenceList.size() > 0) {
			OperationModel model = bdc.operationSequenceList.get(0);
			bdc.operationSequenceList.remove(0);
			processCurrentModel(model, dayago);
		} else {
			Log.e("processCommand()",
					" else  " + bdc.operationSequenceList.size());
			if (bdaCommandQueue != null && bdaCommandQueue.size() > 0)
				bdaCommandQueue.clear();
		}

	}

	private void processCurrentModel(OperationModel model, int dayago) {
		UUID serviceuid = UUID.fromString(model.getServiceUUID());
		BluetoothGattService service = getmBluetoothGatt().getService(
				serviceuid);
		UUID characteristicuid = UUID.fromString(model.getCharUUID());
		BluetoothGattCharacteristic characteristic = null;
		if (service != null) {
			characteristic = service.getCharacteristic(characteristicuid);
		}
		if (characteristic != null) {

			if (model.getOperation().equalsIgnoreCase("Read")) {
				boolean read = getmBluetoothGatt().readCharacteristic(
						characteristic);
				Log.e(TAG, "Read fire = " + read);
			} else if (model.getOperation().equalsIgnoreCase("Write")) {
				byte[] byt = null;

				boolean write = getmBluetoothGatt().writeCharacteristic(
						characteristic);
				Log.e(TAG, "write fire = " + write);
				if (!write) {
					if (bdaCommandQueue != null && bdaCommandQueue.size() > 0)
						bdaCommandQueue.remove(0);
				}

			}
		}

	}

	public String convertCharacteristicsValue(
			BluetoothGattCharacteristic characteristic, BDACommand bdaCommand) {

		// For all other profiles, writes the data formatted in HEX.
		final byte[] data = characteristic.getValue();
		StringBuilder stringBuilder = null;
		if (data != null && data.length > 0) {
			stringBuilder = new StringBuilder(data.length);
			for (byte byteChar : data)
				stringBuilder.append(String.format("%02X ", byteChar));
		}
		String value = null;
		Log.e("bdaCommand.commandNam", "----------------"
				+ bdaCommand.commandName);
		if (bdaCommand.commandName.equalsIgnoreCase("batteryCommand")) {
			value = stringBuilder.toString().trim();
			int batteryval = Integer.parseInt(value, 16);
			Utils.setBatteryValue(batteryval);
			broadcastUpdate(Utils.ACTION_BATTERY_LEVEL);
			/*
			 * if (characteristic.getUuid().toString()
			 * .equalsIgnoreCase("00002a19-0000-1000-8000-00805f9b34fb")) { if
			 * (!stringBuilder.toString().trim().equalsIgnoreCase("00")) { value
			 * = Utils.padIt(stringBuilder.toString().trim()); Log.e(TAG,
			 * "AutolockUnlock decodeversion ========== " + value); value =
			 * String.valueOf(Long.parseLong(value, 16)); Log.e(TAG,
			 * "final value ========== " + value); } else { value =
			 * String.valueOf(Long.parseLong(stringBuilder .toString().trim(),
			 * 16)); } } else { value =
			 * String.valueOf(Long.parseLong(stringBuilder.toString() .trim(),
			 * 16)); }
			 */
		}
		Log.e(TAG, "convertCharacteristicsValue ========== " + value);
		return value;
	}

}
