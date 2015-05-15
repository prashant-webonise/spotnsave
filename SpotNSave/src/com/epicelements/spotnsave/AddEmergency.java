package com.epicelements.spotnsave;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.blecomm.utils.MySingleton;
import com.epicelements.spotnsave.DetailGuardian.lists;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.luttu.AppPrefes;
import com.luttu.PictureOrentation;
import com.luttu.Utils;
import com.util.GlobalFunctions;
import com.util.GlobalFunctions.HttpResponseHandler;
import com.util.ImageSmallerAction;

public class AddEmergency extends FragmentActivity implements OnClickListener {
	// ImageView im_cam;
	// ImageView im_reg;
	ImageSmallerAction imageSmallerAction = new ImageSmallerAction();
	private Button bt_cancel;
	private Button bt_add_contact;
	private Button bt_activaet;
	final Activity activity = this;

	private static final int CONTACT_PICKER_RESULT1 = 1001;

	private Uri fileUri;
	private Utils utilObj = new Utils();
	ImageSmallerAction imageSmalerObj = new ImageSmallerAction();
	int RESULT_LOAD_GALLERY = 99;
	int RESULT_LOAD_CAMERA = 100;
	// private RoundedImageView imShadow;
	private static final int MEDIA_TYPE_IMAGE = 1;
	AppPrefes appPrefes;
	String gc = "noimage";
	String profilepath;
	private EditText ed_first_name;
	private EditText ed_phone;
	boolean update;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);
		setContentView(R.layout.addemergency);
		getActionBar().setTitle("  " + getString(R.string.add_guardian));
		getActionBar().setIcon(R.drawable.ic_menu_back);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF02A9D8")));
		getActionBar().setHomeButtonEnabled(true);
		appPrefes = new AppPrefes(this, "sns");
		findid();
		checksetimage();
		if (!ed_first_name.getText().toString().equals("")) {
			update = true;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
	    switch (menuItem.getItemId()) {
	        case android.R.id.home:
	        	onBackPressed();
	            return true;
	    }
	    return (super.onOptionsItemSelected(menuItem));
	}
	private void findid() {
		// TODO Auto-generated method stub
		// im_cam = (ImageView) findViewById(R.id.im_cam);
		// im_reg = (ImageView) findViewById(R.id.im_reg);
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_activaet = (Button) findViewById(R.id.bt_activaet);
		bt_add_contact = (Button) findViewById(R.id.bt_add_contact);
		ed_first_name = (EditText) findViewById(R.id.ed_first_name);
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		bt_add_contact.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
		bt_activaet.setOnClickListener(contacts1listener);

		// imShadow = (RoundedImageView) findViewById(R.id.im_shadow);
		// imShadow.setOnClickListener(this);
		// imShadow.setBorderWidth(0);
		// imShadow.setOval(false);
	}

	private OnClickListener contacts1listener = new OnClickListener() {

		// http://stackoverflow.com/questions/11218845/how-to-get-contacts-phone-number-in-android
		public void onClick(View v) {
			Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
			startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT1);
		}
	};
	private ProgressDialog pDialog;
	private DetailGuardian detailGuardian;
	private String guardianID;
	private String image;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_cancel:
			finish();
			break;
		case R.id.bt_add_contact:
			String number1 = ed_phone.getText().toString();
			String name = ed_first_name.getText().toString();
			if (name.equals("")) {
				Toast.makeText(AddEmergency.this, "Please enter name ", Toast.LENGTH_LONG).show();
				return;
			}
			if (number1.equals("")) {
				Toast.makeText(AddEmergency.this, "Please enter phone number", Toast.LENGTH_LONG).show();
				return;
			}
			String tag = getIntent().getExtras().getString("imtag");
			appPrefes.SaveData("gname" + tag, ed_first_name.getText().toString());
			appPrefes.SaveData("gnumber" + tag, ed_phone.getText().toString());
			appPrefes.SaveData("gimagesdcard" + tag, profilepath);
			appPrefes.SaveData("ggc" + tag, gc);
			// sendsms();
			String fullname = appPrefes.getData("fullnameregistration"); // getting

			if (number1 != null && !number1.equals("") && fullname == null) {
				sendSMS(number1,
						"I am using the spotNsave app and have chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
			}
			if (number1 != null && !number1.equals("") && fullname != null) {
				sendSMS(number1,
						fullname
								+ " is using the spotNsave app and has chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
			}
			System.out.println("imtag" + tag);
			post();
			break;
		case R.id.im_shadow:
			Upload_Image();
			break;

		default:
			// Intent intent = new Intent(Registration_New.this, Home.class);
			// startActivity(intent);
			break;
		}
	}

	private void post() {
		// TODO Auto-generated method stub
		String phone = ed_phone.getText().toString();
		String name = ed_first_name.getText().toString();
		RequestParams params = new RequestParams();
		AsyncHttpClient client = new AsyncHttpClient();
		params.put("userID", appPrefes.getData("userID"));
		params.put("name", name);
		params.put("phno", phone);
		params.put("permisssion", "true");
		String php = "guardian.php";
		if (update) {
			progressshow("Updating  Guardian...");
			params.put("guardianID", guardianID);
			System.out.println("image" + image);
			params.put("image", image);
			php = "guardian_update.php";
		} else
			progressshow("Creating Guardian...");
		try {
			if (profilepath != null)
				params.put("image", new File(profilepath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GlobalFunctions.postApiCall(this, Registration_New.Url + php, params, client, new HttpResponseHandler() {

			@Override
			public void handle(String response, boolean success) {
				// TODO Auto-generated method stub
				try {
					if (success && !response.equals("null")) {
						DetailFalse session = (new Gson()).fromJson(response.toString(), DetailFalse.class);
						if (session.Status.equals("Failed")) {
						} else {
							appPrefes.SaveData("sessionsession", response);
							setguardian(session.lists.get(0));
						}
					}
					appPrefes.SaveData("gotogetdetail", "true");
					finish();
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(AddEmergency.this, "Server down", Toast.LENGTH_LONG).show();
				}
				progresscancel();
			}
		});
	}

	private void progressshow(String string) {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(AddEmergency.this);
		pDialog.setMessage("Please wait while we save your guardian details");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}

	private void progresscancel() {
		// TODO Auto-generated method stub
		pDialog.dismiss();
	}

	private void sendsms() {
		// TODO Auto-generated method stub

		String number1 = appPrefes.getData("gnumber1"); // getting String
		String number2 = appPrefes.getData("gnumber2"); // getting String
		String number3 = appPrefes.getData("gnumber3"); // getting String
		String number4 = appPrefes.getData("gnumber4"); // getting String
		String number5 = appPrefes.getData("gnumber5"); // getting String
		String fullname = appPrefes.getData("fullnameregistration"); // getting
																		// String

		if (number1 != null && !number1.equals("") && fullname == null) {
			sendSMS(number1,
					"I am using the spotNsave app and have chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
		}
		if (number1 != null && !number1.equals("") && fullname != null) {
			sendSMS(number1,
					fullname
							+ " is using the spotNsave app and has chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
		}
		if (number2 != null && !number2.equals("") && fullname == null) {
			sendSMS(number2,
					"I am using the spotNsave app and have chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
		}
		if (number2 != null && !number2.equals("") && fullname != null) {
			sendSMS(number2,
					fullname
							+ " is using the spotNsave app and has chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
		}
		if (number3 != null && !number3.equals("") && fullname == null) {
			sendSMS(number3,
					"I am using the spotNsave app and have chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
		}
		if (number3 != null && !number3.equals("") && fullname != null) {
			sendSMS(number3,
					fullname
							+ " is using the spotNsave app and has chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
		}
		if (number4 != null && !number4.equals("") && fullname == null) {
			sendSMS(number4,
					"I am using the spotNsave app and have chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
		}
		if (number4 != null && !number4.equals("") && fullname != null) {
			sendSMS(number4,
					fullname
							+ " is using the spotNsave app and has chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
		}
		if (number5 != null && !number5.equals("") && fullname == null) {
			sendSMS(number5,
					"I am using the spotNsave app and have chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
		}
		if (number5 != null && !number5.equals("") && fullname != null) {
			sendSMS(number5,
					fullname
							+ " is using the spotNsave app and has chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
		}
	}

	private void sendSMS(String phoneNumber, String message) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}

	private void Upload_Image() {
		// TODO Auto-generated method stub
		final String[] items = { "Camera", "Gallery" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
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
					Intent i = new Intent(Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, RESULT_LOAD_GALLERY);
				}
			}
		});
		builder.create();
		builder.show();
	}

	private void checksetimage() {
		// TODO Auto-generated method stub
		try {
			String tag = getIntent().getExtras().getString("imtag");
			String detail = getIntent().getExtras().getString("detail");
			if (detail == null) {
				return;
			}
			if (!detail.equals("")) {
				detailGuardian = (new Gson()).fromJson(detail, DetailGuardian.class);
				if (detailGuardian == null)
					return;
				if (detailGuardian.lists == null)
					return;
				int itag = Integer.parseInt(tag) - 1;
				lists lists = detailGuardian.lists.get(0);
				if (lists.id.size() - 1 < itag) {

				} else {
					ed_first_name.setText(lists.name.get(itag));
					ed_phone.setText(lists.phoneno.get(itag));
					// setimage(lists.image.get(itag), imShadow);
					guardianID = lists.id.get(itag);
					image = lists.image.get(itag);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		if (reqCode == RESULT_LOAD_GALLERY && resultCode == RESULT_OK && data != null) {
			gc = "gallery";
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			profilepath = picturePath;
			cursor.close();
			Bitmap bitmap = imageSmalerObj.decodeSampledBitmapFromGallery(picturePath, 100, 100);
			// imShadow.setImageBitmap(bitmap);
		}
		if (reqCode == RESULT_LOAD_CAMERA && resultCode == RESULT_OK) {
			gc = "cam";
			profilepath = fileUri.getPath();
			PictureOrentation orentation = new PictureOrentation();
			// orentation.change(fileUri.getPath(), imShadow,
			// AddEmergency.this);
		}
		switch (reqCode) {
		case (CONTACT_PICKER_RESULT1):
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c = managedQuery(contactData, null, null, null, null);
				if (c.moveToFirst()) {
					String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

					String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

					if (hasPhone.equalsIgnoreCase("1")) {
						Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
								null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
						phones.moveToFirst();
						String cNumber = phones.getString(phones
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						// Toast.makeText(getApplicationContext(), cNumber,
						// Toast.LENGTH_SHORT).show();

						String nameContact = c.getString(c
								.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

						EditText nameEntry = (EditText) findViewById(R.id.ed_first_name);
						nameEntry.setText(nameContact);

						EditText numberEntry = (EditText) findViewById(R.id.ed_phone);
						numberEntry.setText(cNumber);
						if (cNumber.length() == 0) {
							Toast.makeText(this, "No Number found for contact.", Toast.LENGTH_LONG).show();
							break;
						}
						SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0
																											// -
																											// for
																											// private
																											// mode
						Editor editor = pref.edit();
						editor.putString("gname1", nameContact); // Storing
																	// string
						editor.putString("gnumber1", cNumber); // Storing string
						editor.commit(); // commit changes

					}
				}

			}

			break;

		}
	}

	private void setimage(String imagename, final ImageView imageView) {
		// TODO Auto-generated method stub
		float scale = getResources().getDisplayMetrics().density;
		final int imageSizeWidthPX = (int) ((80 * scale) + 0.5);
		final int imageSizeHeightPX = (int) ((80 * scale) + 0.5);
		ImageRequest request = new ImageRequest("http://www.spotnsave.in/App/Modules/Home/Tpl/TwilioApi/upload/"
				+ imagename.replace(" ", "%20"), new Response.Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap response) {
				// TODO Auto-generated method stub

				imageView.setImageBitmap(ImageSmallerAction.getCircleBitmap(response, imageSizeWidthPX,
						imageSizeHeightPX));
			}
		}, 400, 400, null, new Response.ErrorListener() {
			public void onErrorResponse(VolleyError error) {
				// imgDisplay.setImageResource(R.drawable.ic_launcher);
			}
		});
		MySingleton.getInstance(this).addToRequestQueue(request);
	}

	private void setguardian(com.epicelements.spotnsave.DetailFalse.lists lists) {
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
}
