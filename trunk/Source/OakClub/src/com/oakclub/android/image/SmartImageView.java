package com.oakclub.android.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.oakclub.android.util.OakClubUtil;

public class SmartImageView extends ImageView {
    private static final int LOADING_THREADS = 4;
    private ExecutorService threadPool;

//    private SmartImageTask currentTask;


    public SmartImageView(Context context) {
        super(context);
        threadPool = Executors.newSingleThreadExecutor();// Executors.newFixedThreadPool(LOADING_THREADS);
    }

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        threadPool = Executors.newSingleThreadExecutor();// Executors.newFixedThreadPool(LOADING_THREADS);
    }

    public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        threadPool = Executors.newSingleThreadExecutor();// Executors.newFixedThreadPool(LOADING_THREADS);
    }


    // Helpers to set image by URL
    public void setImageUrl(String url, String folder) {
        setImage(new WebImage(url), folder);
    }

    public void setImageUrl(String url, String folder, SmartImageTask.OnCompleteListener completeListener) {
        setImage(new WebImage(url), folder, completeListener);
    }

    public void setImageUrl(String url, String folder, final Integer fallbackResource) {
        setImage(new WebImage(url), folder, fallbackResource);
    }

    public void setImageUrl(String url, String folder, final Integer fallbackResource, SmartImageTask.OnCompleteListener completeListener) {
        setImage(new WebImage(url), folder, fallbackResource, completeListener);
    }

    public void setImageUrl(String url, String folder, final Integer fallbackResource, final Integer loadingResource) {
        setImage(new WebImage(url), folder, fallbackResource, loadingResource);
    }

    public void setImageUrl(String url, String folder, final Integer fallbackResource, final Integer loadingResource, SmartImageTask.OnCompleteListener completeListener) {
        setImage(new WebImage(url), folder, fallbackResource, loadingResource, completeListener);
    }


    // Helpers to set image by contact address book id
    public void setImageContact(long contactId, String folder) {
        setImage(new ContactImage(contactId), folder);
    }

    public void setImageContact(long contactId, String folder, final Integer fallbackResource) {
        setImage(new ContactImage(contactId), folder, fallbackResource);
    }

    public void setImageContact(long contactId, String folder, final Integer fallbackResource, final Integer loadingResource) {
        setImage(new ContactImage(contactId), folder, fallbackResource, fallbackResource);
    }


    // Set image using SmartImage object
    public void setImage(final SmartImage image, String folder) {
        setImage(image, folder, null, null, null);
    }

    public void setImage(final SmartImage image, String folder, final SmartImageTask.OnCompleteListener completeListener) {
        setImage(image, folder, null, null, completeListener);
    }

    public void setImage(final SmartImage image, String folder, final Integer fallbackResource) {
        setImage(image, folder, fallbackResource, fallbackResource, null);
    }

    public void setImage(final SmartImage image, String folder, final Integer fallbackResource, SmartImageTask.OnCompleteListener completeListener) {
        setImage(image, folder, fallbackResource, fallbackResource, completeListener);
    }

    public void setImage(final SmartImage image, String folder, final Integer fallbackResource, final Integer loadingResource) {
        setImage(image, folder, fallbackResource, loadingResource, null);
    }

    public void setImage(final SmartImage image, String folder, final Integer fallbackResource, final Integer loadingResource, final SmartImageTask.OnCompleteListener completeListener) {
        // Set a loading resource
        if(loadingResource != null){
            setImageResource(loadingResource);
        }
        SmartImageTask currentTask = null;
        // Cancel any existing tasks for this image view
//        if(currentTask != null) {
//            currentTask.cancel();
//            currentTask = null;
//        }

        // Set up the new task
        currentTask = new SmartImageTask(getContext(), folder, image);
        currentTask.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if(bitmap != null) {
                	//Bitmap scaledbmp = Bitmap.createScaledBitmap(bitmap, 75, 75, false);
                    setImageBitmap(bitmap);
                } else {
                    // Set fallback resource
                    if(fallbackResource != null) {
                        setImageResource(fallbackResource);
                    }
                }

                if(completeListener != null){
                    completeListener.onComplete();
                }
            }
        });

        // Run the task in a threadpool
        threadPool.execute(currentTask);
    }
    
    @SuppressWarnings("deprecation")
	@Override
    protected void onDetachedFromWindow () {
        setImageDrawable(null);
        setBackgroundDrawable(null);
        setImageBitmap(null);
        //System.gc();
        super.onDetachedFromWindow();
    }

    public static void cancelAllTasks() {
        //threadPool.shutdownNow();
        //threadPool = null;
    }
}