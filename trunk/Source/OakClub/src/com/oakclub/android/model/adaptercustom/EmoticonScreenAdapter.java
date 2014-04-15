package com.oakclub.android.model.adaptercustom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
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
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.viewpagerindicator.IconPagerAdapter;

public class EmoticonScreenAdapter extends PagerAdapter implements
		IconPagerAdapter {

	ArrayList<Integer> page_imgTabs = new ArrayList<Integer>();
	static ArrayList<Drawable> page_imgDrawableTabs = new ArrayList<Drawable>();

	public static ArrayList<Groups> groups = new ArrayList<Groups>();
	
	public static HashMap<String, String> emoticons = new HashMap<String, String>();
	private static ArrayList<HashMap<String, String>> arrayHashMapEmoticon = new ArrayList<HashMap<String, String>>();
	private ArrayList<ArrayList<String>> arrayEmoticon = new ArrayList<ArrayList<String>>();
	private ArrayList<SmileysAdapter> arrayAdaper = new ArrayList<SmileysAdapter>();
	Context context;
	
//	public static ArrayList<HashMap<String, String>> stickers = new ArrayList<HashMap<String, String>>();
	
	public static ChatActivity chat;
	String pathSticker = "";
	String imgSticker = "";
	SmartImageView smartImageView;

	public EmoticonScreenAdapter(Context c) {
		this.context = c;
		
		page_imgTabs.add(R.drawable.tab_often_selector);
		page_imgTabs.add(R.drawable.tab_smile_selector);
		for (int i = 1; i < groups.size(); i++) {
			page_imgTabs.add(R.drawable.tab_sticker_selector);
		}
		
		if (page_imgDrawableTabs.size() == 0) {
			smartImageView = new SmartImageView(c);
			String url = OakClubUtil.getFullLinkStickerOrGift(context, groups.get(1).getIcon().getNormal());
			OakClubUtil.loadImageFromUrl(context, url, smartImageView, "Chat", R.drawable.logo_oakclub);
			page_imgDrawableTabs.add(smartImageView.getDrawable());
			smartImageView = new SmartImageView(c);
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
				final int index = i + 1;
				final String str = url;
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						Log.v("index", index + " " + str);
						stateDrawable.addState(new int[] {-android.R.attr.state_selected, -android.R.attr.state_pressed}, smartImageViewNomal.getDrawable());
						stateDrawable.addState(new int[] {}, smartImageViewPress.getDrawable());
						
						page_imgDrawableTabs.set(index, stateDrawable);
						notifyDataSetChanged();
					}
				}, 5000);
			}
		}
		
		arrayHashMapEmoticon.add(addSmileToEmoticons());
		arrayEmoticon.add(new ArrayList<String>());

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
		if (index >= 2)
			return page_imgDrawableTabs.get(index);
		return null;
	}
	
	public int getIconResId(int position) {
		if (position < 2)
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
				pathSticker = Constants.dataConfig.getSticker().getGroups().get(indexList).getUrl();//"/bundles/likevnblissdate/v3/chat/images/stickers/";
			} catch(Exception ex) {
				
			}
			imgSticker = "<img src=\"" + pathSticker;
			gvEmoticon.setNumColumns(4);
//			fillArrayList(arrayHashMapSticker.get(indexList),
//					arraySticker.get(indexList));
			adapter = new SmileysAdapter(
					groups.get(indexList), context, pathSticker, false);
			gvEmoticon.setAdapter(adapter);
			arrayAdaper.add(adapter);
			break;
		case 1:
			fillArrayList(arrayHashMapEmoticon.get(0),
					arrayEmoticon.get(0));
			adapter = new SmileysAdapter(
					arrayEmoticon.get(0), context, arrayHashMapEmoticon.get(0), "", true);
			gvEmoticon.setAdapter(adapter);
			arrayAdaper.add(adapter);
			break;
		default:
			indexList = POS - 1;
			try {
				pathSticker = Constants.dataConfig.getSticker().getGroups().get(indexList).getUrl();//"/bundles/likevnblissdate/v3/chat/images/stickers/";
			} catch(Exception ex) {
				
			}
			imgSticker = "<img src=\"" + pathSticker;
			gvEmoticon.setNumColumns(4);
//			fillArrayList(arrayHashMapSticker.get(indexList),
//					arraySticker.get(indexList));
			adapter = new SmileysAdapter(
					groups.get(indexList), context, pathSticker, false);
			gvEmoticon.setAdapter(adapter);
			arrayAdaper.add(adapter);
			break;
		}
		
		gvEmoticon.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg3) {
				switch (POS) {
				case 1:
					int pos = ChatActivity.tbMessage.getSelectionStart();
					String value = gvEmoticon.getAdapter().getItem(position)
							.toString();
					value = "(" + value + ")";
					String text = ChatActivity.tbMessage.getText().toString();
					String textHead = text.substring(0, pos);
					String textTail = text.substring(pos, text.length());
					pos = (textHead + value).length();
					value = textHead + value + textTail;
					SpannableStringBuilder spannable = ChatActivity
							.getSmiledText(context, value, true);
					ChatActivity.tbMessage.setText(spannable);
					ChatActivity.tbMessage.setSelection(spannable.length());
					break;
				case 0:
					
					ChatActivity.lltMatch.setVisibility(View.GONE);
					ChatActivity.chatLv.setVisibility(View.VISIBLE);
					value = gvEmoticon.getAdapter().getItem(position).toString();
	                String keyEntry = value;
	                pathSticker = groups.get(0).getList().get(position).getUrl();
	                imgSticker = "<img src=\"" + pathSticker;
	                String path2 = imgSticker + value + ".png\" width=\"125\" height=\"125\" type=\"sticker\"/>";
	                chat.solveSendMessage(path2);

					break;
				default:
					try {
						pathSticker = groups.get(POS - 1).getUrl();//"/bundles/likevnblissdate/v3/chat/images/stickers/";
					} catch(Exception ex) {
						
					}
					imgSticker = "<img src=\"" + pathSticker;
					ChatActivity.lltMatch.setVisibility(View.GONE);
					ChatActivity.chatLv.setVisibility(View.VISIBLE);
					value = gvEmoticon.getAdapter().getItem(position).toString();
	                keyEntry = value;
	                path2 = imgSticker + value + ".png\" width=\"125\" height=\"125\" type=\"sticker\"/>";
	                chat.solveSendMessage(path2);
	                
	                if (groups.get(0).getList() == null) {
	                	groups.get(0).setIcon(groups.get(POS - 1).getIcon());
	                	groups.get(0).setName(groups.get(POS - 1).getName());
	                	groups.get(0).setUrl(groups.get(POS - 1).getUrl());
	                	groups.get(0).setList(new ArrayList<List>());
	                	groups.get(0).getList().add(groups.get(POS - 1).getList().get(position));
	                	groups.get(0).getList().get(0).setUrl(groups.get(POS - 1).getUrl());
	                }
	                boolean flag = false;
	                for (int i = 0; i < groups.get(0).getList().size(); i++) {
	                	if (!groups.get(0).getList().get(i).getName().equals(keyEntry)) {
	                		flag = true;
	                	}
	                }
	                if (flag) {
	                	groups.get(0).getList().add(groups.get(POS - 1).getList().get(position));
                		groups.get(0).getList().get(groups.get(0).getList().size() - 1).setUrl(groups.get(POS - 1).getUrl());
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

	HashMap<String, String> oftenEmoticon() {
		HashMap<String, String> emoticons = new HashMap<String, String>();
		return emoticons;
	}

	HashMap<String, String> addSmileToEmoticons() {
		HashMap<String, String> emoticons = new HashMap<String, String>();
		emoticons.put(":))", R.drawable.laugh + "");
		emoticons.put(":((", R.drawable.cry + "");
		emoticons.put(":)", R.drawable.smile + "");
		emoticons.put(";)", R.drawable.wink + "");
		emoticons.put(":(", R.drawable.sad + "");
		emoticons.put(":P", R.drawable.tongue + "");
		emoticons.put(":-/", R.drawable.confused + "");
		emoticons.put("x(", R.drawable.angry + "");
		emoticons.put(":D", R.drawable.grin + "");
		emoticons.put(":-o", R.drawable.amazed + "");
		emoticons.put(":\"&gt;", R.drawable.shy + "");
		emoticons.put("B-)", R.drawable.cool + "");
		emoticons.put("(bandit)", R.drawable.ninja + "");
		emoticons.put(":-&amp;", R.drawable.sick + "");
		emoticons.put("/:)", R.drawable.doubtful + "");
		emoticons.put(">:)", R.drawable.devil + "");
		emoticons.put("O:-)", R.drawable.angel + "");
		emoticons.put("(:|", R.drawable.nerd + "");
		emoticons.put("=P~", R.drawable.love + "");
		emoticons.put("&lt;:-P", R.drawable.party + "");
		emoticons.put(":x", R.drawable.speechless + "");
		emoticons.put(":O)", R.drawable.clown + "");
		emoticons.put(":-&lt;", R.drawable.bored + "");
		emoticons.put(";PX", R.drawable.pirate + "");
		emoticons.put("(santa)", R.drawable.santa + "");
		emoticons.put("(fight)", R.drawable.karate + "");
		emoticons.put("(emo)", R.drawable.emo + "");
		emoticons.put("(tribal)", R.drawable.indian + "");
		emoticons.put("qB]", R.drawable.punk + "");
		emoticons.put("p^^", R.drawable.beaten + "");
		emoticons.put("\"vvv\"", R.drawable.wacky + "");
		emoticons.put("]:-&gt;", R.drawable.vampire + "");
		emoticons.put(":-*", R.drawable.kiss + "");
		emoticons.put("$_$", R.drawable.millionaire + "");
		emoticons.put(":::^^:::", R.drawable.sweating + "");
		emoticons.put("+_+", R.drawable.frozen + "");

		this.emoticons = emoticons;
		return emoticons;
	}

	HashMap<String, String> oftenSticker() {
		HashMap<String, String> emoticons = new HashMap<String, String>();
		return emoticons;
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
