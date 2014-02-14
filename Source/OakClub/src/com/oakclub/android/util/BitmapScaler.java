package com.oakclub.android.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class BitmapScaler {
	private Bitmap scaled;
	public static int MAX_IMAGE_DIMENSION = 720;
	public static Bitmap scaleImage(Uri uri, Context context) throws IOException {
		final ContentResolver contentResolver = context.getContentResolver();
		int sample = 1;
		{
			InputStream is = contentResolver.openInputStream(uri);
			try {
				sample = getRoughSize(is, MAX_IMAGE_DIMENSION, MAX_IMAGE_DIMENSION);
			} finally {
				is.close();
			}
		}
		{
			InputStream is = contentResolver.openInputStream(uri);
			try {
				return roughScaleImage(is, sample);
			} finally {
				is.close();
			}
		}
	}
	private static int getRoughSize(InputStream is, int newWidth, int newHeight) {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, o);

		return getRoughSize(o.outWidth, o.outHeight, newWidth, newHeight);
	}
	private static int getRoughSize(int width, int height, int newWidth,
			int newHeight) {
		int sample = 1;

		while (true) {
			if (width / 2 < newWidth || height / 2 < newHeight) {
				break;
			}
			width /= 2;
			height /= 2;
			sample *= 2;
		}
		return sample;
	}
	private static Bitmap roughScaleImage(InputStream is, int sample) {
		BitmapFactory.Options scaledOpts = new BitmapFactory.Options();
		scaledOpts.inSampleSize = sample;
		return BitmapFactory.decodeStream(is, null, scaledOpts);
	}
}
