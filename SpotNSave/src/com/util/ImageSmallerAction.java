package com.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.PorterDuff.Mode;
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
    public static Bitmap getCircleBitmap(Bitmap bitmap, int width, int height)
    {
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
        if(width > height)
        {
            radius = height / 2;
        }
        else
        {
            radius = width / 2;
        }

        canvas.drawCircle(width / 2, height / 2, radius, paint); 
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(croppedBitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth)
    {
    	if (source==null) {
    		return source;
		}
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

}
