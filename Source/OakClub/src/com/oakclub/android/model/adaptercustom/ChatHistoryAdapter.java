package com.oakclub.android.model.adaptercustom;

import java.util.ArrayList;

import com.oakclub.android.ChatActivity;
import com.oakclub.android.R;
import com.oakclub.android.model.ChatHistoryData;
import com.oakclub.android.util.OakClubUtil;

import android.content.Context;
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

public class ChatHistoryAdapter extends BaseAdapter {

    ArrayList<ChatHistoryData> list;
    Context context;
    String profile_id;
    String target_avatar;

    public ChatHistoryAdapter(Context context, ArrayList<ChatHistoryData> list, String profile_id, String target_avatar) {
        this.list = list;
        this.context = context;
        this.profile_id = profile_id;
        this.target_avatar = target_avatar;
    }

    @Override
    public int getCount() {
        Log.v("Listsize", list.size() + "");
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
        Log.v("position", position + "");
        if (list.get(position).getFrom() != null){
            if (list.get(position).getFrom().equals(profile_id)) {
                return 0;
            } else {
                return 1;
            }
        }
        Log.v("null", "null");
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
                holder.userAvatar = (ImageView) convertView
                        .findViewById(R.id.user_avatar_left);
                holder.leftTv = (TextView) convertView
                        .findViewById(R.id.message_content_left);
                holder.timeTv = (TextView) convertView
                        .findViewById(R.id.timeDisplay);
            } else {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.item_chat_list_right, null);
                holder.rightTv = (TextView) convertView
                        .findViewById(R.id.message_content_right);
                holder.timeTv = (TextView) convertView
                        .findViewById(R.id.timeDisplay);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Spannable spannable = ChatActivity.getSmiledText(context, item.getBody());
        if (type == 0) {
            String url = OakClubUtil.getFullLink(context, target_avatar);
            ImageView imgAva = holder.userAvatar;
            OakClubUtil.loadImageFromUrl(context, url, imgAva);
            holder.leftTv.setText(spannable);
        } else {
            holder.rightTv.setText(spannable);
        }
        holder.timeTv.setText(item.getTime_string());
        return convertView;
    }
    
    class ViewHolder {
        ImageView userAvatar;
        TextView leftTv;
        TextView rightTv;
        TextView timeTv;
    }

}
