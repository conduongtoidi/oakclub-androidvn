package com.oakclub.android;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netcompss.loader.LoadJNI;
import com.oakclub.android.base.OakClubBaseActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.model.ListPhotoReturnDataItemObject;
import com.oakclub.android.model.UploadPhotoReturnObject;
import com.oakclub.android.model.UploadVideoObject;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;

public class DisableUserActivity extends OakClubBaseActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disable_user);
		init();
	}

	private TextView tvBack;
	private RelativeLayout rltChangeAva;
	private RelativeLayout rltUpdateFriends;
	private RelativeLayout rltUploadVideo;
	private RelativeLayout rltUploadPictures;
	private void init(){
		tvBack = (TextView)findViewById(R.id.activity_disable_user_tvBack);
		rltChangeAva = (RelativeLayout)findViewById(R.id.activity_disable_user_rltChangeAva);
		rltUpdateFriends = (RelativeLayout)findViewById(R.id.activity_disable_user_rltUpdatefriends);
		rltUploadVideo = (RelativeLayout)findViewById(R.id.activity_disable_user_rltUploadvideo);
		rltUploadPictures = (RelativeLayout)findViewById(R.id.activity_disable_user_rltUploadpictures);

		tvBack.setOnClickListener(listener);
		rltChangeAva.setOnClickListener(listener);
		rltUpdateFriends.setOnClickListener(listener);
		rltUploadVideo.setOnClickListener(listener);
		rltUploadPictures.setOnClickListener(listener);
	}
	
	private Intent intent;
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_disable_user_tvBack:
				finish();
				break;
			case R.id.activity_disable_user_rltChangeAva:
				intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        getResources().getString(R.string.txt_complete_action_photo)),
                        UPLOAD_AVATAR);
				break;
			case R.id.activity_disable_user_rltUpdatefriends:
				break;
			case R.id.activity_disable_user_rltUploadvideo:
				intent = new Intent();
	            intent.setType("video/*");
	            intent.setAction(Intent.ACTION_GET_CONTENT);
	            startActivityForResult(Intent.createChooser(intent,
	                    getResources().getString(R.string.txt_complete_action_photo)),
	                    UPLOAD_VIDEO); 
				break;
			case R.id.activity_disable_user_rltUploadpictures:
				intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Complete action using"), UPLOAD_PHOTO);
				break;
			default:
				break;
			}
			
		}
	};

    private Uri mImageCaptureUri;
    private static final int UPLOAD_AVATAR=0;
    private static final int UPLOAD_PHOTO=1;
    private static final int UPLOAD_VIDEO=2;
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mImageCaptureUri = data.getData();
            switch (requestCode) {
            case UPLOAD_AVATAR:
                doUploadPhoto(true);
                break;
            case UPLOAD_PHOTO:
                doUploadPhoto(false);
                break;
            case UPLOAD_VIDEO:
                doUploadVideo();
                break;
            }
        }
    }
	
    private static final int PHOTO_UPLOAD_MAX_SIZE = 3;
    private void doUploadPhoto(boolean isAvatar) {
        File file = null;
        String filePath = "";
        filePath = getPath(this, mImageCaptureUri);
        if(filePath!= null && !filePath.equals("")){
            file = new File(filePath);
            if (checkFileMax(file, PHOTO_UPLOAD_MAX_SIZE)) {
                showDialogWarning(getString(R.string.warning_profile_max_size_photo));
            }
            else{
                UploadProfilePhotoLoader loader = new UploadProfilePhotoLoader(
                        "uploadPhoto", this, isAvatar, file);
                getRequestQueue().addRequest(loader);
            }
        }
    }
    

    private static final int VIDEO_UPLOAD_MAX_SIZE = 3;
    private void doUploadVideo() {
        String filePath = "";
        filePath = getPath(this, mImageCaptureUri);
        if(filePath!= null && !filePath.equals("")){
            if (checkIfFileExistAndNotEmpty(filePath)) {
                new TranscdingBackground(this, filePath, OakClubUtil.getFileStore(this, "").getAbsolutePath()).execute();
            }
            else {
                showDialogWarning(getString(R.string.warning_not_found_video));
            }
        }
    }

    private boolean checkFileMax(File file, int maxSize){
        long fileSizeInMB = file.length()/1024/1024;
        if (fileSizeInMB > maxSize) 
            return true;
        return false;
    }
    
    private void showDialogWarning(String twWarning){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.dialog_warning_ok, null);
        dialog.setView(layout, 0, 0, 0, 0);
        Button btOk = (Button) layout.findViewById(R.id.dialog_internet_access_lltfooter_btOK);
        TextView tvWarning = (TextView)layout.findViewById(R.id.dialog_warning_tvQuestion);
        tvWarning.setText(twWarning);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private static boolean checkIfFileExistAndNotEmpty(String fullFileName) {
       File f = new File(fullFileName);
       long lengthInBytes = f.length();
       Log.d(Constants.TAG, fullFileName + " length in bytes: " + lengthInBytes);
       if (lengthInBytes > 100)
           return true;
       else {
           return false;
       }
   
   }

    @SuppressLint("NewApi")
    private String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection,
            String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public class TranscdingBackground extends AsyncTask<String, Integer, Integer>
    {
       ProgressDialog pdLoading;
       Activity _act;
       String inputFile = "";
       String outputFile = "";
       
       public TranscdingBackground (Activity act, String inputFile, String outputFile) {
           _act = act;
           this.inputFile = inputFile;
           this.outputFile = outputFile;
       }
   
       @Override
       protected void onPreExecute() {
           pdLoading=new ProgressDialog(_act);
           pdLoading.setMessage(_act.getString(R.string.txt_compressing_video));
           pdLoading.setCancelable(false);
           pdLoading.show();
       }

       protected Integer doInBackground(String... paths) {
           Log.i(Constants.TAG, "doInBackground started...");
           String[] complexCommand = {"ffmpeg","-y" ,
                   "-i", inputFile,
                   "-strict","experimental",
                   "-crf", "30",
                   "-preset", "ultrafast", 
                   "-acodec", "aac", 
                   "-ar", "44100", 
                   "-ac", "2", 
                   "-b:a", "96k", 
                   "-vcodec", "libx264", 
                   "-r", "25", 
                   "-b:v", "500k", 
                   "-s", "800x480",
                   "-f", "flv", outputFile + "/video.flv"};
           
           LoadJNI vk = new LoadJNI();
           
           try {
               vk.run(complexCommand, outputFile, DisableUserActivity.this);
             
           } catch (Throwable e) {
               Log.i(Constants.TAG, "vk run exeption.");
           }
           Log.i(Constants.TAG, "doInBackground finished");
           return Integer.valueOf(0);
       }

       protected void onProgressUpdate(Integer... progress) {

       }

       @Override
       protected void onCancelled() {
           Log.i(Constants.TAG, "onCancelled");
           //progressDialog.dismiss();
           super.onCancelled();
       }


       @Override
       protected void onPostExecute(Integer result) {
           Log.i(Constants.TAG, "onPostExecute");
           pdLoading.dismiss();
           File file = new File(outputFile + "/video.flv");

           if (checkFileMax(file, VIDEO_UPLOAD_MAX_SIZE)) {
               showDialogWarning(getString(R.string.warning_profile_max_size_video));
           }
           else{
               UploadProfileVideoLoader loader = new UploadProfileVideoLoader(
                       "uploadVideo", DisableUserActivity.this, file);
               getRequestQueue().addRequest(loader);
           }
           super.onPostExecute(result);

       }
    }
    class UploadProfilePhotoLoader extends RequestUI {
        boolean is_Avatar;
        UploadPhotoReturnObject obj;
        File file;
        ProgressDialog pdLoading;

        public UploadProfilePhotoLoader(Object key, Activity activity,
                boolean is_Avatar, File file) {
            super(key, activity);
            this.is_Avatar = is_Avatar;
            this.file = file;
            pdLoading = new ProgressDialog(activity);
            pdLoading.setMessage(activity.getString(
                    R.string.txt_upload_photo));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        public void execute() throws Exception {
            obj = oakClubApi.UploadProfilePhoto(is_Avatar, file);
        }

        @Override
        public void executeUI(Exception ex) {
            if (pdLoading != null && pdLoading.isShowing()) {
                pdLoading.dismiss();
            }
            if (obj != null && obj.isStatus()) {
                
            } 
            else {
                OakClubUtil.enableDialogWarning(DisableUserActivity.this,
                        getResources().getString(R.string.txt_warning),
                        getResources().getString(R.string.value_upload_failed));
            }
        }
    }
    
    class UploadProfileVideoLoader extends RequestUI {
        UploadVideoObject obj;
        File file;
        ProgressDialog pdLoading;

        public UploadProfileVideoLoader(Object key, Activity activity, File file) {
            super(key, activity);
            this.file = file;
            pdLoading = new ProgressDialog(activity);
            pdLoading.setMessage(activity.getString(
                    R.string.txt_upload_video));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        public void execute() throws Exception {
            obj = oakClubApi.UploadProfileVideo(file);
        }

        @Override
        public void executeUI(Exception ex) {
            if (pdLoading != null && pdLoading.isShowing()) {
                pdLoading.dismiss();
            }
            if (obj != null && obj.isStatus()) {
            	
            } else {
                OakClubUtil.enableDialogWarning(DisableUserActivity.this,
                        getResources().getString(R.string.txt_warning),
                        getResources().getString(R.string.value_upload_failed));
            }
        }

    }
}
