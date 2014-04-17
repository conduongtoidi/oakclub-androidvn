package com.oakclub.android.model.adaptercustom;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.oakclub.android.ChatActivity;
import com.oakclub.android.R;
import com.oakclub.android.image.SmartImageView;
import com.oakclub.android.model.Groups;
import com.oakclub.android.model.List;
import com.oakclub.android.util.OakClubUtil;
import com.viewpagerindicator.IconPagerAdapter;

public class StickerScreenAdapter extends PagerAdapter implements
		IconPagerAdapter {

	ArrayList<Integer> page_imgTabs = new ArrayList<Integer>();
	static ArrayList<Drawable> page_imgDrawableTabs = new ArrayList<Drawable>();

	public static ArrayList<Groups> groups = new ArrayList<Groups>();
	
	private ArrayList<SmileysAdapter> arrayAdaper = new ArrayList<SmileysAdapter>();
	Context context;
	public static ChatActivity chat;
	String pathGift = "";
	String imgSticker = "";
	SmartImageView smartImageView;

	public StickerScreenAdapter(Context c) {
		this.context = c;
		page_imgTabs.add(R.drawable.tab_often_selector);
		for (int i = 1; i < groups.size(); i++) {
			page_imgTabs.add(R.drawable.tab_sticker_selector);
		}
		
		smartImageView = new SmartImageView(c);
		String url = OakClubUtil.getFullLinkStickerOrGift(context, groups.get(1).getIcon().getNormal());
		OakClubUtil.loadImageFromUrl(context, url, smartImageView, "Chat", R.drawable.logo_oakclub);
		page_imgDrawableTabs.add(smartImageView.getDrawable());
		for (int i = 1; i < groups.size(); i++) {
			final SmartImageView smartImageViewNomal = new SmartImageView(c);
			url = OakClubUtil.getFullLinkStickerOrGift(context, groups.get(i).getIcon().getNormal());
			OakClubUtil.loadImageFromUrl(context, url, smartImageViewNomal, "Chat", R.drawable.logo_oakclub);
			
			final SmartImageView smartImageViewPress = new SmartImageView(c);
			url = OakClubUtil.getFullLinkStickerOrGift(context, groups.get(i).getIcon().getHover());
			OakClubUtil.loadImageFromUrl(context, url, smartImageViewPress, "Chat", R.drawable.logo_oakclub);
			
			final StateListDrawable stateDrawable = new StateListDrawable();
			
			page_imgDrawableTabs.add(context.getResources().getDrawable(R.drawable.logo_oakclub));
			final int index = i;
			final String str = url;
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					Log.v("index", index + " " + str);
					stateDrawable.addState(new int[] {-android.R.attr.state_selected, -android.R.attr.state_pressed}, smartImageViewNomal.getDrawable());
					stateDrawable.addState(new int[] {}, smartImageViewPress.getDrawable());
					
					page_imgDrawableTabs.set(index, stateDrawable);
					try {
						if (ChatActivity.mIndicator != null)
							ChatActivity.mIndicator.notifyDataSetChanged();
					} catch (Exception ex) {
						
					}
				}
			}, 5000);
		}
		notifyDataSetChanged();
	}

	// This is the number of pages
	@Override
	public int getCount() {
		return page_imgTabs.size();
	}

	@Override
	public boolean isViewFromObject(View v, Object o) {
		return v.equals(o);
	}

	public Drawable getIconDrawable(int index) {
		if (index >= 1)
			return page_imgDrawableTabs.get(index);
		return null;
	}
	
	public int getIconResId(int position) {
		if (position < 1)
			return page_imgTabs.get(position);
		return 0;
	}

	// public CharSequence getPageTitle(int position) {
	// return page_imgTabs[position] + "";
	// }

	// This is where all the magic happen
	public Object instantiateItem(View pager, int position) {
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.activity_chat_emoticon, null, false);

		final GridView gvEmoticon = (GridView) v
				.findViewById(R.id.activity_chat_emticon_gvEmoticon);

		SmileysAdapter adapter;
		
		final int POS = position;
		int indexList;
		switch (POS) {
		case 0:
			indexList = 0;
			try {
				pathGift = groups.get(indexList).getUrl();//"/bundles/likevnblissdate/v3/chat/images/stickers/";
			} catch(Exception ex) {
				
			}
			imgSticker = "<img src=\"" + pathGift;
			SmileysAdapter.numberColumns = 7;
			gvEmoticon.setNumColumns(SmileysAdapter.numberColumns);
//			fillArrayList(arrayHashMapSticker.get(indexList),
//					arraySticker.get(indexList));
			adapter = new SmileysAdapter(
					groups.get(indexList), context, pathGift, false);
			gvEmoticon.setAdapter(adapter);
			arrayAdaper.add(adapter);
			break;
		default:
			indexList = POS;
			try {
				pathGift = groups.get(indexList).getUrl();//"/bundles/likevnblissdate/v3/chat/images/stickers/";
			} catch(Exception ex) {
				
			}
			imgSticker = "<img src=\"" + pathGift;
			SmileysAdapter.numberColumns = 7;
			gvEmoticon.setNumColumns(SmileysAdapter.numberColumns);
//			fillArrayList(arrayHashMapSticker.get(indexList),
//					arraySticker.get(indexList));
			adapter = new SmileysAdapter(
					groups.get(indexList), context, pathGift, false);
			gvEmoticon.setAdapter(adapter);
			arrayAdaper.add(adapter);
			break;
		}
		
		gvEmoticon.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg3) {
				switch (POS) {
				case 0:					
					ChatActivity.lltMatch.setVisibility(View.GONE);
					String value = gvEmoticon.getAdapter().getItem(position).toString();
	                String keyEntry = value;
	                pathGift = groups.get(0).getList().get(position).getUrl();
	                imgSticker = "<img src=\"" + pathGift;
	                String path2 = imgSticker + value + ".png\" width=\"125\" height=\"125\" type=\"gift\"/>";
	                chat.solveSendMessage(path2);

					break;
				default:
					try {
						pathGift = groups.get(POS).getUrl();//"/bundles/likevnblissdate/v3/chat/images/stickers/";
					} catch(Exception ex) {
						
					}
					imgSticker = "<img src=\"" + pathGift;
					ChatActivity.lltMatch.setVisibility(View.GONE);
					ChatActivity.chatLv.setVisibility(View.VISIBLE);
					value = gvEmoticon.getAdapter().getItem(position).toString();
	                keyEntry = value;
	                path2 = imgSticker + value + ".png\" width=\"125\" height=\"125\" type=\"gift\"/>";
	                chat.solveSendMessage(path2);
	                
	                if (groups.get(0).getList() == null) {
	                	groups.get(0).setIcon(groups.get(POS).getIcon());
	                	groups.get(0).setName(groups.get(POS).getName());
	                	groups.get(0).setUrl(groups.get(POS).getUrl());
	                	groups.get(0).setList(new ArrayList<List>());
	                	groups.get(0).getList().add(groups.get(POS).getList().get(position));
	                	groups.get(0).getList().get(0).setUrl(groups.get(POS).getUrl());
	                }
	                boolean flag = false;
	                for (int i = 0; i < groups.get(0).getList().size(); i++) {
	                	if (!groups.get(0).getList().get(i).getName().equals(keyEntry)) {
	                		flag = true;
	                	}
	                }
	                if (flag) {
	                	groups.get(0).getList().add(groups.get(POS).getList().get(position));
                		groups.get(0).getList().get(groups.get(0).getList().size() - 1).setUrl(groups.get(POS).getUrl());
	                }
	                arrayAdaper.get(0).notifyDataSetChanged();
					break;
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

//	private void fillArrayList(HashMap<String, String> emoticons,
//			ArrayList<String> arrayListSmileys) {
//		arrayListSmileys.clear();
//		Iterator<Entry<String, String>> iterator = emoticons.entrySet()
//				.iterator();
//		while (iterator.hasNext()) {
//			Entry<String, String> entry = iterator.next();
//			arrayListSmileys.add(entry.getKey());
//		}
//	}
	
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
