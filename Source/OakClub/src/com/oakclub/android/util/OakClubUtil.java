package com.oakclub.android.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.oakclub.android.R;
import com.oakclub.android.view.CircleImageView;

public class OakClubUtil {
	public static void loadImageFromUrl(final Context context,
			final ImageView image, final String imageUrl) {
	    int loader = R.drawable.logo_splashscreen;
	    com.oakclub.android.view.ImageLoader imgLoader = new com.oakclub.android.view.ImageLoader(context);
        imgLoader.DisplayImage(imageUrl, loader, image);
	}

	public static void loadImageFromUrl(final Context context,
			final String imageUrl, final ImageView imageView) {
		Constants.imageLoader.displayImage(imageUrl, imageView,
				new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {

						final ImageView imageView = (ImageView) arg1;
						imageView.setImageBitmap(null);
						imageView
								.setBackgroundResource(R.drawable.logo_splashscreen);
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						imageView
								.setBackgroundResource(R.drawable.logo_splashscreen);
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
//                        imageView
//                        .setBackgroundResource(R.drawable.logo_splashscreen);
//                        imageView
//                        .setBackgroundColor(Color.WHITE);
						imageView.setImageBitmap(arg2);
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
					}
				});
	}

	public static void clearCacheWithUrl(String imageUrl,
			ImageLoader imageLoader) {
		if (MemoryCacheUtil.findCachedBitmapsForImageUri(imageUrl,
				imageLoader.getMemoryCache()).size() > 0)
			MemoryCacheUtil.removeFromCache(imageUrl,
					imageLoader.getMemoryCache());
	}

	public static Bitmap loadBitmap(String URL, BitmapFactory.Options options) {
		Bitmap bitmap = null;
		InputStream in = null;
		try {
			in = OpenHttpConnection(URL);
			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();
		} catch (IOException e1) {
		}
		return bitmap;
	}
	public static String getFirstName(String s){
		String[] k  = s.trim().split(" ");
		String t = "";
		if(k.length>1) 
		    t = k[0];
		else t = s;
		if(t.length()>Constants.LENGTH_NAME)
		    t = t.substring(0, 7)+"...";
		return t;
	}

	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	public static String getFullLink(Context c, String url) {
		String t = url;
		if (!url.contains("http")) {
			t = c.getString(R.string.server_address_image) + "?file=" + url
					+ "&width=" + 250 + "&height=" + 250 + "&mode=" + 1;
		} else {
			String[] str = t.split("picture");
			if (str.length == 1) {
				t = t + "?width=" + 250 + "&height=" + 250;
			}
		}
		return t;
	}

	public static String getFullLink(Context c, String url, int w, int h,
			int mode) {
		String t = url;
		if (!url.contains("http")) {
			t = c.getResources().getString(R.string.server_address_image)
					+ "?file=" + url + "&width=" + w + "&height=" + h
					+ "&mode=" + mode;
		} else {
			String[] str = t.split("picture");
			if (str.length == 1) {
				t = t + "?width=" + w + "&height=" + h;
			}
		}
		return t;
	}

    public static String getFullLinkVideo(Context c, String url, String type) {
        String t = url;
        if (!url.contains("http")) {
            t = c.getResources().getString(R.string.server_address_video)
                    +"/" +  url + type;
        } else  t = t + type;
        return t;
    }


	private static InputStream OpenHttpConnection(String strURL)
			throws IOException {
		InputStream inputStream = null;
		URL url = new URL(strURL);
		URLConnection conn = url.openConnection();

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();
			}
		} catch (Exception ex) {
		}
		return inputStream;
	}

	 public static long convertStringDateToLong(String dateTime){
	        try {
	            DateFormat f = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	            Date d = f.parse(dateTime);
	            long milliseconds = d.getTime();
	            return milliseconds;
	        } catch (Exception e) {
	            return 0;
	        }

	 }
	 
	 
	 public static void enableDialogWarning(final Context context, String title, final String message)
	 {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater
                .from(context);
        View layout = inflater.inflate(R.layout.dialog_warning_ok,
                null);
        dialog.setView(layout, 0, 0, 0, 0);
        TextView tvTitle = (TextView)layout.findViewById(R.id.dialog_warning_lltheader_tvTitle);
        tvTitle.setText(title);
        TextView tvQuestion = (TextView)layout.findViewById(R.id.dialog_warning_tvQuestion);
        tvQuestion.setText(message);
        Button btnOK = (Button) layout
                .findViewById(R.id.dialog_internet_access_lltfooter_btOK);
        btnOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String internetFailed = context.getString(R.string.txt_internet_message);
                if(!internetFailed.equals(message))
                    dialog.cancel();
                else{
                    ((Activity) context).finish();
                    System.exit(0);
                }
            }
        });
        dialog.setCancelable(false);
        dialog.show();
	 }
	

	public static float getWidthScreen(Context context) {
		float widthScreen;
		DisplayMetrics displayMetrics = new DisplayMetrics();
		displayMetrics = context.getResources().getDisplayMetrics();
		widthScreen = displayMetrics.widthPixels;
		return widthScreen;
	}

	public static float getHeightScreen(Context context) {
		float heightScreen;
		DisplayMetrics displayMetrics = new DisplayMetrics();
		displayMetrics = context.getResources().getDisplayMetrics();
		heightScreen = displayMetrics.heightPixels;
		return heightScreen;
	}
	
	public static void trimCache(Context context) {
	    try {
	       File dir = context.getCacheDir();
	       if (dir != null && dir.isDirectory()) {
	          deleteDir(dir);
	       }
	    } catch (Exception e) {
	       // TODO: handle exception
	    }
	 }

	public static boolean deleteDir(File dir) {
	    if (dir != null && dir.isDirectory()) {
	       String[] children = dir.list();
	       for (int i = 0; i < children.length; i++) {
	          boolean success = deleteDir(new File(dir, children[i]));
	          if (!success) {
	             return false;
	          }
	       }
	    }
	    return dir.delete();
	}
	
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
	
	public static boolean isForeground(String myPackage, Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager
				.getRunningTasks(1);

		ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
		if (componentInfo.getPackageName().equals(myPackage)) {
			return false;
		}
		return true;
	}
	
	public static void recycleImagesFromView(View view) {
        if(view instanceof ImageView)
        {
            Drawable drawable = ((ImageView)view).getDrawable();
            if(drawable instanceof BitmapDrawable)
            {
                BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
                bitmapDrawable.getBitmap().recycle();
                System.gc();
            }
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                recycleImagesFromView(((ViewGroup) view).getChildAt(i));
            }
        }
    }
	
	public static void releaseImagePager(ViewPager pager){
        if(pager!=null && pager.getChildCount()>0){
            for(int i = 0; i<pager.getChildCount(); i++){
                recycleImagesFromView(pager.getChildAt(i));
            }
        }
    }
	
	public static File getFileStore(Context context){
	    File file;
	    if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
	        file=new File(android.os.Environment.getExternalStorageDirectory(), Constants.TAG);
        else
            file=context.getCacheDir();
        if(!file.exists())
            file.mkdirs();
	    return file;
	} 
}
