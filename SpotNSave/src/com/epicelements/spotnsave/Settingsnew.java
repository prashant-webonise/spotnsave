package com.epicelements.spotnsave;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.luttu.AppPrefes;
import com.luttu.PictureOrentation;
import com.luttu.Utils;
import com.makeramen.RoundedImageView;
import com.util.BitmapWorkerTask;
import com.util.ImageSmallerAction;

public class Settingsnew extends FragmentActivity implements OnClickListener {
	ImageView im_settingsprofile;
	ImageView im_reg;
	private int width;
	private int height;
	private Button bt_mr;
	private Button bt_ms;
	private ImageView li_batteyvisible;
	private AppPrefes appPrefes;
	ImageSmallerAction imageSmallerAction = new ImageSmallerAction();
	private RoundedImageView im_shadow;
	private ImageView im_next;

	private Uri fileUri;
	private Utils utilObj = new Utils();
	ImageSmallerAction imageSmalerObj = new ImageSmallerAction();
	int RESULT_LOAD_GALLERY = 99;
	int RESULT_LOAD_CAMERA = 100;
	private static final int MEDIA_TYPE_IMAGE = 1;
	String profilepath;
	String gc = "noimage";
	protected DetailGuardian detailGuardian;
	private TextView tv_pfname;
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);
		setContentView(R.layout.settings_new);
		appPrefes = new AppPrefes(this, "sns");
		findid();
		getwidth();
	}

	private void findid() {
		// TODO Auto-generated method stub
		im_reg = (ImageView) findViewById(R.id.im_reg);
		im_settingsprofile = (ImageView) findViewById(R.id.im_settingsprofile);
		li_batteyvisible = (ImageView) findViewById(R.id.li_batteyvisible);
		im_shadow = (RoundedImageView) findViewById(R.id.im_shadow);
		tv_pfname = (TextView) findViewById(R.id.tv_pfname);

		im_next = (ImageView) findViewById(R.id.im_next);
		im_next.setOnClickListener(this);
		im_shadow.setBorderWidth(0);
		im_shadow.setOval(false);
		li_batteyvisible.setOnClickListener(this);
		im_settingsprofile.setOnClickListener(this);
		new BitmapWorkerTask(im_shadow, this, appPrefes.getData("gimagemain"))
				.execute();
		// Picasso.with(this).load(new
		// File(appPrefes.getData("gimagemain"))).into(im_shadow);

	}

	private void setimgs() {
		// TODO Auto-generated method stub
		tv_pfname.setText(appPrefes.getData("fullnameregistration")
				.toUpperCase());
		float scale = getResources().getDisplayMetrics().density;
		if (!appPrefes.getData("ggcmain").equals("")) {
			if (appPrefes.getData("ggcmain").equals("cam")) {
				im_shadow.setVisibility(View.VISIBLE);
				PictureOrentation orentation = new PictureOrentation();
				orentation.change(appPrefes.getData("gimagemain"), im_shadow,
						Settingsnew.this);
			} else if (appPrefes.getData("ggcmain").equals("gallery")) {
				im_shadow.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (appPrefes.getData("gotogetdetail").equals("true")) {
			appPrefes.SaveData("settingschaged", "true");
			appPrefes.SaveData("gotogetdetail", "false");
		}
		try {
			setimgs();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void Deactive(View v) {
		appPrefes.SaveData("fullnameregistration", "");
		appPrefes.SaveData("gnumber1", "");
		appPrefes.SaveData("gnumber2", "");
		appPrefes.SaveData("gnumber3", "");
		appPrefes.SaveData("gnumber4", "");
		appPrefes.SaveData("gnumber5", "");
		appPrefes.SaveData("gname1", "");
		appPrefes.SaveData("gname2", "");
		appPrefes.SaveData("gname3", "");
		appPrefes.SaveData("gname4", "");
		appPrefes.SaveData("gname5", "");
		appPrefes.SaveData("gimage1", "");
		appPrefes.SaveData("gimage2", "");
		appPrefes.SaveData("gimage3", "");
		appPrefes.SaveData("gimage4", "");
		appPrefes.SaveData("gimage5", "");
		appPrefes.SaveData("ggc1", "");
		appPrefes.SaveData("ggc2", "");
		appPrefes.SaveData("ggc3", "");
		appPrefes.SaveData("ggc4", "");
		appPrefes.SaveData("ggc5", "");
		appPrefes.SaveData("ggcmain", "");
		if (Home.context != null)
			((FragmentActivity) Home.context).finish();
		Intent i = new Intent(Settingsnew.this, Splash.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		Settingsnew.this.startActivity(i);
		finish();
	}

	public void Guardians(View view) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Settingsnew.this, Settingsnewnew.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_mr:
			bt_mr.setBackgroundResource(R.drawable.rectangleclick);
			bt_ms.setBackgroundResource(R.drawable.rightrectangleclick);
			break;
		case R.id.bt_ms:
			bt_ms.setBackgroundResource(R.drawable.rightrectangle);
			bt_mr.setBackgroundResource(R.drawable.rectangle);
			break;

		case R.id.im_next:
			finish();
			break;

		case R.id.li_batteyvisible:
			if (li_batteyvisible.getTag() == null) {
				checkbattery(false);
				li_batteyvisible.setImageResource(R.drawable.off);
				li_batteyvisible.setTag("bton");
			} else {
				li_batteyvisible.setImageResource(R.drawable.on);
				checkbattery(true);
				li_batteyvisible.setTag(null);
			}
			break;
		case R.id.im_shadow:
			Upload_Image();
			break;
		case R.id.im_settingsprofile:
			Upload_Image();
			break;

		default:
			intent = new Intent(Settingsnew.this, AddEmergency.class);
			String detail = (new Gson()).toJson(detailGuardian);
			intent.putExtra("imtag", v.getTag().toString());
			intent.putExtra("detail", detail);
			System.out.println("tag" + v.getTag().toString());
			startActivity(intent);
			break;
		}
	}

	private void Upload_Image() {
		// TODO Auto-generated method stub
		final String[] items = { "Camera", "Gallery" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("SELECT A ITEM");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					fileUri = utilObj.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
					startActivityForResult(intent, RESULT_LOAD_CAMERA);
				}
				if (item == 1) {
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, RESULT_LOAD_GALLERY);
				}
			}
		});
		builder.create();
		builder.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("onactivityresult");
		if (requestCode == RESULT_LOAD_GALLERY && resultCode == RESULT_OK
				&& data != null) {
			gc = "gallery";
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			profilepath = picturePath;
			cursor.close();
			Bitmap bitmap = imageSmalerObj.decodeSampledBitmapFromGallery(
					picturePath, 100, 100);
			im_shadow.setImageBitmap(bitmap);
			appPrefes.SaveData("gimagemain", profilepath);
			appPrefes.SaveData("ggcmain", gc);
		}
		if (requestCode == RESULT_LOAD_CAMERA && resultCode == RESULT_OK) {
			gc = "cam";
			profilepath = fileUri.getPath();
			PictureOrentation orentation = new PictureOrentation();
			orentation.change(fileUri.getPath(), im_shadow, Settingsnew.this);
			appPrefes.SaveData("gimagemain", profilepath);
			appPrefes.SaveData("ggcmain", gc);
		}
	}

	private void checkbattery(boolean isChecked) {
		// TODO Auto-generated method stub
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				"MyPref", 0); // 0 - for private mode
		final Editor editor = pref.edit();
		if (isChecked) {
			// The toggle is enabled

			editor.putBoolean("batterycheckstate", true); // value to store
			editor.commit();

			Intent intent = new Intent(
					"com.epicelements.spotnsave.START_SERVICE2");
			Settingsnew.this.startService(intent);

		} else {
			// The toggle is disabled
			editor.putBoolean("batterycheckstate", false); // value to store
			editor.commit();

			Intent intent = new Intent(
					"com.epicelements.spotnsave.START_SERVICE2");
			Settingsnew.this.stopService(intent);
		}
	}

	private void getwidth() {
		// TODO Auto-generated method stub
		Point size = new Point();
		width = size.x;
		height = size.y;
		height = 250;
		WindowManager w = getWindowManager();
		Display d = w.getDefaultDisplay();
		width = d.getWidth();
	}

	public void Legal(View v) {
		startActivity(new Intent(Settingsnew.this, Privacy.class));
	}

	public void About(View v) {
	}
}
