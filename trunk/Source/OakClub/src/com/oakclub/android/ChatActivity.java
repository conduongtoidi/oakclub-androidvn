package com.oakclub.android;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.util.Comparators;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.xbill.DNS.TLSARecord;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.da;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.oakclub.android.base.OakClubBaseActivity;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.model.ChatHistoryData;
import com.oakclub.android.model.ChatHistoryReturnObject;
import com.oakclub.android.model.HangoutProfileOtherReturnObject;
import com.oakclub.android.model.HangoutProfileReturnObject;
import com.oakclub.android.model.ListChatData;
import com.oakclub.android.model.ListChatReturnObject;
import com.oakclub.android.model.SendChatReturnObject;
import com.oakclub.android.model.SendMessageReturnObject;
import com.oakclub.android.model.SetLocationReturnObject;
import com.oakclub.android.model.SnapshotData;
import com.oakclub.android.model.adaptercustom.ChatHistoryAdapter;
import com.oakclub.android.model.adaptercustom.SmileysAdapter;
import com.oakclub.android.net.IOakClubApi;
import com.oakclub.android.net.OakClubApi;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.view.CircleImageView;
import com.oakclub.android.view.RadioButtonCustom;
import com.oakclub.android.view.TextViewWithFont;

public class ChatActivity extends OakClubBaseActivity {

    public static ListView chatLv;
    public static ChatHistoryAdapter adapter;
    public static ArrayList<ChatHistoryData> messageArrayList;
    public static String profile_id;
    public static int status;
    public static String match_time;
    private String content = "";
    private boolean isOtherReport = false;
    String target_name;
    String target_avatar;
    Button btnSend;
    EditText tbMessage;
    Chat chat;
    ProgressBar progressBar;
    public static boolean isActive = true;
    SnapshotData snapShotData;
    
    ImageButton btnBack;
    ImageButton btnInfoProfile;
    ImageButton btnReport;
    TextView tvName;
    
    private Button btShowSmile;
    private GridView gvSmile;
    private LinearLayout lltBottom;
    private RelativeLayout.LayoutParams params;
    private boolean isShowSmile = false;
    private boolean isPressInfoProfile = false;
    
    public static LinearLayout lltMatch;
    private CircleImageView userAvatar;
    private TextViewWithFont txt_chat_match_content;
    private TextViewWithFont txt_chat_match_time;
    private TextViewWithFont txt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View v = inflater.inflate(R.layout.activity_chat, null);
        setContentView(v);

        Bundle bundleListChatData = getIntent().getExtras();
        profile_id = bundleListChatData.getString(Constants.BUNDLE_PROFILE_ID);
        target_name = bundleListChatData.getString(Constants.BUNDLE_NAME);
        target_avatar = bundleListChatData.getString(Constants.BUNDLE_AVATAR);
        status = bundleListChatData.getInt(Constants.BUNDLE_STATUS);
        match_time = bundleListChatData.getString(Constants.BUNDLE_MATCH_TIME);
        
        init();
        addSmileToEmoticons();
        addItemGridView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }
    
	public void init() {
		ChatHistoryRequest loader = new ChatHistoryRequest("getListChat", this,
				profile_id, 0);
		getRequestQueue().addRequest(loader);
		snapShotData = new SnapshotData();
		
        btnBack = (ImageButton) findViewById(R.id.activity_chat_fltTop_imgbtnBack);
        btnInfoProfile = (ImageButton) findViewById(R.id.activity_chat_fltTop_imgbtnInfoProfile);
        btnReport = (ImageButton) findViewById(R.id.activity_chat_fltTop_imgbtnReport);
        tvName = (TextView)findViewById(R.id.activty_chat_fltTop_tvName);
        chatLv = (ListView) findViewById(R.id.chat_history_lv);
        btnSend = (Button) findViewById(R.id.activity_chat_rtlbottom_btSend);
        tbMessage = (EditText) findViewById(R.id.activity_chat_rtlbottom_tbMessage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        gvSmile = (GridView) findViewById(R.id.activity_chat_rtlbottom_gvSmile);
        btShowSmile = (Button)findViewById(R.id.activity_chat_rtlbottom_btshowsmile);
        lltBottom = (LinearLayout)findViewById(R.id.activity_chat_rtlbottom);
        lltMatch = (LinearLayout) findViewById(R.id.activity_chat_llt_match);
        txt_chat_match_content = (TextViewWithFont) findViewById(R.id.activity_chat_match_content);
        txt_chat_match_time = (TextViewWithFont) findViewById(R.id.activity_chat_match_time);
        txt = (TextViewWithFont) findViewById(R.id.txt);
        userAvatar = (CircleImageView) findViewById(R.id.user_avatar);
//        status == 0 || status == 1
        if (status == 0) {
        	chatLv.setVisibility(View.GONE);
        	lltMatch.setVisibility(View.VISIBLE);
        	String url = OakClubUtil.getFullLink(this, target_avatar);
        	OakClubUtil.loadImageFromUrl(this,
        			url, userAvatar);
        	int fontSize = (int) OakClubUtil.convertPixelsToDp(OakClubUtil.getWidthScreen(this)/25, this);
        	txt_chat_match_content.setText(String.format("You matched with %s", target_name));
        	txt_chat_match_content.setTextSize(fontSize);
        	txt_chat_match_time.setText(timeMatch(match_time));
        	fontSize = (int) OakClubUtil.convertPixelsToDp(OakClubUtil.getWidthScreen(this)/20, this);
        	txt_chat_match_time.setTextSize(fontSize);
        	txt.setTextSize(fontSize);
        	
        } else {
        	chatLv.setVisibility(View.VISIBLE);
        	lltMatch.setVisibility(View.GONE);
        }
        
        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, 
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        
        
        btShowSmile.setOnClickListener(listener);
        btnInfoProfile.setOnClickListener(listener);
        btnBack.setOnClickListener(listener);
        btnSend.setOnClickListener(listener);
        btnReport.setOnClickListener(listener);
        
        tvName.setText("" + target_name);

        tbMessage.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tbMessage.getWindowToken(), 0);
        
        tbMessage.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                gvSmile.setVisibility(View.GONE);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                lltBottom.setLayoutParams(params);
                isShowSmile = false;
                return false;
            }
        });
        
        tbMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) ChatActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(tbMessage, InputMethodManager.SHOW_IMPLICIT);
                if (hasFocus) {
                    gvSmile.setVisibility(View.GONE);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    lltBottom.setLayoutParams(params);
                    isShowSmile = false;
                }
            }
        });
        
        messageArrayList = new ArrayList<ChatHistoryData>();
        adapter = new ChatHistoryAdapter(getApplicationContext(),
                messageArrayList, profile_id, target_avatar);
        chatLv.setAdapter(adapter);
        
        ChatManager chatManager = xmpp.getChatManager();
        String str = "" + profile_id + "@oakclub.com";
        chat = chatManager.createChat(str, null);
        
        
        
//      chat = chatManager.createChat(str, new MessageListener() {

//
//          @Override
//          public void processMessage(Chat arg0, Message arg1) {
//              ChatHistoryData message = new ChatHistoryData();
//              message.setBody(arg1.getBody());
//              message.setTo(user_id);
//              message.setFrom(profile_id);
//              SimpleDateFormat df = new SimpleDateFormat(Constants.CHAT_TIME_FORMAT);
//              String formattedDate = df.format(new Date());
//              message.setTime_string(formattedDate);
//              messageArrayList.add(message);
//              runOnUiThread(new Runnable() {
//                  
//                  @Override
//                  public void run() {
//                      adapter.notifyDataSetChanged();
//                      chatLv.setSelection(chatLv.getCount() - 1);                     
//                  }
//              });
//          }
//      });
    }
    
    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_chat_fltTop_imgbtnInfoProfile :
                	if (!isPressInfoProfile) {
                		isPressInfoProfile = true;
	                	GetOtherProfile loader2 = new GetOtherProfile(
	            				Constants.GET_HANGOUT_PROFILE, ChatActivity.this, profile_id, snapShotData);
	            		getRequestQueue().addRequest(loader2);
                	}
                    break;
                case R.id.activity_chat_fltTop_imgbtnBack:
                	SharedPreferences.Editor editor = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit();
    				editor.putBoolean(Constants.isLoadChat, true);
    				editor.commit();
    				SetReadMessages loader3 = new SetReadMessages(Constants.SET_READ_MESSAGES, ChatActivity.this, profile_id);
    				getRequestQueue().addRequest(loader3);
                    finish();
                    break;
                case R.id.activity_chat_rtlbottom_btSend :
                	chatLv.setVisibility(View.VISIBLE);
                	lltMatch.setVisibility(View.GONE);
                    solveSendMessage();
                    break;
                case R.id.activity_chat_fltTop_imgbtnReport :
                    solveEditBtn();
                    break;
                case R.id.activity_chat_rtlbottom_btshowsmile:
                    showSmile();
                    break;
            }
            
        }
    };
    
    public void solveEditBtn(){
        final String[] stringList = getResources()
                .getStringArray(R.array.report_user_reasons);
        final ArrayList<RadioButton> radioButtons;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(ChatActivity.this);
        final AlertDialog dialog = builder.create();//new Dialog(ProfileSettingActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View mainRelativeLayout = inflater.inflate(
                R.layout.dialog_report_spam_input, null);
        dialog.setView(mainRelativeLayout,0,0,0,0);
        
        final RadioGroup radioGroup = (RadioGroup)mainRelativeLayout.findViewById(R.id.radioGroup1);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        radioButtons = new ArrayList<RadioButton>();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final EditText editText = (EditText) mainRelativeLayout.findViewById(R.id.editText);
//      lp.setMargins(0, 5, 0, 5);
        for (int i = 0; i < stringList.length; i++) {
            RadioButtonCustom radio = new RadioButtonCustom(this, getResources().getDrawable(R.drawable.radiogroup_selector2));
            radio.setText(stringList[i]);
            radio.setTextColor(Color.BLACK);
            radio.setLayoutParams(new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            int padding = (int) OakClubUtil.convertDpToPixel(12,this);
            radio.setPadding(padding, padding, padding, padding);
//          radio.setBackgroundResource(R.drawable.white_purple_btn_);
            
            if (radio.getText().equals(getApplicationContext().getString(R.string.txt_report_user_other))) {
                radio.setOnClickListener(new OnClickListener() {                    

                    @Override
                    public void onClick(View v) {
                        editText.setVisibility(View.VISIBLE);
                        editText.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                        isOtherReport = true;
                    }
                });
            }
            else {
                radio.setOnClickListener(new OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        
                        editText.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        isOtherReport = false;
                    }
                });
            }
            
            radioButtons.add(radio);
            radioGroup.addView(radio);
            ImageView separator = new ImageButton(this);
            separator.setBackgroundResource(R.drawable.separators);
            separator.setLayoutParams(new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, 1));
            radioGroup.addView(separator);

        }
        
        Button okBtn;
        okBtn = (Button)mainRelativeLayout.findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                int selectedButtonId = radioGroup
                        .getCheckedRadioButtonId();
                int selectedId = -1;
                for (int i = 0; i < radioButtons.size(); i++) {
                    if (radioButtons.get(i).getId() == selectedButtonId) {
                        selectedId = i;
                        if (isOtherReport){
                            content = editText.getText().toString();
                        }
                        else {
                            content = radioButtons.get(i).getText().toString();
                        }
                        break;
                    }
                }
                if (selectedId != -1 ) {
                    showBlockConfirmDialog(getResources().getString(R.string.txt_report_message), 
                            getResources().getString(R.string.txt_report_anyway), true);
                }
                dialog.dismiss();
            }
        }); 
        Button btnBlock = (Button)mainRelativeLayout.findViewById(R.id.dialog_report_spam_input_block_user);
        btnBlock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showBlockConfirmDialog(getResources().getString(R.string.txt_block_message), 
                        getResources().getString(R.string.txt_block_anyway), false);
            }
        });
        
        dialog.setCancelable(true);
        dialog.show();
    }
    
    public void showBlockConfirmDialog(String tvQuestion, String btName, final boolean isReport){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(ChatActivity.this);
        final AlertDialog dialog = builder.create();//new Dialog(ProfileSettingActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View mainRelativeLayout = inflater.inflate(
                R.layout.dialog_report_spam_confirm, null);
        dialog.setView(mainRelativeLayout,0,0,0,0);
        TextView tvQues = (TextView)mainRelativeLayout.findViewById(R.id.dialog_report_spam_confirm_tvquestion);
        tvQues.setText(tvQuestion);
        Button okBtn;
        okBtn = (Button)mainRelativeLayout.findViewById(R.id.ok_btn);
        okBtn.setText(btName);
        okBtn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                if(isReport){
                    ReportUserRequest loader = new ReportUserRequest("reportUser", ChatActivity.this, profile_id, content);
                    getRequestQueue().addRequest(loader);
                }
                else{
                    BlockUserRequest loader = new BlockUserRequest("blockUser", ChatActivity.this, profile_id);
                    getRequestQueue().addRequest(loader);
                }
                
                dialog.dismiss();
            }
        });
        
        Button btnCancel;
        btnCancel = (Button) mainRelativeLayout.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dialog.dismiss();               
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    public void solveSendMessage() {
        try {
            String content = tbMessage.getText().toString();
            if(content.trim().equals(""))
                return;
            final Message xmppmessage = new Message("" + profile_id
                    + "@oakclub.com", Message.Type.chat);
            xmppmessage.setBody(content);
            new Thread() {
                @Override
                public void run() {
                    if (xmpp != null && xmpp.getUser() != null) {
                        xmpp.sendPacket(xmppmessage);
                    }
                }
            }.start();
//          chat.sendMessage(content);
            tbMessage.setText("");
            ChatHistoryData message = new ChatHistoryData();
            message.setBody(content);
            message.setFrom(user_id);
            message.setTo(profile_id);
            SimpleDateFormat df = new SimpleDateFormat(Constants.CHAT_TIME_FORMAT);
            String formattedDate = df.format(new Date());
            message.setTime_string(formattedDate);
            messageArrayList.add(message);
            adapter.notifyDataSetChanged();
            chatLv.setSelection(chatLv.getCount() - 1);
            SendMessageLoader loader = new SendMessageLoader("sendMessage",
                    ChatActivity.this, profile_id, content);
            getRequestQueue().addRequest(loader);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ChatHistoryRequest extends RequestUI {

        private ChatHistoryReturnObject obj;
        private String profile_id;
        private int index;

        public ChatHistoryRequest(Object key, Activity activity,
                String profile_id, int index) {
            super(key, activity);
            this.profile_id = profile_id;
            this.index = index;
        }

        @Override
        public void execute() throws Exception {
            obj = oakClubApi.getHistoryMessages(profile_id, index);
        }

		@Override
		public void executeUI(Exception ex) {
			progressBar.setVisibility(View.GONE);
			if (obj == null || !obj.isStatus()) {
//				Toast.makeText(ChatActivity.this,
//						getString(R.string.abnormal_error_message),
//						Toast.LENGTH_SHORT).show();
			} else {
				if (target_name==null || target_name.length()==0){
					target_name = obj.getUsername();
					target_avatar = obj.getAvatar();
					tvName.setText("" + target_name);
				}
				messageArrayList.clear();
				messageArrayList.addAll(obj.getData());
				Collections.sort(messageArrayList, new Comparator<ChatHistoryData>() {
				    @Override
				    public int compare(ChatHistoryData data1, ChatHistoryData data2) {
				          return (data1.getTime_string().compareTo(data2.getTime_string()));
				    }
				});
				adapter.notifyDataSetChanged();
				chatLv.setSelection(adapter.getCount() - 1);
			}
		}
	}

    public class SendMessageLoader extends RequestUI {
        SendChatReturnObject obj;
        String userId;
        String content;

        public SendMessageLoader(Object key, Activity activity, String userId,
                String content) {
            super(key, activity);
            this.userId = userId;
            this.content = content;
        }

        @Override
        public void execute() throws Exception {
            obj = oakClubApi.SendChatMessage(userId, content);
        }

        @Override
        public void executeUI(Exception ex) {
            
            if (obj!=null && obj.isStatus()) {
//                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.value_send_success),
//                        Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.value_send_failed),
//                        Toast.LENGTH_SHORT).show();
            }

        }
    }
    
    private class ReportUserRequest extends RequestUI{
        String profile_id;
        String content;

        public ReportUserRequest(Object key, Activity activity,String profile_id, String content) {
            super(key, activity);
            this.profile_id = profile_id;
            this.content = content;
        }

        @Override
        public void execute() throws Exception {
            oakClubApi.ReportUserById(profile_id, content);
        }

        @Override
        public void executeUI(Exception ex) {
            
        }
        
    }   
    
    private class BlockUserRequest extends RequestUI{
        String profile_id;

        public BlockUserRequest(Object key, Activity activity,String profile_id) {
            super(key, activity);
            this.profile_id = profile_id;
        }

        @Override
        public void execute() throws Exception {
            oakClubApi.BlockUserById(profile_id);
        }

        @Override
        public void executeUI(Exception ex) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_NAME, 0);
            Editor editor = pref.edit();
            editor.putBoolean(Constants.IS_BLOCK_USER, true);
            editor.commit();
        }
        
    }   
    
    
    private static HashMap<String, Integer> emoticons = new HashMap<String, Integer>();
    private ArrayList<String> arrayListSmileys = new ArrayList<String>();
    private void showSmile(){
        gvSmile.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                    int position, long arg3) {
                int pos = tbMessage.getSelectionStart();
                String value = gvSmile.getAdapter().getItem(position).toString();
                String text = tbMessage.getText().toString();
                String textHead = text.substring(0, pos);
                String textTail = text.substring(pos, text.length());
                pos = (textHead +value).length();
                value = textHead + value + textTail;
                Spannable spannable = getSmiledText(ChatActivity.this, value);
                tbMessage.setText(spannable);
                tbMessage.setSelection(pos);
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
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                    params.addRule(RelativeLayout.ABOVE, R.id.activity_chat_rtlbottom_gvSmile);
                    lltBottom.setLayoutParams(params);
                    isShowSmile = true;
                }
            };
            handle.postDelayed(runnable, 100);
        }
        else{
            gvSmile.setVisibility(View.GONE);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lltBottom.setLayoutParams(params);
            isShowSmile = false;
        }
    }
    
    private void addItemGridView(){
        SmileysAdapter adapter = new SmileysAdapter(arrayListSmileys, ChatActivity.this, emoticons);
        gvSmile.setAdapter(adapter);
    }
    
    private void fillArrayList() {
        Iterator<Entry<String, Integer>> iterator = emoticons.entrySet().iterator();
        while(iterator.hasNext()){
            Entry<String, Integer> entry = iterator.next();
            arrayListSmileys.add(entry.getKey());
        }
    }


    public static Spannable getSmiledText(Context context, String text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        int index;
        for (index = 0; index < builder.length(); index++) {
            for (Entry<String, Integer> entry : emoticons.entrySet()) {
                int length = entry.getKey().length();
                if (index + length > builder.length())
                    continue;
                if (builder.subSequence(index, index + length).toString().equals(entry.getKey())) {
                    builder.setSpan(new ImageSpan(context, entry.getValue()), index, index + length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    index += length - 1;
                    break;
                }
            }
        }
        return builder;
    }
    private void addSmileToEmoticons(){
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
    

    @Override
    public void onBackPressed() {
    	SetReadMessages loader3 = new SetReadMessages(Constants.SET_READ_MESSAGES, ChatActivity.this, profile_id);
		getRequestQueue().addRequest(loader3);
		
        if(isShowSmile){
            gvSmile.setVisibility(View.GONE);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lltBottom.setLayoutParams(params);
            isShowSmile = false;
        }
        else super.onBackPressed();
    }
    
    private String timeMatch(String timeMatch) {
    	String result = "";
    	Calendar cal = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String now = sdf.format(cal.getTime());
        Date dateNow = cal.getTime();
        Date dateMatchTime = null;
        try {
			dateNow = sdf.parse(now);
			dateMatchTime = sdf.parse(timeMatch);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Date resultDate = new Date(dateNow.getTime() - dateMatchTime.getTime());
        int time = (int) (resultDate.getTime() / (1000 * 60));
        if (time == 0) time++;
        
        if (time < 59)
        	result = time + " minute(s) ago";
        else if (time < 60 * 60)
        	result = time / 60 + " hour(s) ago";
        else if (time < 60 * 60 * 24)
        	result = time / (60 * 24) + " day(s) ago";
        else if (time < 60 * 60 * 24 * 12)
        	result = time / (60 * 24 * 30) + " month(s) ago";
        else
        	result = time / (60 * 24 * 30 * 12) + " year(s) ago";
        
//        if (resultDate.getTime() < 0) {
//        	result = "1 minute(s) ago";
//        } else if (resultDate.getYear() - 1970 > 0)
//        	result = resultDate.getYear() - 1970 + " year(s) ago";
//        else if (resultDate.getMonth() > 1)
//        	result = resultDate.getMonth() - 1 + " month(s) ago";
//        else if (resultDate.getDate() > 1)
//        	result = resultDate.getDate() - 1 + " day(s) ago";
//        else if (resultDate.getHours() > 1)
//        	result = resultDate.getHours() - 1 + " hour(s) ago";
//        else 
//        	result = resultDate.getMinutes() + " minute(s) ago";
    	return result;
    }
    
	class GetOtherProfile extends RequestUI {

		HangoutProfileOtherReturnObject data = new HangoutProfileOtherReturnObject();
		String profile_id;
		
		public GetOtherProfile(Object key, Activity activity, String profile_id, SnapshotData snapShotData) {
			super(key, activity);
			this.profile_id = profile_id;
		}

		@Override
		public void execute() throws Exception {
			data = oakClubApi.getHangoutProfileOther(profile_id);
		}

		@Override
		public void executeUI(Exception ex) {
			if (data != null)
			{
			    snapShotData.setFromChat(true);
				snapShotData.setAbout_me(data.getData().getAbout_me());
				snapShotData.setActive(data.getData().getActive());
				Calendar cal = Calendar.getInstance();

				final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String yearNow = sdf.format(cal.getTime());
				Log.v("Year:", yearNow);
				String age = Integer.parseInt(yearNow.split("/")[2])
						- Integer.parseInt(data.getData().getBirthday_date()
								.split("/")[2]) + "";
				Log.v("Age:", age);
				snapShotData.setAge(Integer.parseInt(age));
				snapShotData.setAvatar(data.getData().getAvatar());
				snapShotData.setBirthday_date(data.getData().getBirthday_date());
				snapShotData.setDistance(data.getData().getDistance());
				snapShotData.setHometown_name(data.getData().getHometown_name());
				snapShotData.setInterested(data.getData().getInterested());
				snapShotData.setIs_like(data.getData().getIs_like());
				snapShotData.setLike(""+ data.getData().getLike());
				snapShotData.setLocation_name(data.getData().getLocation_name());
				snapShotData.setMutual_friends(data.getData().getMutual_friends());
				snapShotData.setName(data.getData().getName());
				snapShotData.setPhotos(data.getData().getPhotos());
				snapShotData.setProfile_id(data.getData().getProfile_id());
				snapShotData.setSchool(data.getData().getSchool());
				snapShotData.setShare_interests(data.getData().getShare_interests());
				snapShotData.setVideo_link(data.getData().getVideo_link());
				snapShotData.setViewed(""+data.getData().getViewed());
				snapShotData.setWork(data.getData().getWork());
				
				Intent intent = new Intent(getApplicationContext(),
                        InfoProfileOtherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                Bundle b = new Bundle();
                b.putSerializable("SnapshotDataBundle",
                        snapShotData);
                b.putString("Activity", "SnapshotActivity");
                intent.putExtras(b);
                startActivityForResult(intent, 1);
                isPressInfoProfile = false;
			}
		}
	}
	
	class SetReadMessages extends RequestUI {
		String profile_id;
		
		public SetReadMessages(Object key, Activity activity, String profile_id) {
			super(key, activity);
			this.profile_id = profile_id;
		}

		@Override
		public void execute() throws Exception {
			oakClubApi.SetReadMessages(profile_id);
		}

		@Override
		public void executeUI(Exception ex) {
			ListChatRequest loader3 = new ListChatRequest("getListChat", ChatActivity.this);
			getRequestQueue().addRequest(loader3);
		}
	}
	
	protected class ListChatRequest extends RequestUI {
        private ListChatReturnObject obj;
        public ListChatRequest(Object key, Activity activity) {
            super(key, activity);
        }
        @Override
        public void execute() throws Exception {
            obj = oakClubApi.getListChat();
        }
        @Override
        public void executeUI(Exception ex) {
            if (obj == null || !obj.isStatus()) {
//              Toast.makeText(MatchChatActivity.this,
//                      getString(R.string.abnormal_error_message),
//                      Toast.LENGTH_SHORT).show();
            } else {
                baseAllList.clear();
                for (int i = 0; i < obj.getData().size(); i++) {
                    baseAllList.add(obj.getData().get(i));
                }
                int count = 0 ; 
                for (int i = 0 ; i < baseAllList.size();i++){
                    count = count+baseAllList.get(i).getUnread_count();
                }
                SlidingMenuActivity.totalUnreadMessage = count;
                if (SlidingMenuActivity.mNotificationTv!=null){
                    SlidingMenuActivity.mNotificationTv.setText(""+SlidingMenuActivity.totalUnreadMessage);
                    if (SlidingMenuActivity.totalUnreadMessage>0){
                        SlidingMenuActivity.mNotificationTv.setVisibility(View.VISIBLE);    
                    } else {
                        SlidingMenuActivity.mNotificationTv.setVisibility(View.GONE);
                    }
                }
            }
        }

    }
}
