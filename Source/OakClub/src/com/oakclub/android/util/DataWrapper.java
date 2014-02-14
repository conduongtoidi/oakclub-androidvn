package com.oakclub.android.util;

import java.io.Serializable;
import java.util.ArrayList;

import com.oakclub.android.model.KindOfPhotoObject;
import com.oakclub.android.model.ListPhotoReturnDataItemObject;
import com.oakclub.android.model.SettingObject;

import android.util.Log;

public class DataWrapper implements Serializable {
	private static final long serialVersionUID = 2310640779687082782L;
	private ArrayList<ListPhotoReturnDataItemObject> dataObject;
	
	public DataWrapper(ArrayList<ListPhotoReturnDataItemObject> b) {
		if(b==null) Log.e("DataWrapper", "Error:Object is null");
		setDataObject(b);
	}
	public ArrayList<ListPhotoReturnDataItemObject> getDataObject() {
		return dataObject;
	}
	public void setDataObject(ArrayList<ListPhotoReturnDataItemObject> dataObject) {
		this.dataObject = dataObject;
	}

	
}
