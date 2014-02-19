package com.oakclub.android.fragment;

import java.util.ArrayList;

import org.jivesoftware.smackx.carbons.Carbon.Private;
import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.internal.ed;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.oakclub.android.ChatActivity;
import com.oakclub.android.InfoProfileOtherActivity;
import com.oakclub.android.MainActivity;
import com.oakclub.android.R;
import com.oakclub.android.SlidingActivity;
import com.oakclub.android.TutorialScreenActivity;
import com.oakclub.android.base.SlidingMenuActivity;
import com.oakclub.android.core.RequestUI;
import com.oakclub.android.fragment.ListChatFragment;
import com.oakclub.android.model.GetSnapShot;
import com.oakclub.android.model.SetLikeMessageReturnObject;
import com.oakclub.android.model.SetViewMutualReturnObject;
import com.oakclub.android.model.SnapshotData;
import com.oakclub.android.model.adaptercustom.AdapterSnapShot;
import com.oakclub.android.net.OakClubApi;
import com.oakclub.android.net.OnBootReceiver;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;
import com.oakclub.android.view.ProgressCircle;
import com.oakclub.android.view.TextViewWithFont;

public class SnapshotFragment{

    // First
    private ImageView ivwAvatar;
    private ImageView ivwLikeStamp;
    private ImageView ivwNopeStamp;
    private ImageView ivwMuatualFriend;
    private ImageView ivwShareInterested;
    private ImageView ivwNumPhoto;
    private ImageView ivwMuatualFriendDisable;
    private ImageView ivwShareInterestedDisable;
    private ImageView ivwNumPhotoDisable;

    private LinearLayout fltContent;
    private TextView tvwInfo;
    private TextView tvwNumFriend;
    private TextView tvwNumShareInterested;
    private TextView tvwNumPicture;

    // Second
    private ImageView ivwAvatarSecond;
    private ImageView ivwSecondLikeStamp;
    private ImageView ivwSecondNopeStamp;
    private ImageView ivwSecondMuatualFriend;
    private ImageView ivwSecondShareInterested;
    private ImageView ivwSecondNumPhoto;
    private ImageView ivwSecondMuatualFriendDisable;
    private ImageView ivwSecondShareInterestedDisable;
    private ImageView ivwSecondNumPhotoDisable;

    private LinearLayout fltContentSecond;
    private TextView tvwSecondInfo;
    private TextView tvwSecondNumFriend;
    private TextView tvwSecondNumShareInterested;
    private TextView tvwSecondNumPicture;

    // Third
    private ImageView ivwAvatarThird;
    private ImageView ivwThirdLikeStamp;
    private ImageView ivwThirdNopeStamp;
    private ImageView ivwThirdMuatualFriend;
    private ImageView ivwThirdShareInterested;
    private ImageView ivwThirdNumPhoto;
    private ImageView ivwThirdMuatualFriendDisable;
    private ImageView ivwThirdShareInterestedDisable;
    private ImageView ivwThirdNumPhotoDisable;

    private LinearLayout fltContentThird;
    private TextView tvwThirdInfo;
    private TextView tvwThirdNumFriend;
    private TextView tvwThirdNumShareInterested;
    private TextView tvwThirdNumPicture;

    private ImageView ivwMutualMe;
    private ImageView ivwMutualYou;
    private ImageButton btnNope;
    private ImageButton btnLike;
    private ImageButton btnInfo;

    private FrameLayout fltBody;
    private FrameLayout lltFooter;
    private RelativeLayout progressFinder;
    private FrameLayout btnInvite;
    private String urlAvatar;

    private TextView txtMutualMatch;

    private Dialog dialogMutual;

    private FrameLayout.LayoutParams parms;
    private float dx = 0, dy = 0, x = 0, y = 0;
    private float tempX;
    private float tempY;

    private int widthScreen = 0;
    private float angle = 0;

    private float firstX = -1;
    private float firstY = -1;

    private int contentSnapshot = 1;

    private boolean isAction = false;
    private boolean isFirst = true;

    protected static final int START_RECORD = 0;
    protected static final int LIMIT_RECORD = 20;
    protected static final int SEG_RECORD = 0;
    protected static final int ORIGIN_ROTATE = 25;
    protected String TAG_SNAPSHOT_ID = "snapshot_id";
    protected AdapterSnapShot arrayAdapter;
    protected GetSnapShot objSnapshot;
    protected String profileId = "";
    protected int lengthSnap = 0;
    protected View touchCurrent = null;

    public ArrayList<SnapshotData> arrayListSnapshot;
    public JSONArray jsonArray = null;
    private RelativeLayout progressCircle;

    private String nameProfile;
    private String avaProfile;
    private int status;
    private String match_time;

    private int index = 0;

    /* HieuPham */

    private Handler handler;
    private Runnable runnable;
    private static final int TIMER = 200;
    private boolean isFirstLike;
    private boolean isFirstNope;
    private boolean isActive=true;
    
    private TextView tvFindPeople;

    
    SlidingActivity activity;
    public SnapshotFragment(SlidingActivity activity){
        this.activity = activity;
        //initSnapshot();
    }
    
    
    public void initSnapshot() {
        activity.init(R.layout.activity_snapshot);

        SharedPreferences pref = activity.getSharedPreferences(
                "oakclub_pref", activity.MODE_PRIVATE);
        
        activity.sendBroadcast(new Intent(activity, OnBootReceiver.class));
        
        pref = activity.getSharedPreferences(
                Constants.PREFERENCE_NAME, 0);
        boolean isFirstJoin = pref.getBoolean(
                Constants.PREFERENCE_SHOW_TUTORIAL, true);
        isFirstLike = pref.getBoolean(
                Constants.PREFERENCE_SHOW_LIKE_DIALOG, true);
        isFirstNope = pref.getBoolean(
                Constants.PREFERENCE_SHOW_NOPE_DIALOG, true);
        if (isFirstJoin) {
            showTutorialActivity();
        }
        urlAvatar = OakClubUtil.getFullLink(activity, ProfileSettingFragment.profileInfoObj.getAvatar());
        init(START_RECORD);
        
    }

    private View findViewById(int id){
        return activity.view.findViewById(id);
    }
    
    Intent intent;
    ProgressCircle progCir;
    ProgressCircle progFin;
    private void init(int start) {
        contentSnapshot = 1;

        isFirst = true;
        widthScreen = (int) OakClubUtil.getWidthScreen(activity);
        arrayListSnapshot = new ArrayList<SnapshotData>();

        progressCircle = (RelativeLayout) findViewById(R.id.activity_snapshot_flt_process);
        tvFindPeople = (TextView)progressCircle.findViewById(R.id.progress_snapshot_layout_flt_footer_text);
        tvFindPeople.setGravity(Gravity.CENTER);
        progCir = (ProgressCircle)progressCircle.findViewById(R.id.progress_snapshot_layout_flt_process);
        
        progressFinder = (RelativeLayout) findViewById(R.id.activity_snapshot_flt_process_finder);
        tvFindPeople = (TextView)progressFinder.findViewById(R.id.progress_snapshot_layout_flt_footer_find_text_finder);
        tvFindPeople.setGravity(Gravity.CENTER);
        progFin = (ProgressCircle)progressFinder.findViewById(R.id.progress_snapshot_layout_flt_process_finder);
        
        fltBody = (FrameLayout) findViewById(R.id.activity_snapshot_flt_body);
        lltFooter = (FrameLayout) findViewById(R.id.activity_snapshot_flt_footer);
        btnInvite = (FrameLayout) progressFinder
                .findViewById(R.id.progress_snapshot_layout_flt_footer_action_find_flt_invite);
        btnInvite.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,
                        activity.getString(R.string.txt_share_title) + "\n"
                                + activity.getString(R.string.txt_share_url));
                intent.setType("text/plain");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);

            }
        });

        fltContent = (LinearLayout) findViewById(R.id.activity_snapshot_flt_body_flt_content);
        fltContent.setOnTouchListener(onSlideTouch);

        fltContentSecond = (LinearLayout) findViewById(R.id.activity_snapshot_flt_body_flt_content_second);
        fltContentSecond.setOnTouchListener(onSlideTouch);

        fltContentThird = (LinearLayout) findViewById(R.id.activity_snapshot_flt_body_flt_content_third);
        fltContentThird.setOnTouchListener(onSlideTouch);

        btnNope = (ImageButton) findViewById(R.id.activity_snapshot_flt_footer_ibn_nope);
        btnNope.setOnClickListener(snapEvent);

        btnLike = (ImageButton) findViewById(R.id.activity_snapshot_flt_footer_ibn_like);
        btnLike.setOnClickListener(snapEvent);

        btnInfo = (ImageButton) findViewById(R.id.activity_snapshot_flt_footer_ibn_info);
        btnInfo.setOnClickListener(snapEvent);


        ivwAvatar = (ImageView) fltContent
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_avatar);
        ivwLikeStamp = (ImageView) fltContent
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_like);
        ivwNopeStamp = (ImageView) fltContent
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_nope);
        tvwInfo = (TextView) fltContent
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_info);
        tvwNumFriend = (TextView) fltContent
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_mutual_friend);
        tvwNumShareInterested = (TextView) fltContent
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_shareinterest);
        tvwNumPicture = (TextView) fltContent
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_photo);
        ivwMuatualFriend = (ImageView) fltContent
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_mutual_friend_normal);
        ivwShareInterested = (ImageView) fltContent
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_shareinterest_normal);
        ivwNumPhoto = (ImageView) fltContent
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_photo_normal);
        ivwMuatualFriendDisable = (ImageView) fltContent
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_mutual_friend_disable);
        ivwShareInterestedDisable = (ImageView) fltContent
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_shareinterest_disable);
        ivwNumPhotoDisable = (ImageView) fltContent
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_photo_disable);

        ivwSecondLikeStamp = (ImageView) fltContentSecond
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_like);
        ivwSecondNopeStamp = (ImageView) fltContentSecond
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_nope);
        ivwAvatarSecond = (ImageView) fltContentSecond
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_avatar);
        tvwSecondInfo = (TextView) fltContentSecond
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_info);
        tvwSecondNumFriend = (TextView) fltContentSecond
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_mutual_friend);
        tvwSecondNumShareInterested = (TextView) fltContentSecond
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_shareinterest);
        tvwSecondNumPicture = (TextView) fltContentSecond
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_photo);
        ivwSecondMuatualFriend = (ImageView) fltContentSecond
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_mutual_friend_normal);
        ivwSecondShareInterested = (ImageView) fltContentSecond
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_shareinterest_normal);
        ivwSecondNumPhoto = (ImageView) fltContentSecond
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_photo_normal);
        ivwSecondMuatualFriendDisable = (ImageView) fltContentSecond
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_mutual_friend_disable);
        ivwSecondShareInterestedDisable = (ImageView) fltContentSecond
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_shareinterest_disable);
        ivwSecondNumPhotoDisable = (ImageView) fltContentSecond
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_photo_disable);

        ivwThirdLikeStamp = (ImageView) fltContentThird
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_like);
        ivwThirdNopeStamp = (ImageView) fltContentThird
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_nope);
        ivwAvatarThird = (ImageView) fltContentThird
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_avatar);
        tvwThirdInfo = (TextView) fltContentThird
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_info);
        tvwThirdNumFriend = (TextView) fltContentThird
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_mutual_friend);
        tvwThirdNumShareInterested = (TextView) fltContentThird
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_shareinterest);
        tvwThirdNumPicture = (TextView) fltContentThird
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_tvw_photo);
        ivwThirdMuatualFriend = (ImageView) fltContentThird
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_mutual_friend_normal);
        ivwThirdShareInterested = (ImageView) fltContentThird
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_shareinterest_normal);
        ivwThirdNumPhoto = (ImageView) fltContentThird
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_photo_normal);
        ivwThirdMuatualFriendDisable = (ImageView) fltContentThird
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_mutual_friend_disable);
        ivwThirdShareInterestedDisable = (ImageView) fltContentThird
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_shareinterest_disable);
        ivwThirdNumPhotoDisable = (ImageView) fltContentThird
                .findViewById(R.id.activity_snapshot_flt_body_flt_content_ivw_photo_disable);

        resetAll();
        getListSnapshotData(start);
    }
    
    public void showTutorialActivity() {
        intent = new Intent(activity,
                TutorialScreenActivity.class);
        activity.startActivity(intent);
    }
    

    public void activityResult(int requestCode, int resultCode, Intent data){
        if (data != null && data.getExtras() != null
                && data.getExtras().containsKey("ACTION")) {
            String a = data.getStringExtra("ACTION");
            handler = new Handler();
            if (Constants.ACTION_LIKE.equals(a) && !isAction) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        isAction = true;
                        String action = Constants.ACTION_LIKE;
                        Constants.ACTION = Constants.ACTION_LIKE;
                        animationForActionLikeOrNope(widthScreen,
                                Animation.ABSOLUTE, widthScreen, action);
                    }
                };
                handler.postDelayed(runnable, TIMER);

            } else if (Constants.ACTION_NOPE.equals(a) && !isAction) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        isAction = true;
                        String action = Constants.ACTION_NOPE;
                        Constants.ACTION = Constants.ACTION_NOPE;
                        animationForActionLikeOrNope(-widthScreen,
                                Animation.ABSOLUTE, widthScreen, action);
                    }
                };
                handler.postDelayed(runnable, TIMER);

            } else {
                resetLayout();
            }
        }
    }

    protected void assignInfo(int index) {
        int age = arrayListSnapshot.get(index).getAge();
        if (age <= 1)
            Log.e("Error", "Error:SnapshotActivity:age is wrong");
        String name = arrayListSnapshot.get(index).getName();
        name = OakClubUtil.getFirstName(name);
        String info = name + ", " + age;
        String numFriend = arrayListSnapshot.get(index).getMutual_friends()
                .size()
                + "";
        String numLikePerson = arrayListSnapshot.get(index)
                .getShare_interests().size()
                + "";
        String numPicture = arrayListSnapshot.get(index).getPhotos().size()
                + "";

        switch (contentSnapshot) {
        case 1:
            ivwAvatar.setBackgroundResource(R.drawable.logo_splashscreen);
            tvwInfo.setText(info);
            tvwNumFriend.setText(numFriend);
            tvwNumShareInterested.setText(numLikePerson);
            tvwNumPicture.setText(numPicture);
            if (numPicture.equals("0"))
                ivwNumPhotoDisable.bringToFront();
            else
                ivwNumPhoto.bringToFront();
            if (numFriend.equals("0"))
                ivwMuatualFriendDisable.bringToFront();
            else
                ivwMuatualFriend.bringToFront();
            if (numLikePerson.equals("0"))
                ivwShareInterestedDisable.bringToFront();
            else
                ivwShareInterested.bringToFront();
            break;
        case 2:
            ivwAvatarSecond.setBackgroundResource(R.drawable.logo_splashscreen);
            tvwSecondInfo.setText(info);
            tvwSecondNumFriend.setText(numFriend);
            tvwSecondNumShareInterested.setText(numLikePerson);
            tvwSecondNumPicture.setText(numPicture);
            if (numPicture.equals("0"))
                ivwSecondNumPhotoDisable.bringToFront();
            else
                ivwSecondNumPhoto.bringToFront();
            if (numFriend.equals("0"))
                ivwSecondMuatualFriendDisable.bringToFront();
            else
                ivwSecondMuatualFriend.bringToFront();
            if (numLikePerson.equals("0"))
                ivwSecondShareInterestedDisable.bringToFront();
            else
                ivwSecondShareInterested.bringToFront();
            break;
        case 3:
            ivwAvatarThird.setBackgroundResource(R.drawable.logo_splashscreen);
            tvwThirdInfo.setText(info);
            tvwThirdNumFriend.setText(numFriend);
            tvwThirdNumShareInterested.setText(numLikePerson);
            tvwThirdNumPicture.setText(numPicture);
            if (numPicture.equals("0"))
                ivwThirdNumPhotoDisable.bringToFront();
            else
                ivwThirdNumPhoto.bringToFront();
            if (numFriend.equals("0"))
                ivwThirdMuatualFriendDisable.bringToFront();
            else
                ivwThirdMuatualFriend.bringToFront();
            if (numLikePerson.equals("0"))
                ivwThirdShareInterestedDisable.bringToFront();
            else
                ivwThirdShareInterested.bringToFront();
            break;
        default:
            break;
        }
    }

    private float widthT = -9999, heightT = -9999;
    private OnTouchListener onSlideTouch = new OnTouchListener() {
        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            final int action = event.getAction();
            switch (action) {
            case MotionEvent.ACTION_DOWN: {
                tempX = 0;
                tempY = 0;
                parms = (FrameLayout.LayoutParams) v.getLayoutParams();
                if (firstX == -1 && firstY == -1) {
                    firstX = parms.leftMargin;
                    firstY = parms.topMargin;
                }
                dx = event.getRawX() - parms.leftMargin;
                dy = event.getRawY() - parms.topMargin;
                widthT = -9999;
                heightT = -9999;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                x = event.getRawX();
                y = event.getRawY();
                tempX = (int) (x - dx);
                tempY = (int) (y - dy);
                parms.leftMargin = (int) (tempX);
                if (Math.abs(tempY) <= OakClubUtil
                        .getWidthScreen(activity))
                    parms.topMargin = (int) tempY;//
                v.setLayoutParams(parms);

                if (widthT == -9999 || heightT == -9999) {
                    ViewTreeObserver vto = v.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            v.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                            widthT = v.getMeasuredWidth();
                            heightT = v.getMeasuredHeight();
                            parms.width = (int) widthT;
                            parms.height = (int) heightT;
                            v.setLayoutParams(parms);
                        }
                    });
                }
                float tempAlpha = tempX / 100;
                angle = getAngle(tempX);

                v.setRotation(angle);
                ImageView imgLike = null, imgNope = null; 
                switch (contentSnapshot) {
                case 1:
                    imgLike = ivwLikeStamp;
                    imgNope = ivwNopeStamp;
                    break;
                case 2:
                    imgLike = ivwSecondLikeStamp;
                    imgNope = ivwSecondNopeStamp;
                    break;
                case 3:
                    imgLike = ivwThirdLikeStamp;
                    imgNope = ivwThirdNopeStamp;
                    break;
                default:
                    break;
                }
                if (tempX > 0) {
                    imgLike.setAlpha(tempAlpha);
                    imgNope.setAlpha(0.0f);
                } else {
                    imgNope.setAlpha(-tempAlpha);
                    imgLike.setAlpha(0.0f);
                }
                break;

            }
            case MotionEvent.ACTION_UP: {
                if (Math.abs(tempX) <= 15 && Math.abs(tempY) <= 15) {
                    intent = new Intent(activity, InfoProfileOtherActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    Bundle b = new Bundle();
                    b.putSerializable("SnapshotDataBundle",
                            arrayListSnapshot.get(0));
                    b.putString("Activity", "SnapshotActivity");
                    intent.putExtras(b);
                    activity.startActivityForResult(intent, 1);
                    System.gc();
                    return true;
                }
                
                String target_name = OakClubUtil.getFirstName(arrayListSnapshot.get(0).getName());
                
                if (Math.abs(tempX) > widthScreen / 4) {

                    if(tempX>0 && isFirstLike){
                        String title = activity.getString(R.string.like_string) + "?";
                        String content = activity.getString(R.string.snapshot_popup_like_info) + " " + target_name;
                        showDialogFirst(title, content, true, true);
                    }
                    if(tempX<0 && isFirstNope){
                        String title = activity.getString(R.string.snapshot_not_interested) + "?";
                        String content = activity.getString(R.string.snapshot_popup_dislike_info) + " " + target_name;
                        showDialogFirst(title, content, true, false);
                    }
                    if(isActive){
                        activeSnapshotAnimation();
                    }
                    
                    isActive = true;
                } else {
                    isAction = false;
                    animationForActionLikeOrNope(tempX, tempY, widthScreen,
                            "-1");
                }
                
                break;
            }
            }
            return true;
        }

    };
    
    private void activeSnapshotAnimation(){
        AnimationSet animationSet = new AnimationSet(true); 
        Animation rotate = new RotateAnimation(0, -2 * angle,
                RotateAnimation.RELATIVE_TO_SELF, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0);
        rotate.setDuration(TIMER);
        rotate.setFillAfter(true);

        Animation translate = new TranslateAnimation(0,
                tempX > 0 ? (int) (widthScreen - tempX)
                        : (int) (-widthScreen - tempX), 0, 0);
        translate.setDuration(TIMER);
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
                resetLayout();
                isAction = false;
                String url = OakClubUtil.getFullLink(
                        activity,
                        arrayListSnapshot.get(0).getAvatar());
                boolean isLike = arrayListSnapshot.get(0).getIs_like();
                nameProfile = arrayListSnapshot.get(0).getName();
                avaProfile = arrayListSnapshot.get(0).getAvatar();
                match_time = arrayListSnapshot.get(0).getLike_time();
                arrayListSnapshot.remove(0);
                String proId = profileId;
                String action = Constants.ACTION_LIKE;
                if (tempX < -widthScreen / 4)
                    action = Constants.ACTION_NOPE;

                SnapshotEvent snapEvent = new SnapshotEvent(
                        Constants.SET_FAVORITE, activity,
                        proId, action);
                activity.getRequestQueue().addRequest(snapEvent);
                if (arrayListSnapshot.size() > 0) {
                    swapData(2);
                    if (action == Constants.ACTION_LIKE && isLike) {
                    	status = 0;
                        showDialog(url);
                    } else {
                        setEnableAll(true);
                        profileId = arrayListSnapshot.get(0)
                                .getProfile_id();
                    }
                } else {
                    setEnableAll(false);
                    resetAll();
                    getListSnapshotData(SEG_RECORD);
                }
            }
        });
        animationSet.addAnimation(rotate);
        animationSet.addAnimation(translate);

        isAction = true;
        getCurrentContentView().startAnimation(animationSet);
    }

    private void resetAll() {
        contentSnapshot = 1;
        isFirst = true;
        getCurrentContentView().setEnabled(false);
        btnInfo.setEnabled(false);
        btnLike.setEnabled(false);
        btnNope.setEnabled(false);

        ivwAvatar.setImageBitmap(null);
        ivwAvatar.setBackgroundColor(Color.WHITE);
        ivwAvatar.setBackgroundResource(R.drawable.logo_splashscreen);
        tvwInfo.setText("");
        tvwNumFriend.setText("0");
        tvwNumShareInterested.setText("0");
        tvwNumPicture.setText("0");

        ivwAvatarSecond.setImageBitmap(null);
        ivwAvatarSecond.setBackgroundColor(Color.WHITE);
        ivwAvatarSecond.setBackgroundResource(R.drawable.logo_splashscreen);
        tvwSecondInfo.setText("");
        tvwSecondNumFriend.setText("0");
        tvwSecondNumShareInterested.setText("0");
        tvwSecondNumPicture.setText("0");

        ivwAvatarThird.setImageBitmap(null);
        ivwAvatarThird.setBackgroundColor(Color.WHITE);
        ivwAvatarThird.setBackgroundResource(R.drawable.logo_splashscreen);
        tvwThirdInfo.setText("");
        tvwThirdNumFriend.setText("0");
        tvwThirdNumShareInterested.setText("0");
        tvwThirdNumPicture.setText("0");
    }

    private void animationForActionLikeOrNope(float x, float y, int w,
            final String action) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnim = null;
        RotateAnimation rotateAnim = null;
        if (!isAction) {

            translateAnim = new TranslateAnimation(Animation.ABSOLUTE, -x,
                    Animation.ABSOLUTE, -y);
            rotateAnim = new RotateAnimation(RotateAnimation.ABSOLUTE,
                    getAngle(x), RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    resetLayout();
                }
            });
        } else {
            translateAnim = new TranslateAnimation(Animation.ABSOLUTE, x,
                    Animation.ABSOLUTE, y);
            rotateAnim = new RotateAnimation(RotateAnimation.ABSOLUTE,
                    -getAngle(-x), RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);

            translateAnim.setStartOffset(TIMER);
            rotateAnim.setStartOffset(TIMER);

            Animation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setInterpolator(new AccelerateInterpolator());
            fadeOut.setStartOffset(TIMER);
            fadeOut.setFillAfter(true);
            fadeOut.setDuration(TIMER);
            animationSet.addAnimation(fadeOut);

            fadeOut.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    resetLayout();
                    boolean isLike = arrayListSnapshot.get(0).getIs_like();
                    String url = OakClubUtil.getFullLink(
                            activity, arrayListSnapshot.get(0)
                                    .getAvatar());
                    nameProfile = arrayListSnapshot.get(0).getName();
                    avaProfile = arrayListSnapshot.get(0).getAvatar();
                    arrayListSnapshot.remove(0);
                    String proId = profileId;

                    String action = Constants.ACTION;
                    if (tempX < -widthScreen / 4) {
                        action = Constants.ACTION_NOPE;
                    }

                    SnapshotEvent snapEvent = new SnapshotEvent(
                            Constants.SET_FAVORITE, activity,
                            proId, action);
                    activity.getRequestQueue().addRequest(snapEvent);

                    if (arrayListSnapshot.size() > 0) {
                        swapData(2);
                        if (action == Constants.ACTION_LIKE && isLike) {
                        	status = 0;
                        	match_time = arrayListSnapshot.get(0).getLike_time();
                            showDialog(url);
                        } else {
                            // OakClubUtil.clearCacheWithUrl(url, imageLoader);
                            setEnableAll(true);
                            profileId = arrayListSnapshot.get(0)
                                    .getProfile_id();
                        }
                    } else {
                        setEnableAll(false);
                        resetAll();
                        getListSnapshotData(SEG_RECORD);
                    }
                    isAction = false;
                }
            });

            switch (contentSnapshot) {
            case 1:
                if (action == Constants.ACTION_LIKE) {
                    ScaleAnimation animButton = new ScaleAnimation(1.5f, 1f,
                            1.5f, 1f, Animation.ABSOLUTE,
                            ivwLikeStamp.getWidth() / 2, Animation.ABSOLUTE,
                            ivwLikeStamp.getHeight() / 2);
                    ivwLikeStamp.setAnimation(animButton);
                    ivwLikeStamp.setAlpha(1.0f);
                    ivwNopeStamp.setAlpha(0.0f);
                } else {
                    ScaleAnimation animButton = new ScaleAnimation(1.5f, 1f,
                            1.5f, 1f, Animation.ABSOLUTE,
                            ivwNopeStamp.getWidth() / 2, Animation.ABSOLUTE,
                            ivwNopeStamp.getHeight() / 2);
                    ivwNopeStamp.setAnimation(animButton);
                    ivwLikeStamp.setAlpha(0.0f);
                    ivwNopeStamp.setAlpha(1.0f);
                }
                break;
            case 2:
                if (action == Constants.ACTION_LIKE) {
                    ScaleAnimation animButton = new ScaleAnimation(1.5f, 1f,
                            1.5f, 1f, Animation.ABSOLUTE,
                            ivwSecondLikeStamp.getWidth() / 2,
                            Animation.ABSOLUTE,
                            ivwSecondLikeStamp.getHeight() / 2);
                    ivwSecondLikeStamp.setAnimation(animButton);
                    ivwSecondLikeStamp.setAlpha(1.0f);
                    ivwSecondNopeStamp.setAlpha(0.0f);
                } else {
                    ScaleAnimation animButton = new ScaleAnimation(1.5f, 1f,
                            1.5f, 1f, Animation.ABSOLUTE,
                            ivwSecondNopeStamp.getWidth() / 2,
                            Animation.ABSOLUTE,
                            ivwSecondNopeStamp.getHeight() / 2);
                    ivwSecondNopeStamp.setAnimation(animButton);
                    ivwSecondLikeStamp.setAlpha(0.0f);
                    ivwSecondNopeStamp.setAlpha(1.0f);
                }
                break;
            case 3:
                if (action == Constants.ACTION_LIKE) {
                    ScaleAnimation animButton = new ScaleAnimation(1.5f, 1f,
                            1.5f, 1f, Animation.ABSOLUTE,
                            ivwThirdLikeStamp.getWidth() / 2,
                            Animation.ABSOLUTE,
                            ivwThirdLikeStamp.getHeight() / 2);
                    ivwThirdLikeStamp.setAnimation(animButton);
                    ivwThirdLikeStamp.setAlpha(1.0f);
                    ivwThirdNopeStamp.setAlpha(0.0f);
                } else {
                    ScaleAnimation animButton = new ScaleAnimation(1.5f, 1f,
                            1.5f, 1f, Animation.ABSOLUTE,
                            ivwThirdNopeStamp.getWidth() / 2,
                            Animation.ABSOLUTE,
                            ivwThirdNopeStamp.getHeight() / 2);
                    ivwThirdNopeStamp.setAnimation(animButton);
                    ivwThirdLikeStamp.setAlpha(0.0f);
                    ivwThirdNopeStamp.setAlpha(1.0f);
                }
                break;
            default:
                break;
            }

        }
        translateAnim.setDuration(TIMER);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setDuration(TIMER);

        animationSet.addAnimation(rotateAnim);
        animationSet.addAnimation(translateAnim);
        getCurrentContentView().startAnimation(animationSet);
    }

    private float getAngle(float distance) {
        float angle = (distance / widthScreen) * ORIGIN_ROTATE;
        return angle;
    }

    protected void resetLayout() {
        FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        p.gravity = Gravity.CENTER;
        getCurrentContentView().clearAnimation();
        getCurrentContentView().setLayoutParams(p);
        getCurrentContentView().setRotation(0);
        switch (contentSnapshot) {
        case 1:
            ivwLikeStamp.setAlpha(0.0f);
            ivwNopeStamp.setAlpha(0.0f);
            break;
        case 2:
            ivwSecondLikeStamp.setAlpha(0.0f);
            ivwSecondNopeStamp.setAlpha(0.0f);
            break;
        case 3:
            ivwThirdLikeStamp.setAlpha(0.0f);
            ivwThirdNopeStamp.setAlpha(0.0f);
            break;
        default:
            break;
        }
    }

    private OnClickListener snapEvent = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.activity_snapshot_flt_footer_ibn_like
                    && !isAction) {
                String target_name = OakClubUtil.getFirstName(arrayListSnapshot.get(0).getName()); 
                setLikeSnapshot(target_name);
            } else if (v.getId() == R.id.activity_snapshot_flt_footer_ibn_info) {
                intent = new Intent(activity,
                        InfoProfileOtherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                Bundle b = new Bundle();
                b.putSerializable("SnapshotDataBundle",
                        arrayListSnapshot.get(0));
                b.putString("Snapshot", "SnapshotFragment");
                intent.putExtras(b);
                activity.startActivityForResult(intent, 1);
                System.gc();
            } else if (v.getId() == R.id.activity_snapshot_flt_footer_ibn_nope
                    && !isAction) {
                String target_name = OakClubUtil.getFirstName(arrayListSnapshot.get(0).getName());
                setNopeSnapshot(target_name);
            }
            ;

        }
    };
    
    private void setLikeSnapshot(String name){
        if(isFirstLike)
        {
            String title = activity.getString(R.string.like_string) + "?";
            String content = activity.getString(R.string.snapshot_popup_like_info) + " " + name;
            showDialogFirst(title, content,false, true);
        }
        else{
            handleLikeButton();
        }
    }
    
    private void handleLikeButton(){
        isAction = true;
        String action = Constants.ACTION_LIKE;
        Constants.ACTION = Constants.ACTION_LIKE;
        animationForActionLikeOrNope(widthScreen, Animation.ABSOLUTE,
                widthScreen, action);
    }
    
    private void setNopeSnapshot(String name){
        if(isFirstNope){
            String title = activity.getString(R.string.snapshot_not_interested) + "?";
            String content = activity.getString(R.string.snapshot_popup_like_info) + " " + name;
            showDialogFirst(title, content, false, false);
        }
        else{
            handleNopeButton();
        }
    }

    private void handleNopeButton(){
        isAction = true;
        String action = Constants.ACTION_NOPE;
        Constants.ACTION = Constants.ACTION_NOPE;
        animationForActionLikeOrNope(-widthScreen, Animation.ABSOLUTE,
                widthScreen, action);
    }
    
    private void showDialogFirst(String title, String content, final boolean isDrag, boolean isLike){
        isActive = false;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater
                .from(activity);
        View layout = inflater.inflate(R.layout.dialog_active_snapshot,
                null);
        dialog.setView(layout, 0, 0, 0, 0);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tvTitle = (TextView)layout.findViewById(R.id.dialog_active_snapshot_tvtitle);
        tvTitle.setText(title);
        TextView tvContent = (TextView)layout.findViewById(R.id.dialog_active_snapshot_tvcontent);
        tvContent.setText(content);
        Button btActive = (Button)layout.findViewById(R.id.dialog_active_snapshot_btActive);
        SharedPreferences pref = activity.getSharedPreferences(Constants.PREFERENCE_NAME, 0);
        Editor editor = pref.edit();
        if(isLike){
            editor.putBoolean(Constants.PREFERENCE_SHOW_LIKE_DIALOG, false);
            isFirstLike = false;
            tvTitle.setTextColor(activity.getResources().getColor(R.color.dialog_snapshot_like));
            btActive.setText(activity.getString(R.string.like_string));
            btActive.setBackgroundResource(R.drawable.dialog_snapshot_like);
            btActive.setTextColor(activity.getResources().getColor(R.color.dialog_snapshot_text_like));
            btActive.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isDrag)
                        handleLikeButton();
                    else activeSnapshotAnimation();
                    dialog.cancel();
                }
            });
        }
        else{
            editor.putBoolean(Constants.PREFERENCE_SHOW_NOPE_DIALOG, false);
            isFirstNope = false;
            tvTitle.setTextColor(activity.getResources().getColor(R.color.dialog_snapshot_pass));
            btActive.setText(activity.getString(R.string.pass_string));
            btActive.setBackgroundResource(R.drawable.dialog_snapshot_pass);
            btActive.setTextColor(activity.getResources().getColor(R.color.dialog_snapshot_text_pass));
            btActive.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isDrag)
                        handleNopeButton();
                    else activeSnapshotAnimation();
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
                    isAction = false;
                    animationForActionLikeOrNope(tempX, tempY, widthScreen, "-1");
                }
                dialog.cancel();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
    
    private OnClickListener onDialogEvent = new OnClickListener() {
        @Override
        public void onClick(View v) {switch (v.getId()) {
            case R.id.popup_mutual_match_layout_btn_chat:
                if (activity.oakClubApi == null) {
                    activity.oakClubApi = OakClubApi.createInstance(
                            activity,
                            activity.getString(R.string.default_server_address));
                }

                SetViewMuatualEvent loader = new SetViewMuatualEvent(
                        Constants.SET_VIEW_MUTUAL_MATCH, activity,
                        profileId);
                activity.getRequestQueue().addRequest(loader);
                intent = new Intent(
                        activity.getApplicationContext(),
                        ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BUNDLE_PROFILE_ID, profileId);
                bundle.putString(Constants.BUNDLE_AVATAR, avaProfile);
                bundle.putString(Constants.BUNDLE_NAME, nameProfile);
            	bundle.putInt(Constants.BUNDLE_STATUS, status);
                bundle.putString(Constants.BUNDLE_MATCH_TIME, match_time);
                bundle.putBoolean(Constants.BUNDLE_NOTI, false);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                activity.startActivity(intent);
                dialogMutual.dismiss();
                dialogMutual.setCancelable(true);

                break;
            case R.id.popup_mutual_match_layout_btn_keep_swiping:
                if (index == 1) {
                    index = 0;
                    dialogMutual.dismiss();
                    dialogMutual.setCancelable(true);
                    setEnableAll(true);
                    profileId = arrayListSnapshot.get(0).getProfile_id();
                }
                break;
            }
        }
    };

    private void showProgress() {
        progressCircle.setVisibility(View.VISIBLE);
        fltBody.setVisibility(View.GONE);
        lltFooter.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressCircle.setVisibility(View.GONE);
        fltBody.setVisibility(View.VISIBLE);
        lltFooter.setVisibility(View.VISIBLE);
    }

    private String[] getArrayImage() {
        if (arrayListSnapshot == null)
            return null;
        String s[] = new String[arrayListSnapshot.size()];
        int i = 0;
        for (SnapshotData element : arrayListSnapshot) {
            s[i] = element.getAvatar();
            i++;
        }
        return s;
    }

    private void swapData(int index) {
        if (activity.isInternetAccess()) {
            if (index < arrayListSnapshot.size()) {
                assignInfo(index);
                changeImage(index);
            } else {
                getCurrentContentView().setEnabled(false);
                switch (contentSnapshot) {
                case 1:
                    ivwAvatar.setBackgroundColor(Color.WHITE);
                    tvwInfo.setText("");
                    tvwNumFriend.setText("0");
                    tvwNumShareInterested.setText("0");
                    tvwNumPicture.setText("0");
                    break;
                case 2:
                    ivwAvatarSecond.setBackgroundColor(Color.WHITE);
                    tvwSecondInfo.setText("");
                    tvwSecondNumFriend.setText("0");
                    tvwSecondNumShareInterested.setText("0");
                    tvwSecondNumPicture.setText("0");
                    break;
                case 3:
                    ivwAvatarThird.setBackgroundColor(Color.WHITE);
                    tvwThirdInfo.setText("");
                    tvwThirdNumFriend.setText("0");
                    tvwThirdNumShareInterested.setText("0");
                    tvwThirdNumPicture.setText("0");
                    break;
                default:
                    break;
                }
            }
            if (contentSnapshot < 3)
                contentSnapshot++;
            else
                contentSnapshot = 1;
            getBackContentView().bringToFront();
            getCurrentContentView().bringToFront();
        } else {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(activity);
            final AlertDialog dialog = builder.create();
            LayoutInflater inflater = LayoutInflater
                    .from(activity);
            View layout = inflater.inflate(R.layout.dialog_warning_ok,
                    null);
            dialog.setView(layout, 0, 0, 0, 0);
            Button btnOK = (Button) layout
                    .findViewById(R.id.dialog_internet_access_lltfooter_btOK);
            btnOK.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    activity.finish();
                    // System.exit(0);
                }
            });
            dialog.setCancelable(false);
            dialog.show();

        }
    }

    private ImageView imgAva = null;
    private void changeImage(final int index) {
        final String imageUrl = OakClubUtil.getFullLink(activity, 
                arrayListSnapshot.get(index).getAvatar(), Constants.widthImage, Constants.heightImage, 1);
        handler = new Handler();
        switch (contentSnapshot) {
            case 1: imgAva = ivwAvatar;
                break;
            case 2: imgAva = ivwAvatarSecond;
                break;
            case 3: imgAva = ivwAvatarThird;
                break;
            }
        OakClubUtil.loadImageFromUrl(activity,imageUrl, imgAva);
//      OakClubUtil.loadImageFromUrl(SnapshotActivity.activity,imgAva, imageUrl);
    }

    private void setEnableAll(boolean b) {
        btnInfo.setEnabled(b);
        btnLike.setEnabled(b);
        btnNope.setEnabled(b);
    }

    public void getListSnapshotData(int start) {
        if (activity.oakClubApi == null) {
            activity.oakClubApi = OakClubApi.createInstance(
                    activity.getApplicationContext(),
                    activity.getString(R.string.default_server_address));
        }
        showProgress();
        GetSnapShotLoader loader = new GetSnapShotLoader(
                Constants.GET_SNAPSHOT, activity,
                MainActivity.facebook_user_id, start, LIMIT_RECORD);
        activity.getRequestQueue().addRequest(loader);
    }

    private void showDialog(String url) {
        index = 1;
        dialogMutual = new Dialog(activity);

        //dialogMutual.setTitle("");
        dialogMutual.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = activity.getLayoutInflater().inflate(
                R.layout.popup_mutual_match_layout, null);
        TextViewWithFont tvTitle = (TextViewWithFont)view.findViewById(R.id.tvw_text);
        tvTitle.setFont("helveticaneueultralight.ttf");
        ivwMutualMe = (ImageView) view
                .findViewById(R.id.popup_mutual_match_layout_image_my);
        ivwMutualYou = (ImageView) view
                .findViewById(R.id.popup_mutual_match_layout_image_you);
        txtMutualMatch = (TextView) view.findViewById(R.id.tvw_text_notice);
        dialogMutual.setContentView(view);
        RelativeLayout btnDialogKeepSwiping = (RelativeLayout) view
                .findViewById(R.id.popup_mutual_match_layout_btn_keep_swiping);
        btnDialogKeepSwiping.setOnClickListener(onDialogEvent);
        RelativeLayout btnDialogChat = (RelativeLayout) view
                .findViewById(R.id.popup_mutual_match_layout_btn_chat);
        btnDialogChat.setOnClickListener(onDialogEvent);

        OakClubUtil.loadImageFromUrl(activity, ivwMutualMe,
                urlAvatar);
        OakClubUtil
                .loadImageFromUrl(activity, ivwMutualYou, url);
        txtMutualMatch
                .setText(String.format(
                        activity.getString(
                                R.string.txt_you_and_have_like_each_other),
                        nameProfile));
        dialogMutual.show();
    }
    

    class SnapshotEvent extends RequestUI {
        private String proId = "";
        private String numberSet = "";
        private SetLikeMessageReturnObject resultEvent;

        public SnapshotEvent(Object key, Activity activity, String proId,
                String numberSet) {
            super(key, activity);
            this.proId = proId;
            this.numberSet = numberSet;
        }

        @Override
        public void execute() throws Exception {
            resultEvent = activity.oakClubApi.SetFavoriteInSnapshot(proId, numberSet);
        }

        @Override
        public void executeUI(Exception ex) {

        }

    }

    class SetViewMuatualEvent extends RequestUI {
        private String proId = "";
        private SetViewMutualReturnObject resultEvent;

        public SetViewMuatualEvent(Object key, Activity activity, String proId) {
            super(key, activity);
            this.proId = proId;
        }

        @Override
        public void execute() throws Exception {
            resultEvent = activity.oakClubApi.SetViewedMutualMatch(proId);
        }

        @Override
        public void executeUI(Exception ex) {

        }

    }

    class GetSnapShotLoader extends RequestUI {
        GetSnapShot obj;
        String userId;
        int startIndex;
        int limitIndex;

        public GetSnapShotLoader(Object key, Activity activity, String userId,
                int startIndex, int limitIndex) {
            super(key, activity);
            this.userId = userId;
            this.startIndex = startIndex;
            this.limitIndex = limitIndex;
        }

        @Override
        public void execute() throws Exception {
            obj = activity.oakClubApi.getSnapShot(userId, startIndex, limitIndex);
        }

        @Override
        public void executeUI(Exception ex) {
            if (obj != null && obj.getData() != null
                    && obj.getData().size() > 0) {
                objSnapshot = obj;
                arrayListSnapshot.addAll(obj.getData());
                initData();
                hideProgress();
            } else if (isFirst) {
                setEnableAll(false);
                fltContent.setEnabled(false);
                fltContentSecond.setEnabled(false);

                ivwAvatar.setImageResource(R.drawable.logo_splashscreen);
                tvwInfo.setText("");
                tvwNumFriend.setText("0");
                tvwNumShareInterested.setText("0");
                tvwNumPicture.setText("0");

                ivwAvatarSecond
                        .setImageResource(R.drawable.logo_splashscreen);
                tvwSecondInfo.setText("");
                tvwSecondNumFriend.setText("0");
                tvwSecondNumShareInterested.setText("0");
                tvwSecondNumPicture.setText("0");

                ivwAvatarThird
                        .setImageResource(R.drawable.logo_splashscreen);
                tvwThirdInfo.setText("");
                tvwThirdNumFriend.setText("0");
                tvwThirdNumShareInterested.setText("0");
                tvwThirdNumPicture.setText("0");

                progressCircle.setVisibility(View.GONE);
                progressFinder.setVisibility(View.VISIBLE);
            }
        }
    }

    private LinearLayout getCurrentContentView() {
        switch (contentSnapshot) {
        case 1:
            fltContent.setEnabled(true);
            fltContentSecond.setEnabled(false);
            fltContentThird.setEnabled(false);
            return fltContent;
        case 2:
            fltContentSecond.setEnabled(true);
            fltContentThird.setEnabled(false);
            fltContent.setEnabled(false);
            return fltContentSecond;
        case 3:
            fltContentThird.setEnabled(true);
            fltContent.setEnabled(false);
            fltContentSecond.setEnabled(false);
            return fltContentThird;
        default:
            return null;
        }
    }

    private LinearLayout getBackContentView() {
        switch (contentSnapshot) {
        case 1:
            fltContentSecond.setEnabled(true);
            fltContentThird.setEnabled(false);
            fltContent.setEnabled(false);
            return fltContentSecond;
        case 2:
            fltContentThird.setEnabled(true);
            fltContent.setEnabled(false);
            fltContentSecond.setEnabled(false);
            return fltContentThird;
        case 3:
            fltContent.setEnabled(true);
            fltContentSecond.setEnabled(false);
            fltContentThird.setEnabled(false);
            return fltContent;
        default:
            return null;
        }
    }

    private void initData() {
        contentSnapshot = 3;
        swapData(2);

        contentSnapshot = 2;
        swapData(1);

        contentSnapshot = 1;
        swapData(0);

        contentSnapshot = 1;
        getCurrentContentView().bringToFront();

        profileId = arrayListSnapshot.get(0).getProfile_id();
        setEnableAll(true);

    }

//    public void releaseBitmap(){
//        OakClubUtil.recycleImage(ivwAvatar);
//        OakClubUtil.recycleImage(ivwAvatarSecond);
//        OakClubUtil.recycleImage(ivwAvatarThird);
//        progCir.recycleAvatar();
//        progFin.recycleAvatar();
//    }
}
