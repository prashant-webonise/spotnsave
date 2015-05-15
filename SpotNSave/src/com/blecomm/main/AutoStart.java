package com.blecomm.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.belcomm.scanservice.ScanBGService;
import com.blecomm.utils.Utils;

public class AutoStart extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.e(":::::::::::::::Inside Receiver for auto start ",
				"::::::::::::::::::::::::::::::::");
		if (intent != null
				&& intent.getAction().equals(
						"android.intent.action.BOOT_COMPLETED")) {
			Log.e("Auto start service started ", "");
			// SecuRemote s = new SecuRemote(context);
			
			Intent startServiceIntent = new Intent(context, ScanBGService.class);
			context.startService(startServiceIntent);
		}
	}

}
