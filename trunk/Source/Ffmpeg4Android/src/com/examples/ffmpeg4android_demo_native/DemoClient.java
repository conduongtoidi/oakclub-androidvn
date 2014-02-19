package com.examples.ffmpeg4android_demo_native;

import java.io.File;

import com.netcompss.loader.LoadJNI;
import android.app.Activity;
import android.app.ProgressDialog;

import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 *  To run this Demo Make sure you have on your device this folder:
 *  /sdcard/videokit, 
 *  and you have in this folder a video file called in.mp4
 * @author elih
 *
 */
public class DemoClient extends Activity {
	
	final String demoVideoPath = "/sdcard/videokit/in.mp4";
	
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      

	      setContentView(R.layout.ffmpeg_demo_client);
	      
	      
	      
	      Button invoke =  (Button)findViewById(R.id.invokeButton);
	      invoke.setOnClickListener(new OnClickListener() {
				public void onClick(View v){
					Log.i(Prefs.TAG, "run clicked.");
					if (checkIfFileExistAndNotEmpty(demoVideoPath)) {
						new TranscdingBackground(DemoClient.this).execute();
					}
					else {
						Toast.makeText(getApplicationContext(), demoVideoPath + " not found", Toast.LENGTH_LONG).show();
					}
				}
			});
	      
	      
    
	}
	
	 public class TranscdingBackground extends AsyncTask<String, Integer, Integer>
	 {
	 	
	 	ProgressDialog progressDialog;
	 	Activity _act;
	 	
	 	public TranscdingBackground (Activity act) {
	 		_act = act;
	 	}
	
	 	
	 	
	 	@Override
	 	protected void onPreExecute() {
	 		progressDialog = new ProgressDialog(_act);
	 		progressDialog.setMessage("FFmpeg4Android Transcoding in progress...");
	 		progressDialog.show();
	 		
	 	}

	 	protected Integer doInBackground(String... paths) {
	 		Log.i(Prefs.TAG, "doInBackground started...");
	 		
	 		//String[] complexCommand = {"ffmpeg","-threads", "0", "-y" ,"-i", "/sdcard/videokit/in.mp4","-strict","experimental", "-vf", "crop=iw/2:ih:0:0,split[tmp],pad=2*iw[left]; [tmp]hflip[right]; [left][right] overlay=W/2", "-vcodec", "mpeg4", "-vb", "20M", "-r", "23.956", "/sdcard/videokit/out.mp4"};
	 		//String commandStr = "ffmpeg -y -i /sdcard/videokit/in.mp4 -strict experimental -s 320x240 -r 30 -aspect 3:4 -ab 48000 -ac 2 -ar 22050 -vcodec mpeg4 -b 2097152 /sdcard/videokit/out.mp4";
	 		
	 		String commandStr = getString(R.string.commandText);
	 		
	 		LoadJNI vk = new LoadJNI();
	 		
			try {
				//vk.run(complexCommand, "/sdcard/videokit", getApplicationContext());
				vk.run(utilConvertToComplex(commandStr), "/sdcard/videokit", getApplicationContext());
			} catch (Throwable e) {
				Log.i(Prefs.TAG, "vk run exeption.");
			}
			Log.i(Prefs.TAG, "doInBackground finished");
	 		return Integer.valueOf(0);
	 	}

	 	protected void onProgressUpdate(Integer... progress) {

	 	}

	 	@Override
	 	protected void onCancelled() {
	 		Log.i(Prefs.TAG, "onCancelled");
	 		//progressDialog.dismiss();
	 		super.onCancelled();
	 	}


	 	@Override
	 	protected void onPostExecute(Integer result) {
	 		Log.i(Prefs.TAG, "onPostExecute");
	 		progressDialog.dismiss();
	 		super.onPostExecute(result);

	 	}
	 }
	 
	 private String[] utilConvertToComplex(String str) {
		 String[] complex = str.split(" ");
		 return complex;
	 }
	 
	 private static boolean checkIfFileExistAndNotEmpty(String fullFileName) {
			File f = new File(fullFileName);
			long lengthInBytes = f.length();
			Log.d(Prefs.TAG, fullFileName + " length in bytes: " + lengthInBytes);
			if (lengthInBytes > 100)
				return true;
			else {
				return false;
			}
		
		}

}
