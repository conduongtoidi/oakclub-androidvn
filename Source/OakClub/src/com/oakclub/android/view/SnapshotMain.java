package com.oakclub.android.view;

import java.text.DateFormat;
import java.util.Date;

import com.oakclub.android.AllChatActivity;
import com.oakclub.android.ChatActivity;
import com.oakclub.android.InfoProfileOtherActivity;
import com.oakclub.android.MatchChatActivity;
import com.oakclub.android.R;
import com.oakclub.android.VIPActivity;
import com.oakclub.android.VideoViewActivity;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.fragment.SnapshotFragment;
import com.oakclub.android.helper.operations.ListChatOperation;
import com.oakclub.android.image.SmartImageView;
import com.oakclub.android.model.ChatHistoryData;
import com.oakclub.android.model.HangoutProfileOtherReturnObject;
import com.oakclub.android.model.ListChatData;
import com.oakclub.android.model.SnapshotData;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SnapshotMain extends FrameLayout {

    private SnapshotFragment snapshot;
    public SnapshotFragment getSnapshot() {
        return snapshot;
    }
    public void setSnapshot(SnapshotFragment snapshot) {
        this.snapshot = snapshot;
    }

    private SnapshotData data;
    public SnapshotData getData() {
        return data;
    }
    public void setData(SnapshotData data) {
        this.data = data;
    }

    private String profileId;
    public String getProfileId() {
        return profileId;
    }
    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
    
    private boolean loadingAnim=false;
    public boolean isLoadingAnim() {
        return loadingAnim;
    }
    public void setLoadingAnim(boolean loadingAnim) {
        this.loadingAnim = loadingAnim;
    }
    
    public SnapshotMain(Context context, SnapshotFragment snapshot, SnapshotData data) {
        super(context);
        setSnapshot(snapshot);
        setData(data);
        init();
    }
    private View view;
    private LayoutParams fltParams;
    private RelativeLayout.LayoutParams rltParams;
    private RelativeLayout rltInfo;
    private RelativeLayout rltInfoRight;
    private FrameLayout fltImage;
    private int paddingView;
    
    private SmartImageView imgAvatar;
    private ImageButton imgPlayVideo;
    private ImageView imgMutualFriend;
    private ImageView imgShareInterest;
    private ImageView imgPhoto;
    private ImageView imgVip;
    private ImageView imgVerified;
    private TextView tvName;
    private TextView tvNumFriend;
    private TextView tvMutualFriend;
    private TextView tvShareInterest;
    private TextView tvCountPhoto;

    private ImageView ivwLikeStamp;
    private ImageView ivwNopeStamp;
    
    private float dx = 0, dy = 0, x = 0, y = 0;
    private float tempX;
    private float tempY;
    private float angle = 0;
    private int widthScreen = 0;
    private static final int ORIGIN_ROTATE = 25;
    private static final int TIMER = 100;
    private static final int TIMER_DRAG = 50;
    private float widthT = -9999, heightT = -9999;
    
    private SharedPreferences pref;
    private boolean isFirstLike = false;
    private boolean isFirstNope = false;
    private FrameLayout fltContent;
    private void init(){
        widthScreen = (int) OakClubUtil.getWidthScreen(getContext());
        paddingView = (int)OakClubUtil.convertDpToPixel(10, getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.layout_snapshot, null);
        //view.setPadding(paddingView, paddingView, paddingView, paddingView);
        fltParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(fltParams);
        
        fltContent = new FrameLayout(getContext());
        paddingView = (int)OakClubUtil.convertDpToPixel(20, getContext());
        fltContent.setPadding(paddingView, paddingView, paddingView, paddingView);
        fltContent.addView(view);
        fltContent.setLayoutParams(fltParams);
        fltContent.setBackgroundResource(R.drawable.back_polaroid);
        this.addView(fltContent);
        int paddingBody = widthScreen/12; 
        this.setPadding(paddingBody, paddingBody, paddingBody, paddingBody);

        fltImage = (FrameLayout)view.findViewById(R.id.activity_snapshot_flt_body_flt_content_flt_image);
        paddingView = (int)OakClubUtil.convertDpToPixel(20, getContext());
        fltImage.setPadding(paddingView, paddingView, paddingView, paddingView);
        fltParams = (LayoutParams) fltImage.getLayoutParams();
        rltInfo = (RelativeLayout) view.findViewById(R.id.activity_snapshot_flt_body_flt_content_rlt_info);
        paddingView = (int)OakClubUtil.convertDpToPixel(10, getContext());
        rltInfo.setPadding(paddingView, paddingView, paddingView, paddingView);
        rltInfoRight = (RelativeLayout)rltInfo.findViewById(R.id.activity_snapshot_flt_body_flt_content_rlt_info_right);
        rltParams = (RelativeLayout.LayoutParams) rltInfoRight.getLayoutParams();
        imgAvatar = (SmartImageView) fltImage.findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_avatar);
        imgPlayVideo = (ImageButton) fltImage.findViewById(R.id.activity_snapshot_flt_body_flt_content_imgPlayvideo);
        imgVip = (ImageView) fltImage.findViewById(R.id.activity_snapshot_flt_body_flt_content_imgVip);
        imgMutualFriend = (ImageView) rltInfoRight.findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_mutual_friend);
        imgShareInterest = (ImageView) rltInfoRight.findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_shareinterest);
        imgVerified = (ImageView)rltInfo.findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_photo_verified);
        imgPhoto = (ImageView) rltInfoRight.findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_photo);
        tvName = (TextView)rltInfo.findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_info);
        tvNumFriend = (TextView)rltInfoRight.findViewById(R.id.activity_snapshot_flt_body_flt_content_tv_numFriend);
        tvMutualFriend = (TextView)rltInfoRight.findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_mutual_friend);
        tvShareInterest = (TextView)rltInfoRight.findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_shareinterest);
        tvCountPhoto = (TextView)rltInfoRight.findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_photo);
        
        ivwLikeStamp = (ImageView)fltImage.findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_like);
        ivwNopeStamp = (ImageView)fltImage.findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_nope);
        
        pref = getContext().getSharedPreferences(
                Constants.PREFERENCE_NAME, 0);
        
        loadData(data);
        this.setOnTouchListener(onSlideTouch);
    }
    
    private String nameProfile;
    private String imageUrl;
    private void loadData(SnapshotData data){
        final FrameLayout fltParent = this;
        ViewTreeObserver vto = fltParent.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                fltParent.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
                widthT = fltParent.getMeasuredWidth();
                heightT = fltParent.getMeasuredHeight();
            }
        });
//        vto = fltImage.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//            @SuppressWarnings("deprecation")
//            @Override
//            public void onGlobalLayout() {
//            	fltImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//            	int width = fltImage.getMeasuredWidth();
//            	int height = fltImage.getMeasuredHeight();
//            	if(width<height)
//            		fltParams = new LayoutParams(width, width);
//            	else fltParams = new LayoutParams(height, height);
//                fltImage.setLayoutParams(fltParams);
//            }
//        });
        vto = rltInfo.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                rltInfo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                rltParams = new RelativeLayout.LayoutParams(imgMutualFriend.getMeasuredWidth()*8, imgMutualFriend.getMeasuredWidth()*8);
                rltParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                rltParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                rltInfoRight.setLayoutParams(rltParams);
               
            	int width = fltImage.getMeasuredWidth();
            	int height = fltImage.getMeasuredHeight();
            	if(width<height)
            		fltParams = new LayoutParams(width, width);
            	else fltParams = new LayoutParams(width, height);
//                fltParams = new LayoutParams(fltImage.getMeasuredWidth(), fltImage.getMeasuredHeight());

                fltParams.bottomMargin = imgMutualFriend.getMeasuredHeight() + tvMutualFriend.getMeasuredHeight() + paddingView;
                fltImage.setLayoutParams(fltParams);
                fltParams = new LayoutParams(LayoutParams.MATCH_PARENT, rltInfoRight.getMeasuredHeight());
                fltParams.gravity = Gravity.BOTTOM;
                rltInfo.setLayoutParams(fltParams);
            }
        });

        
        if(data!=null){
            setProfileId(data.getProfile_id());
            int age = data.getAge();
            if (age <= 1)
                Log.e("Error", "Error:SnapshotActivity:age is wrong");
            nameProfile = data.getName();
            nameProfile = OakClubUtil.getFirstName(nameProfile);
            String info = nameProfile + ", " + age;
            String numFriend = data.getMutual_friends().size() + "";
            String numLikePerson = data.getShare_interests().size() + "";
            String numPicture = data.getPhotos().size() + "";
            String urlVideo = data.getVideo_link();
            imageUrl = OakClubUtil.getFullLink(getContext(), 
                   data.getAvatar(), Constants.widthImage, Constants.heightImage, 1);
            imgAvatar.setBackgroundResource(R.drawable.logo_splashscreen);
            imgAvatar.setImageBitmap(null);
            OakClubUtil.loadImageFromUrl(getContext(),imageUrl, imgAvatar);
            if(!urlVideo.equals("")){
                imgPlayVideo.setVisibility(View.VISIBLE);
                imgPlayVideo.setOnClickListener(clickPlayVideo(urlVideo));
            }
            else{ 
                imgPlayVideo.setVisibility(View.GONE);
            }
            
            tvName.setText(info);
            tvMutualFriend.setText(numFriend);
            tvShareInterest.setText(numLikePerson);
            tvCountPhoto.setText(numPicture);
            if(data.getIs_verify()){
                imgVerified.setVisibility(View.VISIBLE);
            }
            if(!data.isIs_vip()){
                if (numFriend.equals("0"))
                    imgMutualFriend.setBackgroundResource(R.drawable.ico_mutualfriend_disable);
                else 
                    imgMutualFriend.setBackgroundResource(R.drawable.ico_mutualfriend_normal);
        
                if (numLikePerson.equals("0"))
                    imgShareInterest.setBackgroundResource(R.drawable.ico_sharedinterest_disable);
                else
                    imgShareInterest.setBackgroundResource(R.drawable.ico_sharedinterest_normal);
                
                if (numPicture.equals("0"))
                    imgPhoto.setBackgroundResource(R.drawable.ico_photo_disable);
                else
                    imgPhoto.setBackgroundResource(R.drawable.ico_photonormal);
            }
            else {
                fltImage.setBackgroundResource(R.drawable.vip_border);
                imgVip.setVisibility(View.VISIBLE);
                tvName.setTextColor(getResources().getColor(R.color.text_vip));
                if (numFriend.equals("0")){
                    tvMutualFriend.setTextColor(getResources().getColor(R.color.text_vip_disable));
                    imgMutualFriend.setBackgroundResource(R.drawable.multual_friends_inactivated);
                }
                else{
                    tvMutualFriend.setTextColor(getResources().getColor(R.color.text_vip));
                    imgMutualFriend.setBackgroundResource(R.drawable.multual_friends_activated);
                }
                if (numLikePerson.equals("0")){
                    tvShareInterest.setTextColor(getResources().getColor(R.color.text_vip_disable));
                    imgShareInterest.setBackgroundResource(R.drawable.interests_inactivated);
                }
                else{
                    tvShareInterest.setTextColor(getResources().getColor(R.color.text_vip));
                    imgShareInterest.setBackgroundResource(R.drawable.interests_activated);
                }
                if (numPicture.equals("0")){
                    tvCountPhoto.setTextColor(getResources().getColor(R.color.text_vip_disable));
                    imgPhoto.setBackgroundResource(R.drawable.total_image_inactivated);
                }
                else{
                    tvCountPhoto.setTextColor(getResources().getColor(R.color.text_vip));
                    imgPhoto.setBackgroundResource(R.drawable.total_image_activated);
                }
            }
        }
    }
    
    private OnClickListener clickPlayVideo(final String url){
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoUrl = OakClubUtil.getFullLinkVideo(getContext(),
                        url, Constants.VIDEO_EXTENSION);
                Intent intent = new Intent(getContext(), VideoViewActivity.class);
                intent.putExtra("url_video", videoUrl);
                getContext().startActivity(intent);
            }
        };
        return listener;
    }

    private float getAngle(float distance) {
        float angle = (distance / widthScreen) * ORIGIN_ROTATE;
        return angle;
    }
    
    private OnTouchListener onSlideTouch = new OnTouchListener() {
        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            final int action = event.getAction();
            isFirstLike = pref.getBoolean(
                    Constants.PREFERENCE_SHOW_LIKE_DIALOG, true);
            isFirstNope = pref.getBoolean(
                    Constants.PREFERENCE_SHOW_NOPE_DIALOG, true);

        	if(snapshot.fltBody.getChildCount()>1)
        		snapshot.fltBody.getChildAt(0).setVisibility(View.VISIBLE);
            switch (action) {
            case MotionEvent.ACTION_DOWN: {
                tempX = 0;
                tempY = 0;
                fltParams = (FrameLayout.LayoutParams) v.getLayoutParams();

                dx = event.getRawX() - fltParams.leftMargin;
                dy = event.getRawY() - fltParams.topMargin;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                fltContent.setBackgroundDrawable(null);
                x = event.getRawX();
                y = event.getRawY();
                tempX = (int) (x - dx);
                tempY = (int) (y - dy);
                fltParams.leftMargin = (int) (tempX);
                if (Math.abs(tempY) <= OakClubUtil
                        .getWidthScreen(getContext()))
                    fltParams.topMargin = (int) tempY;//
                fltParams.width = (int) widthT;
                fltParams.height = (int) heightT;
                v.setLayoutParams(fltParams);

                float tempAlpha = tempX / 100;
                angle = getAngle(tempX);
                v.setRotation(angle);
                if (tempX > 0) {
                    ivwLikeStamp.setAlpha(tempAlpha);
                    ivwNopeStamp.setAlpha(0.0f);
                } else {
                    ivwNopeStamp.setAlpha(-tempAlpha);
                    ivwLikeStamp.setAlpha(0.0f);
                }
                break;

            }
            case MotionEvent.ACTION_UP: {
                fltContent.setBackgroundDrawable(null);
                if (Math.abs(tempX) <= Constants.DISTANCE_MIN_TO_INTO_PROFILE && Math.abs(tempY) <= Constants.DISTANCE_MIN_TO_INTO_PROFILE) {
                    Intent intent = new Intent(getContext(), InfoProfileOtherActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    Bundle b = new Bundle();
                    b.putSerializable("SnapshotDataBundle",
                            data);
                    b.putString("Activity", "SnapshotActivity");
                    intent.putExtras(b);
                    ((FragmentActivity) getContext()).startActivityForResult(intent, 1);
                    System.gc();
                    return true;
                }
                
                if (Math.abs(tempX) > widthScreen / 6) {
                    if(tempX>0 && isFirstLike){
                        showDialogFirst(true, true);
                    }
                    else if(tempX<0 && isFirstNope){
                        showDialogFirst(true, false);
                    }
                    else dragSnapshotAnimation();
                } else {
                    returnSnapshot(tempX, tempY);
                }
                break;
            }
            }
            return true;
        }

    };
    
    public void showDialogMutualMatch(){
        final Dialog dialogMutual = new Dialog(getContext());//, android.R.style.Theme_Translucent);
        dialogMutual.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams params = dialogMutual.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;       
        dialogMutual.getWindow().setAttributes(params); 
        
        dialogMutual.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        View view = ((Activity) getContext()).getLayoutInflater().inflate(
                R.layout.popup_mutual_match_layout, null);

        TextViewWithFont tvTitle = (TextViewWithFont)view.findViewById(R.id.tvw_text);
        tvTitle.setFont("helveticaneueultralight.ttf");
        CircleImageView  ivwMutualMe = (CircleImageView) view
                .findViewById(R.id.popup_mutual_match_layout_image_my);
        CircleImageView ivwMutualYou = (CircleImageView) view
                .findViewById(R.id.popup_mutual_match_layout_image_you);
        TextViewWithFont txtMutualMatch = (TextViewWithFont) view.findViewById(R.id.tvw_text_notice);
        dialogMutual.setContentView(view);
        RelativeLayout btnDialogKeepSwiping = (RelativeLayout) view
                .findViewById(R.id.popup_mutual_match_layout_btn_keep_swiping);
        btnDialogKeepSwiping.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	setLoadingAnim(false);
                dialogMutual.dismiss();
                dialogMutual.setCancelable(true);
            }
        });
        RelativeLayout btnDialogChat = (RelativeLayout) view
                .findViewById(R.id.popup_mutual_match_layout_btn_chat);
        btnDialogChat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	setLoadingAnim(false);
//                snapshot.setMutualMatch(data.getProfile_id());
                
                Intent intent = new Intent(
                    getContext(),
                    ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BUNDLE_PROFILE_ID, data.getProfile_id());
                bundle.putString(Constants.BUNDLE_AVATAR, data.getAvatar());
                bundle.putString(Constants.BUNDLE_NAME, nameProfile);
                bundle.putInt(Constants.BUNDLE_STATUS, 0);
                bundle.putString(Constants.BUNDLE_MATCH_TIME, data.getLike_time());
                bundle.putBoolean(Constants.BUNDLE_NOTI, false);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                getContext().startActivity(intent);
                dialogMutual.dismiss();
                dialogMutual.setCancelable(true);
            }
        });
        OakClubUtil.loadImageFromUrl(getContext(), snapshot.urlAvatar, ivwMutualMe);
        OakClubUtil.loadImageFromUrl(getContext(), imageUrl, ivwMutualYou);
        txtMutualMatch.setText(String.format(
                        getContext().getString(
                                R.string.txt_you_and_have_like_each_other),
                        nameProfile));
        dialogMutual.setCancelable(false);
        dialogMutual.show();
    }
    
    private void showDialogFirst(final boolean isDrag, final boolean isLike){
        String title ="";
        String content="";
        if(isLike){
            title = getContext().getString(R.string.like_string) + "?";
            content =  getContext().getString(R.string.snapshot_popup_like_info) + " " + nameProfile;
        }
        else{
            title =  getContext().getString(R.string.snapshot_not_interested) + "?";
            content =  getContext().getString(R.string.snapshot_popup_dislike_info) + " " + nameProfile;
        }
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater
                .from(getContext());
        View layout = inflater.inflate(R.layout.dialog_active_snapshot,
                null);
        dialog.setView(layout, 0, 0, 0, 0);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tvTitle = (TextView)layout.findViewById(R.id.dialog_active_snapshot_tvtitle);
        tvTitle.setText(title);
        TextView tvContent = (TextView)layout.findViewById(R.id.dialog_active_snapshot_tvcontent);
        tvContent.setText(content);
        Button btActive = (Button)layout.findViewById(R.id.dialog_active_snapshot_btActive);
        SharedPreferences pref = getContext().getSharedPreferences(Constants.PREFERENCE_NAME, 0);
        Editor editor = pref.edit();
        if(isLike){
            editor.putBoolean(Constants.PREFERENCE_SHOW_LIKE_DIALOG, false);
            isFirstLike = false;
            tvTitle.setTextColor(getContext().getResources().getColor(R.color.dialog_snapshot_like));
            btActive.setText(getContext().getString(R.string.like_string));
            btActive.setBackgroundResource(R.drawable.dialog_snapshot_like);
            btActive.setTextColor(getContext().getResources().getColor(R.color.dialog_snapshot_text_like));
            btActive.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isDrag){
                        buttonSnapshotAnimation(Constants.ACTION_LIKE);
                    }
                    else dragSnapshotAnimation();
                    dialog.cancel();
                }
            });
        }
        else{
            editor.putBoolean(Constants.PREFERENCE_SHOW_NOPE_DIALOG, false);
            isFirstNope = false;
            tvTitle.setTextColor(getContext().getResources().getColor(R.color.dialog_snapshot_pass));
            btActive.setText(getContext().getString(R.string.pass_string));
            btActive.setBackgroundResource(R.drawable.dialog_snapshot_pass);
            btActive.setTextColor(getContext().getResources().getColor(R.color.dialog_snapshot_text_pass));
            btActive.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isDrag){
                            buttonSnapshotAnimation(Constants.ACTION_NOPE);
                    }
                    else dragSnapshotAnimation();
                    dialog.cancel();
                }
            }); 
        }
        editor.commit();
        Button btCancel = (Button)layout.findViewById(R.id.dialog_active_snapshot_btCancel);
        btCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDrag){
                    returnSnapshot(tempX, tempY);
                }
                dialog.cancel();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
    

    private void dragSnapshotAnimation(){
        if(isLoadingAnim())
            return;
        setLoadingAnim(true);
        AnimationSet animationSet = new AnimationSet(true); 
        Animation rotate = new RotateAnimation(0, -2 * angle,
                RotateAnimation.RELATIVE_TO_SELF, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0);
        rotate.setDuration(TIMER_DRAG);
        rotate.setFillAfter(true);

        Animation translate = new TranslateAnimation(0,
                tempX > 0 ? (int) (widthScreen - tempX)
                        : (int) (-widthScreen - tempX), 0, 0);
        translate.setDuration(TIMER_DRAG);
        translate.setFillAfter(true);

        translate.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String isLike=Constants.ACTION_LIKE;
                if(tempX<0)
                    isLike=Constants.ACTION_NOPE;
                snapshot.setFavoriteSnapshot(profileId, isLike, data.getIs_like());
                if(snapshot.fltBody.getChildCount()==1)
                    snapshot.fltBody.removeViewAt(0);
                else snapshot.fltBody.removeViewAt(1);
                snapshot.addDataIntoSnapshotLayout();
                if(Constants.ACTION_LIKE.equals(isLike) && data.getIs_like()){
                    showDialogMutualMatch();
                    snapshot.updateListChat(data.getProfile_id());
                }
                else setLoadingAnim(false);
            }
        });
        animationSet.addAnimation(rotate);
        animationSet.addAnimation(translate);
        this.startAnimation(animationSet);
    }
    
    private void returnSnapshot(float x, float y) {
        if(isLoadingAnim())
            return;
        setLoadingAnim(true);
        AnimationSet animationSet = new AnimationSet(true);
        
        TranslateAnimation translateAnim = new TranslateAnimation(Animation.ABSOLUTE, -x,
                Animation.ABSOLUTE, -y);
        RotateAnimation rotateAnim = new RotateAnimation(0, 
                -angle, 
                0, 0,
                0, 0);
        rotateAnim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setLoadingAnim(false);
                
                FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                p.gravity = Gravity.CENTER;
                SnapshotMain.this.clearAnimation();
                SnapshotMain.this.setLayoutParams(p);
                SnapshotMain.this.ivwLikeStamp.setAlpha(0.0f);
                SnapshotMain.this.ivwNopeStamp.setAlpha(0.0f);
                SnapshotMain.this.setRotation(0);
                fltContent.setBackgroundResource(R.drawable.back_polaroid);
            }
        });
        
        translateAnim.setDuration(TIMER_DRAG);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setDuration(TIMER_DRAG);

        animationSet.addAnimation(rotateAnim);
        animationSet.addAnimation(translateAnim);
        this.startAnimation(animationSet);
    }
    
    public void setPressButtonAction(String action){
        isFirstLike = pref.getBoolean(
                Constants.PREFERENCE_SHOW_LIKE_DIALOG, true);
        isFirstNope = pref.getBoolean(
                Constants.PREFERENCE_SHOW_NOPE_DIALOG, true);
        if(Constants.ACTION_LIKE.equals(action) && isFirstLike){
            showDialogFirst(false, true);
        }
        else if(Constants.ACTION_NOPE.equals(action) && isFirstNope){
            showDialogFirst(false, false);
        }
        else buttonSnapshotAnimation(action);
        
    }
    
    private void buttonSnapshotAnimation(final String action) {
        if(isLoadingAnim())
            return;
        setLoadingAnim(true);
        
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnim = null;
        RotateAnimation rotateAnim=null;
        ScaleAnimation animButton =null;

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(TIMER);
        fadeOut.setFillAfter(true);
        fadeOut.setDuration(TIMER);
        animationSet.addAnimation(fadeOut);

        if (Constants.ACTION_LIKE.equals(action)) {
            translateAnim = new TranslateAnimation(Animation.ABSOLUTE, widthScreen,
                    Animation.ABSOLUTE, Animation.ABSOLUTE);
            translateAnim.setStartOffset(TIMER);
            rotateAnim = new RotateAnimation(RotateAnimation.ABSOLUTE,
                    -getAngle(-widthScreen), RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setStartOffset(TIMER);
            
            ivwLikeStamp.setAlpha(1.0f);
            ivwNopeStamp.setAlpha(0.0f);
            animButton = new ScaleAnimation(1.5f, 1f,
                    1.5f, 1f, Animation.ABSOLUTE,
                    ivwLikeStamp.getWidth() / 2, Animation.ABSOLUTE,
                    ivwLikeStamp.getHeight() / 2);
            animButton.setDuration(TIMER);
            ivwLikeStamp.setAnimation(animButton);
            ivwLikeStamp.startAnimation(animButton);
        } else {
            translateAnim = new TranslateAnimation(Animation.ABSOLUTE, -widthScreen,
                    Animation.ABSOLUTE, Animation.ABSOLUTE);
            translateAnim.setStartOffset(TIMER);
            rotateAnim = new RotateAnimation(RotateAnimation.ABSOLUTE,
                    -getAngle(widthScreen), RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setStartOffset(TIMER);
            
            ivwLikeStamp.setAlpha(0.0f);
            ivwNopeStamp.setAlpha(1.0f);
            animButton = new ScaleAnimation(1.5f, 1f,
                    1.5f, 1f, Animation.ABSOLUTE,
                    ivwNopeStamp.getWidth() / 2, Animation.ABSOLUTE,
                    ivwNopeStamp.getHeight() / 2);
            animButton.setDuration(TIMER);
            ivwNopeStamp.setAnimation(animButton);
            ivwNopeStamp.startAnimation(animButton);
        }
        
        fadeOut.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                setLoadingAnim(false);
                snapshot.setFavoriteSnapshot(profileId, action, data.getIs_like());
                if(snapshot.fltBody.getChildCount()==1)
                    snapshot.fltBody.removeViewAt(0);
                else snapshot.fltBody.removeViewAt(1);
                snapshot.addDataIntoSnapshotLayout();
                if(Constants.ACTION_LIKE.equals(action) && data.getIs_like()){
                    showDialogMutualMatch();
                    snapshot.updateListChat(data.getProfile_id());
                }
                else setLoadingAnim(false);
            }
        });    
        translateAnim.setDuration(TIMER);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setDuration(TIMER);
        
        animationSet.addAnimation(rotateAnim);
        animationSet.addAnimation(translateAnim);

    	if(snapshot.fltBody.getChildCount()>1)
    		snapshot.fltBody.getChildAt(0).setVisibility(View.VISIBLE);
        fltContent.setBackgroundDrawable(null);
        this.startAnimation(animationSet);
    }
    

}
