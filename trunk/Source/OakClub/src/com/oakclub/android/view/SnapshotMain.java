package com.oakclub.android.view;

import com.oakclub.android.R;
import com.oakclub.android.model.SnapshotData;
import com.oakclub.android.util.OakClubUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SnapshotMain extends FrameLayout {

    public SnapshotMain(Context context) {
        super(context);
        init();
    }

    public SnapshotMain(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SnapshotMain(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private View view;
    private LayoutParams fltParams;
    private RelativeLayout.LayoutParams rltParams;
    private int widthScreen;
    private int heightScreen;
    private RelativeLayout rltInfo;
    private RelativeLayout rltInfoRight;
    private FrameLayout fltImage;
    private ImageView imgAvatar;
    private ImageView imgFriend;
    private TextView tvName;
    private TextView tvNumFriend;
    private TextView tvMutualFriend;
    private TextView tvShareInterest;
    private TextView tvCountPhoto;
    private int widthImageInfo;
    private int heightRltInfo;
    private int paddingView;
    private void init(){
        widthScreen = (int) OakClubUtil.getWidthScreen(getContext());
        heightScreen = (int) OakClubUtil.getHeightScreen(getContext());
        paddingView = (int)OakClubUtil.convertDpToPixel(20, getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.layout_snapshot, null);
        view.setPadding(paddingView, paddingView, paddingView, paddingView);
        fltParams = (LayoutParams) view.getLayoutParams();
        this.addView(view);
        this.setBackgroundResource(R.drawable.polaroid_back);
        
        fltImage = (FrameLayout)view.findViewById(R.id.activity_snapshot_flt_body_flt_content_flt_image);
        fltParams = (LayoutParams) fltImage.getLayoutParams();
        rltInfo = (RelativeLayout) view.findViewById(R.id.activity_snapshot_flt_body_flt_content_rlt_info);
        rltInfoRight = (RelativeLayout)rltInfo.findViewById(R.id.activity_snapshot_flt_body_flt_content_rlt_info_right);
        rltParams = (RelativeLayout.LayoutParams) rltInfoRight.getLayoutParams();
        imgAvatar = (ImageView) fltImage.findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_avatar);
        imgFriend = (ImageView) rltInfoRight.findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_mutual_friend_disable);
        tvName = (TextView)rltInfo.findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_info);
        tvNumFriend = (TextView)rltInfoRight.findViewById(R.id.activity_snapshot_flt_body_flt_content_tv_numFriend);
        tvMutualFriend = (TextView)rltInfoRight.findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_mutual_friend);
        tvShareInterest = (TextView)rltInfoRight.findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_shareinterest);
        tvCountPhoto = (TextView)rltInfoRight.findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_photo);
        

        
    }
    
    public void loadData(SnapshotData data){
        
        ViewTreeObserver vto = rltInfo.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rltInfo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                rltParams = new RelativeLayout.LayoutParams(imgFriend.getMeasuredWidth()*6, LayoutParams.WRAP_CONTENT);
                rltParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                rltParams.width =imgFriend.getMeasuredWidth()*6;
                rltInfoRight.setLayoutParams(rltParams);
                
                fltParams.bottomMargin = imgFriend.getMeasuredHeight() + tvMutualFriend.getMeasuredHeight() + paddingView;
                fltImage.setLayoutParams(fltParams);
            }
        });
        
    }
}
