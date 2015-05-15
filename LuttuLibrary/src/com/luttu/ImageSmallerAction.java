package com.luttu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;

public class ImageSmallerAction {
	
	public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight)
	{
		
		BitmapFactory.Options option=new BitmapFactory.Options();
		option.inJustDecodeBounds=true;
		
		BitmapFactory.decodeResource(res, resId, option);
		option.inSampleSize=calculateInSampleSize(option, reqWidth, reqHeight);
		option.inTargetDensity=1;
		option.inJustDecodeBounds=false;
		option.inScaled = false;
		return BitmapFactory.decodeResource(res, resId, option);
		
	}
	
	public Bitmap decodeSampledBitmapFromGallery(String path,
	        int reqWidth, int reqHeight)
	{
		
		BitmapFactory.Options option=new BitmapFactory.Options();
		option.inJustDecodeBounds=true;
		option.inScaled = false;
		BitmapFactory.decodeFile(path, option);
		option.inSampleSize=calculateInSampleSize(option, reqWidth, reqHeight);
		option.inJustDecodeBounds=false;
		option.inScaled = false;
		return BitmapFactory.decodeFile(path, option);
		
	}
	
	public Bitmap decodeSampledBitmapFromCamera(Uri fileUri,
	        int reqWidth, int reqHeight)
	{
		System.out.println("inside decode----"+fileUri.getPath());
		BitmapFactory.Options option=new BitmapFactory.Options();
		option.inJustDecodeBounds=true;
		option.inScaled = false;
		BitmapFactory.decodeFile(fileUri.getPath(), option);
		option.inSampleSize=calculateInSampleSize(option, reqWidth, reqHeight);
		option.inJustDecodeBounds=false;
		option.inScaled = false;
		return	BitmapFactory.decodeFile(fileUri.getPath(), option);
		
	}

	private int calculateInSampleSize(Options option, int reqWidth,
			int reqHeight) {
		// TODO Auto-generated method stub
		int imageHeight=option.outHeight;
		int imageWidth=option.outWidth;
		int inSampleSize = 1;
		if(imageHeight>reqHeight|| imageWidth>reqWidth)
		{
			int halfHeight=imageHeight/2;
			int halfWidth=imageWidth/2;
			
			while((halfHeight/inSampleSize)>reqHeight||(halfWidth/inSampleSize)>reqWidth)
			{
				inSampleSize*=2;
			}
		}
		return inSampleSize;
	}

}
