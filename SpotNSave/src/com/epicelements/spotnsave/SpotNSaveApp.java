package com.epicelements.spotnsave;

import android.app.Application;
import butterknife.ButterKnife;

import com.blecomm.dbhelper.DBHelper;

public class SpotNSaveApp extends Application {

	/**
	 * Database Helper class to Access the database.
	 */
	private DBHelper dbhelper;

	/**
	 * @return the dbhelper
	 */
	public DBHelper getDbhelper() {
		return dbhelper;
	}

	/**
	 * @param dbhelper
	 *            the dbhelper to set
	 */
	public void setDbhelper(DBHelper dbhelper) {
		this.dbhelper = dbhelper;
	}

	/**
	 * Called when the application class is called. and it create and open
	 * database.
	 */

	public void onCreate() {
		super.onCreate();
		ButterKnife.setDebug(BuildConfig.DEBUG);
		dbhelper = new DBHelper(this);
		try {

			// create and open database.
			dbhelper.createDataBase();
			dbhelper.openDataBase();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method is called when terminate the Application class.
	 */
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		if (dbhelper != null)
			dbhelper.close();
	}

}
