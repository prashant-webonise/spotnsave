package com.blecomm.utils;


import java.util.ArrayList;

import android.content.Context;

import com.blecomm.dbhelper.DBHelper;
import com.blecomm.model.OperationModel;
import com.epicelements.spotnsave.SpotNSaveApp;

public class BDACommand {
	private DBHelper dbhelper;
	private SpotNSaveApp appStorage;
	public ArrayList<OperationModel> operationSequenceList;
	public String commandName;
	public String expectedValue;
	public String failValue;

	public BDACommand(Context mContext, String command) {
		commandName = command;
		appStorage = (SpotNSaveApp) mContext.getApplicationContext();
		dbhelper = appStorage.getDbhelper();
		operationSequenceList = new ArrayList<OperationModel>();
		operationQueue(command);
	}

	private void operationQueue(String command) {


		System.out.println("OperationQueue " + command );
		operationSequenceList = dbhelper.performOperation(command);

		for (int i = 0; i < operationSequenceList.size(); i++) {

			if (operationSequenceList.get(i).getExpectedValue() != null
					&& !operationSequenceList.get(i).getExpectedValue()
							.equalsIgnoreCase("")) {
				expectedValue = operationSequenceList.get(i).getExpectedValue();
				failValue = operationSequenceList.get(i).getFailValue();
			}

		}


	}

}
