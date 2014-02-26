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
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class StickerActivity extends Activity {
	
	protected GridView gvSticker;
	public IOakClubApi oakClubApi;
	public static HashMap<String, String> stickers = new HashMap<String, String>();
	
    public static ArrayList<String> arrayListSmileys = new ArrayList<String>();
    public static ChatActivity chat;
	
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
//                if (Html.fromHtml(value).toString().length() > 1)
//        		{
//                	pos = (textHead + Html.fromHtml(value).toString()).length();
//        		}
                //Spannable spannable = ChatActivity.getSmiledText(StickerActivity.this, value);
                String path2 = "<img src=\"/bundles/likevnblissdate/v3/chat/images/stickers/" + value + ".png\" width=\"125\" height=\"125\" type=\"sticker\"/>";
                chat.solveSendMessage(path2);
            }
        });
		
		addStickerToStickers();
	}
	
	private void addItemGridView() {
		StickerAdapter adapter = new StickerAdapter(arrayListSmileys,
				StickerActivity.this, stickers);
		gvSticker.setAdapter(adapter);
	}

    private void addStickerToStickers() {
    	
    	AsyncTask<Void, Void, Void> mTask = new AsyncTask<Void, Void, Void>() {
    		GetConfigData getConfig;
			@Override
			protected Void doInBackground(Void... params) {
				oakClubApi = OakClubApi.createInstance(
						getApplicationContext(),
						getString(R.string.default_server_address));
				getConfig = oakClubApi.GetConfig();
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (getConfig != null && getConfig.getStatus()) {
					for (int i = 0; i < getConfig.getData().getStickers().size(); i++) {
						stickers.put(getConfig.getData().getStickers().get(i).getSymbol_name(), getConfig.getData().getStickers().get(i).getImage());
					}
					fillArrayList();
					addItemGridView();
				}
			}
			
    		
    	};
    	
    	if (stickers != null)
    		mTask.execute(null, null, null);
	}
    
	private void fillArrayList() {
		Iterator<Entry<String, String>> iterator = stickers.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			arrayListSmileys.add(entry.getKey());
		}
	}
	
	
}
