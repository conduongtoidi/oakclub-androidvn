package com.oakclub.android.util;

import java.io.File;

import android.content.Context;

public class FileCache {
    private File cacheDir;
    
    public FileCache(Context context, String folder){
        //Find the dir to save cached images
//        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
//            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(), Constants.TAG);
//        else
//            cacheDir=context.getCacheDir();
//        if(!cacheDir.exists())
//            cacheDir.mkdirs();
        cacheDir = OakClubUtil.getFileStore(context, folder);
    }
  
    public File getFile(String url){
        String filename=String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;
  
    }
  
    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }
}
