package com.luttu;

import java.io.File;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class Utils {
	private static final String IMAGE_DIRECTORY_NAME = "Wyc";
	
	public Uri getOutputMediaFileUri(int type) {
//		System.out.println("path1-----"+Uri.fromFile(getOutputMediaFile(type)).getPath());
		return Uri.fromFile(getOutputMediaFile(type));
	}


	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		Long tsLong = System.currentTimeMillis()/1000;
		String timeStamp = tsLong.toString();

		//	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
		//	            Locale.getDefault()).format(new Date());
		File mediaFile;

		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "Wyc_" + timeStamp + ".jpg");


		return mediaFile;
	}

}
