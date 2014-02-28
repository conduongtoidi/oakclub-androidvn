package com.oakclub.android.view;

import com.oakclub.android.InfoProfileOtherActivity;
import com.oakclub.android.R;
import com.oakclub.android.VideoViewActivity;
import com.oakclub.android.image.SmartImageView;
import com.oakclub.android.model.SnapshotData;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
import android.widget.FrameLayout.LayoutParams;

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
    private RelativeLayout rltInfo;
    private RelativeLayout rltInfoRight;
    private FrameLayout fltImage;
    private int paddingView;
    
    private SmartImageView imgAvatar;
    private ImageButton imgPlayVideo;
    private ImageView imgMutualFriend;
    private ImageView imgShareInterest;
    private ImageView imgPhoto;
    private ImageView imgVerified;
    private TextView tvName;
    private TextView tvNumFriend;
    private TextView tvMutualFriend;
    private TextView tvShareInterest;
    private TextView tvCountPhoto;

    private ImageView ivwLikeStamp;
    private ImageView ivwNopeStamp;
    private SnapshotData data;
    
    private float dx = 0, dy = 0, x = 0, y = 0;
    private float tempX;
    private float tempY;
    private float angle = 0;
    private int widthScreen = 0;
    private static final int ORIGIN_ROTATE = 25;
    private static final int TIMER = 200;
    private float widthT = -9999, heightT = -9999;
    
    private boolean flag = false;
    private void init(){
        widthScreen = (int) OakClubUtil.getWidthScreen(getContext());
        paddingView = (int)OakClubUtil.convertDpToPixel(10, getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.layout_snapshot, null);
        view.setPadding(paddingView, paddingView, paddingView, paddingView);
        fltParams = (LayoutParams) view.getLayoutParams();
        this.addView(view);
//        this.setOnTouchListener(onSlideTouch);
        
        fltImage = (FrameLayout)view.findViewById(R.id.activity_snapshot_flt_body_flt_content_flt_image);
        fltParams = (LayoutParams) fltImage.getLayoutParams();
        rltInfo = (RelativeLayout) view.findViewById(R.id.activity_snapshot_flt_body_flt_content_rlt_info);
        rltInfoRight = (RelativeLayout)rltInfo.findViewById(R.id.activity_snapshot_flt_body_flt_content_rlt_info_right);
        rltParams = (RelativeLayout.LayoutParams) rltInfoRight.getLayoutParams();
        imgAvatar = (SmartImageView) fltImage.findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_avatar);
        imgPlayVideo = (ImageButton) fltImage.findViewById(R.id.activity_snapshot_flt_body_flt_content_imgPlayvideo);
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
        
    }
    
    public void loadData(SnapshotData data){
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
                widthT = view.getMeasuredWidth();
                heightT = view.getMeasuredHeight();
            }
        });
        vto = rltInfo.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rltInfo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                rltParams = new RelativeLayout.LayoutParams(imgMutualFriend.getMeasuredWidth()*6, LayoutParams.WRAP_CONTENT);
                rltParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                rltInfoRight.setLayoutParams(rltParams);
                
                fltParams.bottomMargin = imgMutualFriend.getMeasuredHeight() + tvMutualFriend.getMeasuredHeight() + paddingView;
                fltImage.setLayoutParams(fltParams);
            }
        });
        
        if(data!=null){
            this.data = data;
            int age = data.getAge();
            if (age <= 1)
                Log.e("Error", "Error:SnapshotActivity:age is wrong");
            String name = data.getName();
            name = OakClubUtil.getFirstName(name);
            String info = name + ", " + age;
            String numFriend = data.getMutual_friends().size() + "";
            String numLikePerson = data.getShare_interests().size() + "";
            String numPicture = data.getPhotos().size() + "";
            String urlVideo = data.getVideo_link();
            String imageUrl = OakClubUtil.getFullLink(getContext(), 
                   data.getAvatar(), Constants.widthImage, Constants.heightImage, 1);
            imgAvatar.setBackgroundResource(R.drawable.logo_splashscreen);
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
    
    public void resetData(){
        imgAvatar.setImageResource(R.drawable.logo_splashscreen);
        tvName.setText("");
        tvMutualFriend.setText("0");
        tvShareInterest.setText("0");
        tvCountPhoto.setText("0");
    }

    protected void resetLayout() {
        FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        p.gravity = Gravity.CENTER;
        this.clearAnimation();
        this.setLayoutParams(p);
        this.setRotation(0);
        ivwLikeStamp.setAlpha(0.0f);
        ivwNopeStamp.setAlpha(0.0f);
    }
    
    private float getAngle(float distance) {
        float angle = (distance / widthScreen) * ORIGIN_ROTATE;
        return angle;
    }
//
//
//    private OnTouchListener onSlideTouch = new OnTouchListener() {
//        @Override
//        public boolean onTouch(final View v, MotionEvent event) {
//            final int action = event.getAction();
//            switch (action) {
//            case MotionEvent.ACTION_DOWN: {
//                tempX = 0;
//                tempY = 0;
//                fltParams = (FrameLayout.LayoutParams) v.getLayoutParams();
//
//                dx = event.getRawX() - fltParams.leftMargin;
//                dy = event.getRawY() - fltParams.topMargin;
//                widthT = -9999;
//                heightT = -9999;
//                break;
//            }
//            case MotionEvent.ACTION_MOVE: {
//                x = event.getRawX();
//                y = event.getRawY();
//                tempX = (int) (x - dx);
//                tempY = (int) (y - dy);
//                fltParams.leftMargin = (int) (tempX);
//                if (Math.abs(tempY) <= OakClubUtil
//                        .getWidthScreen(getContext()))
//                    fltParams.topMargin = (int) tempY;//
//                v.setLayoutParams(fltParams);
////
//                if (widthT == -9999 || heightT == -9999) {
//                    ViewTreeObserver vto = v.getViewTreeObserver();
//                    vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//                        @Override
//                        public void onGlobalLayout() {
//                            v.getViewTreeObserver()
//                                    .removeGlobalOnLayoutListener(this);
//                            widthT = v.getMeasuredWidth();
//                            heightT = v.getMeasuredHeight();
//                            fltParams.width = (int) widthT;
//                            fltParams.height = (int) heightT;
//                            v.setLayoutParams(fltParams);
//                        }
//                    });
//                }
//                float tempAlpha = tempX / 100;
//                angle = getAngle(tempX);
//
//                v.setRotation(angle);
//                ImageView imgLike = null, imgNope = null; 
//                if (tempX > 0) {
//                    ivwLikeStamp.setAlpha(tempAlpha);
//                    ivwNopeStamp.setAlpha(0.0f);
//                } else {
//                    ivwNopeStamp.setAlpha(-tempAlpha);
//                    ivwLikeStamp.setAlpha(0.0f);
//                }
//                break;
//
//            }
//            case MotionEvent.ACTION_UP: {
//                if (Math.abs(tempX) <= 15 && Math.abs(tempY) <= 15) {
//                    Intent intent = new Intent(getContext(), InfoProfileOtherActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//                    Bundle b = new Bundle();
//                    b.putSerializable("SnapshotDataBundle",
//                            data);
//                    b.putString("Activity", "SnapshotActivity");
//                    intent.putExtras(b);
//                    ((FragmentActivity) getContext()).startActivityForResult(intent, 1);
//                    System.gc();
//                    return true;
//                }
//                
//                String target_name = OakClubUtil.getFirstName(data.getName());
//                if (Math.abs(tempX) > widthScreen / 4) {
//                    if(tempX>0 && isFirstLike){
//                        String title = getContext().getString(R.string.like_string) + "?";
//                        String content =  getContext().getString(R.string.snapshot_popup_like_info) + " " + target_name;
//                        showDialogFirst(title, content, true, true);
//                    }
//                    if(tempX<0 && isFirstNope){
//                        String title =  getContext().getString(R.string.snapshot_not_interested) + "?";
//                        String content =  getContext().getString(R.string.snapshot_popup_dislike_info) + " " + target_name;
//                        showDialogFirst(title, content, true, false);
//                    }
//                    if(isActive){
//                        activeSnapshotAnimation();
//                    }
//                    
//                    isActive = true;
//                } else {
//                    isAction = false;
//                    animationForActionLikeOrNope(tempX, tempY, widthScreen,
//                            "-1");
//                }
//                break;
//            }
//            }
//            return true;
//        }
//
//    };
//    
//    private void showDialogFirst(String title, String content, final boolean isDrag, boolean isLike){
//        isActive = false;
//        AlertDialog.Builder builder;
//        builder = new AlertDialog.Builder(getContext());
//        final AlertDialog dialog = builder.create();
//        LayoutInflater inflater = LayoutInflater
//                .from(getContext());
//        View layout = inflater.inflate(R.layout.dialog_active_snapshot,
//                null);
//        dialog.setView(layout, 0, 0, 0, 0);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        TextView tvTitle = (TextView)layout.findViewById(R.id.dialog_active_snapshot_tvtitle);
//        tvTitle.setText(title);
//        TextView tvContent = (TextView)layout.findViewById(R.id.dialog_active_snapshot_tvcontent);
//        tvContent.setText(content);
//        Button btActive = (Button)layout.findViewById(R.id.dialog_active_snapshot_btActive);
//        SharedPreferences pref = getContext().getSharedPreferences(Constants.PREFERENCE_NAME, 0);
//        Editor editor = pref.edit();
//        if(isLike){
//            editor.putBoolean(Constants.PREFERENCE_SHOW_LIKE_DIALOG, false);
//            isFirstLike = false;
//            tvTitle.setTextColor(getContext().getResources().getColor(R.color.dialog_snapshot_like));
//            btActive.setText(getContext().getString(R.string.like_string));
//            btActive.setBackgroundResource(R.drawable.dialog_snapshot_like);
//            btActive.setTextColor(getContext().getResources().getColor(R.color.dialog_snapshot_text_like));
//            btActive.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(!isDrag)
//                        handleLikeButton();
//                    else activeSnapshotAnimation();
//                    dialog.cancel();
//                }
//            });
//        }
//        else{
//            editor.putBoolean(Constants.PREFERENCE_SHOW_NOPE_DIALOG, false);
//            isFirstNope = false;
//            tvTitle.setTextColor(getContext().getResources().getColor(R.color.dialog_snapshot_pass));
//            btActive.setText(getContext().getString(R.string.pass_string));
//            btActive.setBackgroundResource(R.drawable.dialog_snapshot_pass);
//            btActive.setTextColor(getContext().getResources().getColor(R.color.dialog_snapshot_text_pass));
//            btActive.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(!isDrag)
//                        handleNopeButton();
//                    else activeSnapshotAnimation();
//                    dialog.cancel();
//                }
//            }); 
//        }
//        editor.commit();
//        Button btCancel = (Button)layout.findViewById(R.id.dialog_active_snapshot_btCancel);
//        btCancel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isDrag){
//                    isAction = false;
//                    animationForActionLikeOrNope(tempX, tempY, widthScreen, "-1");
//                }
//                dialog.cancel();
//            }
//        });
//        dialog.setCancelable(false);
//        dialog.show();
//    }
//    
//
//    private void activeSnapshotAnimation(){
//        AnimationSet animationSet = new AnimationSet(true); 
//        Animation rotate = new RotateAnimation(0, -2 * angle,
//                RotateAnimation.RELATIVE_TO_SELF, 0,
//                RotateAnimation.RELATIVE_TO_SELF, 0);
//        rotate.setDuration(TIMER);
//        rotate.setFillAfter(true);
//
//        Animation translate = new TranslateAnimation(0,
//                tempX > 0 ? (int) (widthScreen - tempX)
//                        : (int) (-widthScreen - tempX), 0, 0);
//        translate.setDuration(TIMER);
//        translate.setFillAfter(true);
//
//        translate.setAnimationListener(new AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                resetLayout();
//                isAction = false;
//                String url = OakClubUtil.getFullLink(
//                        getContext(),
//                        data.getAvatar());
//                boolean isLike = data.getIs_like();
//                nameProfile = data.getName();
//                avaProfile = data.getAvatar();
//                match_time = data.getLike_time();
//                chatProfile_id = data
//                        .getProfile_id();
////                arrayListSnapshot.remove(0);
//                String proId = profileId;
//                String action = Constants.ACTION_LIKE;
//                if (tempX < -widthScreen / 4)
//                    action = Constants.ACTION_NOPE;
//
//                SnapshotEvent snapEvent = new SnapshotEvent(
//                        Constants.SET_FAVORITE, activity,
//                        proId, action);
//                activity.getRequestQueue().addRequest(snapEvent);
//            }
//        });
//        animationSet.addAnimation(rotate);
//        animationSet.addAnimation(translate);
//
//        isAction = true;
//        getCurrentContentView().startAnimation(animationSet);
//    }
//    
//    private void animationForActionLikeOrNope(float x, float y, int w,
//            final String action) {
//        AnimationSet animationSet = new AnimationSet(true);
//        TranslateAnimation translateAnim = null;
//        RotateAnimation rotateAnim = null;
//        if (!isAction) {
//            translateAnim = new TranslateAnimation(Animation.ABSOLUTE, -x,
//                    Animation.ABSOLUTE, -y);
//            rotateAnim = new RotateAnimation(RotateAnimation.ABSOLUTE,
//                    getAngle(x), RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//            rotateAnim.setAnimationListener(new AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
////                    resetLayout();
//                }
//            });
//        } else {
//            translateAnim = new TranslateAnimation(Animation.ABSOLUTE, x,
//                    Animation.ABSOLUTE, y);
//            rotateAnim = new RotateAnimation(RotateAnimation.ABSOLUTE,
//                    -getAngle(-x), RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//
//            translateAnim.setStartOffset(TIMER);
//            rotateAnim.setStartOffset(TIMER);
//
//            Animation fadeOut = new AlphaAnimation(1, 0);
//            fadeOut.setInterpolator(new AccelerateInterpolator());
//            fadeOut.setStartOffset(TIMER);
//            fadeOut.setFillAfter(true);
//            fadeOut.setDuration(TIMER);
//            animationSet.addAnimation(fadeOut);
//
//            fadeOut.setAnimationListener(new AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    resetLayout();
//                    boolean isLike = arrayListSnapshot.get(0).getIs_like();
//                    String url = OakClubUtil.getFullLink(
//                            activity, arrayListSnapshot.get(0)
//                                    .getAvatar());
//                    nameProfile = arrayListSnapshot.get(0).getName();
//                    avaProfile = arrayListSnapshot.get(0).getAvatar();
//                    chatProfile_id = arrayListSnapshot.get(0).getProfile_id();
//                    arrayListSnapshot.remove(0);
//                    String proId = profileId;
//
//                    String action = Constants.ACTION;
//                    if (tempX < -widthScreen / 4) {
//                        action = Constants.ACTION_NOPE;
//                    }
//
//                    SnapshotEvent snapEvent = new SnapshotEvent(
//                            Constants.SET_FAVORITE, activity,
//                            proId, action);
//                    activity.getRequestQueue().addRequest(snapEvent);
//
//                    if (arrayListSnapshot.size() > 0) {
//                        swapData(2);
//                        if (action == Constants.ACTION_LIKE && isLike) {
//                            status = 0;
//                            match_time = arrayListSnapshot.get(0).getLike_time();
//                            showDialog(url);
//                        } else {
//                            // OakClubUtil.clearCacheWithUrl(url, imageLoader);
//                            setEnableAll(true);
//                            profileId = arrayListSnapshot.get(0)
//                                    .getProfile_id();
//                        }
//                    } else {
//                        setEnableAll(false);
//                        resetAll();
//                        getListSnapshotData(SEG_RECORD);
//                    }
//                    isAction = false;
//                }
//            });
//            ScaleAnimation animButton;
//            switch (contentSnapshot) {
//            case 1:
//                if (action == Constants.ACTION_LIKE) {
//                    ivwLikeStamp.setAlpha(1.0f);
//                    ivwNopeStamp.setAlpha(0.0f);
//                    animButton = new ScaleAnimation(1.5f, 1f,
//                            1.5f, 1f, Animation.ABSOLUTE,
//                            ivwLikeStamp.getWidth() / 2, Animation.ABSOLUTE,
//                            ivwLikeStamp.getHeight() / 2);
//                    animButton.setDuration(TIMER);
//                    ivwLikeStamp.setAnimation(animButton);
//                    ivwLikeStamp.startAnimation(animButton);
//                } else {
//                    ivwLikeStamp.setAlpha(0.0f);
//                    ivwNopeStamp.setAlpha(1.0f);
//                    animButton = new ScaleAnimation(1.5f, 1f,
//                            1.5f, 1f, Animation.ABSOLUTE,
//                            ivwNopeStamp.getWidth() / 2, Animation.ABSOLUTE,
//                            ivwNopeStamp.getHeight() / 2);
//                    animButton.setDuration(TIMER);
//                    ivwNopeStamp.setAnimation(animButton);
//                    ivwNopeStamp.startAnimation(animButton);
//                }
//                break;
//            case 2:
//                if (action == Constants.ACTION_LIKE) {
//                    ivwSecondLikeStamp.setAlpha(1.0f);
//                    ivwSecondNopeStamp.setAlpha(0.0f);
//                    animButton = new ScaleAnimation(1.5f, 1f,
//                            1.5f, 1f, Animation.ABSOLUTE,
//                            ivwSecondLikeStamp.getWidth() / 2,
//                            Animation.ABSOLUTE,
//                            ivwSecondLikeStamp.getHeight() / 2);
//                    animButton.setDuration(TIMER);
//                    ivwSecondLikeStamp.setAnimation(animButton);
//                    ivwSecondLikeStamp.startAnimation(animButton);
//                } else {
//                    ivwSecondLikeStamp.setAlpha(0.0f);
//                    ivwSecondNopeStamp.setAlpha(1.0f);
//                    animButton = new ScaleAnimation(1.5f, 1f,
//                            1.5f, 1f, Animation.ABSOLUTE,
//                            ivwSecondNopeStamp.getWidth() / 2,
//                            Animation.ABSOLUTE,
//                            ivwSecondNopeStamp.getHeight() / 2);
//                    animButton.setDuration(TIMER);
//                    ivwSecondNopeStamp.setAnimation(animButton);
//                    ivwSecondNopeStamp.startAnimation(animButton);
//                }
//                break;
//            case 3:
//                if (action == Constants.ACTION_LIKE) {
//                    ivwThirdLikeStamp.setAlpha(1.0f);
//                    ivwThirdNopeStamp.setAlpha(0.0f);
//                    animButton = new ScaleAnimation(1.5f, 1f,
//                            1.5f, 1f, Animation.ABSOLUTE,
//                            ivwThirdLikeStamp.getWidth() / 2,
//                            Animation.ABSOLUTE,
//                            ivwThirdLikeStamp.getHeight() / 2);
//                    animButton.setDuration(TIMER);
//                    ivwThirdLikeStamp.setAnimation(animButton);
//                    ivwThirdLikeStamp.startAnimation(animButton);
//                } else {
//                    ivwThirdLikeStamp.setAlpha(0.0f);
//                    ivwThirdNopeStamp.setAlpha(1.0f);
//                    animButton = new ScaleAnimation(1.5f, 1f,
//                            1.5f, 1f, Animation.ABSOLUTE,
//                            ivwThirdNopeStamp.getWidth() / 2,
//                            Animation.ABSOLUTE,
//                            ivwThirdNopeStamp.getHeight() / 2);
//                    animButton.setDuration(TIMER);
//                    ivwThirdNopeStamp.setAnimation(animButton);
//                    ivwThirdNopeStamp.startAnimation(animButton);
//                }
//                break;
//            default:
//                break;
//            }
//
//        }
//        translateAnim.setDuration(TIMER);
//        rotateAnim.setInterpolator(new LinearInterpolator());
//        rotateAnim.setDuration(TIMER);
//
//        animationSet.addAnimation(rotateAnim);
//        animationSet.addAnimation(translateAnim);
//        getCurrentContentView().startAnimation(animationSet);
//    }
}
