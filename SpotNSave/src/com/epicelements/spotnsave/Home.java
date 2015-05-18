package com.epicelements.spotnsave;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class Home extends FragmentActivity implements OnClickListener {
	/*
	 * ImageView im_cam; ImageView im_reg; ImageSmallerAction imageSmallerAction
	 * = new ImageSmallerAction(); private int width; private int height;
	 * private Button bt_mr; private Button bt_ms; private Button bt_activaet;
	 * private ImageView im_emergency1; private ImageView im_emergency2; private
	 * ImageView im_emergency3; private ImageView im_emergency4; private
	 * ImageView im_emergency5; private ImageView im_settings; AppPrefes
	 * appPrefes; private RoundedImageView im_shadow_emergency1; private
	 * RoundedImageView im_shadow_emergency2; private RoundedImageView
	 * im_shadow_emergency3; private RoundedImageView im_shadow_emergency4;
	 * private RoundedImageView im_shadow_emergency5; private TextView
	 * tv_emergency2; private TextView tv_emergency4; private TextView
	 * tv_emergency5; private TextView tv_emergency3; private TextView
	 * tv_emergency1; // private RoundedImageView im_cntersos; private ArcMenu
	 * im_centreprofile; private Button im_cntersos_view; private Button
	 * bt_cntersos_view; private TextView tv1; private boolean noguard; public
	 * static Context context; private BluetoothDeviceActor BDA; TextView
	 * txtbatteryVal; private ProgressDialog pDialog; protected DetailGuardian
	 * detailGuardian; Button bt_location; private TextView textView2; private
	 * Timer timer; private long startTime = 0L; long timeInMilliseconds = 0L;
	 * long timeSwapBuff = 0L; long updatedTime = 0L; private MyTimerTask
	 * myTimerTask; private Button control_hint;
	 */
	protected FragmentManager fragmentManager;
	public static Context context;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		getActionBar().setTitle("  " + getString(R.string.personal_safety));
		getActionBar().setIcon(R.drawable.navigatio_btn);
		getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary_color)));
		getActionBar().setHomeButtonEnabled(true);

		setContentView(R.layout.dashboard);

		/*
		 * Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
		 * setSupportActionBar(toolbar);
		 */

		// toolbar.setOnMenuItemClickListener(this);

		context = this;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
	    switch (menuItem.getItemId()) {
	        case android.R.id.home:
	        	Intent intent = new Intent(this, Settingsnew.class);
				startActivity(intent);
	            return true;
	    }
	    return (super.onOptionsItemSelected(menuItem));
	}

	@Override
	protected void onResume() {
		super.onResume();
		changeFragment(new HomeFragment());
	}

	public void changeFragment(Fragment targetFragment) {

		if (targetFragment != null) {
			fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

			String packageNameAsTag = ((Object) targetFragment).getClass().getCanonicalName();

			if (fragmentManager.findFragmentByTag(packageNameAsTag) == null) {
				Log.i("Home", "replacing fragment");
				fragmentTransaction.replace(R.id.mainContent, targetFragment, packageNameAsTag);
				fragmentTransaction.commit();
			}
		}
	}

	/*
	 * @Override protected void onCreate(Bundle arg0) { // TODO Auto-generated
	 * method stub requestWindowFeature(Window.FEATURE_NO_TITLE);
	 * super.onCreate(arg0); setContentView(R.layout.home); appPrefes = new
	 * AppPrefes(this, "sns"); findid(); getwidth(); context = this; //
	 * turnGPSOn(); GPSTracker gpsTracker = new GPSTracker(this); // new
	 * GpsGet(this); //
	 * System.out.println("gpsTracker"+gpsTracker.getLatitude());
	 * 
	 * Intent intent = new Intent("com.epicelements.spotnsave.START_SERVICE5");
	 * getApplicationContext().startService(intent); if
	 * (getPackageManager().hasSystemFeature
	 * (PackageManager.FEATURE_BLUETOOTH_LE)) {
	 * 
	 * if (Utils.BDA != null && Utils.BDA.isConnected()) {
	 * Utils.BDA.deviceIsReadyForCommunication("batteryCommand", 0);
	 * Utils.BDA.setContext(Home.this); } else { BDA = new
	 * BluetoothDeviceActor(); BDA.initialization(Home.this);
	 * BDA.setDeviceId(1); BDA.scanDeviceObject(); Utils.BDA = BDA;
	 * startBGService(); } } textView2 = (TextView)
	 * findViewById(R.id.textView2); ((ViewGroup)
	 * textView2.getParent()).setVisibility(View.GONE); changefont();
	 * 
	 * }
	 * 
	 * protected void changefont() { // TODO Auto-generated method stub //
	 * Typeface mFont = Typeface.createFromAsset(getAssets(), //
	 * "FontAwesome.otf"); // bt_location.setTypeface(mFont); //
	 * tv1.setTypeface(mFont); }
	 * 
	 * private void turnGPSOnsdfn() { // if (android.os.Build.VERSION.SDK_INT >=
	 * // android.os.Build.VERSION_CODES.KITKAT) { // return; // } Intent intent
	 * = new Intent("android.location.GPS_ENABLED_CHANGE");
	 * intent.putExtra("enabled", true); sendBroadcast(intent);
	 * 
	 * String provider = Settings.Secure.getString(getContentResolver(),
	 * Settings.Secure.LOCATION_PROVIDERS_ALLOWED); if
	 * (!provider.contains("gps")) { // if gps is disabled final Intent poke =
	 * new Intent(); poke.setClassName("com.android.settings",
	 * "com.android.settings.widget.SettingsAppWidgetProvider");
	 * poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	 * poke.setData(Uri.parse("3")); sendBroadcast(poke);
	 * 
	 * } }
	 * 
	 * private void check() { // TODO Auto-generated method stub Bundle sdf =
	 * getIntent().getExtras(); try { System.out.println("sdds" +
	 * sdf.getString("cancel")); appPrefes.SaveData("sos", "deactive"); if
	 * (appPrefes.getData("sos").equals("active")) {
	 * bt_cntersos_view.setVisibility(View.VISIBLE);
	 * im_cntersos_view.setVisibility(View.GONE); //
	 * tv1.setText("spotNsave is now Online");
	 * tv1.setText("Emergency Alert: Activated"); //
	 * im_cntersos.setVisibility(View.GONE); } else { //
	 * tv1.setText("spotNsave is now Offline");
	 * tv1.setText("Emergency Alert: De-Activated");
	 * bt_cntersos_view.setVisibility(View.GONE);
	 * im_cntersos_view.setVisibility(View.VISIBLE); //
	 * im_cntersos.setVisibility(View.VISIBLE); } } catch (NullPointerException
	 * e) { // TODO: handle exception } }
	 * 
	 * @Override protected void onResume() { // TODO Auto-generated method stub
	 * super.onResume(); // setimage(); setsos(); check(); if
	 * (appPrefes.getData("gotogetdetail").equals("true")) {
	 * appPrefes.SaveData("gotogetdetail", "false"); } if
	 * (appPrefes.getData("settingschaged").equals("true")) {
	 * appPrefes.SaveData("settingschaged", "false"); }
	 * registerReceiver(mBatteryReceiver, Utils.makeIntentFilter()); if
	 * (appPrefes.getData("sos").equals("active")) { if
	 * (appPrefes.getIntData("remainsecond") == 0) {
	 * appPrefes.SaveIntData("remainsecond", 120); } Integer remain =
	 * appPrefes.getIntData("remainsecond"); if (timer == null) timer = new
	 * Timer(); if (myTimerTask == null) { timeSwapBuff = remain * 1000L;
	 * startTime = SystemClock.uptimeMillis(); myTimerTask = new MyTimerTask();
	 * timer.schedule(myTimerTask, 1000, 1000); } ((ViewGroup)
	 * textView2.getParent()).setVisibility(View.VISIBLE);
	 * control_hint.setText(""); //
	 * im_centreprofile.setBackgroundResource(R.drawable.tap_to_cancel);
	 * bt_cntersos_view.setVisibility(View.VISIBLE);
	 * im_cntersos_view.setVisibility(View.GONE); //
	 * tv1.setText("spotNsave is now Online");
	 * tv1.setText("Emergency Alert: Activated"); //
	 * im_cntersos.setVisibility(View.GONE); } else { ((ViewGroup)
	 * textView2.getParent()).setVisibility(View.GONE);
	 * control_hint.setText("SOS"); //
	 * im_centreprofile.setBackgroundResource(R.drawable.bt_selector_like); //
	 * tv1.setText("spotNsave is now Offline");
	 * tv1.setText("Emergency Alert: De-Activated");
	 * bt_cntersos_view.setVisibility(View.GONE);
	 * im_cntersos_view.setVisibility(View.VISIBLE); //
	 * im_cntersos.setVisibility(View.VISIBLE); } gettingguardians(); }
	 * 
	 * private void findid() { // TODO Auto-generated method stub tv1 =
	 * (TextView) findViewById(R.id.tv1); bt_location = (Button)
	 * findViewById(R.id.bt_location); im_emergency1 = (ImageView)
	 * findViewById(R.id.im_emergency1); im_shadow_emergency1 =
	 * (RoundedImageView) findViewById(R.id.im_shadow_emergency1); tv_emergency1
	 * = (TextView) findViewById(R.id.tv_emergency1); im_emergency2 =
	 * (ImageView) findViewById(R.id.im_emergency2); im_shadow_emergency2 =
	 * (RoundedImageView) findViewById(R.id.im_shadow_emergency2); tv_emergency2
	 * = (TextView) findViewById(R.id.tv_emergency2); im_emergency3 =
	 * (ImageView) findViewById(R.id.im_emergency3); im_shadow_emergency3 =
	 * (RoundedImageView) findViewById(R.id.im_shadow_emergency3); tv_emergency3
	 * = (TextView) findViewById(R.id.tv_emergency3); im_emergency4 =
	 * (ImageView) findViewById(R.id.im_emergency4); im_shadow_emergency4 =
	 * (RoundedImageView) findViewById(R.id.im_shadow_emergency4); tv_emergency4
	 * = (TextView) findViewById(R.id.tv_emergency4); im_emergency5 =
	 * (ImageView) findViewById(R.id.im_emergency5); im_shadow_emergency5 =
	 * (RoundedImageView) findViewById(R.id.im_shadow_emergency5); tv_emergency5
	 * = (TextView) findViewById(R.id.tv_emergency5); // im_cntersos =
	 * (RoundedImageView) findViewById(R.id.im_cntersos); txtbatteryVal =
	 * (TextView) findViewById(R.id.txtbattery);
	 * 
	 * // //////////////////////////////////////////////////////////////
	 * im_centreprofile = (ArcMenu) findViewById(R.id.im_centreprofile);
	 * control_hint = (Button) im_centreprofile.findViewById(R.id.control_hint);
	 * control_hint.setOnClickListener(sosOnClickListener);
	 * 
	 * // //////////////////////////////////////////////////////////////
	 * 
	 * im_cntersos_view = (Button) findViewById(R.id.im_cntersos_view);
	 * bt_cntersos_view = (Button) findViewById(R.id.bt_cntersos_view);
	 * 
	 * im_emergency1.setTag("1"); im_emergency2.setTag("2");
	 * im_emergency3.setTag("3"); im_emergency4.setTag("4");
	 * im_emergency5.setTag("5"); im_reg = (ImageView)
	 * findViewById(R.id.im_reg); im_settings = (ImageView)
	 * findViewById(R.id.im_settings); // bt_mr = (Button)
	 * findViewById(R.id.bt_mr); // bt_ms = (Button) findViewById(R.id.bt_ms);
	 * // bt_activaet = (Button) findViewById(R.id.bt_activaet);
	 * im_emergency1.setOnClickListener(this);
	 * im_emergency2.setOnClickListener(this);
	 * im_emergency3.setOnClickListener(this);
	 * im_emergency4.setOnClickListener(this);
	 * im_emergency5.setOnClickListener(this);
	 * im_settings.setOnClickListener(this);
	 * im_centreprofile.setOnClickListener(sosOnClickListener);
	 * 
	 * im_shadow_emergency1.setBorderWidth(0);
	 * im_shadow_emergency1.setOval(false);
	 * im_shadow_emergency2.setBorderWidth(0);
	 * im_shadow_emergency2.setOval(false);
	 * im_shadow_emergency3.setBorderWidth(0);
	 * im_shadow_emergency3.setOval(false);
	 * im_shadow_emergency4.setBorderWidth(0);
	 * im_shadow_emergency4.setOval(false);
	 * im_shadow_emergency5.setBorderWidth(0);
	 * im_shadow_emergency5.setOval(false);
	 * bt_cntersos_view.setOnClickListener(this); //
	 * bt_ms.setOnClickListener(this);
	 * im_cntersos_view.setOnClickListener(sosOnClickListener); }
	 * 
	 * private void gettingguardians() { // TODO Auto-generated method stub
	 * clearall(); RequestParams params = new RequestParams(); AsyncHttpClient
	 * client = new AsyncHttpClient(); params.put("userID",
	 * appPrefes.getData("userID")); // progressshow("Getting guadians");
	 * GlobalFunctions.postApiCall(this, Registration_New.Url +
	 * "guardianlist.php", params, client, new HttpResponseHandler() {
	 * 
	 * @Override public void handle(String response, boolean success) { // TODO
	 * Auto-generated method stub // progresscancel(); if (response != null) {
	 * try { detailGuardian = (new Gson()).fromJson(response.toString(),
	 * DetailGuardian.class); if (detailGuardian.Status.equals("success")) {
	 * setguardian(); } } catch (Exception e) { // TODO: handle exception
	 * e.printStackTrace(); }
	 * 
	 * } } }); }
	 * 
	 * private void progressshow(String message) { // TODO Auto-generated method
	 * stub pDialog = new ProgressDialog(Home.this);
	 * pDialog.setMessage(message); pDialog.setIndeterminate(false);
	 * pDialog.setCancelable(true); pDialog.show(); }
	 * 
	 * private void progresscancel() { // TODO Auto-generated method stub
	 * pDialog.dismiss(); }
	 * 
	 * private void clearall() { // TODO Auto-generated method stub
	 * tv_emergency1.setText(""); tv_emergency2.setText("");
	 * tv_emergency3.setText(""); tv_emergency4.setText("");
	 * tv_emergency5.setText(""); im_shadow_emergency1.setImageBitmap(null);
	 * im_shadow_emergency2.setImageBitmap(null);
	 * im_shadow_emergency3.setImageBitmap(null);
	 * im_shadow_emergency4.setImageBitmap(null);
	 * im_shadow_emergency5.setImageBitmap(null); }
	 * 
	 * private void setsos() { // TODO Auto-generated method stub
	 * System.out.println("name" + appPrefes.getData("gname1")); noguard =
	 * false; if (!appPrefes.getData("gname1").equals("")) { noguard = true; }
	 * if (!appPrefes.getData("gname2").equals("")) { noguard = true; } if
	 * (!appPrefes.getData("gname2").equals("")) { noguard = true; } if
	 * (!appPrefes.getData("gname4").equals("")) { noguard = true; } if
	 * (!appPrefes.getData("gname5").equals("")) { noguard = true; } }
	 * 
	 * private void setimage() { // TODO Auto-generated method stub // noguard =
	 * true; if (appPrefes.getData("gname1").trim().length() > 10) { String n1 =
	 * appPrefes.getData("gname1").split(" ")[0]; tv_emergency1.setText(n1); }
	 * else { tv_emergency1.setText(appPrefes.getData("gname1")); } if
	 * (appPrefes.getData("gname2").trim().length() > 10) {
	 * tv_emergency2.setText(appPrefes.getData("gname2").split(" ")[0]); } else
	 * { tv_emergency2.setText(appPrefes.getData("gname2")); } if
	 * (appPrefes.getData("gname2").trim().length() > 10) {
	 * tv_emergency3.setText(appPrefes.getData("gname3").split(" ")[0]); } else
	 * { tv_emergency3.setText(appPrefes.getData("gname3")); } if
	 * (appPrefes.getData("gname4").trim().length() > 10) {
	 * tv_emergency4.setText(appPrefes.getData("gname4").split(" ")[0]); } else
	 * { tv_emergency4.setText(appPrefes.getData("gname4")); } if
	 * (appPrefes.getData("gname5").trim().length() > 10) {
	 * tv_emergency5.setText(appPrefes.getData("gname5").split(" ")[0]); } else
	 * { tv_emergency5.setText(appPrefes.getData("gname5")); }
	 * 
	 * if (!appPrefes.getData("gname1").equals("")) { noguard = true; } if
	 * (!appPrefes.getData("gname2").equals("")) { noguard = true; } if
	 * (!appPrefes.getData("gname2").equals("")) { noguard = true; } if
	 * (!appPrefes.getData("gname4").equals("")) { noguard = true; } if
	 * (!appPrefes.getData("gname5").equals("")) { noguard = true; }
	 * im_shadow_emergency1.setVisibility(View.GONE);
	 * im_shadow_emergency2.setVisibility(View.GONE);
	 * im_shadow_emergency3.setVisibility(View.GONE);
	 * im_shadow_emergency4.setVisibility(View.GONE);
	 * im_shadow_emergency5.setVisibility(View.GONE); if
	 * (!appPrefes.getData("ggc1").equals("")) { if
	 * (appPrefes.getData("ggc1").equals("cam")) {
	 * im_shadow_emergency1.setVisibility(View.VISIBLE); PictureOrentation
	 * orentation = new PictureOrentation();
	 * orentation.change(appPrefes.getData("gimage1"), im_shadow_emergency1,
	 * Home.this); } else if (appPrefes.getData("ggc1").equals("gallery")) {
	 * im_shadow_emergency1.setVisibility(View.VISIBLE); Bitmap bitmap =
	 * imageSmallerAction
	 * .decodeSampledBitmapFromGallery(appPrefes.getData("gimage1"), 100, 100);
	 * im_shadow_emergency1.setImageBitmap(bitmap); } } if
	 * (!appPrefes.getData("ggc2").equals("")) { if
	 * (appPrefes.getData("ggc2").equals("cam")) {
	 * im_shadow_emergency2.setVisibility(View.VISIBLE); PictureOrentation
	 * orentation = new PictureOrentation();
	 * orentation.change(appPrefes.getData("gimage2"), im_shadow_emergency2,
	 * Home.this); } else if (appPrefes.getData("ggc2").equals("gallery")) {
	 * im_shadow_emergency2.setVisibility(View.VISIBLE); Bitmap bitmap =
	 * imageSmallerAction
	 * .decodeSampledBitmapFromGallery(appPrefes.getData("gimage2"), 100, 100);
	 * im_shadow_emergency2.setImageBitmap(bitmap); } } if
	 * (!appPrefes.getData("ggc3").equals("")) { if
	 * (appPrefes.getData("ggc3").equals("cam")) {
	 * im_shadow_emergency3.setVisibility(View.VISIBLE); PictureOrentation
	 * orentation = new PictureOrentation();
	 * orentation.change(appPrefes.getData("gimage3"), im_shadow_emergency3,
	 * Home.this); } else if (appPrefes.getData("ggc3").equals("gallery")) {
	 * im_shadow_emergency3.setVisibility(View.VISIBLE); Bitmap bitmap =
	 * imageSmallerAction
	 * .decodeSampledBitmapFromGallery(appPrefes.getData("gimage3"), 100, 100);
	 * im_shadow_emergency3.setImageBitmap(bitmap); } } if
	 * (!appPrefes.getData("ggc4").equals("")) { if
	 * (appPrefes.getData("ggc4").equals("cam")) {
	 * im_shadow_emergency4.setVisibility(View.VISIBLE); PictureOrentation
	 * orentation = new PictureOrentation();
	 * orentation.change(appPrefes.getData("gimage4"), im_shadow_emergency4,
	 * Home.this); } else if (appPrefes.getData("ggc4").equals("gallery")) {
	 * im_shadow_emergency4.setVisibility(View.VISIBLE); Bitmap bitmap =
	 * imageSmallerAction
	 * .decodeSampledBitmapFromGallery(appPrefes.getData("gimage4"), 100, 100);
	 * im_shadow_emergency4.setImageBitmap(bitmap); } } if
	 * (!appPrefes.getData("ggc5").equals("")) { if
	 * (appPrefes.getData("ggc5").equals("cam")) {
	 * im_shadow_emergency5.setVisibility(View.VISIBLE); PictureOrentation
	 * orentation = new PictureOrentation();
	 * orentation.change(appPrefes.getData("gimage5"), im_shadow_emergency5,
	 * Home.this); } else if (appPrefes.getData("ggc5").equals("gallery")) {
	 * im_shadow_emergency5.setVisibility(View.VISIBLE); Bitmap bitmap =
	 * imageSmallerAction
	 * .decodeSampledBitmapFromGallery(appPrefes.getData("gimage5"), 100, 100);
	 * im_shadow_emergency5.setImageBitmap(bitmap); } }
	 * 
	 * im_shadow_emergency1.setVisibility(View.GONE);
	 * im_shadow_emergency2.setVisibility(View.GONE);
	 * im_shadow_emergency3.setVisibility(View.GONE);
	 * im_shadow_emergency4.setVisibility(View.GONE);
	 * im_shadow_emergency5.setVisibility(View.GONE); }
	 * 
	 * private OnClickListener sosOnClickListener = new OnClickListener() {
	 * public void onClick(View v) { if
	 * (appPrefes.getData("sos").equals("active")) { stop(); } else { if
	 * (noguard) { Intent intent = new Intent(Home.this, SosView.class);
	 * startActivity(intent); overridePendingTransition(R.anim.slide_in_up,
	 * R.anim.slide_out_up); } else { new
	 * AlertDialog.Builder(context).setTitle("Error message")
	 * .setMessage("Please select a minimum of one guardian to send an SOS"
	 * ).setCancelable(false) .setNegativeButton("Ok", new ondialog()).show(); }
	 * }
	 * 
	 * } };
	 * 
	 * class ondialog implements DialogInterface.OnClickListener {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) { //
	 * TODO Auto-generated method stub Intent intent = new Intent(Home.this,
	 * Settingsnew.class); startActivity(intent); }
	 * 
	 * }
	 * 
	 * public void Location(View view) { // TODO Auto-generated method stub
	 * Intent intent = new Intent(Home.this, MyLocfragment.class);
	 * 
	 * startActivity(intent); }
	 * 
	 * @Override public void onClick(View v) { // TODO Auto-generated method
	 * stub switch (v.getId()) { case R.id.bt_mr:
	 * bt_mr.setBackgroundResource(R.drawable.rectangleclick);
	 * bt_ms.setBackgroundResource(R.drawable.rightrectangleclick); break; case
	 * R.id.bt_ms: bt_ms.setBackgroundResource(R.drawable.rightrectangle);
	 * bt_mr.setBackgroundResource(R.drawable.rectangle); break; case
	 * R.id.im_settings: Intent intent = new Intent(Home.this,
	 * Settingsnew.class); startActivity(intent); break; case
	 * R.id.bt_cntersos_view: stop(); // intent = new Intent(Home.this,
	 * AllSosStopped.class); // startActivity(intent); break; case
	 * R.id.im_centreprofile:
	 * 
	 * break;
	 * 
	 * default: intent = new Intent(Home.this, AddEmergency.class); String
	 * detail = (new Gson()).toJson(detailGuardian); intent.putExtra("imtag",
	 * v.getTag().toString()); intent.putExtra("detail", detail);
	 * startActivity(intent); break; } }
	 * 
	 * private void setbitmap(final ImageView imageView, final int id) { // TODO
	 * Auto-generated method stub new AsyncTask<Void, Void, Bitmap>() {
	 * 
	 * @Override protected Bitmap doInBackground(Void... params) { // TODO
	 * Auto-generated method stub Bitmap bitmap =
	 * imageSmallerAction.decodeSampledBitmapFromResource(getResources(), id,
	 * width, height); return bitmap; }
	 * 
	 * @Override protected void onPostExecute(Bitmap bitmap) { // TODO
	 * Auto-generated method stub super.onPostExecute(bitmap);
	 * 
	 * imageView.setImageBitmap(bitmap); } }.execute(); }
	 * 
	 * private void setbitmapwithsize(final ImageView imageView, final int id,
	 * final int width, final int height) { // TODO Auto-generated method stub
	 * new AsyncTask<Void, Void, Bitmap>() {
	 * 
	 * @Override protected Bitmap doInBackground(Void... params) { // TODO
	 * Auto-generated method stub Bitmap bitmap =
	 * imageSmallerAction.decodeSampledBitmapFromResource(getResources(), id,
	 * width, height); return bitmap; }
	 * 
	 * @Override protected void onPostExecute(Bitmap bitmap) { // TODO
	 * Auto-generated method stub super.onPostExecute(bitmap);
	 * 
	 * imageView.setImageBitmap(bitmap); } }.execute(); }
	 * 
	 * private void stop() { // TODO Auto-generated method stub
	 * appPrefes.SaveData("sos", "deactive"); if
	 * (appPrefes.getData("sos").equals("active")) { ((ViewGroup)
	 * textView2.getParent()).setVisibility(View.VISIBLE);
	 * control_hint.setText(""); //
	 * im_centreprofile.setBackgroundResource(R.drawable.tap_to_cancel);
	 * bt_cntersos_view.setVisibility(View.VISIBLE);
	 * im_cntersos_view.setVisibility(View.GONE); //
	 * tv1.setText("spotNsave is now Online");
	 * tv1.setText("Emergency Alert: Activated"); //
	 * im_cntersos.setVisibility(View.GONE); } else { ((ViewGroup)
	 * textView2.getParent()).setVisibility(View.GONE);
	 * control_hint.setText("SOS"); //
	 * im_centreprofile.setBackgroundResource(R.drawable.bt_selector_like); //
	 * tv1.setText("spotNsave is now Offline");
	 * tv1.setText("Emergency Alert: De-Activated");
	 * bt_cntersos_view.setVisibility(View.GONE);
	 * im_cntersos_view.setVisibility(View.VISIBLE); //
	 * im_cntersos.setVisibility(View.VISIBLE); } String number1 =
	 * appPrefes.getData("gnumber1"); // getting String String number2 =
	 * appPrefes.getData("gnumber2"); // getting String String number3 =
	 * appPrefes.getData("gnumber3"); // getting String String number4 =
	 * appPrefes.getData("gnumber4"); // getting String String number5 =
	 * appPrefes.getData("gnumber5"); // getting String String fullname =
	 * appPrefes.getData("fullnameregistration"); // getting // String Intent
	 * intent = new Intent("com.epicelements.spotnsave.START_SERVICE4");
	 * Home.this.stopService(intent); if
	 * (SOSLocationProviderService.myNotificationManager5 != null) {
	 * SOSLocationProviderService
	 * .myNotificationManager5.cancel(SOSLocationProviderService
	 * .NOTIFICATION_IDD); } if (number1 != null && !number1.equals("") &&
	 * fullname == null) { sendSMS(number1,
	 * "I have canceled the SOS and is no longer in an emergency."); } if
	 * (number1 != null && !number1.equals("") && fullname != null) {
	 * sendSMS(number1, fullname +
	 * " has cancelled the SOS and is no longer in an emergency"); } if (number2
	 * != null && !number2.equals("") && fullname == null) { sendSMS(number2,
	 * "I have canceled the SOS and is no longer in an emergency."); } if
	 * (number2 != null && !number2.equals("") && fullname != null) {
	 * sendSMS(number2, fullname +
	 * " has cancelled the SOS and is no longer in an emergency"); } if (number3
	 * != null && !number3.equals("") && fullname == null) { sendSMS(number3,
	 * "I have canceled the SOS and is no longer in an emergency."); } if
	 * (number3 != null && !number3.equals("") && fullname != null) {
	 * sendSMS(number3, fullname +
	 * " has cancelled the SOS and is no longer in an emergency"); } if (number4
	 * != null && !number4.equals("") && fullname == null) { sendSMS(number4,
	 * "I have canceled the SOS and is no longer in an emergency."); } if
	 * (number4 != null && !number4.equals("") && fullname != null) {
	 * sendSMS(number4, fullname +
	 * " has cancelled the SOS and is no longer in an emergency"); } if (number5
	 * != null && !number5.equals("") && fullname == null) { sendSMS(number5,
	 * "I have canceled the SOS and is no longer in an emergency."); } if
	 * (number5 != null && !number5.equals("") && fullname != null) {
	 * sendSMS(number5, fullname +
	 * " has cancelled the SOS and is no longer in an emergency"); }
	 * 
	 * }
	 * 
	 * private void sendSMS(String phoneNumber, String message) { SmsManager sms
	 * = SmsManager.getDefault(); sms.sendTextMessage(phoneNumber, null,
	 * message, null, null); }
	 * 
	 * private void getwidth() { // TODO Auto-generated method stub
	 * WindowManager w = getWindowManager(); Display d = w.getDefaultDisplay();
	 * width = d.getWidth(); height = d.getHeight(); }
	 * 
	 * @Override protected void onDestroy() { // TODO Auto-generated method stub
	 * super.onDestroy(); try { if (mBatteryReceiver != null) {
	 * unregisterReceiver(mBatteryReceiver); } } catch (IllegalArgumentException
	 * ex) {
	 * 
	 * } catch (Exception e) { // TODO: handle exception } // Utils.BDA = null;
	 * // BDA = null; // this.startService(new
	 * Intent(this,ScanBGService.class)); }
	 * 
	 * public static void startBGService() {
	 * 
	 * if (!isMyServiceRunning()) { Log.e("service started first time", "");
	 * ScanBGService.intentService = new Intent(context, ScanBGService.class);
	 * context.startService(ScanBGService.intentService);
	 * 
	 * } else { Log.e(" service is already running ", ""); }
	 * 
	 * Handler handler = new Handler(); handler.postDelayed(new Runnable() {
	 * 
	 * @Override public void run() {
	 * 
	 * ScanBGService.serviceBDA = Utils.getBDA();
	 * 
	 * } }, 3000);
	 * 
	 * }
	 */
	static boolean isMyServiceRunning() {
		try {
			final ActivityManager manager;
			if (context != null) {
				manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
				for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
					if ("com.belcomm.scanservice.ScanBGService".equalsIgnoreCase(service.service.getClassName())) {
						Log.e("com.belcomm.scanservice.ScanBGService", "");
						return true;
					}
				}
			}
		} catch (NullPointerException ex) {
		} catch (Exception e) {
		}
		return false;
	}
	/*
	 * private final BroadcastReceiver mBatteryReceiver = new
	 * BroadcastReceiver() {
	 * 
	 * @Override public void onReceive(Context context, Intent intent) { String
	 * action = intent.getAction(); if
	 * (Utils.ACTION_BATTERY_LEVEL.equals(action)) { String val =
	 * String.valueOf(Utils.getBatteryValue());
	 * txtbatteryVal.setVisibility(View.VISIBLE); txtbatteryVal.setText(val +
	 * "%"); } } };
	 * 
	 * private void setguardian() { // TODO Auto-generated method stub if
	 * (detailGuardian != null) { lists lists = detailGuardian.lists.get(0); if
	 * (lists.id.size() == 1) { tv_emergency1.setText(lists.name.get(0)); } else
	 * if (lists.id.size() == 2) { tv_emergency1.setText(lists.name.get(0));
	 * tv_emergency2.setText(lists.name.get(1)); } else if (lists.id.size() ==
	 * 3) { tv_emergency1.setText(lists.name.get(0));
	 * tv_emergency2.setText(lists.name.get(1));
	 * tv_emergency3.setText(lists.name.get(2)); } else if (lists.id.size() ==
	 * 4) { tv_emergency1.setText(lists.name.get(0));
	 * tv_emergency2.setText(lists.name.get(1));
	 * tv_emergency3.setText(lists.name.get(2));
	 * tv_emergency4.setText(lists.name.get(3)); } else if (lists.id.size() ==
	 * 5) { tv_emergency1.setText(lists.name.get(0));
	 * tv_emergency2.setText(lists.name.get(1));
	 * tv_emergency3.setText(lists.name.get(2));
	 * tv_emergency4.setText(lists.name.get(3));
	 * tv_emergency5.setText(lists.name.get(4)); } } showGuardians();
	 * 
	 * }
	 * 
	 * class MyTimerTask extends TimerTask {
	 * 
	 * @Override public void run() { // int remtime =
	 * appPrefes.getIntData("remainsecond"); // remtime--; //
	 * System.out.println("kittunna remainsecond time" + remtime); //
	 * appPrefes.SaveIntData("remainsecond", remtime); timeInMilliseconds =
	 * SystemClock.uptimeMillis() - startTime; updatedTime = timeSwapBuff -
	 * timeInMilliseconds; final String hms = String.format( "%02d:%02d",
	 * TimeUnit.MILLISECONDS.toMinutes(updatedTime) -
	 * TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(updatedTime)),
	 * TimeUnit.MILLISECONDS.toSeconds(updatedTime) -
	 * TimeUnit.MINUTES.toSeconds(
	 * TimeUnit.MILLISECONDS.toMinutes(updatedTime))); long lastsec =
	 * TimeUnit.MILLISECONDS.toSeconds(updatedTime) -
	 * TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(updatedTime));
	 * System.out.println(lastsec + " hms " + hms); if (hms.equals("00:00") || 0
	 * > lastsec) { if (textView2 != null) { try { timeSwapBuff = 120 * 1000L;
	 * appPrefes.SaveIntData("remainsecond", 120); startTime =
	 * SystemClock.uptimeMillis(); } catch (Exception e) { // TODO: handle
	 * exception }
	 * 
	 * } } runOnUiThread(new Runnable() {
	 * 
	 * @Override public void run() { try { if (textView2 != null)
	 * textView2.setText(hms); } catch (Exception e) { // TODO: handle exception
	 * }
	 * 
	 * } }); } }
	 * 
	 * private void showGuardians() { final int maxGuardiansAllowed = 5; if
	 * (detailGuardian != null) { int guardianCount =
	 * detailGuardian.lists.size(); View item0 =
	 * getLayoutInflater().inflate(R.layout.add_new_guardian_button, null);
	 * 
	 * im_centreprofile.addItem(item0, new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { startActivity(new
	 * Intent(Home.this, AddEmergency.class)); } });
	 * 
	 * if (maxGuardiansAllowed > guardianCount) { for (int i = 0; i <
	 * guardianCount; i++) { Button item = new Button(this);
	 * item.setBackgroundResource(R.drawable.drawable_guardian_icon);
	 * item.setText(detailGuardian.lists.get(i).name.get(i));
	 * item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
	 * item.setSingleLine(true);
	 * 
	 * im_centreprofile.addItem(item, new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { Intent intent = new
	 * Intent(Home.this, Settingsnewnew.class); startActivity(intent); } }); }
	 * 
	 * for (int i = guardianCount; i < maxGuardiansAllowed; i++) {
	 * 
	 * View item = getLayoutInflater().inflate(R.layout.add_new_guardian_button,
	 * null);
	 * 
	 * im_centreprofile.addItem(item, new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { startActivity(new
	 * Intent(Home.this, AddEmergency.class)); } }); } } } else
	 * 
	 * for (int i = 0; i <= maxGuardiansAllowed; i++) {
	 * 
	 * View item = getLayoutInflater().inflate(R.layout.add_new_guardian_button,
	 * null);
	 * 
	 * im_centreprofile.addItem(item, new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { Toast.makeText(Home.this,
	 * "position:", Toast.LENGTH_SHORT).show(); } }); } }
	 * 
	 * @Override protected void onStop() { super.onStop(); }
	 */
}
