package com.oakclub.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.oakclub.android.model.adaptercustom.SmileysAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spannable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class OftenSmileActivity extends Activity {

	protected GridView gvSmile;
	float lastX;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_smile);

		gvSmile = (GridView) findViewById(R.id.activity_chat_rtlbottom_gvSmile);

		addSmileToEmoticons();
		addItemGridView();
		gvSmile.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg3) {
				int pos = ChatActivity.tbMessage.getSelectionStart();
				String value = gvSmile.getAdapter().getItem(position)
						.toString();
				String text = ChatActivity.tbMessage.getText().toString();
				String textHead = text.substring(0, pos);
				String textTail = text.substring(pos, text.length());
				pos = (textHead + value).length();
				value = textHead + value + textTail;
				Spannable spannable = ChatActivity.getSmiledText(
						OftenSmileActivity.this, value);
				ChatActivity.tbMessage.setText(spannable);
				ChatActivity.tbMessage.setSelection(spannable.length());
			}
		});

	}

	public static SmileysAdapter adapterOftenSmile;

	private void addItemGridView() {
//		adapterOftenSmile = new SmileysAdapter(arrayListOftenSmileys,
//				OftenSmileActivity.this, oftenEmoticons);
//		gvSmile.setAdapter(adapterOftenSmile);
	}

	public static HashMap<String, Integer> oftenEmoticons = new HashMap<String, Integer>();
	private ArrayList<String> arrayListOftenSmileys = new ArrayList<String>();

	private void addSmileToEmoticons() {
		fillArrayList();
	}

	private void fillArrayList() {
		Iterator<Entry<String, Integer>> iterator = oftenEmoticons.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, Integer> entry = iterator.next();
			arrayListOftenSmileys.add(entry.getKey());
		}
	}

}
