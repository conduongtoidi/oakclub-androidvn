package com.oakclub.android.model.adaptercustom;

import java.net.URL;
import java.util.ArrayList;

import com.google.android.gms.internal.ho;
import com.oakclub.android.R;
import com.oakclub.android.base.OakClubBaseActivity;
import com.oakclub.android.model.ListChatData;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.view.CircleImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterListChat extends BaseAdapter {

	private Context mContext;
	private ArrayList<ListChatData> mListChatData;

	public AdapterListChat(Context context, ArrayList<ListChatData> data) {
		this.mContext = context;
		this.mListChatData = data;
	}

	@Override
	public int getCount() {
		return mListChatData.size();
	}

	@Override
	public Object getItem(int position) {
		return mListChatData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mListChatData.indexOf((getItem(position)));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListChatHolder holder = null;
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ListChatHolder();
			convertView = mInflater.inflate(R.layout.item_listview_listchat,
					parent, false);
			holder.imgAvatar = (CircleImageView) convertView
					.findViewById(R.id.item_listview_listchat_imgleft);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.item_listview_listchat_lltcontent_tvname);
			holder.tvTimeMatches = (TextView) convertView
					.findViewById(R.id.item_listview_listchat_lltcontent_tvmatches);
			holder.tvLastMessage = (TextView) convertView
					.findViewById(R.id.item_listview_listchat_lltcontent_tvlastchat);
			holder.imgChatMutual = (ImageView) convertView
					.findViewById(R.id.item_listview_listchat_imgright);

			convertView.setTag(holder);
		} else
			holder = (ListChatHolder) convertView.getTag();
		String url = mListChatData.get(position).getAvatar();
		if (url != null)
			OakClubUtil.loadImageFromUrl(mContext, holder.imgAvatar,
				OakClubUtil.getFullLink(mContext, url));
		holder.tvName.setText(mListChatData.get(position).getName());
		
		switch (mListChatData.get(position).getStatus()) {
		case 0:
		case 1:
			try {
			holder.tvTimeMatches.setText("Matched on "
					+ mListChatData.get(position).getMatch_time()
							.split("/")[1]
					+ "/"
					+ mListChatData.get(position).getMatch_time()
							.split("/")[0]);
			} catch (Exception ex) {
				
			}
			break;
		case 2:
		case 3:
			try {
				holder.tvTimeMatches.setText("Last message on "
						+ mListChatData.get(position).getLast_message_time()
								.split("/")[1]
						+ "/"
						+ mListChatData.get(position).getLast_message_time()
								.split("/")[0]);
			} catch (Exception ex) {

			}
			break;
		default:
			break;
		}

		holder.tvLastMessage.setText(mListChatData.get(position)
				.getLast_message());
		holder.tvLastMessage.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
		Log.v("Chat Status", mListChatData.get(position).getStatus() + "");
		holder.imgChatMutual
				.setImageResource(OakClubBaseActivity.mChatStatusIcon[mListChatData
						.get(position).getStatus()]);

		holder.imgChatMutual.setTag(OakClubBaseActivity.mChatStatusIcon[mListChatData
                        .get(position).getStatus()]);
		return convertView;
	}

	private static class ListChatHolder {
		public CircleImageView imgAvatar;
		public TextView tvName;
		public TextView tvTimeMatches;
		public TextView tvLastMessage;
		public ImageView imgChatMutual;
	}
}
