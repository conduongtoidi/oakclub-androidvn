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
import com.oakclub.android.util.Constants;
import com.viewpagerindicator.IconPagerAdapter;

public class StickerScreenAdapter extends PagerAdapter implements
		IconPagerAdapter {

	int[] page_imgTabs = { R.drawable.tab_often_selector,
			R.drawable.tab_sticker_selector, R.drawable.tab_sticker_selector };

	public static ArrayList<HashMap<String, String>> stickers = new ArrayList<HashMap<String, String>>();
	private static ArrayList<HashMap<String, String>> arrayHashMapSticker = new ArrayList<HashMap<String, String>>();
	private ArrayList<ArrayList<String>> arraySticker = new ArrayList<ArrayList<String>>();
	private ArrayList<StickerAdapter> arrayAdaper = new ArrayList<StickerAdapter>();
	Context context;
	public static ChatActivity chat;
	String pathSticker = "";
	String imgSticker = "";

	public StickerScreenAdapter(Context c) {
		this.context = c;
		//addCat();
		arrayHashMapSticker.add(oftenSticker());
		arrayHashMapSticker.add(addToSticker(0));
		arrayHashMapSticker.add(addToSticker(1));
		
//		for (int i = 0; i < arrayHashMapSticker.size(); i++) {
//			arraySticker.add(new ArrayList<String>());
//		}
		arraySticker.add(new ArrayList<String>());
		arraySticker.add(new ArrayList<String>());
		arraySticker.add(new ArrayList<String>());
	}

	// This is the number of pages
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

		final int POS = position;
		switch (POS) {
		case 1:
			try {
				pathSticker = Constants.dataConfig.getConfigs().getSticker().getUrl();//"/bundles/likevnblissdate/v3/chat/images/stickers/";
			} catch(Exception ex) {
				
			}
			imgSticker = "<img src=\"" + pathSticker;
			break;
		case 2:
			try {
				pathSticker = Constants.dataConfig.getConfigs().getCats().getUrl();//"/bundles/likevnblissdate/v3/chat/images/sticker_cats/";
			} catch (Exception ex) {
				
			}
			imgSticker = "<img src=\"" + pathSticker;
		default:
			break;
		}
		
		final GridView gvSticker = (GridView) v
				.findViewById(R.id.activity_chat_emticon_gvEmoticon);
		gvSticker.setNumColumns(4);
		fillArrayList(arrayHashMapSticker.get(position),
				arraySticker.get(position));
		StickerAdapter adapter = new StickerAdapter(
				arraySticker.get(position), context,
				arrayHashMapSticker.get(position), pathSticker);
		gvSticker.setAdapter(adapter);
		arrayAdaper.add(adapter);
		
		
		gvSticker.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg3) {
				switch (POS) {
				case 1:
					try {
						pathSticker = Constants.dataConfig.getConfigs().getSticker().getUrl();//"/bundles/likevnblissdate/v3/chat/images/stickers/";
					} catch(Exception ex) {
						
					}
					imgSticker = "<img src=\"" + pathSticker;
					break;
				case 2:
					try {
						pathSticker = Constants.dataConfig.getConfigs().getCats().getUrl();//"/bundles/likevnblissdate/v3/chat/images/stickers/";
					} catch(Exception ex) {
						
					}
					imgSticker = "<img src=\"" + pathSticker;
				default:
					break;
				}
				ChatActivity.lltMatch.setVisibility(View.GONE);
				String value = gvSticker.getAdapter().getItem(position).toString();
                String keyEntry = value;
                String path2 = imgSticker + value + ".png\" width=\"125\" height=\"125\" type=\"sticker\"/>";
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
					arraySticker.get(0).add(keyEntry);
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

	HashMap<String, String> addToSticker(int index) {
		return stickers.get(index);
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
	
//	private void addCat()
//	{
//		HashMap<String, String> stickers = new HashMap<String, String>();
//		stickers.put("angry_cat", "angry_cat.png");
//		stickers.put("bask_cat", "bask_cat.png");
//		stickers.put("bleble_cat", "bleble_cat.png");
//		stickers.put("callme_cat", "callme_cat.png");
//		stickers.put("catchme_cat", "catchme_cat.png");
//		stickers.put("cry_cat", "cry_cat.png");
//		stickers.put("eat_cat", "eat_cat.png");
//		stickers.put("errrr_cat", "errrr_cat.png");
//		stickers.put("fun_cat", "fun_cat.png");
//		stickers.put("gilf_cat", "gilf_cat.png");
//		stickers.put("goodnight_cat", "goodnight_cat.png");
//		stickers.put("happy_birthday_cat", "happy_birthday_cat.png");
//		stickers.put("hi_cat", "hi_cat.png");
//		stickers.put("hide_cat", "hide_cat.png");
//		stickers.put("hihi_cat", "hihi_cat.png");
//		stickers.put("hitU_cat", "hitU_cat.png");
//		stickers.put("hug_cat", "hug_cat.png");
//		stickers.put("keep_away_1_cat", "keep_away_1_cat.png");
//		stickers.put("keep_away_cat", "keep_away_cat.png");
//		stickers.put("love_cat", "love_cat.png");
//		stickers.put("makelove_cat", "makelove_cat.png");
//		stickers.put("mmm_cat", "mmm_cat.png");
//		stickers.put("no_cat", "no_cat.png");
//		stickers.put("oappp_1_cat", "oappp_1_cat.png");
//		stickers.put("oappp_cat", "oappp_cat.png");
//		stickers.put("ok_cat", "ok_cat.png");
//		stickers.put("picnic_cat", "picnic_cat.png");
//		stickers.put("play_cat", "play_cat.png");
//		stickers.put("runforurlife_cat", "runforurlife_cat.png");
//		stickers.put("shopping_cat", "shopping_cat.png");
//		stickers.put("smile_cat", "smile_cat.png");
//		stickers.put("sorry_cat", "sorry_cat.png");
//		stickers.put("surrender_cat", "surrender_cat.png");
//		stickers.put("swim_cat", "swim_cat.png");
//		stickers.put("swing_cat", "swing_cat.png");
//		stickers.put("welove_cat", "welove_cat.png");
//		stickers.put("what_cat", "what_cat.png");
//		
//        StickerScreenAdapter.stickers.add(stickers);
//	}
}
