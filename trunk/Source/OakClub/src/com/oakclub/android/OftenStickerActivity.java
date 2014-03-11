package com.oakclub.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.oakclub.android.model.adaptercustom.StickerAdapter;
import com.oakclub.android.net.IOakClubApi;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class OftenStickerActivity extends Activity {
	
	protected GridView gvSticker;
	public static HashMap<String, String> oftenStickers = new HashMap<String, String>();
	
    private ArrayList<String> arrayListOftenSticker = new ArrayList<String>();
    public static ChatActivity chat;
    float lastX;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_smile);
		
		gvSticker = (GridView) findViewById(R.id.activity_chat_rtlbottom_gvSmile);
		gvSticker.setNumColumns(3);
		gvSticker.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                    int position, long arg3) {
                String value = gvSticker.getAdapter().getItem(position).toString();
                String path2 = "<img src=\"/bundles/likevnblissdate/v3/chat/images/stickers/" + value + ".png\" width=\"125\" height=\"125\" type=\"sticker\"/>";
                chat.solveSendMessage(path2);
            }
        });
		
		addStickerToStickers();
	}
	
	public static StickerAdapter adapterOftenSticker;
	private void addItemGridView() {
		adapterOftenSticker = new StickerAdapter(arrayListOftenSticker,
				OftenStickerActivity.this, oftenStickers);
		gvSticker.setAdapter(adapterOftenSticker);
	}

    private void addStickerToStickers() {
    	fillArrayList();
		addItemGridView();
	}
    
	private void fillArrayList() {
		Iterator<Entry<String, String>> iterator = oftenStickers.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			arrayListOftenSticker.add(entry.getKey());
		}
	}
		

}
