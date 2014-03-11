package com.oakclub.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.oakclub.android.model.GetConfigData;
import com.oakclub.android.model.adaptercustom.StickerAdapter;
import com.oakclub.android.net.IOakClubApi;
import com.oakclub.android.net.OakClubApi;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

public class StickerActivity extends Activity {
	
	protected GridView gvSticker;
	private LinearLayout llt;
	public static HashMap<String, String> stickers = new HashMap<String, String>();
	
    public static ArrayList<String> arrayListSticker = new ArrayList<String>();
    public static ChatActivity chat;
    float lastX;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_smile);
		
		gvSticker = (GridView) findViewById(R.id.activity_chat_rtlbottom_gvSmile);
		llt = (LinearLayout) findViewById(R.id.activity_smile_llt);
		gvSticker.setNumColumns(3);
		gvSticker.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                    int position, long arg3) {
                String value = gvSticker.getAdapter().getItem(position).toString();
                String keyEntry = value;
                String path2 = "<img src=\"/bundles/likevnblissdate/v3/chat/images/stickers/" + value + ".png\" width=\"125\" height=\"125\" type=\"sticker\"/>";
                chat.solveSendMessage(path2);
                
                Iterator<Entry<String, String>> iterator = stickers.entrySet()
        				.iterator();
                String valueEntry = "";
        		while (iterator.hasNext()) {
        			Entry<String, String> entry = iterator.next();
        			if (entry.getKey().equals(keyEntry)) {
        				valueEntry = entry.getValue();
        			}
        		}
    			if (OftenStickerActivity.oftenStickers != null && !OftenStickerActivity.oftenStickers.containsKey(keyEntry)) {
    				OftenStickerActivity.oftenStickers.put(keyEntry, valueEntry);
    			}
            }
        });
		
		addStickerToStickers();
	}
	
	private void addItemGridView() {
		StickerAdapter adapter = new StickerAdapter(arrayListSticker,
				StickerActivity.this, stickers);
		gvSticker.setAdapter(adapter);
	}

    private void addStickerToStickers() {
    	fillArrayList();
		addItemGridView();
	}
    
	private void fillArrayList() {
		Iterator<Entry<String, String>> iterator = stickers.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			arrayListSticker.add(entry.getKey());
		}
	}
	
	
}
