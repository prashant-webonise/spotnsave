package com.belcomm.scanservice;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.blecomm.main.BluetoothDeviceActor;
import com.blecomm.utils.Utils;

public class ScanBGService extends Service {

	static Context context;
	public static BluetoothDeviceActor serviceBDA;
	private Timer timer;
	private ScheduleTask scheduleTask;
	public static Intent intentService = null;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.e("onStartCommand", "called");
		context = this;
		timer = new Timer();
		scheduleTask = new ScheduleTask();
		timer.schedule(scheduleTask, 15000, 18000);

		return START_STICKY;
	}

	public static BluetoothDeviceActor getServiceBDA() {
		return serviceBDA;
	}

	public static void setServiceBDA(BluetoothDeviceActor serviceBDA) {
		ScanBGService.serviceBDA = serviceBDA;
	}

	class ScheduleTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (serviceBDA != null && serviceBDA.isConnected()) {
				// do nothing
				Log.e("ScheduleTask", "already connected");
			} else {
				
				Log.e("ScheduleTask", "start scan in bg");
				serviceBDA = new BluetoothDeviceActor();
				serviceBDA.initialization(context);
				serviceBDA.setDeviceId(1);
				serviceBDA.setContext(context);
				Utils.BDA = serviceBDA;
				serviceBDA.scanBGDeviceObject(context);
			}
		}

	}

}
