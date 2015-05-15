package com.blecomm.dbhelper;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.blecomm.model.CharacteristicsModel;
import com.blecomm.model.OperationModel;
import com.epicelements.spotnsave.R;

public class DBHelper extends SQLiteOpenHelper {

	private static Context context;
	private static SQLiteDatabase db;
	private final static int DB_VERSION = 1;
	
	private final static String TAG = DBHelper.class.getSimpleName();

	public DBHelper(Context mcontext) {
		super(mcontext, mcontext.getResources().getString(R.string.DB_NAME),
				null, DB_VERSION);
		context = mcontext;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	/**
	 * Function is used to create a Database.
	 * 
	 * @throws java.io.IOException
	 */
	public void createDataBase() throws IOException {
		// ---Check whether database is already created or not---
		boolean dbExist = checkDataBase();

		if (!dbExist) {
			this.getReadableDatabase();
			try {
				// ---If not created then copy the database---
				copyDataBase();
				this.close();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
			this.getWritableDatabase();
		} else {
			this.getWritableDatabase();
		}
	}

	/**
	 * Copy the database to the output stream
	 * 
	 * @throws java.io.IOException
	 */
	private void copyDataBase() throws IOException {
		InputStream myInput = context.getAssets().open(
				context.getString(R.string.DB_NAME));
		String outFileName = context.getString(R.string.DB_PATH)
				+ context.getString(R.string.DB_NAME);
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	/**
	 * Open the database
	 * 
	 * @throws android.database.SQLException
	 */
	public void openDataBase() throws SQLException {
		// --- Open the database---
		String myPath = context.getString(R.string.DB_PATH)
				+ context.getString(R.string.DB_NAME);
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	/**
	 * Check whether database already created or not
	 * 
	 * @return boolean
	 */
	private boolean checkDataBase() {
		try {
			String myPath = context.getString(R.string.DB_PATH)
					+ context.getString(R.string.DB_NAME);
			File f = new File(myPath);
			if (f.exists())
				return true;
			else
				return false;
		} catch (SQLiteException e) {
			e.printStackTrace();
			return false;
		}
	}

	
	public ArrayList<OperationModel> performOperation(String command) {

		ArrayList<OperationModel> list = new ArrayList<OperationModel>();

		int operationId = -1;
		Cursor cursor = null;

		cursor = db.query("OperationType", null, "OperationName='" + command
				+ "' AND DeviceId=1", null, null, null, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			operationId = cursor.getInt(cursor.getColumnIndex("ID"));

		}
		if (cursor != null)
			cursor.close();

		list = getOperationSequenceData(operationId, command);

		return list;

	}
	
	public ArrayList<OperationModel> getOperationSequenceData(int operationId,
			String command) {

		OperationModel model = null;
		ArrayList<OperationModel> list = new ArrayList<OperationModel>();

		String query = "select OS.ID,OS.OperationId,OS.CharId,OS.Operation,OS.Value,OS.ExpectedValue,CH.CharUUID,"
				+ "CH.ServiceId,CH.CharName,CH.CharReturnType,SE.ServiceUUID,OT.OperationName, OS.FailValue from OperationSequence OS, Characteristics CH, Services SE,"
				+ "OperationType OT where OS.OperationId="
				+ operationId
				+ " and CH.ID=OS.CharId and SE.ID=CH.ServiceId and OS.OperationId = OT.ID";

		Log.e("Query", "Query == " + query);

		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();

			for (int i = 0; i < cursor.getCount(); i++) {

				model = new OperationModel();
				model.setId(cursor.getInt(cursor.getColumnIndex("ID")));
				model.setCharRefId(cursor.getInt(cursor
						.getColumnIndex("CharId")));
				model.setCharUUID(cursor.getString(cursor
						.getColumnIndex("CharUUID")));
				model.setOperation(cursor.getString(cursor
						.getColumnIndex("Operation")));
				model.setOperationRefId(cursor.getInt(cursor
						.getColumnIndex("OperationId")));
				model.setServiceUUID(cursor.getString(cursor
						.getColumnIndex("ServiceUUID")));
				model.setOperationName(cursor.getString(cursor
						.getColumnIndex("OperationName")));
				model.setCharName(cursor.getString(cursor
						.getColumnIndex("CharName")));
				model.setCharReturnType(cursor.getString(cursor
						.getColumnIndex("CharReturnType")));
				model.setFailValue(cursor.getString(cursor
						.getColumnIndex("FailValue")));
				model.setExpectedValue(cursor.getString(cursor
						.getColumnIndex("ExpectedValue")));
				model.setValue(cursor.getString(cursor.getColumnIndex("Value")));

				list.add(model);
				cursor.moveToNext();

			}
		}
		if (cursor != null)
			cursor.close();

		return list;
	}
	
	// get Service UUID
		public int getServiceIdFromUUIDAndDeviceId(UUID uuid, int deviceId) {

			Log.e(TAG, "UUID = " + uuid);
			
			int id = -1;
			Cursor cursor = null;

			cursor = db.query("Services", null, "ServiceUUID='" + uuid + "'",
					null, null, null, null);

			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				id = cursor.getInt(cursor.getColumnIndex("ID"));

			}

			if (cursor != null)
				cursor.close();

			return id;
		}
	
		// get Characteristic of service.
		public Hashtable<String, CharacteristicsModel> getCharacteristicsOfService(
				int serviceId, int deviceId) {

			Hashtable<String, CharacteristicsModel> table = new Hashtable<String, CharacteristicsModel>();

			CharacteristicsModel model = null;
			Cursor cursor = null;

			cursor = db.query("Characteristics", null, "ServiceId=" + serviceId,
					null, null, null, null);

			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();

				for (int i = 0; i < cursor.getCount(); i++) {

					model = new CharacteristicsModel();
					model.setId(cursor.getInt(cursor.getColumnIndex("ID")));
					model.setCharName(cursor.getString(cursor
							.getColumnIndex("CharName")));
					model.setCharUUID(cursor.getString(
							cursor.getColumnIndex("CharUUID")).trim());
					model.setServiceId(cursor.getInt(cursor
							.getColumnIndex("ServiceId")));
					model.setDeviceId(cursor.getInt(cursor
							.getColumnIndex("DeviceId")));
					model.setObservable(cursor.getInt(cursor
							.getColumnIndex("IsObservable")));

					table.put(model.getCharUUID(), model);
					cursor.moveToNext();

				}

			}

			if (cursor != null)
				cursor.close();

			return table;
		}
		
}
