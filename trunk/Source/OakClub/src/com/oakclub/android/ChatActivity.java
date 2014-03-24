package com.oakclub.android;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.http.entity.StringEntity;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.oakclub.android.base.ChatBaseActivity;
import com.oakclub.android.base.LoginBaseActivity;
import com.oakclub.android.base.OakClubBaseActivity;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.fragment.ProfileSettingFragment;
import com.oakclub.android.helper.operations.ListChatOperation;
import com.oakclub.android.model.ChatHistoryData;
import com.oakclub.android.model.ChatHistoryReturnObject;
import com.oakclub.android.model.DataConfig;
import com.oakclub.android.model.GetConfigData;
import com.oakclub.android.model.GetDataLanguageReturnObject;
import com.oakclub.android.model.HangoutProfileOtherReturnObject;
import com.oakclub.android.model.ListChatData;
import com.oakclub.android.model.SendChatReturnObject;
import com.oakclub.android.model.SendRegisterReturnObject;
import com.oakclub.android.model.SnapshotData;
import com.oakclub.android.model.Sticker;
import com.oakclub.android.model.adaptercustom.AdapterListChat;
import com.oakclub.android.model.adaptercustom.ChatHistoryAdapter;
import com.oakclub.android.model.adaptercustom.EmoticonScreenAdapter;
import com.oakclub.android.model.adaptercustom.SmileysAdapter;
import com.oakclub.android.model.adaptercustom.StickerScreenAdapter;
import com.oakclub.android.model.parse.ParseDataChatHistory;
import com.oakclub.android.model.parse.ParseDataChatList;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.view.CircleImageView;
import com.oakclub.android.view.RadioButtonCustom;
import com.oakclub.android.view.TextViewWithFont;
import com.viewpagerindicator.TabPageIndicator;

public class ChatActivity extends OakClubBaseActivity {

	public static ListView chatLv;
	public static ChatHistoryAdapter adapter;
	public static ArrayList<ChatHistoryData> messageArrayList;
	public static String profile_id;
	public static int status;
	public static boolean isGotHistory;
	public static String match_time;
	public static boolean isLoadFromNoti = false;
	private String content = "";
	private boolean isOtherReport = false;
	String target_name;
	String target_avatar;
	Button btnSend;
	public static EditText tbMessage;
	Chat chat;
	ProgressBar progressBar;
	public static boolean isActive = false;
	SnapshotData snapShotData;

	ImageButton btnBack;
	ImageButton btnInfoProfile;
	ImageButton btnReport;
	TextView tvName;
	

	// private Button btShowSmile;
	// private Button btShowKeyboard;

	private ImageView btShowSmile;
	private ImageView btShowSticker;
	private boolean isShowSmile = false;
	private boolean isShowSticker;

	private RelativeLayout rltChatViewPage;
	private LinearLayout lltBottom;
	private RelativeLayout.LayoutParams params;

	private boolean isPressInfoProfile = false;

	public static LinearLayout lltMatch;
	private CircleImageView userAvatar;
	private TextViewWithFont txt_chat_match_content;
	private TextViewWithFont txt_chat_match_time;
	private TextViewWithFont txt;

	public static boolean isPressSticker = false;
	public static HashMap<String, Bitmap> bitmapSticker = new HashMap<String, Bitmap>();
	protected ProgressDialog pd;
	protected ProgressDialog pdLogin;
	ViewPager mPager;
	TabPageIndicator mIndicator;
	EmoticonScreenAdapter emoticonScreenAdapter;
	StickerScreenAdapter stickerScreenAdapter;

	private SharedPreferences pref;
	private Editor editor;
	
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
		isLoadFromNoti = bundleListChatData.getBoolean(Constants.BUNDLE_NOTI);
		isGotHistory = (status != 0);
		pref = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_NAME, 0);
        editor = pref.edit();
		if (xmpp == null) {
			pdLogin = new ProgressDialog(ChatActivity.this);
			pdLogin.setMessage(getString(R.string.txt_loading));
			pdLogin.setCancelable(false);
			pdLogin.show();
			startXMPP();
			appVer = android.os.Build.VERSION.RELEASE;
			nameDevice = android.os.Build.MODEL;
			
			SendRegisterRequest request = new SendRegisterRequest(
					"sendRegister", ChatActivity.this,
					facebook_user_id, access_token);
			getRequestQueue().addRequest(request);
			
			GetConfig loader = new GetConfig(Constants.GETCONFIG, ChatActivity.this);
	        getRequestQueue().addRequest(loader);
		} else {
			initEmoticonPage();
		}
		init();
	}

	private void startXMPP() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		facebook_user_id = pref.getString(Constants.PREFERENCE_USER_ID, null);
		access_token = pref.getString(Constants.HEADER_ACCESS_TOKEN, null);
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		android_token = GCMRegistrar.getRegistrationId(this);

		ConnectionConfiguration config = new ConnectionConfiguration(
				getString(R.string.xmpp_server_address),
				Constants.XMPP_SERVER_PORT, Constants.XMPP_SERVICE_NAME);
		config.setSASLAuthenticationEnabled(true);

		xmpp = new XMPPConnection(config);
		try {
			SASLAuthentication.supportSASLMechanism("DIGEST-MD5", 0);
			// SASLAuthentication.supportSASLMechanism("PLAIN", 0);
			xmpp.connect();
			String username = pref.getString("username", "");
			String password = pref.getString("password", "");
			xmpp.login(username, password);
			Log.v("xmpp chat", username);
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
			xmpp.addPacketListener(new org.jivesoftware.smack.PacketListener() {

				@Override
				public void processPacket(Packet packet) {
					SharedPreferences pref = getSharedPreferences(
							Constants.PREFERENCE_NAME, MODE_PRIVATE);
					boolean isLogin = pref.getBoolean(
							Constants.PREFERENCE_LOGGINED, false);
					if (isLogin) {
						Message message = (Message) packet;
						if (message.getBody() != null) {

				            editor.putBoolean(Constants.IS_LOAD_CHAT_AGAIN, true);
				            editor.commit();
				            
							solveReceiveNewMessage(message);
						}
					} else {
						xmpp.disconnect();
					}

				}
			}, filter);
		} catch (XMPPException e) {
			// Toast.makeText(getApplicationContext(), "XMPP Error", 1).show();
			e.printStackTrace();
		} catch (Exception e) {
			// Toast.makeText(getApplicationContext(), "XMPP Error 2",
			// 1).show();
			e.printStackTrace();
		}
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

	@SuppressWarnings("deprecation")
	public void init() {
		isActive = true;
		snapShotData = new SnapshotData();

		btnBack = (ImageButton) findViewById(R.id.activity_chat_fltTop_imgbtnBack);
		btnInfoProfile = (ImageButton) findViewById(R.id.activity_chat_fltTop_imgbtnInfoProfile);
		btnReport = (ImageButton) findViewById(R.id.activity_chat_fltTop_imgbtnReport);
		tvName = (TextView) findViewById(R.id.activty_chat_fltTop_tvName);
		chatLv = (ListView) findViewById(R.id.chat_history_lv);
		btnSend = (Button) findViewById(R.id.activity_chat_rtlbottom_btSend);
		tbMessage = (EditText) findViewById(R.id.activity_chat_rtlbottom_tbMessage);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		btShowSmile = (ImageView) findViewById(R.id.activity_chat_rtlbottom_btSmile);
		btShowSticker = (ImageView) findViewById(R.id.activity_chat_rtlbottom_btSticker);
		lltBottom = (LinearLayout) findViewById(R.id.activity_chat_rtlbottom);
		rltChatViewPage = (RelativeLayout) findViewById(R.id.activity_chat_viewpage);
		lltMatch = (LinearLayout) findViewById(R.id.activity_chat_llt_match);
		txt_chat_match_content = (TextViewWithFont) findViewById(R.id.activity_chat_match_content);
		txt_chat_match_time = (TextViewWithFont) findViewById(R.id.activity_chat_match_time);
		txt = (TextViewWithFont) findViewById(R.id.txt);
		userAvatar = (CircleImageView) findViewById(R.id.user_avatar);
		

		StickerScreenAdapter.chat = this;
        
        ListChatOperation listChatDb = new ListChatOperation(ChatActivity.this);
        
		if (status == 0) {
			progressBar.setVisibility(View.GONE);
			chatLv.setVisibility(View.GONE);
			lltMatch.setVisibility(View.VISIBLE);
			String url = OakClubUtil.getFullLink(this, target_avatar);
			OakClubUtil.loadImageFromUrl(this, url, userAvatar);
			int fontSize = (int) OakClubUtil.convertPixelsToDp(
					OakClubUtil.getWidthScreen(this) / 25, this);
			txt_chat_match_content.setText(String.format(getString(R.string.txt_chat_match_content),
					target_name));
			txt_chat_match_content.setTextSize(fontSize);
			txt_chat_match_time.setText(timeMatch(match_time));
			fontSize = (int) OakClubUtil.convertPixelsToDp(
					OakClubUtil.getWidthScreen(this) / 20, this);
			txt_chat_match_time.setTextSize(fontSize);
			txt.setTextSize(fontSize);
			listChatDb.updateReadMutualMatch(profile_id);

            editor.putBoolean(Constants.IS_LOAD_CHAT_AGAIN, true);
            editor.commit();
            
			SetViewMuatualEvent setViewMutual = new SetViewMuatualEvent(
	                Constants.SET_VIEW_MUTUAL_MATCH, ChatActivity.this,
	                profile_id);
	        getRequestQueue().addRequest(setViewMutual);
		} else {
			ChatHistoryRequest loader = new ChatHistoryRequest("getListChat", this,
					profile_id);
			getRequestQueue().addRequest(loader);
			
			chatLv.setVisibility(View.VISIBLE);
			lltMatch.setVisibility(View.GONE);
			listChatDb.updateReadMessage(profile_id);
		}
		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lltBottom.setLayoutParams(params);
		tvName.setText(target_name);


		btShowSmile.setOnClickListener(listener);
		btShowSticker.setOnClickListener(listener);
		btnInfoProfile.setOnClickListener(listener);
		btnBack.setOnClickListener(listener);
		btnSend.setOnClickListener(listener);
		btnReport.setOnClickListener(listener);

		tbMessage.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(tbMessage.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		tbMessage.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				lltBottom.setLayoutParams(params);
				rltChatViewPage.setVisibility(View.GONE);
				//when VIP account click chat button				
				if(!isGotHistory){
					ListChatOperation listChatDb = new ListChatOperation(ChatActivity.this);
					ChatHistoryRequest loader = new ChatHistoryRequest("getListChat", ChatActivity.this,
							profile_id);
					getRequestQueue().addRequest(loader);
					listChatDb.updateReadMessage(profile_id);
					isGotHistory = true;
				}
				return false;
			}
		});
		
		tbMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				InputMethodManager imm = (InputMethodManager) ChatActivity.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(tbMessage, InputMethodManager.SHOW_IMPLICIT);
				if (hasFocus) {
					params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					lltBottom.setLayoutParams(params);
					rltChatViewPage.setVisibility(View.GONE);
				}
			}
		});

		messageArrayList = new ArrayList<ChatHistoryData>();
		adapter = new ChatHistoryAdapter(getApplicationContext(),
				messageArrayList, profile_id, target_avatar);
		chatLv.setAdapter(adapter);
		chatLv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						0);
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				lltBottom.setLayoutParams(params);
				rltChatViewPage.setVisibility(View.GONE);
				return false;
			}
		});
		//typing();
	}

	private TextWatcher tw = null;
	private void typing(){
        tw = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
//                Spannable spannable = getSmiledText(getApplicationContext(), tbMessage.getText().toString(), true);
//				ChatActivity.tbMessage.setText(spannable);
//				ChatActivity.tbMessage.setSelection(spannable.length());
            	
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                    int arg2, int arg3) {
            
            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before,
                    int count) {
            	tbMessage.removeTextChangedListener(tw);
            	Spannable spannable = getSmiledText(getApplicationContext(), tbMessage.getText().toString(), true);
				tbMessage.setText(spannable);
				tbMessage.setSelection(spannable.length());
                tbMessage.addTextChangedListener(tw);
            }
        };
        tbMessage.addTextChangedListener(tw);
    }
	
	private void initEmoticonPage() {
		try {
			emoticonScreenAdapter = new EmoticonScreenAdapter(this);
			stickerScreenAdapter = new StickerScreenAdapter(this);
			 
	        mPager = (ViewPager)findViewById(R.id.activity_chat_viewpage_emoticon);
	        mIndicator = (TabPageIndicator)findViewById(R.id.indicator);
		} catch (Exception ex) {
			
		}
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_chat_fltTop_imgbtnInfoProfile:
				if (!isPressInfoProfile) {
					pd = new ProgressDialog(ChatActivity.this);
					pd.setMessage(getString(R.string.txt_loading));
					pd.setCancelable(false);
					pd.show();
					isPressInfoProfile = true;
					GetOtherProfile loader2 = new GetOtherProfile(
							Constants.GET_HANGOUT_PROFILE, ChatActivity.this,
							profile_id, snapShotData);
					getRequestQueue().addRequest(loader2);
				}
				break;
			case R.id.activity_chat_fltTop_imgbtnBack:
				
				editor.putBoolean(Constants.isLoadChat, true);
				editor.commit();
				SetReadMessages loader3 = new SetReadMessages(
						Constants.SET_READ_MESSAGES, ChatActivity.this,
						profile_id);
				getRequestQueue().addRequest(loader3);

				ListChatOperation listChatDb = new ListChatOperation(ChatActivity.this);
				if(status ==2){
					listChatDb.updateReadMessage(profile_id);
					
		            editor.putBoolean(Constants.IS_LOAD_CHAT_AGAIN, true);
		            editor.commit();
				}
				if (isLoadFromNoti) {
					Intent intent = new Intent(ChatActivity.this,
							SlidingActivity.class);
					startActivity(intent);
					finish();
				} else {
					SlidingMenuActivity.getTotalNotification(listChatDb.getTotalNotification());
					finish();
				}
				break;
			case R.id.activity_chat_rtlbottom_btSend:
				if (xmpp != null && !xmpp.isConnected()) {
					try {
						StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
								.permitAll().build();
						StrictMode.setThreadPolicy(policy);
						xmpp.connect();
					} catch (XMPPException e) {
						e.printStackTrace();
					}
				}
				chatLv.setVisibility(View.VISIBLE);
				lltMatch.setVisibility(View.GONE);
				editor.putBoolean(Constants.IS_LOAD_CHAT_AGAIN, true);
	            editor.commit();
				solveSendMessage();
				break;
			case R.id.activity_chat_fltTop_imgbtnReport:
				solveEditBtn();
				break;
			case R.id.activity_chat_rtlbottom_btSmile:
				rltChatViewPage.setVisibility(View.GONE);
				if (!isShowSmile) {
					isShowSmile = true;
					isShowSticker = false;
					showSmile();
				} else {
					isShowSmile = false;
					params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					lltBottom.setLayoutParams(params);
					rltChatViewPage.setVisibility(View.GONE);
				}
				break;
			case R.id.activity_chat_rtlbottom_btSticker:
				rltChatViewPage.setVisibility(View.GONE);
				if (!isShowSticker) {
					isShowSticker = true;
					isShowSmile = false;
					showSticker();
				} else {
					isShowSticker = false;
					params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					lltBottom.setLayoutParams(params);
					rltChatViewPage.setVisibility(View.GONE);
				}
				break;
			}

		}

		private void showSticker() {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

			mPager.setAdapter(stickerScreenAdapter);
	        mIndicator.setViewPager(mPager);
	        mIndicator.notifyDataSetChanged();
	        mIndicator.setCurrentItem(1);
			params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
			params.addRule(RelativeLayout.ABOVE,
					 R.id.activity_chat_viewpage);
			lltBottom.setLayoutParams(params);
			rltChatViewPage.setVisibility(View.VISIBLE);
		}

		private void showSmile() {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

			mPager.setAdapter(emoticonScreenAdapter);
	        mIndicator.setViewPager(mPager);
	        mIndicator.notifyDataSetChanged();
	        mIndicator.setCurrentItem(1);
			params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
			params.addRule(RelativeLayout.ABOVE,
						 R.id.activity_chat_viewpage);
			lltBottom.setLayoutParams(params);
			rltChatViewPage.setVisibility(View.VISIBLE);
		}
	};

	public void solveEditBtn() {
		final String[] stringList = getResources().getStringArray(
				R.array.report_user_reasons);
		final ArrayList<RadioButton> radioButtons;
		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(ChatActivity.this);
		final AlertDialog dialog = builder.create();// new
													// Dialog(ProfileSettingActivity.this);
		final LayoutInflater inflater = LayoutInflater.from(this);
		View mainRelativeLayout = inflater.inflate(
				R.layout.dialog_report_spam_input, null);
		dialog.setView(mainRelativeLayout, 0, 0, 0, 0);

		final RadioGroup radioGroup = (RadioGroup) mainRelativeLayout
				.findViewById(R.id.radioGroup1);
		radioGroup.setOrientation(RadioGroup.VERTICAL);
		radioButtons = new ArrayList<RadioButton>();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		final EditText editText = (EditText) mainRelativeLayout
				.findViewById(R.id.editText);
		// lp.setMargins(0, 5, 0, 5);
		for (int i = 0; i < stringList.length; i++) {
			RadioButtonCustom radio = new RadioButtonCustom(this,
					getResources().getDrawable(R.drawable.radiogroup_selector2));
			radio.setText(stringList[i]);
			radio.setTextColor(Color.BLACK);
			radio.setLayoutParams(new RadioGroup.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			int padding = (int) OakClubUtil.convertDpToPixel(12, this);
			radio.setPadding(padding, padding, padding, padding);
			// radio.setBackgroundResource(R.drawable.white_purple_btn_);

			if (radio.getText().equals(
					getApplicationContext().getString(
							R.string.txt_report_user_other))) {
				radio.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						editText.setVisibility(View.VISIBLE);
						editText.requestFocus();
						InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
						imm.showSoftInput(editText,
								InputMethodManager.SHOW_IMPLICIT);
						isOtherReport = true;
					}
				});
			} else {
				radio.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						editText.setVisibility(View.GONE);
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(editText.getWindowToken(),
								0);
						isOtherReport = false;
					}
				});
			}

			radioButtons.add(radio);
			radioGroup.addView(radio);
			ImageView separator = new ImageButton(this);
			separator.setBackgroundResource(R.drawable.separators);
			separator.setLayoutParams(new RadioGroup.LayoutParams(
					LayoutParams.MATCH_PARENT, 1));
			radioGroup.addView(separator);

		}

		Button okBtn;
		okBtn = (Button) mainRelativeLayout.findViewById(R.id.ok_btn);
		okBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int selectedButtonId = radioGroup.getCheckedRadioButtonId();
				int selectedId = -1;
				for (int i = 0; i < radioButtons.size(); i++) {
					if (radioButtons.get(i).getId() == selectedButtonId) {
						selectedId = i;
						if (isOtherReport) {
							content = editText.getText().toString();
						} else {
							content = radioButtons.get(i).getText().toString();
						}
						break;
					}
				}
				if (selectedId != -1) {
					showBlockConfirmDialog(
							getResources().getString(
									R.string.txt_report_message),
							getResources()
									.getString(R.string.txt_report_anyway),
							true);
				}
				dialog.dismiss();
			}
		});
		Button btnBlock = (Button) mainRelativeLayout
				.findViewById(R.id.dialog_report_spam_input_block_user);
		btnBlock.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
				showBlockConfirmDialog(
						getResources().getString(R.string.txt_block_message),
						getResources().getString(R.string.txt_block_anyway),
						false);
				dialog.dismiss();
			}
		});

		Button btnCancel = (Button) mainRelativeLayout
				.findViewById(R.id.dialog_report_spam_input_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setCancelable(true);
		dialog.show();
	}

	public void showBlockConfirmDialog(String tvQuestion, String btName,
			final boolean isReport) {
		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(ChatActivity.this);
		final AlertDialog dialog = builder.create();// new
													// Dialog(ProfileSettingActivity.this);
		final LayoutInflater inflater = LayoutInflater.from(this);
		View mainRelativeLayout = inflater.inflate(
				R.layout.dialog_report_spam_confirm, null);
		dialog.setView(mainRelativeLayout, 0, 0, 0, 0);
		TextView tvQues = (TextView) mainRelativeLayout
				.findViewById(R.id.dialog_report_spam_confirm_tvquestion);
		tvQues.setText(tvQuestion);
		Button okBtn;
		okBtn = (Button) mainRelativeLayout.findViewById(R.id.ok_btn);
		okBtn.setText(btName);
		okBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isReport) {
					ReportUserRequest loader = new ReportUserRequest(
							"reportUser", ChatActivity.this, profile_id,
							content);
					getRequestQueue().addRequest(loader);
				} else {

                	ListChatOperation listChatDb = new ListChatOperation(ChatActivity.this);
                	listChatDb.deleteListChat(profile_id);
                	
					editor.putBoolean(Constants.IS_LOAD_CHAT_AGAIN, true);
					editor.commit();
					
					BlockUserRequest loader = new BlockUserRequest("blockUser",
							ChatActivity.this, profile_id);
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
			if (content.trim().equals(""))
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
			
            editor.putBoolean(Constants.IS_LOAD_CHAT_AGAIN, true);
            editor.commit();
			
			
			tbMessage.setText("");
			ChatHistoryData message = new ChatHistoryData();
			message.setBody(content);
			message.setFrom(user_id);
			message.setTo(profile_id);
			SimpleDateFormat df = new SimpleDateFormat(
					Constants.CHAT_SERVER_FORMAT);
			String formattedDate = df.format(new Date());
			message.setTime_string(formattedDate);
			            
            ListChatOperation listChatdb = new ListChatOperation(ChatActivity.this);
            ListChatData data = listChatdb.getChatData(profile_id);
            data.setLast_message(content);
            data.setLast_message_time(formattedDate);
            data.setLast_active_time(formattedDate);
            data.setStatus(3);
            listChatdb.updateListChat(data);
			
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

	public void solveSendMessage(String content) {
		try {
			if (content.trim().equals(""))
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

            editor.putBoolean(Constants.IS_LOAD_CHAT_AGAIN, true);
            editor.commit();
			
			ChatHistoryData message = new ChatHistoryData();
			message.setBody(content);
			message.setFrom(user_id);
			message.setTo(profile_id);
			SimpleDateFormat df = new SimpleDateFormat(
					Constants.CHAT_CLIENT_FORMAT);
			String formattedDate = df.format(new Date());
			message.setTime_string(formattedDate);

            ListChatOperation listChatdb = new ListChatOperation(ChatActivity.this);
            ListChatData data = listChatdb.getChatData(profile_id);
            data.setLast_message(content);
            data.setLast_message_time(formattedDate);
            data.setLast_active_time(formattedDate);
            data.setStatus(3);
            listChatdb.updateListChat(data);
			
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

		public ChatHistoryRequest(Object key, Activity activity,
				String profile_id) {
			super(key, activity);
			this.profile_id = profile_id;
		}

		@Override
		public void execute() throws Exception {
//			setMap(oakClubApiTemp.getChatHistory(profile_id));
			obj = oakClubApi.getHistoryMessages(profile_id);
		}

		@Override
		public void executeUI(Exception ex) {
			progressBar.setVisibility(View.GONE);
			if (obj == null || !obj.isStatus()) {
				// Toast.makeText(ChatActivity.this,
				// getString(R.string.abnormal_error_message),
				// Toast.LENGTH_SHORT).show();
			} else {
				if (target_name == null || target_name.length() == 0) {
					target_name = obj.getUsername();
					target_avatar = obj.getAvatar();
					tvName.setText("" + target_name);
				}
				
				
				
				messageArrayList.clear();
				messageArrayList.addAll(obj.getData());
				if(obj.getData() != null && obj.getData().size() != 0){
					chatLv.setVisibility(View.VISIBLE);
					lltMatch.setVisibility(View.GONE);
				}
				Collections.sort(messageArrayList,
						new Comparator<ChatHistoryData>() {
							@Override
							public int compare(ChatHistoryData data1,
									ChatHistoryData data2) {
								return (data1.getTime_string().compareTo(data2
										.getTime_string()));
							}
						});
				adapter.notifyDataSetChanged();
				chatLv.setSelection(adapter.getCount() - 1);
			}
			
//			
//			if(getMap()==null|| !getMap().get("errorCode").equals(0)){
//				
//			}
//			else{
//				HashMap<String, Object> object = (HashMap<String, Object>) getMap().get("data");
//				ParseDataChatHistory parse = new ParseDataChatHistory(object);
//				ArrayList<ChatHistoryData> listData = new ArrayList<ChatHistoryData>();
//				listData = parse.getList();
//				messageArrayList.clear();
//				if(listData!=null && listData.size()>0){
//					messageArrayList.addAll(listData);
//					chatLv.setVisibility(View.VISIBLE);
//					lltMatch.setVisibility(View.GONE);
//					Collections.sort(messageArrayList,
//							new Comparator<ChatHistoryData>() {
//								@Override
//								public int compare(ChatHistoryData data1,
//										ChatHistoryData data2) {
//									return (data1.getTime_string().compareTo(data2
//											.getTime_string()));
//								}
//							});
//					adapter.notifyDataSetChanged();
//					chatLv.setSelection(adapter.getCount() - 1);
//				}
//			}
		}
	}

	public class SendMessageLoader extends RequestUI {
		SendChatReturnObject obj;
		String profileId;
		String message;

		public SendMessageLoader(Object key, Activity activity, String profileId,
				String message) {
			super(key, activity);
			this.profileId = profileId;
			this.message = message;
		}

		@Override
		public void execute() throws Exception {
			obj = oakClubApi.SendChatMessage(profileId, message);
//			setMap(oakClubApiTemp.sendChatMessage(profileId, message));
		}

		@Override
		public void executeUI(Exception ex) {

			if (obj != null && obj.isStatus()) {
				 
			} else {
				
			}

//			if(getMap()==null|| !getMap().get("errorCode").equals(0)){
//				
//			}
		}
	}

	private class ReportUserRequest extends RequestUI {
		String profile_id;
		String content;

		public ReportUserRequest(Object key, Activity activity,
				String profile_id, String content) {
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

	private class BlockUserRequest extends RequestUI {
		String profile_id;

		public BlockUserRequest(Object key, Activity activity, String profile_id) {
			super(key, activity);
			this.profile_id = profile_id;
		}

		@Override
		public void execute() throws Exception {
			oakClubApi.BlockUserById(profile_id);
		}

		@Override
		public void executeUI(Exception ex) {
			finish();
		}

	}

	public static String getXMLString(String content) {
	    content = content.replace("&amp", "&");
	    content = content.replace("&lt;", "<");
	    content = content.replace("&gt;", ">");
	    content = content.replace("&apos;", "'");
	    content = content.replace("&quot;", "\"");
	    return content;
	}
	
	public static Spannable getSmiledText(Context context, String text, boolean isPress) {
		//text = getXMLString(text);
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		builder = new SpannableStringBuilder(text);
		int index;
		for (index = builder.length(); index >= 0; index--) {
			int length = 0;
			boolean flag = false;
			ImageSpan imageSpan = null;
			for (Entry<String, String> entry : EmoticonScreenAdapter.emoticons
					.entrySet()) {
				String entryKey = entry.getKey();//Html.fromHtml(entry.getKey()).toString();
				int lengthEntry = entryKey.length();
				if (index - lengthEntry < 0)
					continue;
				if (builder.subSequence(index - lengthEntry, index).toString().toLowerCase()
						.equals(entryKey.toLowerCase())) {
					if (length < lengthEntry) {
						builder.removeSpan(imageSpan);
						if (isPress) {
							Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), Integer.parseInt(entry.getValue()));
							int size = (int) OakClubUtil.convertPixelsToDp((float) (OakClubUtil.getWidthScreen(context)/15 * 1.75), context);
							Bitmap scaledbmp=Bitmap.createScaledBitmap(
									bitmap, (int) (tbMessage.getTextSize() * 1.75), (int) (tbMessage.getTextSize() * 1.75), false);
							imageSpan = new ImageSpan(scaledbmp, ImageSpan.ALIGN_BASELINE);
						} else {
							imageSpan = new ImageSpan(context, Integer.parseInt(entry.getValue()));
						}
						builder.setSpan(imageSpan, index - lengthEntry, index,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						flag = true;
						length = entryKey.length();
					}
					// break;
				}
			}
			if (flag) {
				index -= length - 1;
			}
		}
		return builder;
	}

	@Override
	public void onBackPressed() {
		SetReadMessages loader3 = new SetReadMessages(
				Constants.SET_READ_MESSAGES, ChatActivity.this, profile_id);
		getRequestQueue().addRequest(loader3);

		ListChatOperation listChatDb = new ListChatOperation(ChatActivity.this);
		if(status == 2){
			listChatDb.updateReadMessage(profile_id);
			
            editor.putBoolean(Constants.IS_LOAD_CHAT_AGAIN, true);
            editor.commit();
		}
		if (isShowSmile || isShowSticker) {
			isShowSmile = false;
			isShowSticker = false;
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			lltBottom.setLayoutParams(params);
			rltChatViewPage.setVisibility(View.GONE);
		}else if (isLoadFromNoti) {
			Intent intent = new Intent(ChatActivity.this,
					SlidingActivity.class);
			startActivity(intent);
			finish();
		} else {
			finish();
		}
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
		long time = 0;
		if (dateMatchTime != null && dateNow != null) {
			time = (dateNow.getTime() - dateMatchTime.getTime()) / (1000 * 60);
		}
		if (time <= 0)
			time = 1;

		if (time < 59) {
			result = String.format(getString(R.string.txt_minutes_ago), time);
			if ((int)time == 1) {
				result = getString(R.string.txt_one_minutes_ago);
			}
		}
		else if (time < 60 * 60) {
			result = String.format(getString(R.string.txt_hour_ago), time / 60);
			if ((int)(time / 60) == 1) {
				result = getString(R.string.txt_one_hour_ago);
			}
		}
		else if (time < 60 * 60 * 24) {
			result = String.format(getString(R.string.txt_day_ago), time / (60 * 24));
			if ((int)(time / (60 * 24)) == 1) {
				result = getString(R.string.txt_one_day_ago);
			}
		}
		else if (time < 60 * 60 * 24 * 30) {
			result = String.format(getString(R.string.txt_month_ago), time / (60 * 24 * 30));
			if ((int)(time / (60 * 24 * 30)) == 1) {
				result = getString(R.string.txt_one_month_ago);
			}
		}
		else {
			result = String.format(getString(R.string.txt_year_ago), time / (60 * 24 * 30 * 12));
			if ((int)(time / (60 * 24 * 30 * 12)) == 1) {
				result = getString(R.string.txt_one_year_ago);
			}
		}

		return result;
	}

	class GetOtherProfile extends RequestUI {

		HangoutProfileOtherReturnObject data = new HangoutProfileOtherReturnObject();
		String profile_id;

		public GetOtherProfile(Object key, Activity activity,
				String profile_id, SnapshotData snapShotData) {
			super(key, activity);
			this.profile_id = profile_id;
		}

		@Override
		public void execute() throws Exception {
			data = oakClubApi.getHangoutProfileOther(profile_id);
		}

		@Override
		public void executeUI(Exception ex) {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			if (data != null) {
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
				snapShotData
						.setBirthday_date(data.getData().getBirthday_date());

				float lat = Float.parseFloat(data.getData().getLocation()
						.getCoordinates().latitude);
				float lon = Float.parseFloat(data.getData().getLocation()
						.getCoordinates().longitude);

				float latitude = Float
						.parseFloat(ProfileSettingFragment.profileInfoObj
								.getLocation().getCoordinates().latitude);
				float longitude = Float
						.parseFloat(ProfileSettingFragment.profileInfoObj
								.getLocation().getCoordinates().longitude);

				float distance = 0;
				try {
					distance = (float) (6371 * Math.acos(Math.cos(Math
							.toRadians(lat))
							* Math.cos(Math.toRadians(latitude))
							* Math.cos(Math.toRadians(longitude)
									- Math.toRadians(lon))
							+ Math.sin(Math.toRadians(lat))
							* Math.sin(Math.toRadians(latitude))));
				} catch (Exception e) {

				}

				snapShotData.setDistance(distance);
				snapShotData
						.setHometown_name(data.getData().getHometown_name());
				snapShotData.setInterested(data.getData().getInterested());
				snapShotData.setIs_like(data.getData().getIs_like());
				snapShotData.setLike("" + data.getData().getLike());
				snapShotData
						.setLocation_name(data.getData().getLocation_name());
				snapShotData.setMutual_friends(data.getData()
						.getMutual_friends());
				snapShotData.setName(data.getData().getName());
				snapShotData.setPhotos(data.getData().getPhotos());
				snapShotData.setProfile_id(data.getData().getProfile_id());
				snapShotData.setSchool(data.getData().getSchool());
				snapShotData.setShare_interests(data.getData()
						.getShare_interests());
				snapShotData.setVideo_link(data.getData().getVideo_link());
				snapShotData.setViewed("" + data.getData().getViewed());
				snapShotData.setWork(data.getData().getWork());

				Intent intent = new Intent(getApplicationContext(),
						InfoProfileOtherActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				Bundle b = new Bundle();
				b.putSerializable("SnapshotDataBundle", snapShotData);
				b.putString("Activity", "SnapshotActivity");
				intent.putExtras(b);
				startActivityForResult(intent, 1);
				isPressInfoProfile = false;
			} else {
				OakClubUtil.enableDialogWarning(ChatActivity.this,
						getString(R.string.txt_warning),
						getString(R.string.txt_internet_message));
			}
		}
	}

	class SetReadMessages extends RequestUI {
		String profileId;

		public SetReadMessages(Object key, Activity activity, String profileId) {
			super(key, activity);
			this.profileId = profileId;
		}

		@Override
		public void execute() throws Exception {
			oakClubApi.SetReadMessages(profile_id);
//			setMap(oakClubApiTemp.readChatMessage(profileId));
		}

		@Override
		public void executeUI(Exception ex) {
//			if(getMap()==null|| !getMap().get("errorCode").equals(0)){
//				
//			}
		}
	}

	protected void solveReceiveNewMessage(Message msg) {
		ChatHistoryData message = new ChatHistoryData();
		message.setBody(msg.getBody());
		String str;
		str = msg.getTo();
		str = str.substring(0, str.indexOf('@'));

		message.setTo(str);
		str = msg.getFrom();
		str = str.substring(0, str.indexOf('@'));

		message.setFrom(str);
		SimpleDateFormat df = new SimpleDateFormat(Constants.CHAT_CLIENT_FORMAT);
		String formattedDate = df.format(new Date());
		message.setTime_string(formattedDate);
		updateNewMessage(message);
	}

	private void updateNewMessage(final ChatHistoryData message) {

		if (ChatActivity.profile_id != null
				&& ChatActivity.profile_id.equals(message.getFrom())) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ChatActivity.chatLv.setVisibility(View.VISIBLE);
					ChatActivity.lltMatch.setVisibility(View.GONE);
					ChatActivity.messageArrayList.add(message);
					ChatActivity.adapter.notifyDataSetChanged();
					ChatActivity.chatLv.setSelection(ChatActivity.adapter
							.getCount() - 1);
				}
			});
		}
		
		ListChatOperation listChatDb = new ListChatOperation(this);
		if(!listChatDb.checkProfileExist(message.getFrom())){
			GetOtherProfileFromMessage loader2 = new GetOtherProfileFromMessage(
					Constants.GET_HANGOUT_PROFILE, ChatActivity.this,
					message.getFrom(), message);
			getRequestQueue().addRequest(loader2);
		}
		else{
			ListChatData data = listChatDb.getChatData(message.getFrom());
			data.setLast_message(message.getBody());
			data.setLast_message_time(message.getTime_string());
			data.setLast_active_time(message.getTime_string());
			Log.v("chat activi", isActive + "");
			if(ChatActivity.isActive && ChatActivity.profile_id != null && ChatActivity.profile_id.equals(message.getFrom())){
				data.setStatus(3);
				listChatDb.updateReadMessage(data);
			}
			else {
				data.setUnread_count(data.getUnread_count()+1);
				data.setStatus(2);
				listChatDb.updateNewMessage(data);
			}
		}
		
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ChatBaseActivity.updateListChat(ChatActivity.this);
				
			}
		});
	}
	
	class GetOtherProfileFromMessage extends RequestUI {

		HangoutProfileOtherReturnObject data = new HangoutProfileOtherReturnObject();
		String profile_id;
		ChatHistoryData message;

		public GetOtherProfileFromMessage(Object key, Activity activity,
				String profile_id, ChatHistoryData message) {
			super(key, activity);
			this.profile_id = profile_id;
			this.message = message;
		}

		@Override
		public void execute() throws Exception {
			data = oakClubApi.getHangoutProfileOther(profile_id);
		}

		@Override
		public void executeUI(Exception ex) {
			if (data != null) {
				ListChatData newMessage = new ListChatData();
				newMessage.setProfile_id(message.getFrom());
				newMessage.setName(data.getData().getName());
				newMessage.setAvatar(data.getData().getAvatar());
				newMessage.setLast_message(message.getBody());
				newMessage.setLast_message_time(message.getTime_string());
				newMessage.setLast_active_time(message.getTime_string());
				newMessage.setStatus(2);
				newMessage.setMatches(false);
				newMessage.setUnread_count(1);
				
				ListChatOperation listChatDb = new ListChatOperation(ChatActivity.this);
				listChatDb.insertListChat(newMessage);
								
				AllChatActivity.allList.clear();
				AllChatActivity.allList.addAll(listChatDb.getListChat());
				AllChatActivity.adapterAll.ignoreDisabled=true;
				AllChatActivity.adapterAll.notifyDataSetChanged();

				MatchChatActivity.matchedList.clear();
				MatchChatActivity.matchedList.addAll(listChatDb.getListMatch());
				MatchChatActivity.adapterMatch.ignoreDisabled=true;
				MatchChatActivity.adapterMatch.notifyDataSetChanged();

				VIPActivity.vipList.clear();
				VIPActivity.vipList.addAll(listChatDb.getListVip());
				VIPActivity.adapterVip.ignoreDisabled=true;
				VIPActivity.adapterVip.notifyDataSetChanged();
					
				SlidingMenuActivity.getTotalNotification(listChatDb.getTotalNotification());
				
	            
			} else {
				OakClubUtil.enableDialogWarning(ChatActivity.this,
						getString(R.string.txt_warning),
						getString(R.string.txt_internet_message));
			}
		}
	}

	class SendRegisterRequest extends RequestUI {
		SendRegisterReturnObject obj;
		String user_id;
		String access_token;

		public SendRegisterRequest(Object key, Activity activity,
				String user_id, String access_token) {
			super(key, activity);
			this.user_id = user_id;
			this.access_token = access_token;
		}

		@Override
		public void execute() throws Exception {
			obj = oakClubApi.sendRegister(user_id, access_token, "3", appVer,
					nameDevice, android_token);
		}

		@Override
		public void executeUI(Exception ex) {
			if (pdLogin != null && pdLogin.isShowing()) {
				pdLogin.dismiss();
			}
			if (obj == null
					|| (!obj.isStatus() && obj.getError_status().equals("1"))) {
				OakClubUtil.enableDialogWarning(ChatActivity.this,
						getResources().getString(R.string.txt_warning),
						getResources().getString(R.string.txt_signin_failed));
			} else {
				ProfileSettingFragment.profileInfoObj = obj.getData();

				// Register custom Broadcast receiver to show messages on
				// activity
				registerGCM();

				user_id = obj.getData().getProfile_id();

				GetDataLanguageLoader loader2 = new GetDataLanguageLoader(
						"getDataLanguage", ChatActivity.this);
				loader2.setPriority(RequestUI.PRIORITY_LOW);
				getRequestQueue().addRequest(loader2);
			}
		}
	}

	class GetDataLanguageLoader extends RequestUI {
		GetDataLanguageReturnObject obj;

		public GetDataLanguageLoader(Object key, Activity activity) {
			super(key, activity);
		}

		@Override
		public void execute() throws Exception {
			obj = oakClubApi.GetDataLanguage();
		}

		@Override
		public void executeUI(Exception ex) {
			if (obj != null && obj.getData() != null) {
				SlidingMenuActivity.mDataLanguageObj = obj.getData();
			}
		}

	}

    class SetViewMuatualEvent extends RequestUI {
        private String proId = "";

        public SetViewMuatualEvent(Object key, Activity activity, String proId) {
            super(key, activity);
            this.proId = proId;
        }

        @Override
        public void execute() throws Exception {
            oakClubApi.SetViewedMutualMatch(proId);
        }

        @Override
        public void executeUI(Exception ex) {

        }

    }
    
    class GetConfig extends RequestUI {
        GetConfigData obj;

        public GetConfig(Object key, Activity activity) {
            super(key, activity);
        }

        @Override
        public void execute() throws Exception {
            obj = oakClubApi.GetConfig();
        }

        @Override
        public void executeUI(Exception ex) {
            if (obj != null && obj.getData() != null) {
            	HashMap<String, String> stickers = new HashMap<String, String>();
                for (int i = 0; i < obj.getData().getStickers().size(); i++) {
                    stickers.put(obj.getData().getStickers().get(i).getSymbol_name(), obj.getData().getStickers().get(i).getImage());
                }
                StickerScreenAdapter.stickers.add(stickers);
                initEmoticonPage();
            }
        }
    }
}
