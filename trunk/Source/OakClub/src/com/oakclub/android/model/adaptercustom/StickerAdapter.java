package com.oakclub.android.model.adaptercustom;

import java.util.ArrayList;
import java.util.HashMap;

import com.oakclub.android.ChatActivity;
import com.oakclub.android.R;
import com.oakclub.android.util.OakClubUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class StickerAdapter extends BaseAdapter {

    private ArrayList<String> arrayListStickers = new ArrayList<String>();
    private Context context;
    private HashMap<String, String> stickers = new HashMap<String, String>();
    private ArrayList<String> showListStickers = new ArrayList<String>();
    private int imageWidth = 0;
    private int widthScreen = 0;
    
    public StickerAdapter(ArrayList<String> arraylistSmileys, Context context, HashMap<String, String> emoticons) {
        this.arrayListStickers = arraylistSmileys;
        this.context = context;
        this.stickers = emoticons;
        widthScreen = (int) OakClubUtil.getWidthScreen(context);
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayListStickers.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arrayListStickers.get(position);
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
        
        LayoutParams params = new LayoutParams(widthScreen/3, widthScreen/3);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_chat_smiley_ivSmile);
        if (!ChatActivity.bitmapSticker.containsKey(arrayListStickers.get(position))) {
        	String url = OakClubUtil.getFullLinkSticker(context,
                    stickers.get(arrayListStickers.get(position)));
            OakClubUtil.loadStickerFromUrl(context, url, imageView, arrayListStickers.get(position));
        } else {
        	imageView.setImageBitmap(ChatActivity.bitmapSticker.get(arrayListStickers.get(position)));
        }
        
        imageView.setLayoutParams(params);
        
        return view;
    }

}
