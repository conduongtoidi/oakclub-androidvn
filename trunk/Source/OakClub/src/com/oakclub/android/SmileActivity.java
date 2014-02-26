package com.oakclub.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.oakclub.android.model.adaptercustom.SmileysAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class SmileActivity extends Activity {
	
	protected GridView gvSmile;
	
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
                String value = gvSmile.getAdapter().getItem(position).toString();
                String text = ChatActivity.tbMessage.getText().toString();
                String textHead = text.substring(0, pos);
                String textTail = text.substring(pos, text.length());
                pos = (textHead +value).length();
//                if (Html.fromHtml(value).toString().length() > 1)
//        		{
//                	pos = (textHead + Html.fromHtml(value).toString()).length();
//        		}
                value = textHead + value + textTail;
                Spannable spannable = ChatActivity.getSmiledText(SmileActivity.this, value);
                ChatActivity.tbMessage.setText(spannable);
                ChatActivity.tbMessage.setSelection(spannable.length());
            }
        });
	}
	
	private void addItemGridView() {
		SmileysAdapter adapter = new SmileysAdapter(arrayListSmileys,
				SmileActivity.this, emoticons);
		gvSmile.setAdapter(adapter);
	}    
    
    public static HashMap<String, Integer> emoticons = new HashMap<String, Integer>();
    private ArrayList<String> arrayListSmileys = new ArrayList<String>();
    
    private void showSmile(){
        gvSmile.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                    int position, long arg3) {
                int pos = ChatActivity.tbMessage.getSelectionStart();
                String value = gvSmile.getAdapter().getItem(position).toString();
                String text = ChatActivity.tbMessage.getText().toString();
                String textHead = text.substring(0, pos);
                String textTail = text.substring(pos, text.length());
                pos = (textHead +value).length();
//                if (Html.fromHtml(value).toString().length() > 1)
//        		{
//                	pos = (textHead + Html.fromHtml(value).toString()).length();
//        		}
                value = textHead + value + textTail;
                Spannable spannable = ChatActivity.getSmiledText(SmileActivity.this, value);
                ChatActivity.tbMessage.setText(spannable);
                ChatActivity.tbMessage.setSelection(spannable.length());
            }
        });
      
        if(gvSmile.getVisibility() == View.GONE){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            Handler handle = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    gvSmile.setVisibility(View.VISIBLE);
//                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
//                    params.addRule(RelativeLayout.ABOVE, R.id.activity_chat_rtlbottom_gvSmile);
//                    lltBottom.setLayoutParams(params);
//                    isShowSmile = true;
                }
            };
            handle.postDelayed(runnable, 100);
        }
        else{
            gvSmile.setVisibility(View.GONE);
//            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            lltBottom.setLayoutParams(params);
//            isShowSmile = false;
        }
    }

    private void addSmileToEmoticons() {
		emoticons.put(":)", R.drawable.smile);
		emoticons.put(";)", R.drawable.wink);
		emoticons.put(":(", R.drawable.sad);
		emoticons.put(":P", R.drawable.tongue);
		emoticons.put(":-/", R.drawable.confused);
		emoticons.put("x(", R.drawable.angry);
		emoticons.put(":D", R.drawable.grin);
		emoticons.put(":-o", R.drawable.amazed);
		emoticons.put(":((", R.drawable.cry);
		emoticons.put(":&quot;&gt;", R.drawable.shy);
		emoticons.put("B-)", R.drawable.cool);
		emoticons.put(":))", R.drawable.laugh);
		emoticons.put("(bandit)", R.drawable.ninja);
		emoticons.put(":-&amp;", R.drawable.sick);
		emoticons.put("/:)", R.drawable.doubtful);
		emoticons.put("&gt;:)", R.drawable.devil);
		emoticons.put("O:-)", R.drawable.angel);
		emoticons.put("(:|", R.drawable.nerd);
		emoticons.put("=P~", R.drawable.love);
		emoticons.put("&lt;:-P", R.drawable.party);
		emoticons.put(":x", R.drawable.speechless);
		emoticons.put(":O)", R.drawable.clown);
		emoticons.put(":-&lt;", R.drawable.bored);
		emoticons.put(";PX", R.drawable.pirate);
		emoticons.put("(santa)", R.drawable.santa);
		emoticons.put("(fight)", R.drawable.karate);
		emoticons.put("(emo)", R.drawable.emo);
		emoticons.put("(tribal)", R.drawable.indian);
		emoticons.put("qB]", R.drawable.punk);
		emoticons.put("p^^", R.drawable.beaten);
		emoticons.put("&quot;vvv&quot;", R.drawable.wacky);
		emoticons.put(":-&gt;", R.drawable.vampire);

		fillArrayList();
	}
    
	private void fillArrayList() {
		Iterator<Entry<String, Integer>> iterator = emoticons.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, Integer> entry = iterator.next();
			arrayListSmileys.add(entry.getKey());
		}
	}
}
