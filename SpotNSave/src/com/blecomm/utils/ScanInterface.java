package com.blecomm.utils;


import android.bluetooth.BluetoothDevice;

public interface ScanInterface {

	public void connectedDevice(BluetoothDevice device);
	public void addDevice(BluetoothDevice device);

}
