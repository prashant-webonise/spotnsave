package com.epicelements.spotnsave;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.makeramen.RoundedImageView;
import com.util.BitmapWorkerTask;
import com.util.GlobalFunctions;
import com.util.GlobalFunctions.HttpResponseHandler;
import com.util.ImageSmallerAction;

public class Settingsnewnew extends FragmentActivity implements OnClickListener {
	ImageView im_reg;
	ImageSmallerAction imageSmallerAction = new ImageSmallerAction();
	private int width;
	private int height;
	private Button bt_mr;
	private Button bt_ms;
	private RoundedImageView im_shadow_emergency1;
	private RoundedImageView im_shadow_emergency2;
	private RoundedImageView im_shadow_emergency3;
	private RoundedImageView im_shadow_emergency4;
	private RoundedImageView im_shadow_emergency5;
	private AppPrefes appPrefes;
	private TextView tv_emergency2;
	private TextView tv_emergency4;
	private TextView tv_emergency5;
	private TextView tv_emergency3;
	private TextView tv_emergency1;
	private TextView tv_setttingsprofieeditno1;
	private TextView tv_setttingsprofieeditno2;
	private TextView tv_setttingsprofieeditno3;
	private TextView tv_setttingsprofieeditno4;
	private TextView tv_setttingsprofieeditno5;
	private ImageView im_setttingscross2;
	private ImageView im_setttingscross1;
	private ImageView im_setttingscross3;
	private ImageView im_setttingscross4;
	private ImageView im_setttingscross5;
	private ImageView im_setttingsprofieedit1;
	private ImageView im_setttingsprofieedit2;
	private ImageView im_setttingsprofieedit3;
	private ImageView im_setttingsprofieedit4;
	private ImageView im_setttingsprofieedit5;
	private ImageView im_next;

	private Uri fileUri;
	private Utils utilObj = new Utils();
	ImageSmallerAction imageSmalerObj = new ImageSmallerAction();
	int RESULT_LOAD_GALLERY = 99;
	int RESULT_LOAD_CAMERA = 100;
	private static final int MEDIA_TYPE_IMAGE = 1;
	String profilepath;
	String gc = "noimage";
	private ProgressDialog pDialog;
	ImageView im_setttingscrosstick1;
	ImageView im_setttingscrosstick2;
	ImageView im_setttingscrosstick3;
	ImageView im_setttingscrosstick4;
	ImageView im_setttingscrosstick5;
	protected DetailFalse detailGuardian;
	private ImageView im_reg_button;
	private ImageView im_add_button;
	private TextView tv_addguard;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);
		setContentView(R.layout.settings__newnew);
		appPrefes = new AppPrefes(this, "sns");
		findid();
		getwidth();
		setpreferenceguadian();
	}

	private void findid() {
		// TODO Auto-generated method stub
		im_reg = (ImageView) findViewById(R.id.im_reg);
		im_reg_button = (ImageView) findViewById(R.id.im_reg_button);
		im_shadow_emergency1 = (RoundedImageView) findViewById(R.id.im_shadow_settting1);
		im_shadow_emergency2 = (RoundedImageView) findViewById(R.id.im_shadow_settting2);
		im_shadow_emergency3 = (RoundedImageView) findViewById(R.id.im_shadow_settting3);
		im_shadow_emergency4 = (RoundedImageView) findViewById(R.id.im_shadow_settting4);
		im_shadow_emergency5 = (RoundedImageView) findViewById(R.id.im_shadow_settting5);
		tv_emergency1 = (TextView) findViewById(R.id.tv_setttingsprofieedit1);
		tv_emergency2 = (TextView) findViewById(R.id.tv_setttingsprofieedit2);
		tv_emergency3 = (TextView) findViewById(R.id.tv_setttingsprofieedit3);
		tv_emergency4 = (TextView) findViewById(R.id.tv_setttingsprofieedit4);
		tv_emergency5 = (TextView) findViewById(R.id.tv_setttingsprofieedit5);
		tv_addguard = (TextView) findViewById(R.id.tv_addguard);
		tv_setttingsprofieeditno1 = (TextView) findViewById(R.id.tv_setttingsprofieeditno1);
		tv_setttingsprofieeditno2 = (TextView) findViewById(R.id.tv_setttingsprofieeditno2);
		tv_setttingsprofieeditno3 = (TextView) findViewById(R.id.tv_setttingsprofieeditno3);
		tv_setttingsprofieeditno4 = (TextView) findViewById(R.id.tv_setttingsprofieeditno4);
		tv_setttingsprofieeditno5 = (TextView) findViewById(R.id.tv_setttingsprofieeditno5);
		im_setttingscrosstick1 = (ImageView) findViewById(R.id.im_setttingscrosstick1);
		im_setttingscrosstick2 = (ImageView) findViewById(R.id.im_setttingscrosstick2);
		im_setttingscrosstick3 = (ImageView) findViewById(R.id.im_setttingscrosstick3);
		im_setttingscrosstick4 = (ImageView) findViewById(R.id.im_setttingscrosstick4);
		im_setttingscrosstick5 = (ImageView) findViewById(R.id.im_setttingscrosstick5);
		im_setttingscrosstick1.setOnClickListener(new Click());
		im_setttingscrosstick2.setOnClickListener(new Click());
		im_setttingscrosstick3.setOnClickListener(new Click());
		im_setttingscrosstick4.setOnClickListener(new Click());
		im_setttingscrosstick5.setOnClickListener(new Click());

		im_next = (ImageView) findViewById(R.id.im_next);
		im_next.setOnClickListener(this);

		im_setttingscross1 = (ImageView) findViewById(R.id.im_setttingscross1);
		im_setttingscross2 = (ImageView) findViewById(R.id.im_setttingscross2);
		im_setttingscross3 = (ImageView) findViewById(R.id.im_setttingscross3);
		im_setttingscross4 = (ImageView) findViewById(R.id.im_setttingscross4);
		im_setttingscross5 = (ImageView) findViewById(R.id.im_setttingscross5);
		im_setttingscross1.setOnClickListener(this);
		im_setttingscross2.setOnClickListener(this);
		im_setttingscross3.setOnClickListener(this);
		im_setttingscross4.setOnClickListener(this);
		im_setttingscross5.setOnClickListener(this);

		im_setttingsprofieedit1 = (ImageView) findViewById(R.id.im_setttingsprofieedit1);
		im_setttingsprofieedit2 = (ImageView) findViewById(R.id.im_setttingsprofieedit2);
		im_setttingsprofieedit3 = (ImageView) findViewById(R.id.im_setttingsprofieedit3);
		im_setttingsprofieedit4 = (ImageView) findViewById(R.id.im_setttingsprofieedit4);
		im_setttingsprofieedit5 = (ImageView) findViewById(R.id.im_setttingsprofieedit5);
		im_add_button = (ImageView) findViewById(R.id.im_add_button);
		im_reg_button.setTag("6");
		im_add_button.setTag("1");
		im_setttingsprofieedit1.setTag("1");
		im_setttingsprofieedit2.setTag("2");
		im_setttingsprofieedit3.setTag("3");
		im_setttingsprofieedit4.setTag("4");
		im_setttingsprofieedit5.setTag("5");
		im_setttingsprofieedit1.setOnClickListener(this);
		im_setttingsprofieedit2.setOnClickListener(this);
		im_setttingsprofieedit3.setOnClickListener(this);
		im_setttingsprofieedit4.setOnClickListener(this);
		im_setttingsprofieedit5.setOnClickListener(this);
		im_add_button.setOnClickListener(this);
		im_reg_button.setOnClickListener(this);

		// bt_mr = (Button) findViewById(R.id.bt_mr);
		// bt_ms = (Button) findViewById(R.id.bt_ms);
		// bt_activaet = (Button) findViewById(R.id.bt_activaet);
		// im_emergency1.setOnClickListener(this);
		// im_emergency2.setOnClickListener(this);
		// im_emergency3.setOnClickListener(this);
		// im_emergency4.setOnClickListener(this);
		// im_emergency5.setOnClickListener(this);
		// bt_mr.setOnClickListener(this);
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
		changeFonts((ViewGroup) im_next.getParent());
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (appPrefes.getData("gotogetdetail").equals("true")) {
			appPrefes.SaveData("settingschaged", "true");
			appPrefes.SaveData("gotogetdetail", "false");
			// gettingguardians();
			setpreferenceguadian();
		}
		setyourtag();
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
		Intent i = new Intent(Settingsnewnew.this, Splash.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		Settingsnewnew.this.startActivity(i);
		finish();
	}

	private void setimages() {
		// TODO Auto-generated method stub
		tv_emergency1.setText(appPrefes.getData("gname1"));
		tv_emergency2.setText(appPrefes.getData("gname2"));
		tv_emergency3.setText(appPrefes.getData("gname3"));
		tv_emergency4.setText(appPrefes.getData("gname4"));
		tv_emergency5.setText(appPrefes.getData("gname5"));

		float scale = getResources().getDisplayMetrics().density;
		int imageSizeWidthPX = (int) ((80 * scale) + 0.5);
		int imageSizeHeightPX = (int) ((80 * scale) + 0.5);

		if (!appPrefes.getData("gname1").equals("")) {
			((View) im_setttingscross1.getParent()).setVisibility(View.VISIBLE);
		}
		if (!appPrefes.getData("gname2").equals("")) {
			((View) im_setttingscross2.getParent()).setVisibility(View.VISIBLE);
		}
		if (!appPrefes.getData("gname3").equals("")) {
			((View) im_setttingscross3.getParent()).setVisibility(View.VISIBLE);
		}
		if (!appPrefes.getData("gname4").equals("")) {
			((View) im_setttingscross4.getParent()).setVisibility(View.VISIBLE);
		}
		if (!appPrefes.getData("gname5").equals("")) {
			((View) im_setttingscross5.getParent()).setVisibility(View.VISIBLE);
		}
		if (!appPrefes.getData("ggc1").equals("")) {
			if (appPrefes.getData("ggc1").equals("cam")) {
				((View) im_setttingscross1.getParent())
						.setVisibility(View.VISIBLE);
				im_shadow_emergency1.setVisibility(View.VISIBLE);
				PictureOrentation orentation = new PictureOrentation();
				orentation.change(appPrefes.getData("gimage1"),
						im_shadow_emergency1, Settingsnewnew.this);
			} else if (appPrefes.getData("ggc1").equals("gallery")) {
				im_setttingscross1.setVisibility(View.VISIBLE);
				im_shadow_emergency1.setVisibility(View.VISIBLE);
				Bitmap bitmap = imageSmallerAction
						.decodeSampledBitmapFromGallery(
								appPrefes.getData("gimage1"), 100, 100);
				im_shadow_emergency1.setImageBitmap(ImageSmallerAction
						.getCircleBitmap(bitmap, imageSizeWidthPX,
								imageSizeHeightPX));
			}
		}
		if (!appPrefes.getData("ggc2").equals("")) {
			if (appPrefes.getData("ggc2").equals("cam")) {
				im_setttingscross2.setVisibility(View.VISIBLE);
				im_shadow_emergency2.setVisibility(View.VISIBLE);
				PictureOrentation orentation = new PictureOrentation();
				orentation.change(appPrefes.getData("gimage2"),
						im_shadow_emergency2, Settingsnewnew.this);
			} else if (appPrefes.getData("ggc2").equals("gallery")) {
				im_setttingscross2.setVisibility(View.VISIBLE);
				im_shadow_emergency2.setVisibility(View.VISIBLE);
				Bitmap bitmap = imageSmallerAction
						.decodeSampledBitmapFromGallery(
								appPrefes.getData("gimage2"), 100, 100);
				im_shadow_emergency2.setImageBitmap(ImageSmallerAction
						.getCircleBitmap(bitmap, imageSizeWidthPX,
								imageSizeHeightPX));
			}
		}
		if (!appPrefes.getData("ggc3").equals("")) {
			if (appPrefes.getData("ggc3").equals("cam")) {
				im_setttingscross3.setVisibility(View.VISIBLE);
				im_shadow_emergency3.setVisibility(View.VISIBLE);
				PictureOrentation orentation = new PictureOrentation();
				orentation.change(appPrefes.getData("gimage3"),
						im_shadow_emergency3, Settingsnewnew.this);
			} else if (appPrefes.getData("ggc3").equals("gallery")) {
				im_setttingscross3.setVisibility(View.VISIBLE);
				im_shadow_emergency3.setVisibility(View.VISIBLE);
				Bitmap bitmap = imageSmallerAction
						.decodeSampledBitmapFromGallery(
								appPrefes.getData("gimage3"), 100, 100);
				im_shadow_emergency3.setImageBitmap(ImageSmallerAction
						.getCircleBitmap(bitmap, imageSizeWidthPX,
								imageSizeHeightPX));
			}
		}
		if (!appPrefes.getData("ggc4").equals("")) {
			if (appPrefes.getData("ggc4").equals("cam")) {
				im_setttingscross4.setVisibility(View.VISIBLE);
				im_shadow_emergency4.setVisibility(View.VISIBLE);
				PictureOrentation orentation = new PictureOrentation();
				orentation.change(appPrefes.getData("gimage4"),
						im_shadow_emergency4, Settingsnewnew.this);
			} else if (appPrefes.getData("ggc4").equals("gallery")) {
				im_setttingscross4.setVisibility(View.VISIBLE);
				im_shadow_emergency4.setVisibility(View.VISIBLE);
				Bitmap bitmap = imageSmallerAction
						.decodeSampledBitmapFromGallery(
								appPrefes.getData("gimage4"), 100, 100);
				im_shadow_emergency4.setImageBitmap(ImageSmallerAction
						.getCircleBitmap(bitmap, imageSizeWidthPX,
								imageSizeHeightPX));
			}
		}
		if (!appPrefes.getData("ggc5").equals("")) {
			if (appPrefes.getData("ggc5").equals("cam")) {
				im_setttingscross5.setVisibility(View.VISIBLE);
				im_shadow_emergency5.setVisibility(View.VISIBLE);
				PictureOrentation orentation = new PictureOrentation();
				orentation.change(appPrefes.getData("gimage5"),
						im_shadow_emergency5, Settingsnewnew.this);
			} else if (appPrefes.getData("ggc5").equals("gallery")) {
				try {
					im_setttingscross5.setVisibility(View.VISIBLE);
					im_shadow_emergency5.setVisibility(View.VISIBLE);
					Bitmap bitmap = imageSmallerAction
							.decodeSampledBitmapFromGallery(
									appPrefes.getData("gimage5"), 100, 100);
					im_shadow_emergency5.setImageBitmap(ImageSmallerAction
							.getCircleBitmap(bitmap, imageSizeWidthPX,
									imageSizeHeightPX));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
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
		case R.id.im_setttingscross1:
			((View) im_setttingscross1.getParent()).setVisibility(View.GONE);
			deleteguardians(im_setttingscross1);
			clear(0);
			break;
		case R.id.im_setttingscross2:
			((View) im_setttingscross2.getParent()).setVisibility(View.GONE);
			deleteguardians(im_setttingscross2);
			clear(1);
			break;
		case R.id.im_setttingscross3:
			((View) im_setttingscross3.getParent()).setVisibility(View.GONE);
			deleteguardians(im_setttingscross3);
			clear(2);
			break;
		case R.id.im_setttingscross4:
			((View) im_setttingscross4.getParent()).setVisibility(View.GONE);
			deleteguardians(im_setttingscross4);
			clear(3);
			break;
		case R.id.im_setttingscross5:
			((View) im_setttingscross5.getParent()).setVisibility(View.GONE);
			deleteguardians(im_setttingscross5);
			clear(4);
			break;

		case R.id.im_next:
			finish();
			break;
		case R.id.im_shadow:
			Upload_Image();
			break;
		case R.id.im_settingsprofile:
			Upload_Image();
			break;

		default:
			intent = new Intent(Settingsnewnew.this, AddEmergency.class);
			String detail = appPrefes.getData("sessionsession");
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
			appPrefes.SaveData("gimagemain", profilepath);
			appPrefes.SaveData("ggcmain", gc);
		}
		if (requestCode == RESULT_LOAD_CAMERA && resultCode == RESULT_OK) {
			gc = "cam";
			profilepath = fileUri.getPath();
			PictureOrentation orentation = new PictureOrentation();
			appPrefes.SaveData("gimagemain", profilepath);
			appPrefes.SaveData("ggcmain", gc);
		}
	}

	private void removbedialog(final int position) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(this,
				android.R.style.Theme_Light_NoTitleBar_Fullscreen);
		dialog.setContentView(R.layout.removecontact);
		Button bt_yes = (Button) dialog.findViewById(R.id.bt_yes);
		Button bt_no = (Button) dialog.findViewById(R.id.bt_no);
		bt_yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				dialog.cancel();
			}
		});
		bt_no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		dialog.show();
	}

	private void clear(int position) {
		// TODO Auto-generated method stub
		if (position == 0) {
			tv_emergency1.setText("");
			tv_setttingsprofieeditno1.setText("");
			((View) im_setttingscross1.getParent()).setVisibility(View.GONE);
			appPrefes.SaveData("ggc1", "");
			appPrefes.SaveData("gname1", "");
			appPrefes.SaveData("gnumber1", "");
			appPrefes.SaveData("gimage1", "");
			im_shadow_emergency1.setImageBitmap(null);
		} else if (position == 1) {
			tv_emergency2.setText("");
			tv_setttingsprofieeditno2.setText("");
			((View) im_setttingscross2.getParent()).setVisibility(View.GONE);
			appPrefes.SaveData("ggc2", "");
			appPrefes.SaveData("gname2", "");
			appPrefes.SaveData("gnumber2", "");
			appPrefes.SaveData("gimage2", "");
			im_shadow_emergency2.setImageBitmap(null);
		} else if (position == 2) {
			tv_emergency3.setText("");
			tv_setttingsprofieeditno3.setText("");
			((View) im_setttingscross3.getParent()).setVisibility(View.GONE);
			appPrefes.SaveData("ggc3", "");
			appPrefes.SaveData("gname3", "");
			appPrefes.SaveData("gnumber3", "");
			appPrefes.SaveData("gimage3", "");
			im_shadow_emergency3.setImageBitmap(null);
		} else if (position == 3) {
			tv_emergency4.setText("");
			tv_setttingsprofieeditno4.setText("");
			((View) im_setttingscross4.getParent()).setVisibility(View.GONE);
			appPrefes.SaveData("ggc4", "");
			appPrefes.SaveData("gname4", "");
			appPrefes.SaveData("gnumber4", "");
			appPrefes.SaveData("gimage4", "");
			im_shadow_emergency4.setImageBitmap(null);
		} else if (position == 4) {
			tv_emergency5.setText("");
			tv_setttingsprofieeditno5.setText("");
			((View) im_setttingscross5.getParent()).setVisibility(View.GONE);
			appPrefes.SaveData("ggc5", "");
			appPrefes.SaveData("gname5", "");
			appPrefes.SaveData("gnumber5", "");
			appPrefes.SaveData("gimage5", "");
			im_shadow_emergency5.setImageBitmap(null);
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

	private void gettingguardians() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		AsyncHttpClient client = new AsyncHttpClient();
		params.put("userID", appPrefes.getData("userID"));
		GlobalFunctions.postApiCall(this, Registration_New.Url
				+ "guardianlist.php", params, client,
				new HttpResponseHandler() {

					@Override
					public void handle(String response, boolean success) {
						// TODO Auto-generated method stub
						if (response != null) {
							if (success && !response.equals("null")) {
								appPrefes.SaveData("sessionsession", response);
								setyourtag();
								DetailFalse session = (new Gson()).fromJson(
										response.toString(), DetailFalse.class);
								if (!session.Status.equals("Error")) {
									setguardian(session.lists.get(0));
								}
							}
						}
					}
				});
	}

	protected void changeFonts(ViewGroup root) {
		Typeface tf = Typeface.createFromAsset(getAssets(), "FontAwesome.otf");
		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				((TextView) v).setTypeface(tf);
			} else if (v instanceof Button) {
				((Button) v).setTypeface(tf);
			} else if (v instanceof EditText) {
				((EditText) v).setTypeface(tf);
			} else if (v instanceof ViewGroup) {
				changeFonts((ViewGroup) v);
			}
		}
	}

	private void setyourtag() {
		// TODO Auto-generated method stub
		im_add_button.setVisibility(View.VISIBLE);
		tv_addguard.setVisibility(View.VISIBLE);
		try {
			DetailFalse detailGuardian = (new Gson()).fromJson(
					appPrefes.getData("sessionsession"), DetailFalse.class);
			com.epicelements.spotnsave.DetailFalse.lists lists = detailGuardian.lists
					.get(0);
			if (lists.id.size() == 0) {
				im_add_button.setTag("1");
			} else if (lists.id.size() == 1) {
				im_add_button.setTag("2");
			} else if (lists.id.size() == 2) {
				im_add_button.setTag("3");
			} else if (lists.id.size() == 3) {
				im_add_button.setTag("4");
			} else if (lists.id.size() == 4) {
				im_add_button.setTag("5");
			}
			if (lists.id.size() == 5) {
				im_add_button.setVisibility(View.GONE);
				tv_addguard.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void setguardian() {
		// TODO Auto-generated method stub
		if (detailGuardian != null) {
			// lists lists = detailGuardian.lists.get(0);
			lists lists = null;
			if (lists.id.size() != 0) {
				im_reg_button.setVisibility(View.GONE);
			}
			if (lists.id.size() == 0) {
				im_add_button.setTag("1");
			} else if (lists.id.size() == 1) {
				im_add_button.setTag("2");
			} else if (lists.id.size() == 2) {
				im_add_button.setTag("3");
			} else if (lists.id.size() == 3) {
				im_add_button.setTag("4");
			} else if (lists.id.size() == 4) {
				im_add_button.setTag("5");
			}
			if (lists.id.size() == 5) {
				im_add_button.setVisibility(View.GONE);
				tv_addguard.setVisibility(View.GONE);
			}
			if (lists.id.size() == 1) {
				tv_emergency1.setText(lists.name.get(0));
				tv_setttingsprofieeditno1.setText(lists.phoneno.get(0));
				im_setttingscross1.setTag(lists.id.get(0));
				((View) im_setttingscross1.getParent())
						.setVisibility(View.VISIBLE);
				setimage(lists.image.get(0), im_shadow_emergency1);
				if (lists.permisssion.get(0).equals("true")) {
					im_setttingscrosstick1.setImageResource(R.drawable.tick);
					im_setttingscrosstick1.setContentDescription("true");
				}
			} else if (lists.id.size() == 2) {
				tv_emergency1.setText(lists.name.get(0));
				tv_setttingsprofieeditno1.setText(lists.phoneno.get(0));
				im_setttingscross1.setTag(lists.id.get(0));
				((View) im_setttingscross1.getParent())
						.setVisibility(View.VISIBLE);
				setimage(lists.image.get(0), im_shadow_emergency1);
				if (lists.permisssion.get(0).equals("true")) {
					im_setttingscrosstick1.setImageResource(R.drawable.tick);
					im_setttingscrosstick1.setContentDescription("true");
				}
				tv_emergency2.setText(lists.name.get(1));
				tv_setttingsprofieeditno2.setText(lists.phoneno.get(1));
				im_setttingscross2.setTag(lists.id.get(1));
				((View) im_setttingscross2.getParent())
						.setVisibility(View.VISIBLE);
				setimage(lists.image.get(1), im_shadow_emergency2);
				if (lists.permisssion.get(1).equals("true")) {
					im_setttingscrosstick2.setImageResource(R.drawable.tick);
					im_setttingscrosstick2.setContentDescription("true");
				}
			} else if (lists.id.size() == 3) {
				tv_emergency1.setText(lists.name.get(0));
				tv_setttingsprofieeditno1.setText(lists.phoneno.get(0));
				im_setttingscross1.setTag(lists.id.get(0));
				((View) im_setttingscross1.getParent())
						.setVisibility(View.VISIBLE);
				setimage(lists.image.get(0), im_shadow_emergency1);
				if (lists.permisssion.get(0).equals("true")) {
					im_setttingscrosstick1.setImageResource(R.drawable.tick);
					im_setttingscrosstick1.setContentDescription("true");
				}
				tv_emergency2.setText(lists.name.get(1));
				tv_setttingsprofieeditno2.setText(lists.phoneno.get(1));
				im_setttingscross2.setTag(lists.id.get(1));
				((View) im_setttingscross2.getParent())
						.setVisibility(View.VISIBLE);
				setimage(lists.image.get(1), im_shadow_emergency2);
				if (lists.permisssion.get(1).equals("true")) {
					im_setttingscrosstick2.setImageResource(R.drawable.tick);
					im_setttingscrosstick2.setContentDescription("true");
				}
				tv_emergency3.setText(lists.name.get(2));
				tv_setttingsprofieeditno3.setText(lists.phoneno.get(2));
				im_setttingscross3.setTag(lists.id.get(2));
				((View) im_setttingscross3.getParent())
						.setVisibility(View.VISIBLE);
				setimage(lists.image.get(2), im_shadow_emergency3);
				if (lists.permisssion.get(2).equals("true")) {
					im_setttingscrosstick3.setImageResource(R.drawable.tick);
					im_setttingscrosstick3.setContentDescription("true");
				}
			} else if (lists.id.size() == 4) {
				tv_emergency1.setText(lists.name.get(0));
				tv_setttingsprofieeditno1.setText(lists.phoneno.get(0));
				im_setttingscross1.setTag(lists.id.get(0));
				((View) im_setttingscross1.getParent())
						.setVisibility(View.VISIBLE);
				setimage(lists.image.get(0), im_shadow_emergency1);
				if (lists.permisssion.get(0).equals("true")) {
					im_setttingscrosstick1.setContentDescription("true");
					im_setttingscrosstick1.setImageResource(R.drawable.tick);
				}
				tv_emergency2.setText(lists.name.get(1));
				tv_setttingsprofieeditno2.setText(lists.phoneno.get(1));
				im_setttingscross2.setTag(lists.id.get(1));
				((View) im_setttingscross2.getParent())
						.setVisibility(View.VISIBLE);
				setimage(lists.image.get(1), im_shadow_emergency2);
				if (lists.permisssion.get(1).equals("true")) {
					im_setttingscrosstick2.setImageResource(R.drawable.tick);
					im_setttingscrosstick2.setContentDescription("true");
				}
				tv_emergency3.setText(lists.name.get(2));
				tv_setttingsprofieeditno3.setText(lists.phoneno.get(2));
				im_setttingscross3.setTag(lists.id.get(2));
				((View) im_setttingscross3.getParent())
						.setVisibility(View.VISIBLE);
				setimage(lists.image.get(2), im_shadow_emergency3);
				if (lists.permisssion.get(2).equals("true")) {
					im_setttingscrosstick3.setImageResource(R.drawable.tick);
					im_setttingscrosstick3.setContentDescription("true");
				}
				tv_emergency4.setText(lists.name.get(3));
				tv_setttingsprofieeditno4.setText(lists.phoneno.get(3));
				im_setttingscross4.setTag(lists.id.get(3));
				((View) im_setttingscross4.getParent())
						.setVisibility(View.VISIBLE);
				setimage(lists.image.get(3), im_shadow_emergency4);
				if (lists.permisssion.get(3).equals("true")) {
					im_setttingscrosstick4.setImageResource(R.drawable.tick);
					im_setttingscrosstick4.setContentDescription("true");
				}
			} else if (lists.id.size() == 5) {
				tv_emergency1.setText(lists.name.get(0));
				tv_setttingsprofieeditno1.setText(lists.phoneno.get(0));
				im_setttingscross1.setTag(lists.id.get(0));
				((View) im_setttingscross1.getParent())
						.setVisibility(View.VISIBLE);
				setimage(lists.image.get(0), im_shadow_emergency1);
				if (lists.permisssion.get(0).equals("true")) {
					im_setttingscrosstick1.setContentDescription("true");
					im_setttingscrosstick1.setImageResource(R.drawable.tick);
				}
				tv_emergency2.setText(lists.name.get(1));
				im_setttingscross2.setTag(lists.id.get(1));
				((View) im_setttingscross2.getParent())
						.setVisibility(View.VISIBLE);
				setimage(lists.image.get(1), im_shadow_emergency2);
				if (lists.permisssion.get(1).equals("true")) {
					im_setttingscrosstick2.setImageResource(R.drawable.tick);
					im_setttingscrosstick2.setContentDescription("true");
				}
				tv_emergency3.setText(lists.name.get(2));
				tv_setttingsprofieeditno3.setText(lists.phoneno.get(2));
				im_setttingscross3.setTag(lists.id.get(2));
				((View) im_setttingscross3.getParent())
						.setVisibility(View.VISIBLE);
				setimage(lists.image.get(2), im_shadow_emergency3);
				if (lists.permisssion.get(2).equals("true")) {
					im_setttingscrosstick3.setImageResource(R.drawable.tick);
					im_setttingscrosstick3.setContentDescription("true");
				}
				tv_emergency4.setText(lists.name.get(3));
				tv_setttingsprofieeditno4.setText(lists.phoneno.get(3));
				im_setttingscross4.setTag(lists.id.get(3));
				((View) im_setttingscross4.getParent())
						.setVisibility(View.VISIBLE);
				setimage(lists.image.get(3), im_shadow_emergency4);
				if (lists.permisssion.get(3).equals("true")) {
					im_setttingscrosstick4.setImageResource(R.drawable.tick);
					im_setttingscrosstick4.setContentDescription("true");
				}
				tv_emergency5.setText(lists.name.get(4));
				tv_setttingsprofieeditno5.setText(lists.phoneno.get(4));
				im_setttingscross5.setTag(lists.id.get(4));
				((View) im_setttingscross5.getParent())
						.setVisibility(View.VISIBLE);
				setimage(lists.image.get(4), im_shadow_emergency5);
				if (lists.permisssion.get(4).equals("true")) {
					im_setttingscrosstick5.setImageResource(R.drawable.tick);
					im_setttingscrosstick5.setContentDescription("true");
				}
			}
		}
	}

	private void clearall() {
		// TODO Auto-generated method stub
		tv_emergency1.setText("");
		tv_emergency2.setText("");
		tv_emergency3.setText("");
		tv_emergency4.setText("");
		tv_emergency5.setText("");
		((View) im_setttingscross1.getParent()).setVisibility(View.GONE);
		((View) im_setttingscross2.getParent()).setVisibility(View.GONE);
		((View) im_setttingscross3.getParent()).setVisibility(View.GONE);
		((View) im_setttingscross4.getParent()).setVisibility(View.GONE);
		((View) im_setttingscross5.getParent()).setVisibility(View.GONE);
		im_shadow_emergency1.setImageBitmap(null);
		im_shadow_emergency2.setImageBitmap(null);
		im_shadow_emergency3.setImageBitmap(null);
		im_shadow_emergency4.setImageBitmap(null);
		im_shadow_emergency5.setImageBitmap(null);
		im_setttingscrosstick1.setImageResource(R.drawable.untick);
		im_setttingscrosstick2.setImageResource(R.drawable.untick);
		im_setttingscrosstick3.setImageResource(R.drawable.untick);
		im_setttingscrosstick4.setImageResource(R.drawable.untick);
		im_setttingscrosstick5.setImageResource(R.drawable.untick);
	}

	private void deleteguardians(ImageView imageView) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		AsyncHttpClient client = new AsyncHttpClient();
		params.put("userID", appPrefes.getData("userID"));
		params.put("guardianID", imageView.getTag().toString());
		progressshow("Delete guadian");
		GlobalFunctions.postApiCall(this, Registration_New.Url
				+ "delguardian.php", params, client, new HttpResponseHandler() {

			@Override
			public void handle(String response, boolean success) {
				// TODO Auto-generated method stub
				progresscancel();
				// gettingguardians();
				appPrefes.SaveData("settingschaged", "true");
				gettingguardians();
			}
		});
	}

	private void setimage(String imagename, final ImageView imageView) {
		// TODO Auto-generated method stub
		if (1 == 1)
			return;
		float scale = getResources().getDisplayMetrics().density;
		final int imageSizeWidthPX = (int) ((80 * scale) + 0.5);
		final int imageSizeHeightPX = (int) ((80 * scale) + 0.5);
		ImageRequest request = new ImageRequest(
				"http://www.spotnsave.in/App/Modules/Home/Tpl/TwilioApi/upload/"
						+ imagename.replace(" ", "%20"),
				new Response.Listener<Bitmap>() {

					@Override
					public void onResponse(Bitmap response) {
						// TODO Auto-generated method stub
						imageView.setImageBitmap(ImageSmallerAction
								.getCircleBitmap(response, imageSizeWidthPX,
										imageSizeHeightPX));
					}
				}, 100, 100, null, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						// imgDisplay.setImageResource(R.drawable.ic_launcher);
					}
				});
		MySingleton.getInstance(this).addToRequestQueue(request);
	}

	private void setimag() {
		// TODO Auto-generated method stub
		new BitmapWorkerTask(im_shadow_emergency1, this,
				appPrefes.getData("gimagemain")).execute();
	}

	private void progressshow(String message) {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(Settingsnewnew.this);
		pDialog.setMessage(message);
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}

	private void progresscancel() {
		// TODO Auto-generated method stub
		pDialog.dismiss();
	}

	class Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getTag() != null) {
				ImageView imageView = (ImageView) v;
				String cdes = imageView.getContentDescription().toString();
				upadateprofile(Integer.parseInt((String) v.getTag()),
						cdes.equals("true") ? false : true);
				imageView
						.setImageResource(cdes.equals("true") ? R.drawable.untick
								: R.drawable.tick);
				imageView.setContentDescription(cdes.equals("true") ? "false"
						: "true");
			}
		}

	}

	private void upadateprofile(int object, boolean isChecked) {
		// TODO Auto-generated method stub
		DetailFalse detailGuardian = (new Gson()).fromJson(
				appPrefes.getData("sessionsession"), DetailFalse.class);
		System.out.println("isChecked" + isChecked);
		RequestParams params = new RequestParams();
		AsyncHttpClient client = new AsyncHttpClient();
		params.put("userID", appPrefes.getData("userID"));
		params.put("name", detailGuardian.lists.get(0).name.get(object));
		params.put("phno", detailGuardian.lists.get(0).phoneno.get(object));
		params.put("permisssion", "" + isChecked);
		params.put("guardianID", detailGuardian.lists.get(0).id.get(object));
		params.put("image", detailGuardian.lists.get(0).image.get(object));
		progressshow("Update Guardian...");
		GlobalFunctions.postApiCall(this, Registration_New.Url
				+ "guardian_update.php", params, client,
				new HttpResponseHandler() {

					@Override
					public void handle(String response, boolean success) {
						// TODO Auto-generated method stub
						progresscancel();
					}
				});
	}

	private void setpreferenceguadian() {
		// TODO Auto-generated method stub
		System.out.println("sessionsession"
				+ appPrefes.getData("sessionsession"));
		detailGuardian = (new Gson()).fromJson(
				appPrefes.getData("sessionsession"), DetailFalse.class);
		clearall();
		if (!appPrefes.getData("gname1").equals("")) {
			tv_emergency1.setText(appPrefes.getData("gname1"));
			tv_setttingsprofieeditno1.setText(appPrefes.getData("gnumber1"));
			im_setttingscross1.setTag(appPrefes.getData("gid1"));
			((View) im_setttingscross1.getParent()).setVisibility(View.VISIBLE);
			setimage(appPrefes.getData("gimage1"), im_shadow_emergency1);
			if (appPrefes.getData("gpermisson1").equals("true")) {
				im_setttingscrosstick1.setContentDescription("true");
				im_setttingscrosstick1.setImageResource(R.drawable.tick);
			}
		}
		if (!appPrefes.getData("gname2").equals("")) {
			tv_emergency2.setText(appPrefes.getData("gname2"));
			tv_setttingsprofieeditno2.setText(appPrefes.getData("gnumber2"));
			im_setttingscross2.setTag(appPrefes.getData("gid2"));
			((View) im_setttingscross2.getParent()).setVisibility(View.VISIBLE);
			setimage(appPrefes.getData("gimage2"), im_shadow_emergency2);
			if (appPrefes.getData("gpermisson2").equals("true")) {
				im_setttingscrosstick2.setContentDescription("true");
				im_setttingscrosstick2.setImageResource(R.drawable.tick);
			}
		}
		if (!appPrefes.getData("gname3").equals("")) {
			tv_emergency3.setText(appPrefes.getData("gname3"));
			tv_setttingsprofieeditno3.setText(appPrefes.getData("gnumber3"));
			im_setttingscross3.setTag(appPrefes.getData("gid3"));
			((View) im_setttingscross3.getParent()).setVisibility(View.VISIBLE);
			setimage(appPrefes.getData("gimage3"), im_shadow_emergency3);
			if (appPrefes.getData("gpermisson3").equals("true")) {
				im_setttingscrosstick3.setContentDescription("true");
				im_setttingscrosstick3.setImageResource(R.drawable.tick);
			}
		}
		if (!appPrefes.getData("gname4").equals("")) {
			tv_emergency4.setText(appPrefes.getData("gname4"));
			tv_setttingsprofieeditno4.setText(appPrefes.getData("gnumber4"));
			im_setttingscross4.setTag(appPrefes.getData("gid4"));
			((View) im_setttingscross4.getParent()).setVisibility(View.VISIBLE);
			setimage(appPrefes.getData("gimage4"), im_shadow_emergency4);
			if (appPrefes.getData("gpermisson4").equals("true")) {
				im_setttingscrosstick4.setContentDescription("true");
				im_setttingscrosstick4.setImageResource(R.drawable.tick);
			}
		}
		if (!appPrefes.getData("gname5").equals("")) {
			tv_emergency5.setText(appPrefes.getData("gname5"));
			tv_setttingsprofieeditno5.setText(appPrefes.getData("gnumber5"));
			im_setttingscross5.setTag(appPrefes.getData("gid5"));
			((View) im_setttingscross5.getParent()).setVisibility(View.VISIBLE);
			setimage(appPrefes.getData("gimage5"), im_shadow_emergency5);
			if (appPrefes.getData("gpermisson5").equals("true")) {
				im_setttingscrosstick5.setContentDescription("true");
				im_setttingscrosstick5.setImageResource(R.drawable.tick);
			}
		}
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
