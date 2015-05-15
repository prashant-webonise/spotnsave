package com.epicelements.spotnsave;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.epicelements.spotnsave.DetailFalse.lists;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.luttu.AppPrefes;
import com.luttu.PictureOrentation;
import com.luttu.Utils;
import com.makeramen.RoundedImageView;
import com.util.GlobalFunctions;
import com.util.GlobalFunctions.HttpResponseHandler;
import com.util.ImageSmallerAction;

public class Registration_New extends FragmentActivity implements
		OnClickListener {
	ImageView im_cam;
	ImageView im_reg;
	ImageSmallerAction imageSmallerAction = new ImageSmallerAction();
	private int width;
	private int height;
	private ImageView bt_mr;
	private ImageView bt_ms;
	private ImageView bt_activaet;
	private Uri fileUri;
	private Utils utilObj = new Utils();
	ImageSmallerAction imageSmalerObj = new ImageSmallerAction();
	int RESULT_LOAD_GALLERY = 99;
	int RESULT_LOAD_CAMERA = 100;
	@InjectView(R.id.ed_country)
	AutoCompleteTextView ed_country;
	private RoundedImageView imShadow;
	private EditText ed_phone;
	private static final int MEDIA_TYPE_IMAGE = 1;
	AppPrefes appPrefes;
	String profilepath;
	String gc = "noimage";
	Dialog levelDialog;
	// Progress Dialog
	private ProgressDialog pDialog;
	private String genderr = "male";

	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	private EditText ed_name;
	// testing from a real server:
	private static final String LOGIN_URL = "http://www.konnectc.com/spotnsave/create_user.php";

	// ids
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	HashMap<String, String> hashcountries = new HashMap<String, String>();
	static final String Url = "http://www.spotnsave.in/App/Modules/Home/Tpl/TwilioApi/";

	String Cid = "";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);
		setContentView(R.layout.registernewscroll);
		ButterKnife.inject(this);
		bool();
		appPrefes = new AppPrefes(this, "sns");
		findid();
		getwidth();
		popupscountry();
	}

	private void bool() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				"MyPref", 0); // 0 - for private mode
		Editor editor = pref.edit();
		editor.putBoolean("loggedIn", true); // Storing boolean - true/false
		editor.commit(); // commit changes
	}

	private void findid() {
		// TODO Auto-generated method stub
		im_cam = (ImageView) findViewById(R.id.im_cam);
		im_reg = (ImageView) findViewById(R.id.im_reg);
		bt_mr = (ImageView) findViewById(R.id.bt_mr);
		bt_ms = (ImageView) findViewById(R.id.bt_ms);
		bt_activaet = (ImageView) findViewById(R.id.bt_activaet);
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		ed_name = (EditText) findViewById(R.id.ed_name);
		im_cam.setOnClickListener(this);
		bt_mr.setOnClickListener(this);
		bt_ms.setOnClickListener(this);
		bt_activaet.setOnClickListener(mRegisterlistener);

		imShadow = (RoundedImageView) findViewById(R.id.im_shadow);
		imShadow.setBorderWidth(0);
		imShadow.setOval(false);

		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String countryCode = tMgr.getSimCountryIso();
		ed_country = (AutoCompleteTextView) findViewById(R.id.ed_country);
		ed_country.setText(GetCountryZipCode(countryCode));
		// String locale =
		// getResources().getConfiguration().locale.getCountry();
		ed_phone.setText("+" + GetCountryZipCode());

		ed_country.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				hidekey();
				System.out.println("editeraction");
				ed_country.dismissDropDown();
				Cid = hashcountries.get(ed_country.getText().toString());
				ed_phone.setText("+86");
			}
		});
		final int psize = ed_phone.getText().toString().length();
		ed_phone.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				int size = ed_phone.getText().toString().length();
				ed_phone.setSelection(size);
			}
		});
		// ed_phone.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		//
		// int size = ed_phone.getText().toString().length();
		// ed_phone.setSelection(size);
		// return false;
		// }
		// });
		ed_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int size = ed_phone.getText().toString().length();
				ed_phone.setSelection(size);
			}
		});
		ed_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (ed_phone.getText().toString().length() <= psize) {
					ed_phone.setText("+" + GetCountryZipCodes(Cid) + " ");
					ed_phone.setSelection(psize);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	protected void hidekey() {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}

	private ArrayList<String> getcountry() {
		// TODO Auto-generated method stub
		Locale[] locale = Locale.getAvailableLocales();
		ArrayList<String> countries = new ArrayList<String>();
		String country, countrycode;
		for (Locale loc : locale) {
			countrycode = loc.getCountry();
			country = loc.getDisplayCountry();
			if (country.length() > 0 && !countries.contains(country)) {
				countries.add(country);
				hashcountries.put(country, countrycode);
			}
		}
		Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
		return countries;
	}

	void popupscountry() {
		// TODO Auto-generated method stub
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, getcountry());
		ed_country.setThreshold(1);
		ed_country.setAdapter(adapter);
	}

	String GetCountryZipCode() {

		String CountryID = "";
		String CountryZipCode = "";

		TelephonyManager manager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		// getNetworkCountryIso
		CountryID = manager.getSimCountryIso().toUpperCase();
		// CountryID="CN";
		String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
		for (int i = 0; i < rl.length; i++) {
			String[] g = rl[i].split(",");
			if (g[1].trim().equals(CountryID.trim())) {
				CountryZipCode = g[0];
			}
		}
		Cid = CountryID;
		return CountryZipCode;
	}

	String GetCountryZipCodes(String CountryID) {
		String CountryZipCode = "";
		String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
		for (int i = 0; i < rl.length; i++) {
			String[] g = rl[i].split(",");
			if (g[1].trim().equals(CountryID.trim())) {
				CountryZipCode = g[0];
			}
		}
		return CountryZipCode;
	}

	private String GetCountryZipCode(String ssid) {
		Locale loc = new Locale("", ssid);

		return loc.getDisplayCountry().trim();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_mr:
			genderr = "male";
			bt_mr.setImageResource(R.drawable.male_pushed);
			bt_ms.setImageResource(R.drawable.female);
			break;
		case R.id.bt_ms:
			genderr = "female";
			bt_ms.setImageResource(R.drawable.female_pushed);
			bt_mr.setImageResource(R.drawable.male);
			break;
		case R.id.im_cam:
			Upload_Image();
			break;

		default:
			break;
		}
	}

	private void setbitmap(final ImageView imageView, final int id) {
		// TODO Auto-generated method stub
		new AsyncTask<Void, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Bitmap bitmap = imageSmallerAction
						.decodeSampledBitmapFromResource(getResources(), id,
								width, height);
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

	private void setbitmapwithsize(final ImageView imageView, final int id,
			final int width, final int height) {
		// TODO Auto-generated method stub
		new AsyncTask<Void, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Bitmap bitmap = imageSmallerAction
						.decodeSampledBitmapFromResource(getResources(), id,
								width, height);
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

	private void getwidth() {
		// TODO Auto-generated method stub
		Point size = new Point();
		width = size.x;
		height = size.y;

		WindowManager w = getWindowManager();
		Display d = w.getDefaultDisplay();
		width = d.getWidth();
		height = d.getHeight();
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
					picturePath, 150, 150);

			float scale = getResources().getDisplayMetrics().density;
			int imageSizeWidthPX = (int) ((120 * scale) + 0.5);
			int imageSizeHeightPX = (int) ((120 * scale) + 0.5);
			try {
				imShadow.setImageBitmap(ImageSmallerAction.getCircleBitmap(
						bitmap, imageSizeWidthPX, imageSizeHeightPX));
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		if (requestCode == RESULT_LOAD_CAMERA && resultCode == RESULT_OK) {
			gc = "cam";
			profilepath = fileUri.getPath();
			PictureOrentation orentation = new PictureOrentation();
			orentation.change(fileUri.getPath(), imShadow,
					Registration_New.this);
		}
	}

	private OnClickListener mRegisterlistener = new OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (getConnectivityStatusString(Registration_New.this).equals(
					"Not connected to Internet")) {
				Toast.makeText(Registration_New.this,
						"Not connected to Internet", Toast.LENGTH_LONG).show();
			} else {
				checknull();

				// new CreateUser().execute();
			}

		}
	};

	private void checknull() {
		// TODO Auto-generated method stub
		String firstname = ed_name.getText().toString();
		String country = ed_country.getText().toString();
		String phone = ed_phone.getText().toString();
		appPrefes.SaveData("country", country);
		if (firstname.equals("")) {
			Toast.makeText(Registration_New.this, "Enter name",
					Toast.LENGTH_LONG).show();
		} else if (phone.equals("")) {
			Toast.makeText(Registration_New.this, "Enter country",
					Toast.LENGTH_LONG).show();
		} else if (country.equals("")) {
			Toast.makeText(Registration_New.this, "Enter number",
					Toast.LENGTH_LONG).show();
		} else {
			post();
		}
	}

	private void post() {
		// TODO Auto-generated method stub
		final String firstname = ed_name.getText().toString();
		String country = ed_country.getText().toString();
		String phone = ed_phone.getText().toString();
		RequestParams params = new RequestParams();
		AsyncHttpClient client = new AsyncHttpClient();
		params.put("name", firstname);
		params.put("sex", "Mr");
		params.put("country", country);
		params.put("phno", phone);
		try {
			if (profilepath != null)
				params.put("image", new File(profilepath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		progressshow();
		GlobalFunctions.postApiCall(this, Url + "register.php?", params,
				client, new HttpResponseHandler() {

					@Override
					public void handle(String response, boolean success) {
						// TODO Auto-generated method stub
						progresscancel();
						if (success && !response.contains("null")) {
							try {
								DetailFalse session = (new Gson()).fromJson(
										response.toString(), DetailFalse.class);
								appPrefes.SaveData("fullnameregistration",
										firstname);
								appPrefes.SaveData("gimagemain", profilepath);
								appPrefes.SaveData("ggcmain", gc);
								if (firstname.equals("")) {
									appPrefes.SaveData("fullnameregistration",
											null);
								}
								appPrefes.SaveData("sessionsession", response);
								appPrefes.SaveData("userID", session.Details);
								Intent i = new Intent(Registration_New.this,
										AddGuard.class);
								finish();
								startActivity(i);
								if (session.Status.equals("register")) {
									Toast.makeText(
											Registration_New.this,
											"Congratulations your account has been created",
											Toast.LENGTH_LONG).show();
								} else {
									setguardian(session.lists.get(0));
									Toast.makeText(Registration_New.this,
											"Login Success", Toast.LENGTH_LONG)
											.show();
								}
							} catch (Exception e) {
								// TODO: handle exception
								Toast.makeText(Registration_New.this,
										"Server down", Toast.LENGTH_LONG)
										.show();
							}
						} else {
							Toast.makeText(Registration_New.this,
									"Server error", Toast.LENGTH_LONG).show();
						}
					}
				});
	}

	private void setguardian(lists lists) {
		// TODO Auto-generated method stub
		if (lists.id.size() == 1) {
			appPrefes.SaveData("gname1", lists.name.get(0));
			appPrefes.SaveData("gnumber1", lists.phoneno.get(0));
			appPrefes.SaveData("gimage1", lists.image.get(0));
			appPrefes.SaveData("gpermisson1", lists.permisssion.get(0));
			appPrefes.SaveData("gid1", lists.id.get(0));
		} else if (lists.id.size() == 2) {
			appPrefes.SaveData("gname1", lists.name.get(0));
			appPrefes.SaveData("gnumber1", lists.phoneno.get(0));
			appPrefes.SaveData("gimage1", lists.image.get(0));
			appPrefes.SaveData("gpermisson1", lists.permisssion.get(0));
			appPrefes.SaveData("gid1", lists.id.get(0));
			appPrefes.SaveData("gname2", lists.name.get(1));
			appPrefes.SaveData("gnumber2", lists.phoneno.get(1));
			appPrefes.SaveData("gimage2", lists.image.get(1));
			appPrefes.SaveData("gpermisson2", lists.permisssion.get(1));
			appPrefes.SaveData("gid2", lists.id.get(1));
		} else if (lists.id.size() == 3) {
			appPrefes.SaveData("gname1", lists.name.get(0));
			appPrefes.SaveData("gnumber1", lists.phoneno.get(0));
			appPrefes.SaveData("gimage1", lists.image.get(0));
			appPrefes.SaveData("gpermisson1", lists.permisssion.get(0));
			appPrefes.SaveData("gid1", lists.id.get(0));
			appPrefes.SaveData("gname2", lists.name.get(1));
			appPrefes.SaveData("gnumber2", lists.phoneno.get(1));
			appPrefes.SaveData("gimage2", lists.image.get(1));
			appPrefes.SaveData("gpermisson2", lists.permisssion.get(1));
			appPrefes.SaveData("gid2", lists.id.get(1));
			appPrefes.SaveData("gname3", lists.name.get(2));
			appPrefes.SaveData("gnumber3", lists.phoneno.get(2));
			appPrefes.SaveData("gimage3", lists.image.get(2));
			appPrefes.SaveData("gpermisson3", lists.permisssion.get(2));
			appPrefes.SaveData("gid3", lists.id.get(2));
		} else if (lists.id.size() == 4) {
			appPrefes.SaveData("gname1", lists.name.get(0));
			appPrefes.SaveData("gnumber1", lists.phoneno.get(0));
			appPrefes.SaveData("gimage1", lists.image.get(0));
			appPrefes.SaveData("gid1", lists.id.get(0));
			appPrefes.SaveData("gpermisson1", lists.permisssion.get(0));
			appPrefes.SaveData("gname2", lists.name.get(1));
			appPrefes.SaveData("gnumber2", lists.phoneno.get(1));
			appPrefes.SaveData("gimage2", lists.image.get(1));
			appPrefes.SaveData("gpermisson2", lists.permisssion.get(1));
			appPrefes.SaveData("gid2", lists.id.get(1));
			appPrefes.SaveData("gname3", lists.name.get(2));
			appPrefes.SaveData("gnumber3", lists.phoneno.get(2));
			appPrefes.SaveData("gimage3", lists.image.get(2));
			appPrefes.SaveData("gpermisson3", lists.permisssion.get(2));
			appPrefes.SaveData("gid3", lists.id.get(2));
			appPrefes.SaveData("gname4", lists.name.get(3));
			appPrefes.SaveData("gnumber4", lists.phoneno.get(3));
			appPrefes.SaveData("gimage4", lists.image.get(3));
			appPrefes.SaveData("gpermisson4", lists.permisssion.get(3));
			appPrefes.SaveData("gid4", lists.id.get(3));
		} else if (lists.id.size() == 5) {
			appPrefes.SaveData("gname1", lists.name.get(0));
			appPrefes.SaveData("gnumber1", lists.phoneno.get(0));
			appPrefes.SaveData("gimage1", lists.image.get(0));
			appPrefes.SaveData("gpermisson1", lists.permisssion.get(0));
			appPrefes.SaveData("gid1", lists.id.get(0));
			appPrefes.SaveData("gname2", lists.name.get(1));
			appPrefes.SaveData("gnumber2", lists.phoneno.get(1));
			appPrefes.SaveData("gimage2", lists.image.get(1));
			appPrefes.SaveData("gpermisson2", lists.permisssion.get(1));
			appPrefes.SaveData("gid2", lists.id.get(1));
			appPrefes.SaveData("gname3", lists.name.get(2));
			appPrefes.SaveData("gnumber3", lists.phoneno.get(2));
			appPrefes.SaveData("gimage3", lists.image.get(2));
			appPrefes.SaveData("gpermisson3", lists.permisssion.get(2));
			appPrefes.SaveData("gid3", lists.id.get(2));
			appPrefes.SaveData("gname4", lists.name.get(3));
			appPrefes.SaveData("gnumber4", lists.phoneno.get(3));
			appPrefes.SaveData("gimage4", lists.image.get(3));
			appPrefes.SaveData("gpermisson4", lists.permisssion.get(3));
			appPrefes.SaveData("gid4", lists.id.get(3));
			appPrefes.SaveData("gname5", lists.name.get(4));
			appPrefes.SaveData("gnumber5", lists.phoneno.get(4));
			appPrefes.SaveData("gimage5", lists.image.get(4));
			appPrefes.SaveData("gpermisson5", lists.permisssion.get(4));
			appPrefes.SaveData("gid5", lists.id.get(4));
		}
	}

	public String getConnectivityStatusString(Context context) {
		int conn = getConnectivityStatus(context);
		String status = null;
		if (conn == TYPE_WIFI) {
			status = "Wifi enabled";
		} else if (conn == TYPE_MOBILE) {
			status = "Mobile data enabled";
		} else if (conn == TYPE_NOT_CONNECTED) {
			status = "Not connected to Internet";
		}
		return status;
	}

	public int getConnectivityStatus(Context context) {
		if (context == null) {
			return 0;
		}
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		}
		return TYPE_NOT_CONNECTED;
	}

	private void progressshow() {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(Registration_New.this);
		pDialog.setMessage("Please wait while we save your details");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}

	private void progresscancel() {
		// TODO Auto-generated method stub
		pDialog.dismiss();
	}

	class CreateUser extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Registration_New.this);
			pDialog.setMessage("Creating User...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag
			int success;
			String username = ed_name.getText().toString().toLowerCase();
			String password = "123";
			String firstname = ed_name.getText().toString();
			String lastname = ed_name.getText().toString();
			String dateofbirth = "1993-10-03";
			String gender = genderr;
			String phone = ed_phone.getText().toString().trim();
			String country = ed_country.getText().toString();
			// SimpleDateFormat sdf = new
			// SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			// String currentdate = sdf.format(new Date());

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("email", username));
				params.add(new BasicNameValuePair("password", password));
				params.add(new BasicNameValuePair("firstname", firstname));
				params.add(new BasicNameValuePair("lastname", lastname));
				params.add(new BasicNameValuePair("dateofbirth", dateofbirth));
				params.add(new BasicNameValuePair("gender", gender));
				params.add(new BasicNameValuePair("phone", phone));
				// params.add(new BasicNameValuePair("currentdate",
				// currentdate));
				params.add(new BasicNameValuePair("country", country));

				Log.d("request!", "starting");

				// Posting user data to script
				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
						params);

				// full json response
				Log.d("Login attempt", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("User Created!", json.toString());
					Intent i = new Intent(Registration_New.this, AddGuard.class);
					finish();
					startActivity(i);
					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {
				System.out.println(file_url);
				Toast.makeText(Registration_New.this, file_url,
						Toast.LENGTH_LONG).show();
			}

		}

	}
}
