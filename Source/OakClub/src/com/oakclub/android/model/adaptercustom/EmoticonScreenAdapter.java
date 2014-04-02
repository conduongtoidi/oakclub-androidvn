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
import com.viewpagerindicator.IconPagerAdapter;

public class EmoticonScreenAdapter extends PagerAdapter implements
		IconPagerAdapter {

	int[] page_imgTabs = {R.drawable.tab_smile_selector };

	public static HashMap<String, String> emoticons = new HashMap<String, String>();
	private static ArrayList<HashMap<String, String>> arrayHashMapEmoticon = new ArrayList<HashMap<String, String>>();
	private ArrayList<ArrayList<String>> arrayEmoticon = new ArrayList<ArrayList<String>>();
	private ArrayList<SmileysAdapter> arrayAdaper = new ArrayList<SmileysAdapter>();
	Context context;

	public EmoticonScreenAdapter(Context c) {
		this.context = c;
		//arrayHashMapEmoticon.add(oftenEmoticon());
		arrayHashMapEmoticon.add(addSmileToEmoticons());
		//arrayEmoticon.add(new ArrayList<String>());
		arrayEmoticon.add(new ArrayList<String>());
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

		final GridView gvEmoticon = (GridView) v
				.findViewById(R.id.activity_chat_emticon_gvEmoticon);

		fillArrayList(arrayHashMapEmoticon.get(position),
				arrayEmoticon.get(position));
		SmileysAdapter adapter = new SmileysAdapter(
				arrayEmoticon.get(position), context,
				arrayHashMapEmoticon.get(position));
		gvEmoticon.setAdapter(adapter);
		arrayAdaper.add(adapter);
//		final int POS = position;
		gvEmoticon.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg3) {
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

				
//
//				Iterator<Entry<String, String>> iterator = arrayHashMapEmoticon
//						.get(POS).entrySet().iterator();
//				String valueEntry = "";
//				while (iterator.hasNext()) {
//					Entry<String, String> entry = iterator.next();
//					if (entry.getKey().equals(keyEntry)) {
//						valueEntry = entry.getValue();
//					}
//				}
//				if (!arrayHashMapEmoticon.get(0).containsKey(keyEntry)) {
//					arrayHashMapEmoticon.get(0).put(keyEntry, valueEntry);
//					fillArrayList(arrayHashMapEmoticon.get(0),
//							arrayEmoticon.get(0));
//					arrayAdaper.get(0).notifyDataSetChanged();
//				}

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
