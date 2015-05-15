package com.epicelements.spotnsave;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.luttu.AppPrefes;
import com.util.ImageSmallerAction;

public class Splash extends FragmentActivity implements OnClickListener {
	ImageView im_next;
	ImageSmallerAction imageSmallerAction = new ImageSmallerAction();
	private AppPrefes appPrefes;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);
		setContentView(R.layout.splash);
		appPrefes = new AppPrefes(this, "sns");
		// appPrefes.SaveData("fullnameregistration", "Luttu");
		checkfirst();
		findid();
		getwidth();
	}

	private void findid() {
		// TODO Auto-generated method stub
		im_next = (ImageView) findViewById(R.id.im_next);
		im_next.setOnClickListener(this);
	}

	public void Legal(View v) {
		String LicenseInfo = GooglePlayServicesUtil
				.getOpenSourceSoftwareLicenseInfo(getApplicationContext());
		AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(Splash.this);
		LicenseDialog.setTitle("Legal Notices");
		LicenseDialog.setMessage(LicenseInfo);
		LicenseDialog.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Splash.this, Registration_New.class);
		startActivity(intent);
		finish();

	}

	private void checkfirst() {
		// TODO Auto-generated method stub
		if (!appPrefes.getData("fullnameregistration").equals("")) {
			Intent intent = new Intent(Splash.this, AddGuard.class);
			startActivity(intent);
			finish();
		}
	}

	private void getwidth() {
		// TODO Auto-generated method stub
		WindowManager w = getWindowManager();

		int width;
		int height;
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		// Display display = getWindowManager().getDefaultDisplay();
		// Point size = new Point();
		// display.getSize(size);
		// width = size.x;
		// height = size.y;
		// } else {
		try {
			Display d = w.getDefaultDisplay();
			width = d.getWidth();
			height = d.getHeight();
		} catch (Exception e) {
			// TODO: handle exception
			width = 400;
			height = 600;
		}
		// }
	}
}
