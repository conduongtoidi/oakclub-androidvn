package com.oakclub.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oakclub.android.R;
import com.oakclub.android.util.OakClubUtil;

public class CircleImageWithRing extends FrameLayout {
	String url;
	Context context;
	CircleImageView imageView;
	LinearLayout imageRing;
	int resourceId ;
	public CircleImageWithRing(Context context ) {
		super(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		View rootView = inflater.inflate(R.layout.circle_image_with_ring, this);
		imageView = (CircleImageView)findViewById(R.id.circle_image);
		imageRing = (LinearLayout)findViewById(R.id.circle_ring);
	}

	public CircleImageWithRing(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);
		View rootView = inflater.inflate(R.layout.circle_image_with_ring, this);
		imageView = (CircleImageView)findViewById(R.id.circle_image);
	}
	public void setImageUrl(String url){
		this.url = url;
		OakClubUtil.loadImageFromUrl(context, url, imageView);
	}
	
	public void setImageBitmap(int id){
		this.resourceId = id;
		imageView.setImageResource(id);
	}
	public void setRingBitmap(int id){
		this.resourceId = id;
		imageRing.setBackgroundResource(id);
	}

}
