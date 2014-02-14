package com.oakclub.android.model.adaptercustom;

import java.util.ArrayList;
import com.oakclub.android.model.SnapShotInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

public class AdapterSnapShot extends ArrayAdapter<SnapShotInfo>{

	private ArrayList<SnapShotInfo> listData;
	private Context context;

	public AdapterSnapShot(Context context,
			int textViewResourceId, ArrayList<SnapShotInfo> objects) {
		super(context, textViewResourceId, objects);
		
		this.context = context;
		listData = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		TextView tvwName;
		if(convertView==null){
			tvwName = new TextView(context);
		}else tvwName = (TextView)convertView;
		SnapShotInfo item = listData.get(position);
		tvwName.setText(item.first_name);
		
		return tvwName;
	}
	
	

}
