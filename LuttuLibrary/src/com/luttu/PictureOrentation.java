package com.luttu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.luttu.ImageSmallerAction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class PictureOrentation {

	public String change(String picturePath, ImageView image, Context context) {

		Bitmap newTopImage1 = null, mNewSaving = null;
		Bitmap bitmap = null;
		String FtoSave = null;
		System.out.println("picturePath:" + picturePath);
		try {
			bitmap = imageSmalerObj.decodeSampledBitmapFromGallery(picturePath,
					200, 200);
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inSampleSize = 3;
			// bitmap = BitmapFactory.decodeFile(picturePath, options);

			ExifInterface exif = null;
			try {
				exif = new ExifInterface(picturePath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String orientString = exif
					.getAttribute(ExifInterface.TAG_ORIENTATION);
			int orientation = orientString != null ? Integer
					.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
			System.out.println("orientationnnnnn" + orientation);

			int rotationAngle = 0;
			if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
				rotationAngle = 90;
			if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
				rotationAngle = 180;
			if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
				rotationAngle = 270;
			System.out.println("BitMap:" + bitmap.getHeight() + ":"
					+ bitmap.getWidth());

			Matrix matrix = new Matrix();
			matrix.postRotate(rotationAngle);
			int width = bitmap.getWidth(), height = bitmap.getHeight();

			if (bitmap.getWidth() > bitmap.getHeight()) {
				width = bitmap.getHeight();
			} else if (bitmap.getWidth() < bitmap.getHeight()) {
				height = bitmap.getWidth();
			}

			newTopImage1 = Bitmap.createBitmap(bitmap, (bitmap.getWidth() / 2)
					- (width / 2), (bitmap.getHeight() / 2) - (height / 2),
					width, height, matrix, true);

			try {
				@SuppressWarnings("deprecation")
				BitmapDrawable mBitmapDrawable = new BitmapDrawable(
						newTopImage1);
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(new Date());
				mNewSaving = mBitmapDrawable.getBitmap();
				File mediaStorageDir = new File(
						Environment
								.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						"Wyc");
				// File mediaFile = new File(mediaStorageDir.getPath()
				// + File.separator + "Lutt_" + timeStamp + ".jpg");
				FtoSave = mediaStorageDir.getPath() + File.separator + "Wyc_"
						+ timeStamp + ".jpg";

				// String file_path = FtoSave;

				File mFile = new File(FtoSave);
				FileOutputStream mFileOutputStream = new FileOutputStream(mFile);
				mNewSaving
						.compress(CompressFormat.JPEG, 100, mFileOutputStream);
				mFileOutputStream.flush();
				mFileOutputStream.close();

				float scale = context.getResources().getDisplayMetrics().density;
				int imageSizeWidthPX = (int) ((120 * scale) + 0.5);
				int imageSizeHeightPX = (int) ((120 * scale) + 0.5);
				if (image != null) {
					image.setImageBitmap(getCircleBitmap(mNewSaving,imageSizeWidthPX,imageSizeHeightPX));
				}
			} catch (FileNotFoundException e) {
				Log.e("p", "FileNotFoundExceptionError " + e.toString());
			} catch (IOException e) {
				Log.e("k", "IOExceptionError " + e.toString());
			}
			Log.i("o", "Image Created");
		} catch (OutOfMemoryError localOutOfMemoryError) {
			while (true) {

				bitmap.recycle();
				newTopImage1.recycle();
				mNewSaving.recycle();
				System.gc();

			}
		}
		System.out.println("FtoSavein:" + FtoSave);
		return FtoSave;

	}

	ImageSmallerAction imageSmalerObj = new ImageSmallerAction();

	public String change(String picturePath) {

		Bitmap newTopImage1 = null, mNewSaving = null;
		Bitmap bitmap = null;
		String FtoSave = null;
		System.out.println("picturePath:" + picturePath);
		try {
			bitmap = BitmapFactory.decodeFile(picturePath);
			// bitmap =
			// imageSmalerObj.decodeSampledBitmapFromGallery(picturePath,
			// 200, 200);
			int rotationAngle = 90;
			System.out.println("BitMap:" + bitmap.getHeight() + ":"
					+ bitmap.getWidth());
			Matrix matrix = new Matrix();
			matrix.postRotate(rotationAngle);
			int width = bitmap.getWidth(), height = bitmap.getHeight();

			if (bitmap.getWidth() > bitmap.getHeight()) {
				width = bitmap.getHeight();
			} else if (bitmap.getWidth() < bitmap.getHeight()) {
				height = bitmap.getWidth();
			}
			newTopImage1 = Bitmap.createBitmap(bitmap, (bitmap.getWidth() / 2)
					- (width / 2), (bitmap.getHeight() / 2) - (height / 2),
					width, height, matrix, true);
			try {
				@SuppressWarnings("deprecation")
				BitmapDrawable mBitmapDrawable = new BitmapDrawable(
						newTopImage1);
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(new Date());
				mNewSaving = mBitmapDrawable.getBitmap();

				File mediaStorageDir = new File(
						Environment
								.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						"Wyc");
				File mediaFile = new File(mediaStorageDir.getPath()
						+ File.separator + "Wyc_" + timeStamp + ".jpg");
				FtoSave = mediaFile.getAbsolutePath();
				if (!mediaStorageDir.exists()) {
					if (!mediaStorageDir.mkdirs()) {
						Log.d("MyCameraApp", "failed to create directory");
						return null;
					}
				}
				FileOutputStream mFileOutputStream = new FileOutputStream(
						mediaFile);
				mNewSaving
						.compress(CompressFormat.JPEG, 100, mFileOutputStream);
				mFileOutputStream.flush();
				mFileOutputStream.close();

				// image.setImageBitmap(mNewSaving);
			} catch (FileNotFoundException e) {
				Log.e("p", "FileNotFoundExceptionError " + e.toString());
			} catch (IOException e) {
				Log.e("k", "IOExceptionError " + e.toString());
			}
			Log.i("o", "Image Created");
		} catch (OutOfMemoryError localOutOfMemoryError) {
			while (true) {

				bitmap.recycle();
				newTopImage1.recycle();
				mNewSaving.recycle();
				System.gc();

			}
		}
		System.out.println("FtoSavein:" + FtoSave);
		return FtoSave;
	}

	public static Bitmap getCircleBitmap(Bitmap bitmap, int width, int height) {
		Bitmap croppedBitmap = scaleCenterCrop(bitmap, width, height);
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();

		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);

		int radius = 0;
		if (width > height) {
			radius = height / 2;
		} else {
			radius = width / 2;
		}

		canvas.drawCircle(width / 2, height / 2, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(croppedBitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap scaleCenterCrop(Bitmap source, int newHeight,
			int newWidth) {
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();

		float xScale = (float) newWidth / sourceWidth;
		float yScale = (float) newHeight / sourceHeight;
		float scale = Math.max(xScale, yScale);

		float scaledWidth = scale * sourceWidth;
		float scaledHeight = scale * sourceHeight;

		float left = (newWidth - scaledWidth) / 2;
		float top = (newHeight - scaledHeight) / 2;

		RectF targetRect = new RectF(left, top, left + scaledWidth, top
				+ scaledHeight);

		Bitmap dest = Bitmap.createBitmap(newWidth, newHeight,
				source.getConfig());
		Canvas canvas = new Canvas(dest);
		canvas.drawBitmap(source, null, targetRect, null);

		return dest;
	}
}
