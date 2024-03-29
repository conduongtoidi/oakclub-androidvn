package com.oakclub.android.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

public class SmartImageTask implements Runnable {
    private static final int BITMAP_READY = 0;

    private boolean cancelled = false;
    private OnCompleteHandler onCompleteHandler;
    private SmartImage image;
    private Context context;
    private String folder;

    public static class OnCompleteHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap)msg.obj;
            onComplete(bitmap);
        }

        public void onComplete(Bitmap bitmap){};
    }

    public abstract static class OnCompleteListener {
        public abstract void onComplete();
    }

    public SmartImageTask(Context context, String folder, SmartImage image) {
        this.image = image;
        this.context = context;
        this.folder = folder;
        
        cancelled = false;
    }

    @Override
    public void run() {
        if(image != null) {
        	try {
	            complete(image.getBitmap(context, folder));
	            context = null;
        	} catch (Exception ex) {
        		
        	}
        }
    }

    public void setOnCompleteHandler(OnCompleteHandler handler){
        this.onCompleteHandler = handler;
    }

    public void cancel() {
        cancelled = true;
    }

    public void complete(Bitmap bitmap){
        if(onCompleteHandler != null && !cancelled) {
            onCompleteHandler.sendMessage(onCompleteHandler.obtainMessage(BITMAP_READY, bitmap));
        }
    }
}