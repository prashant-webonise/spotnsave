package com.epicelements.spotnsave;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;
import com.luttu.AppPrefes;

public class AddGuard extends FragmentActivity {
	private AppPrefes appPrefes;
	private TextView tv_add_guard_text;
	private boolean continuee;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addingguardian);
		appPrefes = new AppPrefes(this, "sns");
		tv_add_guard_text = (TextView) findViewById(R.id.tv_add_guard_text);
		checkf();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			getlist();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void getlist() {
		// TODO Auto-generated method stub
		DetailFalse detailGuardian = (new Gson()).fromJson(
				appPrefes.getData("sessionsession"), DetailFalse.class);
		if (detailGuardian != null) {
			// System.out.println("myfirstsize"
			// + detailGuardian.lists.get(0).id.size());
			int size = detailGuardian.lists.get(0).id.size();
			int remain = 5 - detailGuardian.lists.get(0).id.size();
			if (size == 0) {
				return;
			}
			tv_add_guard_text.setText("You have chosen " + size
					+ " guardian, you can still add " + remain
					+ " more guardians to your list");
			if (size >= 1) {
				continuee = true;
			}
			if (size == 5) {
				Intent intent = new Intent(AddGuard.this, SetUppage.class);
				startActivity(intent);
				finish();
			}
		}
	}

	private void checkf() {
		// TODO Auto-generated method stub
		try {
			DetailFalse detailGuardian = (new Gson()).fromJson(
					appPrefes.getData("sessionsession"), DetailFalse.class);
			if (detailGuardian != null) {
				int size = detailGuardian.lists.get(0).id.size();
				if (appPrefes.getData("setup").equals("")) {
					Intent intent = new Intent(AddGuard.this, SetUppage.class);
					startActivity(intent);
					finish();
					return;
				} else if (size >= 1) {
					Intent intent = new Intent(AddGuard.this, Home.class);
					startActivity(intent);
					finish();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void Continu(View view) {
		// TODO Auto-generated method stub
		if (!continuee)
			guarderror();
		else {
			Intent intent = new Intent(AddGuard.this, SetUppage.class);
			startActivity(intent);
			finish();
		}
	}

	public void AddGuardBt(View view) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, AddEmergency.class);
		intent.putExtra("imtag", "1");
		startActivity(intent);
	}

	private void guarderror() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
				.setTitle("Error message")
				.setMessage(
						"Please select a minimum of One Guardian to continue")
				.setCancelable(false).setNegativeButton("Ok", null).show();
	}
}
