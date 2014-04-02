/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.oakclub.android.base;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.os.StrictMode;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.oakclub.android.R;
import com.oakclub.android.core.IRequestQueue;
import com.oakclub.android.core.RequestQueue;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class OakClubApplication extends Application implements IRequestQueue{
	private RequestQueue mRequestQueue = null;
	public static String uuid = "";
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}

		super.onCreate();
		pingActivities();
		initImageLoader(getApplicationContext());
		
	}

	private void pingActivities(){
		Thread SplashTimer = new Thread(){   
		       public void run(){  
		            try{  
		                while(true){
		                	HttpClient hClient = new DefaultHttpClient();
		            		HttpConnectionParams.setConnectionTimeout(hClient.getParams(),
		            				20000);
		            		HttpConnectionParams.setSoTimeout(hClient.getParams(), 60000);
		            		try {
		            			HttpGet hget = new HttpGet();
		            			String headerValue = "UsernameToken "
		            								+"Username=\""+ OakClubBaseActivity.facebook_user_id
		            								+"\", AccessToken=\""+OakClubBaseActivity.access_token
		            								+"\", Nonce=\"1ifn7s\", Created=\"2013-10-19T07:12:43.407Z\"";
		            			hget.setHeader(Constants.HEADER_X_WSSE,headerValue);
		            			hget.setHeader(Constants.HEADER_ACCEPT,"application/json");
		            			hget.setHeader(Constants.HEADER_ACCEPT,"text/html");
		            			hget.setHeader(Constants.HTTP_USER_AGENT, "Android");
		            			hget.setURI(new URI(getApplicationContext().getString(R.string.default_server_address) + "/pingActivities"));
		            			HttpResponse response = hClient.execute(hget);
		            			StatusLine statusLine = response.getStatusLine();

		            			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
		            				ByteArrayOutputStream out = new ByteArrayOutputStream();
		            				response.getEntity().writeTo(out);
		            				out.close();
		            			}
		            		} catch (ConnectTimeoutException e) {
		            		} catch (Exception e) {
		            		}
		                    sleep(60000);  
		                }
		            }  
		            catch (InterruptedException e) {  
		            e.printStackTrace();  
		            }   
		        }  
		};  
		SplashTimer.start();  
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them, 
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				//.denyCacheImageMultipleSizesInMemory()
				//.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.FIFO)
//				.enableLogging() // Not necessary in common
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
	@Override
	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = new RequestQueue();
		}
		return mRequestQueue;
	}
	
}