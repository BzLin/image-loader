package com.novoda.imageloader.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.novoda.imageloader.core.Settings;
import com.novoda.imageloader.core.file.FileUtil;

public class BitmapUtil {

  private static final String TAG = "ImageLoader";

  public Bitmap decodeFileAndScale(File f, boolean scale, Settings settings) {
  	FileInputStream fis = null;
    try {
      f.setLastModified(System.currentTimeMillis());
      fis = new FileInputStream(f);
      Bitmap unscaledBitmap = BitmapFactory.decodeStream(fis);
      return scaleBitmap(unscaledBitmap, settings.getImageWidth(), settings.getImageHeight());
    } catch (FileNotFoundException e) {
      Log.e(TAG, e.getMessage(), e);
    } finally {
    	new FileUtil().closeSilently(fis);
    }
    return null;
  }

  public Bitmap scaleBitmap(Bitmap b, int width, int height) {
    int imageHeight = b.getHeight();
    int imageWidth = b.getWidth();
    int finalWidth = width;
    int finalHeight = height;
    if (imageHeight > imageWidth) {
      float factor = ((float) height) / ((float) imageHeight);
      finalHeight = new Float(imageHeight * factor).intValue();
      finalWidth = new Float(imageWidth * factor).intValue();
    } else {
      float factor = ((float) width) / ((float) imageWidth);
      finalHeight = new Float(imageHeight * factor).intValue();
      finalWidth = new Float(imageWidth * factor).intValue();
    }
    return Bitmap.createScaledBitmap(b, finalWidth, finalHeight, true);
  }

	public Bitmap decodeDefaultAndScaleBitmap(Context context, Settings settings) {
    return decodeResourceAndScale(context, settings, settings.getDefaultImageId());
  }
  
  public Bitmap decodeNotFoundAndScaleBitmap(Context context, Settings settings) {
    return decodeResourceAndScale(context, settings, settings.getNotFoundImageId());
  }

  private Bitmap decodeResourceAndScale(Context context, Settings settings, int resourceId) {
    Bitmap image = BitmapFactory.decodeResource(context.getResources(), resourceId);
    return scaleBitmap(image, settings.getImageWidth(), settings.getImageHeight());
  }

}