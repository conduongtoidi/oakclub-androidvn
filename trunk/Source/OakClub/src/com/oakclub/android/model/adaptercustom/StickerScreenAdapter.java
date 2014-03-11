package com.oakclub.android.model.adaptercustom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.oakclub.android.ChatActivity;
import com.oakclub.android.R;
import com.viewpagerindicator.IconPagerAdapter;

public class StickerScreenAdapter extends PagerAdapter implements
		IconPagerAdapter {

	int[] page_imgTabs = { R.drawable.tab_often_selector,
			R.drawable.tab_sticker_selector };

	public static HashMap<String, String> stickers = new HashMap<String, String>();
	private static ArrayList<HashMap<String, String>> arrayHashMapSticker = new ArrayList<HashMap<String, String>>();
	private ArrayList<ArrayList<String>> arraySticker = new ArrayList<ArrayList<String>>();
	private ArrayList<StickerAdapter> arrayAdaper = new ArrayList<StickerAdapter>();
	Context context;
	public static ChatActivity chat;

	public StickerScreenAdapter(Context c) {
		this.context = c;
		arrayHashMapSticker.add(oftenSticker());
		arrayHashMapSticker.add(addToSticker());
		arraySticker.add(new ArrayList<String>());
		arraySticker.add(new ArrayList<String>());
	}

	// This is the number of pages -- 5
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return page_imgTabs.length;
	}

	@Override
	public boolean isViewFromObject(View v, Object o) {
		// TODO Auto-generated method stub
		return v.equals(o);
	}

	public int getIconResId(int position) {
		return page_imgTabs[position];
	}

	// public CharSequence getPageTitle(int position) {
	// return page_imgTabs[position] + "";
	// }

	// This is where all the magic happen
	public Object instantiateItem(View pager, int position) {
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.activity_chat_emoticon, null, false);

		final GridView gvSticker = (GridView) v
				.findViewById(R.id.activity_chat_emticon_gvEmoticon);
		gvSticker.setNumColumns(3);
		fillArrayList(arrayHashMapSticker.get(position),
				arraySticker.get(position));
		StickerAdapter adapter = new StickerAdapter(
				arraySticker.get(position), context,
				arrayHashMapSticker.get(position));
		gvSticker.setAdapter(adapter);
		arrayAdaper.add(adapter);
		final int POS = position;
		gvSticker.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg3) {
				String value = gvSticker.getAdapter().getItem(position).toString();
                String keyEntry = value;
                String path2 = "<img src=\"/bundles/likevnblissdate/v3/chat/images/stickers/" + value + ".png\" width=\"125\" height=\"125\" type=\"sticker\"/>";
                chat.solveSendMessage(path2);

				Iterator<Entry<String, String>> iterator = arrayHashMapSticker
						.get(POS).entrySet().iterator();
				String valueEntry = "";
				while (iterator.hasNext()) {
					Entry<String, String> entry = iterator.next();
					if (entry.getKey().equals(keyEntry)) {
						valueEntry = entry.getValue();
					}
				}
				if (!arrayHashMapSticker.get(0).containsKey(keyEntry)) {
					arrayHashMapSticker.get(0).put(keyEntry, valueEntry);
					fillArrayList(arrayHashMapSticker.get(0),
							arraySticker.get(0));
					arrayAdaper.get(0).notifyDataSetChanged();
				}

			}
		});

		// This is very important
		((ViewPager) pager).addView(v, 0);

		return v;
	}

	@Override
	public void destroyItem(View pager, int position, Object view) {
		((ViewPager) pager).removeView((View) view);
	}

	@Override
	public void finishUpdate(View view) {
	}

	@Override
	public void restoreState(Parcelable p, ClassLoader c) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View view) {
	}

	HashMap<String, String> oftenSticker() {
		HashMap<String, String> emoticons = new HashMap<String, String>();
		return emoticons;
	}

	HashMap<String, String> addToSticker() {
		return stickers;
	}

	private void fillArrayList(HashMap<String, String> emoticons,
			ArrayList<String> arrayListSmileys) {
		arrayListSmileys.clear();
		Iterator<Entry<String, String>> iterator = emoticons.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			arrayListSmileys.add(entry.getKey());
		}
	}
}
