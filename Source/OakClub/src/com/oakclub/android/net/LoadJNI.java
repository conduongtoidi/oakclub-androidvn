package com.oakclub.android.net;

import com.oakclub.android.util.Constants;

import android.content.Context;
import android.util.Log;

public final class LoadJNI {

	static {
		System.loadLibrary("loader-jni");
	}
	
	/**
	 * 
	 * @param args ffmpeg command
	 * @param videokitSdcardPath working directory 
	 * @param ctx Android context
	 */
	public void run(String[] args, String videokitSdcardPath, Context ctx) {
		load(args, videokitSdcardPath, getVideokitLibPath(ctx));
	}
	
	private static String getVideokitLibPath(Context ctx) {
		String videokitLibPath = ctx.getFilesDir().getParent()  + "/lib/libvideokit.so";
		Log.i(Constants.TAG, "videokitLibPath: " + videokitLibPath);
		return videokitLibPath;
		
	}

	public native String load(String[] args, String videokitSdcardPath, String videokitLibPath);
}
