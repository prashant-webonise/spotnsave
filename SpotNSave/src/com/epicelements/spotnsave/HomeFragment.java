package com.epicelements.spotnsave;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.belcomm.scanservice.ScanBGService;
import com.blecomm.main.BluetoothDeviceActor;
import com.blecomm.utils.Utils;
import com.capricorn.ArcLayout;
import com.capricorn.ArcMenu;
import com.epicelements.spotnsave.DetailGuardian.lists;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.luttu.AppPrefes;
import com.luttu.GPSTracker;
import com.luttu.PictureOrentation;
import com.makeramen.RoundedImageView;
import com.util.Constant;
import com.util.GlobalFunctions;
import com.util.GlobalFunctions.HttpResponseHandler;
import com.util.ImageSmallerAction;
import com.util.NewDetailGuardian;

public class HomeFragment extends Fragment implements OnClickListener {

	private static final String TAG = "HomeFragment";
	private View mRootView;
	ImageView im_cam;
	ImageView im_reg;
	ImageSmallerAction imageSmallerAction = new ImageSmallerAction();
	private int width;
	private int height;
	private Button bt_mr;
	private Button bt_ms;
	private Button bt_activaet;
	private ImageView im_emergency1;
	private ImageView im_emergency2;
	private ImageView im_emergency3;
	private ImageView im_emergency4;
	private ImageView im_emergency5;
	private ImageView im_settings;
	AppPrefes appPrefes;
	private RoundedImageView im_shadow_emergency1;
	private RoundedImageView im_shadow_emergency2;
	private RoundedImageView im_shadow_emergency3;
	private RoundedImageView im_shadow_emergency4;
	private RoundedImageView im_shadow_emergency5;
	private TextView tv_emergency2;
	private TextView tv_emergency4;
	private TextView tv_emergency5;
	private TextView tv_emergency3;
	private TextView tv_emergency1;
	// private RoundedImageView im_cntersos;
	private ArcMenu im_centreprofile;
	private Button im_cntersos_view;
	private Button bt_cntersos_view;
	private TextView tv1;
	private boolean noguard;
	public static Context context;
	private BluetoothDeviceActor BDA;
	TextView txtbatteryVal;
	private ProgressDialog pDialog;
	protected DetailGuardian detailGuardian;
	Button bt_location;
	private TextView textView2;
	private Timer timer;
	private long startTime = 0L;
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	private MyTimerTask myTimerTask;
	private Button control_hint;
	private TextView txtViewGuardiansAdded;
	private static Home mContext;
	final int maxGuardiansAllowed = 5;
	int guardianCount;
	RelativeLayout sosActiveLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.home, null);
		mContext = (Home) getActivity();
		Log.i("Home", "initializing");
		initialize();

		return mRootView;
	}

	private void initialize() {
		appPrefes = new AppPrefes(mContext, "sns");
		findid();
		getwidth();
		context = mContext.getApplicationContext();
		// turnGPSOn();
		GPSTracker gpsTracker = new GPSTracker(mContext);
		// new GpsGet(this);
		// System.out.println("gpsTracker"+gpsTracker.getLatitude());

		Intent intent = new Intent("com.epicelements.spotnsave.START_SERVICE5");
		mContext.getApplicationContext().startService(intent);
		if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

			if (Utils.BDA != null && Utils.BDA.isConnected()) {
				Utils.BDA.deviceIsReadyForCommunication("batteryCommand", 0);
				Utils.BDA.setContext(mContext);
			} else {
				BDA = new BluetoothDeviceActor();
				BDA.initialization(mContext);
				BDA.setDeviceId(1);
				BDA.scanDeviceObject();
				Utils.BDA = BDA;
				startBGService();
			}
		}
		textView2 = (TextView) mRootView.findViewById(R.id.textView2);
		((ViewGroup) textView2.getParent()).setVisibility(View.GONE);
		changefont();

	}

	private void findid() {
		// TODO Auto-generated method stub
		tv1 = (TextView) mRootView.findViewById(R.id.tv1);
		bt_location = (Button) mRootView.findViewById(R.id.bt_location);
		bt_location.setOnClickListener(this);
		im_emergency1 = (ImageView) mRootView.findViewById(R.id.im_emergency1);
		im_shadow_emergency1 = (RoundedImageView) mRootView.findViewById(R.id.im_shadow_emergency1);
		tv_emergency1 = (TextView) mRootView.findViewById(R.id.tv_emergency1);
		im_emergency2 = (ImageView) mRootView.findViewById(R.id.im_emergency2);
		im_shadow_emergency2 = (RoundedImageView) mRootView.findViewById(R.id.im_shadow_emergency2);
		tv_emergency2 = (TextView) mRootView.findViewById(R.id.tv_emergency2);
		im_emergency3 = (ImageView) mRootView.findViewById(R.id.im_emergency3);
		im_shadow_emergency3 = (RoundedImageView) mRootView.findViewById(R.id.im_shadow_emergency3);
		tv_emergency3 = (TextView) mRootView.findViewById(R.id.tv_emergency3);
		im_emergency4 = (ImageView) mRootView.findViewById(R.id.im_emergency4);
		im_shadow_emergency4 = (RoundedImageView) mRootView.findViewById(R.id.im_shadow_emergency4);
		tv_emergency4 = (TextView) mRootView.findViewById(R.id.tv_emergency4);
		im_emergency5 = (ImageView) mRootView.findViewById(R.id.im_emergency5);
		im_shadow_emergency5 = (RoundedImageView) mRootView.findViewById(R.id.im_shadow_emergency5);
		tv_emergency5 = (TextView) mRootView.findViewById(R.id.tv_emergency5);
		// im_cntersos = (RoundedImageView) findViewById(R.id.im_cntersos);
		txtbatteryVal = (TextView) mRootView.findViewById(R.id.txtbattery);

		// //////////////////////////////////////////////////////////////
		im_centreprofile = (ArcMenu) mRootView.findViewById(R.id.im_centreprofile);
		control_hint = (Button) im_centreprofile.findViewById(R.id.control_hint);
		ArcLayout item_layout = (ArcLayout) im_centreprofile.findViewById(R.id.item_layout);
		
		control_hint.setOnClickListener(sosOnClickListener);
		im_centreprofile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		item_layout.setOnClickListener(sosOnClickListener);
		item_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});

		// //////////////////////////////////////////////////////////////

		im_cntersos_view = (Button) mRootView.findViewById(R.id.im_cntersos_view);
		bt_cntersos_view = (Button) mRootView.findViewById(R.id.bt_cntersos_view);

		im_emergency1.setTag("1");
		im_emergency2.setTag("2");
		im_emergency3.setTag("3");
		im_emergency4.setTag("4");
		im_emergency5.setTag("5");
		im_reg = (ImageView) mRootView.findViewById(R.id.im_reg);
		im_settings = (ImageView) mRootView.findViewById(R.id.im_settings);
		// bt_mr = (Button) findViewById(R.id.bt_mr);
		// bt_ms = (Button) findViewById(R.id.bt_ms);
		// bt_activaet = (Button) findViewById(R.id.bt_activaet);
		im_emergency1.setOnClickListener(this);
		im_emergency2.setOnClickListener(this);
		im_emergency3.setOnClickListener(this);
		im_emergency4.setOnClickListener(this);
		im_emergency5.setOnClickListener(this);
		im_settings.setOnClickListener(this);
		im_centreprofile.setOnClickListener(sosOnClickListener);

		im_shadow_emergency1.setBorderWidth(0);
		im_shadow_emergency1.setOval(false);
		im_shadow_emergency2.setBorderWidth(0);
		im_shadow_emergency2.setOval(false);
		im_shadow_emergency3.setBorderWidth(0);
		im_shadow_emergency3.setOval(false);
		im_shadow_emergency4.setBorderWidth(0);
		im_shadow_emergency4.setOval(false);
		im_shadow_emergency5.setBorderWidth(0);
		im_shadow_emergency5.setOval(false);
		bt_cntersos_view.setOnClickListener(this);
		// bt_ms.setOnClickListener(this);
		im_cntersos_view.setOnClickListener(sosOnClickListener);
		sosActiveLayout = (RelativeLayout) mRootView.findViewById(R.id.sosActiveLayout);

		sosActiveLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stop();
			}
		});

		txtViewGuardiansAdded = (TextView) mRootView.findViewById(R.id.txtViewGuardiansAdded);
	}

	protected void changefont() {
		// TODO Auto-generated method stub
		// Typeface mFont = Typeface.createFromAsset(getAssets(),
		// "FontAwesome.otf");
		// bt_location.setTypeface(mFont);
		// tv1.setTypeface(mFont);
	}

	private void turnGPSOnsdfn() {
		// if (android.os.Build.VERSION.SDK_INT >=
		// android.os.Build.VERSION_CODES.KITKAT) {
		// return;
		// }
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", true);
		mContext.sendBroadcast(intent);

		String provider = Settings.Secure.getString(mContext.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (!provider.contains("gps")) { // if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			mContext.sendBroadcast(poke);

		}
	}

	private void check() {
		// TODO Auto-generated method stub
		Bundle sdf = mContext.getIntent().getExtras();
		try {
			System.out.println("sdds" + sdf.getString("cancel"));
			appPrefes.SaveData("sos", "deactive");
			if (appPrefes.getData("sos").equals("active")) {
				bt_cntersos_view.setVisibility(View.VISIBLE);
				im_cntersos_view.setVisibility(View.GONE);
				// tv1.setText("spotNsave is now Online");
				tv1.setText("Emergency Alert: Activated");
				// im_cntersos.setVisibility(View.GONE);
			} else {
				// tv1.setText("spotNsave is now Offline");
				tv1.setText("Emergency Alert: De-Activated");
				bt_cntersos_view.setVisibility(View.GONE);
				im_cntersos_view.setVisibility(View.VISIBLE);
				// im_cntersos.setVisibility(View.VISIBLE);
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// setimage();
		setsos();
		check();
		if (appPrefes.getData("gotogetdetail").equals("true")) {
			appPrefes.SaveData("gotogetdetail", "false");
		}
		if (appPrefes.getData("settingschaged").equals("true")) {
			appPrefes.SaveData("settingschaged", "false");
		}
		// mContext.registerReceiver(mBatteryReceiver,
		// Utils.makeIntentFilter());
		if (appPrefes.getData("sos").equals("active")) {
			if (appPrefes.getIntData("remainsecond") == 0) {
				appPrefes.SaveIntData("remainsecond", 120);
			}
			Integer remain = appPrefes.getIntData("remainsecond");
			if (timer == null)
				timer = new Timer();
			if (myTimerTask == null) {
				timeSwapBuff = remain * 1000L;
				startTime = SystemClock.uptimeMillis();
				myTimerTask = new MyTimerTask();
				timer.schedule(myTimerTask, 1000, 1000);
			}
			sosActiveLayout.setVisibility(View.VISIBLE);
			im_centreprofile.setVisibility(View.GONE);
			((ViewGroup) textView2.getParent()).setVisibility(View.VISIBLE);
			// im_centreprofile.setBackgroundResource(R.drawable.tap_to_cancel);
			bt_cntersos_view.setVisibility(View.VISIBLE);
			im_cntersos_view.setVisibility(View.GONE);
			// tv1.setText("spotNsave is now Online");
			tv1.setText("Emergency Alert: Activated");
			// im_cntersos.setVisibility(View.GONE);
		} else {
			sosActiveLayout.setVisibility(View.GONE);
			im_centreprofile.setVisibility(View.VISIBLE);
			((ViewGroup) textView2.getParent()).setVisibility(View.GONE);
			control_hint.setText("SOS");
			// im_centreprofile.setBackgroundResource(R.drawable.bt_selector_like);
			// tv1.setText("spotNsave is now Offline");
			tv1.setText("Emergency Alert: De-Activated");
			bt_cntersos_view.setVisibility(View.GONE);
			im_cntersos_view.setVisibility(View.VISIBLE);
			// im_cntersos.setVisibility(View.VISIBLE);
		}
		txtbatteryVal.setVisibility(View.VISIBLE);

		getBatteryLevel();
		gettingguardians();
	}

	private void gettingguardians() {
		// TODO Auto-generated method stub
		clearall();
		RequestParams params = new RequestParams();
		AsyncHttpClient client = new AsyncHttpClient();
		params.put("userID", appPrefes.getData("userID"));
		// progressshow("Getting guadians");
		GlobalFunctions.postApiCall(mContext, Registration_New.Url + "guardianlist.php", params, client,
				new HttpResponseHandler() {

					@Override
					public void handle(String response, boolean success) {
						// TODO Auto-generated method stub
						// progresscancel();
						if (response != null) {
							try {
								detailGuardian = (new Gson()).fromJson(response.toString(), DetailGuardian.class);
								if (detailGuardian.Status.equals("success")) {
									setguardian();
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}

						}
					}
				});
	}

	private void progressshow(String message) {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(mContext);
		pDialog.setMessage(message);
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}

	private void progresscancel() {
		// TODO Auto-generated method stub
		pDialog.dismiss();
	}

	private void clearall() {
		// TODO Auto-generated method stub
		tv_emergency1.setText("");
		tv_emergency2.setText("");
		tv_emergency3.setText("");
		tv_emergency4.setText("");
		tv_emergency5.setText("");
		im_shadow_emergency1.setImageBitmap(null);
		im_shadow_emergency2.setImageBitmap(null);
		im_shadow_emergency3.setImageBitmap(null);
		im_shadow_emergency4.setImageBitmap(null);
		im_shadow_emergency5.setImageBitmap(null);
	}

	private void setsos() {
		// TODO Auto-generated method stub
		System.out.println("name" + appPrefes.getData("gname1"));
		noguard = false;
		if (!appPrefes.getData("gname1").equals("")) {
			noguard = true;
		}
		if (!appPrefes.getData("gname2").equals("")) {
			noguard = true;
		}
		if (!appPrefes.getData("gname2").equals("")) {
			noguard = true;
		}
		if (!appPrefes.getData("gname4").equals("")) {
			noguard = true;
		}
		if (!appPrefes.getData("gname5").equals("")) {
			noguard = true;
		}
	}

	private void setimage() {
		// TODO Auto-generated method stub
		// noguard = true;
		if (appPrefes.getData("gname1").trim().length() > 10) {
			String n1 = appPrefes.getData("gname1").split(" ")[0];
			tv_emergency1.setText(n1);
		} else {
			tv_emergency1.setText(appPrefes.getData("gname1"));
		}
		if (appPrefes.getData("gname2").trim().length() > 10) {
			tv_emergency2.setText(appPrefes.getData("gname2").split(" ")[0]);
		} else {
			tv_emergency2.setText(appPrefes.getData("gname2"));
		}
		if (appPrefes.getData("gname2").trim().length() > 10) {
			tv_emergency3.setText(appPrefes.getData("gname3").split(" ")[0]);
		} else {
			tv_emergency3.setText(appPrefes.getData("gname3"));
		}
		if (appPrefes.getData("gname4").trim().length() > 10) {
			tv_emergency4.setText(appPrefes.getData("gname4").split(" ")[0]);
		} else {
			tv_emergency4.setText(appPrefes.getData("gname4"));
		}
		if (appPrefes.getData("gname5").trim().length() > 10) {
			tv_emergency5.setText(appPrefes.getData("gname5").split(" ")[0]);
		} else {
			tv_emergency5.setText(appPrefes.getData("gname5"));
		}

		if (!appPrefes.getData("gname1").equals("")) {
			noguard = true;
		}
		if (!appPrefes.getData("gname2").equals("")) {
			noguard = true;
		}
		if (!appPrefes.getData("gname2").equals("")) {
			noguard = true;
		}
		if (!appPrefes.getData("gname4").equals("")) {
			noguard = true;
		}
		if (!appPrefes.getData("gname5").equals("")) {
			noguard = true;
		}
		im_shadow_emergency1.setVisibility(View.GONE);
		im_shadow_emergency2.setVisibility(View.GONE);
		im_shadow_emergency3.setVisibility(View.GONE);
		im_shadow_emergency4.setVisibility(View.GONE);
		im_shadow_emergency5.setVisibility(View.GONE);
		if (!appPrefes.getData("ggc1").equals("")) {
			if (appPrefes.getData("ggc1").equals("cam")) {
				im_shadow_emergency1.setVisibility(View.VISIBLE);
				PictureOrentation orentation = new PictureOrentation();
				orentation.change(appPrefes.getData("gimage1"), im_shadow_emergency1, mContext);
			} else if (appPrefes.getData("ggc1").equals("gallery")) {
				im_shadow_emergency1.setVisibility(View.VISIBLE);
				Bitmap bitmap = imageSmallerAction.decodeSampledBitmapFromGallery(appPrefes.getData("gimage1"), 100,
						100);
				im_shadow_emergency1.setImageBitmap(bitmap);
			}
		}
		if (!appPrefes.getData("ggc2").equals("")) {
			if (appPrefes.getData("ggc2").equals("cam")) {
				im_shadow_emergency2.setVisibility(View.VISIBLE);
				PictureOrentation orentation = new PictureOrentation();
				orentation.change(appPrefes.getData("gimage2"), im_shadow_emergency2, mContext);
			} else if (appPrefes.getData("ggc2").equals("gallery")) {
				im_shadow_emergency2.setVisibility(View.VISIBLE);
				Bitmap bitmap = imageSmallerAction.decodeSampledBitmapFromGallery(appPrefes.getData("gimage2"), 100,
						100);
				im_shadow_emergency2.setImageBitmap(bitmap);
			}
		}
		if (!appPrefes.getData("ggc3").equals("")) {
			if (appPrefes.getData("ggc3").equals("cam")) {
				im_shadow_emergency3.setVisibility(View.VISIBLE);
				PictureOrentation orentation = new PictureOrentation();
				orentation.change(appPrefes.getData("gimage3"), im_shadow_emergency3, mContext);
			} else if (appPrefes.getData("ggc3").equals("gallery")) {
				im_shadow_emergency3.setVisibility(View.VISIBLE);
				Bitmap bitmap = imageSmallerAction.decodeSampledBitmapFromGallery(appPrefes.getData("gimage3"), 100,
						100);
				im_shadow_emergency3.setImageBitmap(bitmap);
			}
		}
		if (!appPrefes.getData("ggc4").equals("")) {
			if (appPrefes.getData("ggc4").equals("cam")) {
				im_shadow_emergency4.setVisibility(View.VISIBLE);
				PictureOrentation orentation = new PictureOrentation();
				orentation.change(appPrefes.getData("gimage4"), im_shadow_emergency4, mContext);
			} else if (appPrefes.getData("ggc4").equals("gallery")) {
				im_shadow_emergency4.setVisibility(View.VISIBLE);
				Bitmap bitmap = imageSmallerAction.decodeSampledBitmapFromGallery(appPrefes.getData("gimage4"), 100,
						100);
				im_shadow_emergency4.setImageBitmap(bitmap);
			}
		}
		if (!appPrefes.getData("ggc5").equals("")) {
			if (appPrefes.getData("ggc5").equals("cam")) {
				im_shadow_emergency5.setVisibility(View.VISIBLE);
				PictureOrentation orentation = new PictureOrentation();
				orentation.change(appPrefes.getData("gimage5"), im_shadow_emergency5, mContext);
			} else if (appPrefes.getData("ggc5").equals("gallery")) {
				im_shadow_emergency5.setVisibility(View.VISIBLE);
				Bitmap bitmap = imageSmallerAction.decodeSampledBitmapFromGallery(appPrefes.getData("gimage5"), 100,
						100);
				im_shadow_emergency5.setImageBitmap(bitmap);
			}
		}

		im_shadow_emergency1.setVisibility(View.GONE);
		im_shadow_emergency2.setVisibility(View.GONE);
		im_shadow_emergency3.setVisibility(View.GONE);
		im_shadow_emergency4.setVisibility(View.GONE);
		im_shadow_emergency5.setVisibility(View.GONE);
	}

	private OnClickListener sosOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			if (appPrefes.getData("sos").equals("active")) {
				stop();
			} else {
				if (noguard) {
					Intent intent = new Intent(mContext, SosView.class);
					startActivity(intent);
					mContext.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
				} else {
					new AlertDialog.Builder(context).setTitle("Error message")
							.setMessage("Please select a minimum of one guardian to send an SOS").setCancelable(false)
							.setNegativeButton("Ok", new ondialog()).show();
				}
			}

		}
	};

	class ondialog implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(mContext, Settingsnew.class);
			startActivity(intent);
		}

	}

	public void Location(View view) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(mContext, MyLocfragment.class);

		mContext.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
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
		case R.id.im_settings:
			Intent intent = new Intent(mContext, Settingsnew.class);
			startActivity(intent);
			break;
		case R.id.bt_cntersos_view:
			stop();
			// intent = new Intent(Home.this, AllSosStopped.class);
			// startActivity(intent);
			break;
		case R.id.im_centreprofile:

			break;
		case R.id.bt_location:
			Location(v);
			break;
		default:
			/*
			 * intent = new Intent(mContext, AddEmergency.class); String detail
			 * = (new Gson()).toJson(detailGuardian); intent.putExtra("imtag",
			 * v.getTag().toString()); intent.putExtra("detail", detail);
			 * startActivity(intent);
			 */
			break;
		}
	}

	private void setbitmap(final ImageView imageView, final int id) {
		// TODO Auto-generated method stub
		new AsyncTask<Void, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Bitmap bitmap = imageSmallerAction.decodeSampledBitmapFromResource(getResources(), id, width, height);
				return bitmap;
			}

			@Override
			protected void onPostExecute(Bitmap bitmap) {
				// TODO Auto-generated method stub
				super.onPostExecute(bitmap);

				imageView.setImageBitmap(bitmap);
			}
		}.execute();
	}

	private void setbitmapwithsize(final ImageView imageView, final int id, final int width, final int height) {
		// TODO Auto-generated method stub
		new AsyncTask<Void, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Bitmap bitmap = imageSmallerAction.decodeSampledBitmapFromResource(getResources(), id, width, height);
				return bitmap;
			}

			@Override
			protected void onPostExecute(Bitmap bitmap) {
				// TODO Auto-generated method stub
				super.onPostExecute(bitmap);

				imageView.setImageBitmap(bitmap);
			}
		}.execute();
	}

	private void stop() {
		// TODO Auto-generated method stub
		appPrefes.SaveData("sos", "deactive");
		if (appPrefes.getData("sos").equals("active")) {
			((ViewGroup) textView2.getParent()).setVisibility(View.VISIBLE);
			// im_centreprofile.setBackgroundResource(R.drawable.tap_to_cancel);
			bt_cntersos_view.setVisibility(View.VISIBLE);
			im_cntersos_view.setVisibility(View.GONE);
			// tv1.setText("spotNsave is now Online");
			tv1.setText("Emergency Alert: Activated");

			sosActiveLayout.setVisibility(View.VISIBLE);
			im_centreprofile.setVisibility(View.GONE);
			// im_cntersos.setVisibility(View.GONE);
		} else {

			sosActiveLayout.setVisibility(View.GONE);
			im_centreprofile.setVisibility(View.VISIBLE);
			((ViewGroup) textView2.getParent()).setVisibility(View.GONE);
			control_hint.setText(R.string.sos);
			// im_centreprofile.setBackgroundResource(R.drawable.bt_selector_like);
			// tv1.setText("spotNsave is now Offline");
			tv1.setText("Emergency Alert: De-Activated");
			bt_cntersos_view.setVisibility(View.GONE);
			im_cntersos_view.setVisibility(View.VISIBLE);
			// im_cntersos.setVisibility(View.VISIBLE);
		}
		String number1 = appPrefes.getData("gnumber1"); // getting String
		String number2 = appPrefes.getData("gnumber2"); // getting String
		String number3 = appPrefes.getData("gnumber3"); // getting String
		String number4 = appPrefes.getData("gnumber4"); // getting String
		String number5 = appPrefes.getData("gnumber5"); // getting String
		String fullname = appPrefes.getData("fullnameregistration"); // getting
																		// String
		Intent intent = new Intent("com.epicelements.spotnsave.START_SERVICE4");
		mContext.stopService(intent);
		if (SOSLocationProviderService.myNotificationManager5 != null) {
			SOSLocationProviderService.myNotificationManager5.cancel(SOSLocationProviderService.NOTIFICATION_IDD);
		}
		if (number1 != null && !number1.equals("") && fullname == null) {
			sendSMS(number1, "I have canceled the SOS and is no longer in an emergency.");
		}
		if (number1 != null && !number1.equals("") && fullname != null) {
			sendSMS(number1, fullname + " has cancelled the SOS and is no longer in an emergency");
		}
		if (number2 != null && !number2.equals("") && fullname == null) {
			sendSMS(number2, "I have canceled the SOS and is no longer in an emergency.");
		}
		if (number2 != null && !number2.equals("") && fullname != null) {
			sendSMS(number2, fullname + " has cancelled the SOS and is no longer in an emergency");
		}
		if (number3 != null && !number3.equals("") && fullname == null) {
			sendSMS(number3, "I have canceled the SOS and is no longer in an emergency.");
		}
		if (number3 != null && !number3.equals("") && fullname != null) {
			sendSMS(number3, fullname + " has cancelled the SOS and is no longer in an emergency");
		}
		if (number4 != null && !number4.equals("") && fullname == null) {
			sendSMS(number4, "I have canceled the SOS and is no longer in an emergency.");
		}
		if (number4 != null && !number4.equals("") && fullname != null) {
			sendSMS(number4, fullname + " has cancelled the SOS and is no longer in an emergency");
		}
		if (number5 != null && !number5.equals("") && fullname == null) {
			sendSMS(number5, "I have canceled the SOS and is no longer in an emergency.");
		}
		if (number5 != null && !number5.equals("") && fullname != null) {
			sendSMS(number5, fullname + " has cancelled the SOS and is no longer in an emergency");
		}

	}

	private void sendSMS(String phoneNumber, String message) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}

	private void getwidth() {
		// TODO Auto-generated method stub
		WindowManager w = mContext.getWindowManager();
		Display d = w.getDefaultDisplay();
		width = d.getWidth();
		height = d.getHeight();
	}

	/*
	 * @Override protected void onDestroy() { // TODO Auto-generated method stub
	 * super.onDestroy(); try { if (mBatteryReceiver != null) {
	 * unregisterReceiver(mBatteryReceiver); } } catch (IllegalArgumentException
	 * ex) {
	 * 
	 * } catch (Exception e) { // TODO: handle exception } // Utils.BDA = null;
	 * // BDA = null; // this.startService(new
	 * Intent(this,ScanBGService.class)); }
	 */

	public static void startBGService() {

		if (!isMyServiceRunning()) {
			Log.e("service started first time", "");
			ScanBGService.intentService = new Intent(context, ScanBGService.class);
			context.startService(ScanBGService.intentService);

		} else {
			Log.e(" service is already running ", "");
		}

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {

				ScanBGService.serviceBDA = Utils.getBDA();

			}
		}, 3000);

	}

	static boolean isMyServiceRunning() {
		return Home.isMyServiceRunning();
	}

	public void getBatteryLevel() {

		float val;

		Intent batteryIntent = mContext.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		float level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		float scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		// Error checking that probably isn't needed but I added just in case.
		if (level == -1 || scale == -1) {
			val = 50;
		}

		val = (level / scale) * 100;

		txtbatteryVal.setText(getString(R.string.device_battery_));
		setBatteryImage(txtbatteryVal, (int) val);
	}

	private void setBatteryImage(TextView txtbatteryVal, int value) {
		if (value > 0 && value <= 5) {
			txtbatteryVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stat_sys_battery_0, 0);
		} else if (value > 5 && value < 18) {
			txtbatteryVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stat_sys_battery_15, 0);
		} else if (value >= 18 && value < 32) {
			txtbatteryVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stat_sys_battery_28, 0);
		} else if (value >= 32 && value < 48) {
			txtbatteryVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stat_sys_battery_43, 0);
		} else if (value >= 48 && value < 61) {
			txtbatteryVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stat_sys_battery_57, 0);
		} else if (value >= 61 && value < 75) {
			txtbatteryVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stat_sys_battery_71, 0);
		} else if (value >= 75 && value < 95) {
			txtbatteryVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stat_sys_battery_85, 0);
		} else if (value >= 95) {
			txtbatteryVal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stat_sys_battery_100, 0);
		}

	}

	/*
	 * private final BroadcastReceiver mBatteryReceiver = new
	 * BroadcastReceiver() {
	 * 
	 * @Override public void onReceive(Context context, Intent intent) { int
	 * currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1); int
	 * scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1); int level =
	 * -1; if (currentLevel >= 0 && scale > 0) { level = (currentLevel * 100) /
	 * scale; } txtbatteryVal.setVisibility(View.VISIBLE); //
	 * txtbatteryVal.setText(getString(R.string.device_battery_) +
	 * getBatteryLevel() + "%");
	 * 
	 * Log.i("AA", "###########");
	 * 
	 * 
	 * String action = intent.getAction(); if
	 * (Utils.ACTION_BATTERY_LEVEL.equals(action)) { String val =
	 * String.valueOf(Utils.getBatteryValue());
	 * 
	 * Log.i("AA", "$$$$$$$$$"+val);
	 * 
	 * txtbatteryVal.setVisibility(View.VISIBLE);
	 * txtbatteryVal.setText(getString(R.string.device_battery_) + val + "%"); }
	 * 
	 * }
	 * 
	 * };
	 */

	private void setguardian() {
		// TODO Auto-generated method stub
		if (detailGuardian != null) {
			lists lists = detailGuardian.lists.get(0);
			if (lists.id.size() == 1) {
				tv_emergency1.setText(lists.name.get(0));
			} else if (lists.id.size() == 2) {
				tv_emergency1.setText(lists.name.get(0));
				tv_emergency2.setText(lists.name.get(1));
			} else if (lists.id.size() == 3) {
				tv_emergency1.setText(lists.name.get(0));
				tv_emergency2.setText(lists.name.get(1));
				tv_emergency3.setText(lists.name.get(2));
			} else if (lists.id.size() == 4) {
				tv_emergency1.setText(lists.name.get(0));
				tv_emergency2.setText(lists.name.get(1));
				tv_emergency3.setText(lists.name.get(2));
				tv_emergency4.setText(lists.name.get(3));
			} else if (lists.id.size() == 5) {
				tv_emergency1.setText(lists.name.get(0));
				tv_emergency2.setText(lists.name.get(1));
				tv_emergency3.setText(lists.name.get(2));
				tv_emergency4.setText(lists.name.get(3));
				tv_emergency5.setText(lists.name.get(4));
			}
		}
		showGuardians();

	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			// int remtime = appPrefes.getIntData("remainsecond");
			// remtime--;
			// System.out.println("kittunna remainsecond time" + remtime);
			// appPrefes.SaveIntData("remainsecond", remtime);
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			updatedTime = timeSwapBuff - timeInMilliseconds;
			final String hms = String.format(
					"%02d:%02d",
					TimeUnit.MILLISECONDS.toMinutes(updatedTime)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(updatedTime)),
					TimeUnit.MILLISECONDS.toSeconds(updatedTime)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(updatedTime)));
			long lastsec = TimeUnit.MILLISECONDS.toSeconds(updatedTime)
					- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(updatedTime));
			System.out.println(lastsec + " hms " + hms);
			if (hms.equals("00:00") || 0 > lastsec) {
				if (textView2 != null) {
					try {
						timeSwapBuff = 120 * 1000L;
						appPrefes.SaveIntData("remainsecond", 120);
						startTime = SystemClock.uptimeMillis();
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			}
			mContext.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					try {
						if (textView2 != null)
							textView2.setText(hms);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			});
		}
	}

	private void showGuardians() {

		im_centreprofile.removeAllChildItems();

		if (detailGuardian != null && detailGuardian.lists.get(0).id.size() > 0) {
			guardianCount = detailGuardian.lists.get(0).id.size();

			Log.i(TAG, "@@@@@@@@@@@ guardianCount " + guardianCount);

			if (maxGuardiansAllowed > guardianCount) {
				for (int i = 0; i < guardianCount; i++) {

					final NewDetailGuardian guardian = new NewDetailGuardian();
					guardian.getGuardianDetailFor(detailGuardian.lists.get(0), i);
					
					Button item = new Button(mContext);
					item.setBackgroundResource(R.drawable.drawable_guardian_icon);
					item.setText(guardian.getName());
					item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
					item.setSingleLine(true);

					Log.i(TAG, "@@@@@@@@@@@ guardian namew " + guardian.getName());

					item.setTag(String.valueOf(guardianCount));
					im_centreprofile.addItem(item, new OnClickListener() {

						@Override
						public void onClick(View v) {				
							Intent intent = new Intent(mContext, AddEmergency.class);
							intent.putExtra(Constant.KEY_SER, guardian);
							startActivity(intent);
						}
					});
				}

				for (int i = guardianCount; i < maxGuardiansAllowed; i++) {

					View item = mContext.getLayoutInflater().inflate(R.layout.add_new_guardian_button, null);
					item.setTag(String.valueOf(guardianCount));
					im_centreprofile.addItem(item, new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(new Intent(mContext, AddEmergency.class));
							intent.putExtra("imtag", v.getTag().toString());
							startActivity(intent);
						}
					});
				}
			}
		} else

			for (int i = 0; i < maxGuardiansAllowed; i++) {

				View item = mContext.getLayoutInflater().inflate(R.layout.add_new_guardian_button, null);
				item.setTag(String.valueOf(i));
				im_centreprofile.addItem(item, new OnClickListener() {

					@Override
					public void onClick(View v) {
						
					}
				});
			}

		showGuardianCount();
	}

	private void showGuardianCount() {
		txtViewGuardiansAdded.setText(getString(R.string.guardians_added_) + guardianCount + "/" + maxGuardiansAllowed);
	}

}
