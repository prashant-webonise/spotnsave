package com.epicelements.spotnsave;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;

public class Privacy extends Activity {
	private ProgressDialog pDialog;
	private TextView textView2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.privacy);
		textView2 = (TextView) findViewById(R.id.textView2);
		privacy();
	}

	private void privacy() {
		// TODO Auto-generated method stub
		progressshow();
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String LicenseInfo = GooglePlayServicesUtil
						.getOpenSourceSoftwareLicenseInfo(getApplicationContext());
				return LicenseInfo;
			}

			protected void onPostExecute(String LicenseInfo) {
				textView2.setText(LicenseInfo);
				progresscancel();
			};
		}.execute();
	}

	private void progressshow() {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("getting legal notices");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}

	private void progresscancel() {
		// TODO Auto-generated method stub
		pDialog.dismiss();
	}

	public void Close(View view) {
		// TODO Auto-generated method stub
		finish();
	}
}
