package com.oakclub.android.view;

import java.util.ArrayList;

import com.oakclub.android.InfoProfileOtherActivity;
import com.oakclub.android.R;
import com.oakclub.android.model.FacebookInfoObject;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ViewPagerCustom extends HorizontalScrollView{
	private ArrayList<FacebookInfoObject> listFacebookObject;
	private LinearLayout linear;
	public ViewPagerCustom(Context context, AttributeSet attrs) {
		super(context, attrs);
		//init();
	}
	public ViewPagerCustom(Context context, AttributeSet attrs,ArrayList<FacebookInfoObject> list) {
		super(context, attrs);
		listFacebookObject = list;
		//init();
	}
	public void setListForView(ArrayList<FacebookInfoObject> list){
		this.listFacebookObject = list;
	}
	public void init(){
		linear = new LinearLayout(this.getContext());
		LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layout.leftMargin = 4;
		layout.topMargin = 2;
		layout.bottomMargin = 2;
		linear.setLayoutParams(layout);
		this.addView(linear);
	}
	
	public void assignInfo(){
		LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for(int i = 0 ; i< listFacebookObject.size();i++){
			RelativeLayout view = null;
			
			view = (RelativeLayout) inflater.inflate(R.layout.item_fbobject_profile, null);
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			//layout.weight = 1;
			view.setLayoutParams(layout);
			CircleImageView imgAvatar = (CircleImageView) view.findViewById(R.id.rltFacebookObjectProfile_fltImageParent_imgPicture);
            TextView tvName = (TextView)  view.findViewById(R.id.rltFacebookObjectProfile_tvName);
            tvName.setGravity(Gravity.CENTER);
            String url = listFacebookObject.get(i).getAvatar();
            String name = listFacebookObject.get(i).getName();
            name = OakClubUtil.getFirstName(name);
            OakClubUtil.loadImageFromUrl(ViewPagerCustom.this.getContext(),url, imgAvatar, Constants.SNAPSHOT_FOLDER);
            tvName.setText(name);
            linear.addView(view);
		}
		invalidate();
	}
	
	class ImageAdapter extends BaseAdapter{
		private ArrayList<FacebookInfoObject> listItem;
		public ImageAdapter(ArrayList<FacebookInfoObject> list) {
			this.listItem = list;
		}
		@Override
		public int getCount() {
			return listItem.size();
		}

		@Override
		public Object getItem(int arg0) {
			return listItem.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			RelativeLayout view = null;
			if(convertView==null){
				LayoutInflater inflater = (LayoutInflater) ViewPagerCustom.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_fbobject_profile, null);
			};
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			layout.weight = 1;
			view = (RelativeLayout)convertView;
			view.setLayoutParams(layout);
			CircleImageView imgAvatar = (CircleImageView) view.findViewById(R.id.rltFacebookObjectProfile_fltImageParent_imgPicture);
            TextView tvName = (TextView)  view.findViewById(R.id.rltFacebookObjectProfile_tvName);
            String url = listItem.get(position).getAvatar();
            String name = listItem.get(position).getName();
            name = OakClubUtil.getFirstName(name);
            OakClubUtil.loadImageFromUrl(ViewPagerCustom.this.getContext(),url, imgAvatar, Constants.SNAPSHOT_FOLDER);
            tvName.setText(name);
			return view;
		}
		
	}
}
