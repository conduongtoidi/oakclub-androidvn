package com.oakclub.android.model.adaptercustom;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oakclub.android.image.SmartImageView;
import com.oakclub.android.ChatActivity;
import com.oakclub.android.R;
import com.oakclub.android.model.ChatHistoryData;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ChatHistoryAdapter extends BaseAdapter {

	ArrayList<ChatHistoryData> list;
	Context context;
	String profile_id;
	String target_avatar;

	public ChatHistoryAdapter(Context context, ArrayList<ChatHistoryData> list,
			String profile_id, String target_avatar) {
		this.list = list;
		this.context = context;
		this.profile_id = profile_id;
		this.target_avatar = target_avatar;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	public int getItemViewType(int position) {
		if (list.get(position).getFrom() != null) {
			if (list.get(position).getFrom().equals(profile_id)) {
				return 0;
			} else {
				return 1;
			}
		}
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		int type = getItemViewType(position);
		ChatHistoryData item;
		item = list.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			if (type == 0) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_chat_list_left, null);
				holder.userAvatar = (SmartImageView) convertView
						.findViewById(R.id.user_avatar_left);
				holder.leftTv = (TextView) convertView
						.findViewById(R.id.message_content_left);
				holder.timeTv = (TextView) convertView
						.findViewById(R.id.timeDisplay);
				holder.leftImg = (ImageView) convertView
						.findViewById(R.id.image_content_left);
			} else {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_chat_list_right, null);
				holder.rightTv = (TextView) convertView
						.findViewById(R.id.message_content_right);
				holder.rightImg = (ImageView) convertView
						.findViewById(R.id.image_content_right);
				holder.timeTv = (TextView) convertView
						.findViewById(R.id.timeDisplay);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String text = item.getBody();
		Spannable spannable = ChatActivity.getSmiledText(context,
				item.getBody(), false);
		
		String pathImg = "<img src=\"([^\"]+)";
		Matcher matcher = Pattern.compile(pathImg).matcher(text);
		
		if (type == 0) {
			String url = OakClubUtil.getFullLink(context, target_avatar);
			SmartImageView imgAva = holder.userAvatar;
			OakClubUtil.loadImageFromUrl(context, url, imgAva, "");
			holder.leftTv.setText(spannable);
			ImageView imgView = holder.leftImg;
			TextView textView = holder.leftTv;
			if (imgView != null)
				getSticker(position, item, text, matcher, imgView, textView);
		} else {
			holder.rightTv.setText(spannable);
			ImageView imgView = holder.rightImg;
			TextView textView = holder.rightTv;
			if (imgView != null)
				getSticker(position, item, text, matcher, imgView,
					textView);
		}
		holder.timeTv.setText(item.getTime_string());
		return convertView;
	}

	private void getSticker(int position, ChatHistoryData item, String text, Matcher matcher, ImageView imgView,
			TextView textView) {
		
		String pathSticker = "";
		try {
			pathSticker = Constants.dataConfig.getConfigs().getSticker().getUrl();//"/bundles/likevnblissdate/v3/chat/images/stickers/";
		} catch(Exception ex) {
			
		}
		String pathSticker_Cat = "";
		try {
			pathSticker_Cat = Constants.dataConfig.getConfigs().getCats().getUrl();//"/bundles/likevnblissdate/v3/chat/images/sticker_cats/";
		} catch (Exception ex) {
			
		}
		imgView.setVisibility(View.GONE);
		textView.setVisibility(View.VISIBLE);
		try {
			if (Html.fromHtml(item.getBody()).toString().length() > 1) {
				text = Html.fromHtml(item.getBody()).toString();
			}
			while (matcher.find()) {
				if (text.contains("type=\"sticker\"")){
					String imgName = matcher.group(1).replace(pathSticker, "").split("\"")[0];
					imgName = imgName.replace(pathSticker_Cat, "");
					String imgLink = matcher.group(1);
					Log.v("img right", imgName + " " + position);
					imgView.setVisibility(View.VISIBLE);
					textView.setVisibility(View.GONE);
					if (ChatActivity.bitmapSticker.isEmpty() || !ChatActivity.bitmapSticker.containsKey(imgName.replace(".png", ""))) {
						String urlImg = OakClubUtil
								.getFullLinkStickerOrGift(context, imgLink);
			            OakClubUtil.loadStickerFromUrl(context, urlImg, imgView, imgName.replace(".png", ""));
			        } else {
			        	imgView.setImageBitmap(ChatActivity.bitmapSticker.get(imgName.replace(".png", "")));
			        }
					int widthScreen = (int) OakClubUtil.getWidthScreen(context);
					LayoutParams params = new LayoutParams(widthScreen/3, widthScreen/3);
					imgView.setLayoutParams(params);
				} else {
					String giftOld = "/bundles/likevnhangout/images/gift/";
					String giftNew = "/bundles/likevnblissdate/v3/chat/images/gifts/";
					if (matcher.group(1).contains(giftOld) || matcher.group(1).contains(giftNew)) {
						try {
							String img = "";
							if (matcher.group(1).contains(giftOld)) {
								img = matcher.group(1).replace(giftOld, "").split("\"")[0].replace(
									".png", "");
								
								textView.setVisibility(View.GONE);
								imgView.setVisibility(View.VISIBLE);
								imgView.setImageResource(context.getResources()
										.getIdentifier(img.toLowerCase(), "drawable",
												context.getPackageName()));
							} else if (matcher.group(1).contains(giftNew)) {
								img = matcher.group(1).replace(giftNew, "").split("\"")[0];
								textView.setVisibility(View.GONE);
								imgView.setVisibility(View.VISIBLE);
								
								if (ChatActivity.bitmapSticker.isEmpty() || !ChatActivity.bitmapSticker.containsKey(img.replace(".png", ""))) {
									String urlImg = OakClubUtil
											.getFullLinkStickerOrGift(context, giftNew + img);
						            OakClubUtil.loadStickerFromUrl(context, urlImg, imgView, img.replace(".png", ""));
						        } else {
						        	imgView.setImageBitmap(ChatActivity.bitmapSticker.get(img.replace(".png", "")));
						        }
								int widthScreen = (int) OakClubUtil.getWidthScreen(context);
								LayoutParams params = new LayoutParams(widthScreen/3, widthScreen/3);
								imgView.setLayoutParams(params);

							} 
						} catch (Exception e) {

						}
					}
				}
			}
		} catch (Exception ex) {

		}
	}

	class ViewHolder {
	    SmartImageView userAvatar;
		TextView leftTv;
		TextView rightTv;
		TextView timeTv;
		ImageView leftImg;
		ImageView rightImg;
	}

}
