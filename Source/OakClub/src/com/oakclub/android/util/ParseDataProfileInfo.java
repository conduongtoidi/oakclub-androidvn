package com.oakclub.android.util;

import java.util.HashMap;
import android.content.Context;

import com.oakclub.android.R;
import com.oakclub.android.model.SnapshotData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.facebook.*;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.model.GraphUser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParseDataProfileInfo {
	public static HashMap<String, String> getDataOtherProfile(Context context, SnapshotData obj){
		if(obj==null) return null;
		HashMap<String, String> info = new HashMap<String, String>();
		float distance = obj.getDistance();
		String distanceString = "";
		 
		//if (distance>40) distance=40.0f;
		if(distance < 1) distanceString = context.getString(R.string.value_distance_lessthan1);
		else distanceString = String.format(context.getString(R.string.value_distance), distance);//distance+ " km away";
		info.put("distance", distanceString);
		
		int active = obj.getActive();
		String activeString = "";
		if(active == -1) activeString = context.getString(R.string.value_active_online);
		else if(active == 0) activeString = context.getString(R.string.value_active_justnow);
		else if(active<5) activeString =  String.format(context.getString(R.string.value_active_dayago), active);
		else activeString = context.getString(R.string.value_active_morethan5daysago);
		info.put("active", activeString);
		
		return info;
	}
	public static String getInterested(Context context ,int k){
		switch (k) {
		case 0:
			return context.getString(R.string.txt_male);

		case 1:
			return context.getString(R.string.txt_female);
		case 2:
			return context.getString(R.string.txt_both);
		}
		return "";
	}

}
