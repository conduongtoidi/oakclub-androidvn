package com.oakclub.android.model.adaptercustom;

import java.util.ArrayList;
import java.util.HashMap;

import com.oakclub.android.ChatActivity;
import com.oakclub.android.R;
import com.oakclub.android.image.SmartImageView;
import com.oakclub.android.model.Groups;
import com.oakclub.android.util.OakClubUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class SmileysAdapter extends BaseAdapter {

    private ArrayList<String> arrayListSmileys = new ArrayList<String>();
    private Context context;
    private HashMap<String, String> emoticons = new HashMap<String, String>();
    private ArrayList<String> showListSmileys = new ArrayList<String>();
    private int imageWidth = 0;
    private int widthScreen = 0;
    private String urlLink;
    private boolean isSmile;
    private Groups group;
    
    public SmileysAdapter(ArrayList<String> arraylistSmileys, Context context, HashMap<String, String> emoticons, String urlLink, boolean isSmile) {
        this.arrayListSmileys = arraylistSmileys;
        this.context = context;
        this.emoticons = emoticons;
        this.urlLink = urlLink;
        this.isSmile = isSmile;
        widthScreen = (int) OakClubUtil.getWidthScreen(context);
    }
    
    public SmileysAdapter(Groups group, Context context, String urlLink, boolean isSmile) {
        this.group = group;
        this.context = context;
        this.urlLink = urlLink;
        this.isSmile = isSmile;
        widthScreen = (int) OakClubUtil.getWidthScreen(context);
    }
    
    @Override
    public int getCount() {
        if (isSmile)
        	return arrayListSmileys.size();
        if (group.getList() != null) 
        	return group.getList().size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
    	if (isSmile)
    		return arrayListSmileys.get(position);
    	return group.getList().get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        final LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_chat_smiley, parent, false);
        //convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_smiley, null);
        if (isSmile) {
	        LayoutParams params = new LayoutParams(widthScreen/8, widthScreen/8);
	        ImageView imageView = (ImageView) view.findViewById(R.id.item_chat_smiley_ivSmile);
	        try{
	        	imageView.setBackgroundResource(Integer.parseInt(emoticons.get(arrayListSmileys.get(position))));
	        } catch(Exception ex) {
	        	
	        }
	        imageView.setLayoutParams(params);
        } else {
        	LayoutParams params = new LayoutParams(widthScreen/4, widthScreen/4);
            SmartImageView smartImageView = (SmartImageView) view.findViewById(R.id.item_chat_smiley_ivSmile);
            String url = "";
            if (group.getList() != null && group.getList().get(position).getUrl() != null) {
            	url = OakClubUtil.getFullLinkStickerOrGift(context, group.getList().get(position).getUrl() + group.getList().get(position).getImage());
            } else {
            	url = OakClubUtil.getFullLinkStickerOrGift(context, group.getUrl() + group.getList().get(position).getImage());
            }
            OakClubUtil.loadImageFromUrl(context, url, smartImageView, "Sticker " + group.getName());
            
            smartImageView.setLayoutParams(params);
        }
        
        return view;
    }

}
