package com.oakclub.android.model.adaptercustom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.oakclub.android.ChatActivity;
import com.oakclub.android.R;
import com.oakclub.android.util.Constants;
import com.viewpagerindicator.IconPagerAdapter;

public class EmoticonScreenAdapter extends PagerAdapter implements
		IconPagerAdapter {

	int[] page_imgTabs = {R.drawable.tab_smile_selector, R.drawable.tab_often_selector,
			R.drawable.tab_sticker_selector, R.drawable.tab_sticker_selector };

	public static HashMap<String, String> emoticons = new HashMap<String, String>();
	private static ArrayList<HashMap<String, String>> arrayHashMapEmoticon = new ArrayList<HashMap<String, String>>();
	private ArrayList<ArrayList<String>> arrayEmoticon = new ArrayList<ArrayList<String>>();
	private ArrayList<SmileysAdapter> arrayAdaper = new ArrayList<SmileysAdapter>();
	Context context;
	
	public static ArrayList<HashMap<String, String>> stickers = new ArrayList<HashMap<String, String>>();
	private static ArrayList<HashMap<String, String>> arrayHashMapSticker = new ArrayList<HashMap<String, String>>();
	private ArrayList<ArrayList<String>> arraySticker = new ArrayList<ArrayList<String>>();
	
	public static ChatActivity chat;
	String pathSticker = "";
	String imgSticker = "";

	public EmoticonScreenAdapter(Context c) {
		this.context = c;

		//arrayHashMapEmoticon.add(oftenEmoticon());
		arrayHashMapEmoticon.add(addSmileToEmoticons());
		//arrayEmoticon.add(new ArrayList<String>());
		arrayEmoticon.add(new ArrayList<String>());
		arrayHashMapSticker.add(oftenSticker());
		arrayHashMapSticker.add(addToSticker(0));
		arrayHashMapSticker.add(addToSticker(1));
		
		arraySticker.add(new ArrayList<String>());
		arraySticker.add(new ArrayList<String>());
		arraySticker.add(new ArrayList<String>());
	}

	// This is the number of pages
	@Override
	public int getCount() {
		return page_imgTabs.length;
	}
	
	@Override
	public boolean isViewFromObject(View v, Object o) {
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

		final GridView gvEmoticon = (GridView) v
				.findViewById(R.id.activity_chat_emticon_gvEmoticon);

		SmileysAdapter adapter;
		
//		final int POS = position;
		
		final int POS = position;
		int indexList;
		switch (POS) {
		case 0:
			fillArrayList(arrayHashMapEmoticon.get(position),
					arrayEmoticon.get(position));
			adapter = new SmileysAdapter(
					arrayEmoticon.get(position), context,
					arrayHashMapEmoticon.get(position), "", true);
			gvEmoticon.setAdapter(adapter);
			arrayAdaper.add(adapter);
			break;
		case 1:
			indexList = 0;
			gvEmoticon.setNumColumns(4);
			fillArrayList(arrayHashMapSticker.get(indexList),
					arraySticker.get(indexList));
			adapter = new SmileysAdapter(
					arraySticker.get(indexList), context,
					arrayHashMapSticker.get(indexList), pathSticker, false);
			gvEmoticon.setAdapter(adapter);
			arrayAdaper.add(adapter);
			break;
		case 2:
			indexList = 1;
			try {
				pathSticker = Constants.dataConfig.getConfigs().getSticker().getUrl();//"/bundles/likevnblissdate/v3/chat/images/stickers/";
			} catch(Exception ex) {
				
			}
			imgSticker = "<img src=\"" + pathSticker;
			gvEmoticon.setNumColumns(4);
			fillArrayList(arrayHashMapSticker.get(indexList),
					arraySticker.get(indexList));
			adapter = new SmileysAdapter(
					arraySticker.get(indexList), context,
					arrayHashMapSticker.get(indexList), pathSticker, false);
			gvEmoticon.setAdapter(adapter);
			arrayAdaper.add(adapter);
			break;
		case 3:
			indexList = 2;
			try {
				pathSticker = Constants.dataConfig.getConfigs().getCats().getUrl();//"/bundles/likevnblissdate/v3/chat/images/sticker_cats/";
			} catch (Exception ex) {
				
			}
			imgSticker = "<img src=\"" + pathSticker;
			gvEmoticon.setNumColumns(4);
			fillArrayList(arrayHashMapSticker.get(indexList),
					arraySticker.get(indexList));
			adapter = new SmileysAdapter(
					arraySticker.get(indexList), context,
					arrayHashMapSticker.get(indexList), pathSticker, false);
			gvEmoticon.setAdapter(adapter);
			arrayAdaper.add(adapter);
		default:
			break;
		}
		
		gvEmoticon.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg3) {
				switch (POS) {
				case 0:
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
				case 1:
					
					ChatActivity.lltMatch.setVisibility(View.GONE);
					value = gvEmoticon.getAdapter().getItem(position).toString();
	                String keyEntry = value;
	                if (keyEntry.contains("_cat")) {
	                	try {
							pathSticker = Constants.dataConfig.getConfigs().getCats().getUrl();//"/bundles/likevnblissdate/v3/chat/images/stickers/";
						} catch(Exception ex) {
							
						}
	                } else {
	                	try {
							pathSticker = Constants.dataConfig.getConfigs().getSticker().getUrl();//"/bundles/likevnblissdate/v3/chat/images/stickers/";
						} catch(Exception ex) {
							
						}
	                }
	                imgSticker = "<img src=\"" + pathSticker;
	                String path2 = imgSticker + value + ".png\" width=\"125\" height=\"125\" type=\"sticker\"/>";
	                chat.solveSendMessage(path2);

					Iterator<Entry<String, String>> iterator = arrayHashMapSticker
							.get(1).entrySet().iterator();
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
					break;
				case 2:
					try {
						pathSticker = Constants.dataConfig.getConfigs().getSticker().getUrl();//"/bundles/likevnblissdate/v3/chat/images/stickers/";
					} catch(Exception ex) {
						
					}
					imgSticker = "<img src=\"" + pathSticker;
					ChatActivity.lltMatch.setVisibility(View.GONE);
					value = gvEmoticon.getAdapter().getItem(position).toString();
	                keyEntry = value;
	                path2 = imgSticker + value + ".png\" width=\"125\" height=\"125\" type=\"sticker\"/>";
	                chat.solveSendMessage(path2);

					iterator = arrayHashMapSticker
							.get(1).entrySet().iterator();
					valueEntry = "";
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
					break;
				case 3:
					try {
						pathSticker = Constants.dataConfig.getConfigs().getCats().getUrl();//"/bundles/likevnblissdate/v3/chat/images/stickers/";
					} catch(Exception ex) {
						
					}
					imgSticker = "<img src=\"" + pathSticker;
					ChatActivity.lltMatch.setVisibility(View.GONE);
					value = gvEmoticon.getAdapter().getItem(position).toString();
	                keyEntry = value;
	                path2 = imgSticker + value + ".png\" width=\"125\" height=\"125\" type=\"sticker\"/>";
	                chat.solveSendMessage(path2);

					iterator = arrayHashMapSticker
							.get(2).entrySet().iterator();
					valueEntry = "";
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
					break;
				default:
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
}
