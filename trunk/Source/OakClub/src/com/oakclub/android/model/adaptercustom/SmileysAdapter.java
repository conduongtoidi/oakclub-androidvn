package com.oakclub.android.model.adaptercustom;

import java.util.ArrayList;
import java.util.HashMap;

import com.oakclub.android.ChatActivity;
import com.oakclub.android.R;
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
    
    public SmileysAdapter(ArrayList<String> arraylistSmileys, Context context, HashMap<String, String> emoticons, String urlLink, boolean isSmile) {
        this.arrayListSmileys = arraylistSmileys;
        this.context = context;
        this.emoticons = emoticons;
        this.urlLink = urlLink;
        this.isSmile = isSmile;
        widthScreen = (int) OakClubUtil.getWidthScreen(context);
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayListSmileys.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arrayListSmileys.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
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
            ImageView imageView = (ImageView) view.findViewById(R.id.item_chat_smiley_ivSmile);
            if (!ChatActivity.bitmapSticker.containsKey(arrayListSmileys.get(position))) {
            	String url = OakClubUtil.getFullLinkStickerOrGift(context,
                        urlLink + emoticons.get(arrayListSmileys.get(position)));
                OakClubUtil.loadStickerFromUrl(context, url, imageView, arrayListSmileys.get(position));
            } else {
            	imageView.setImageBitmap(ChatActivity.bitmapSticker.get(arrayListSmileys.get(position)));
            }
            
            imageView.setLayoutParams(params);
        }
        
        return view;
    }

}
