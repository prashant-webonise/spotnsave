package com.util;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
	private final WeakReference<ImageView> imageViewReference;
	private Context mcontext;
	private String imagename;

	public BitmapWorkerTask(ImageView imageView, Context context,
			String immagename) {
		// Use a WeakReference to ensure the ImageView can be garbage collected
		imageViewReference = new WeakReference<ImageView>(imageView);
		mcontext = context;
		imagename = immagename;
	}

	// Decode image in background.
	@Override
	protected Bitmap doInBackground(Integer... params) {

		try {
			return ImageSmallerAction.getCircleBitmap(Picasso.with(mcontext)
					.load(new File(imagename)).get(), 200, 200);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// Once complete, see if ImageView is still around and set bitmap.
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (imageViewReference != null && bitmap != null) {
			final ImageView imageView = imageViewReference.get();
			if (imageView != null) {
				imageView.setImageBitmap(null);
				imageView.setImageBitmap(bitmap);
			}
		}
	}
}
