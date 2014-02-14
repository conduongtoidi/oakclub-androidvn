package com.oakclub.android.core;


import org.xbill.DNS.tests.primary;

import com.oakclub.android.net.AppService;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Helper class which splits Request into two methods. execute() is called from
 * separate thread, and executeUI from UI thread.
 * @author DatTx
 * 2013
 */
public abstract class RequestUI extends Request {

	private Activity activity;
	private boolean isCancelled = false;
	private AppService appService;
	/**
	 * 
	 * @param key
	 *            Key for identifying this Request.
	 * @param activity
	 *            Activity to execute runOnUiThread() on.
	 */
	public RequestUI(Object key, Activity activity) {
		super(key);
		this.activity = activity;
	}

	public RequestUI(Object key, AppService appService) {
		super(key);
		this.appService = appService;
	}
	
	@Override
	public final void cancel() {
		isCancelled = true;
	}

	/**
	 * This method is called from separate Thread. Implement this to execute
	 * asynchronous code. If Exception is thrown request execution ends at once.
	 */
	public abstract void execute() throws Exception;

	/**
	 * This method is called always from UI thread. Implement this to update UI.
	 * Exception is null, or one thrown from execute() method.
	 */
	public abstract void executeUI(Exception ex);

	/**
	 * Returns boolean indicating if this Request has been canceled.
	 * 
	 * @return
	 */
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public final void run() {
		if (!isCancelled) {
			Exception ex = null;
			try {
				execute();
			} catch (Exception e) {
				ex = e;
			} finally {
				if (activity != null) {
					activity.runOnUiThread(new RunnableUI(ex));
				}
				if (appService != null) {
					new Handler(Looper.getMainLooper()).post(new RunnableUI(ex));
				}
			}
		}
	}

	/**
	 * Runnable for UI thread execution.
	 * 
	 * 
	 */
	private class RunnableUI implements Runnable {

		private Exception mEx;

		public RunnableUI(Exception ex) {
			mEx = ex;
		}

		@Override
		public void run() {
			if (!isCancelled) 
			{
				executeUI(mEx);
			}
		}
	}

}