package com.oakclub.android.view;

import com.oakclub.android.R;
import com.oakclub.android.fragment.ProfileSettingFragment;
import com.oakclub.android.util.Constants;
import com.oakclub.android.util.OakClubUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ProgressCircle extends FrameLayout{
	
	private ImageView iv = null;
	private CircleImageView civAvatar = null;
	private Paint paintCircle1,paintCircle2, strokeCircle1, strokeCircle2;
	private boolean isStop = false;
	private float radius1, radius2;
	private int alphaCircle1, alphaCircle2;
	private int centerX,centerY;
    protected static float Radius = 250;
    protected static float RadiusMin = 0;
    protected static float RadiusAverage = 170;
    protected static int AlphaMax = 255;
    protected static int Step = 130;
	protected static final int WIDTH_IMAGE = 150;
	protected static final int WIDTH_IMAGE_AVATAR = 100;
	private FrameLayout.LayoutParams params;
    private boolean enablePaint1 = true;
    private boolean enablePaint2 = false;
    private float valueRiseRadius, valueRiseAnpha = 0;
	
	public ProgressCircle(Context context, AttributeSet attrs, ImageView iv) {
		super(context, attrs);
		setImageView(iv);
		init();
	}
	public ProgressCircle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public ProgressCircle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init(){
		
		if(iv==null){
			iv = new ImageView(getContext());
			iv.setImageResource(R.drawable.map_border_loaded);
			params = new FrameLayout.LayoutParams(WIDTH_IMAGE,WIDTH_IMAGE);
			params.gravity = Gravity.CENTER;
			iv.setLayoutParams(params);
			this.addView(iv);
		}

        String url = "";
        
		if(ProfileSettingFragment.profileInfoObj!=null && !ProfileSettingFragment.profileInfoObj.getAvatar().equals("")){
		    url = OakClubUtil.getFullLink(getContext(), ProfileSettingFragment.profileInfoObj.getAvatar());
		}
		
		civAvatar = new CircleImageView(getContext());   
        OakClubUtil.loadImageFromUrl(getContext() ,url, civAvatar);
        params = new FrameLayout.LayoutParams(WIDTH_IMAGE_AVATAR,WIDTH_IMAGE_AVATAR);
        params.gravity = Gravity.CENTER;
        civAvatar.setLayoutParams(params);
        this.addView(civAvatar);
		
		paintCircle1 = new Paint();
		paintCircle2 = new Paint();
		strokeCircle1 = new Paint();
		strokeCircle2 = new Paint();

        alphaCircle1 = AlphaMax;
        radius1 = RadiusMin;
        alphaCircle2 = AlphaMax;
        radius2 = RadiusMin;	
		isStop = false;
		Step = (int)Radius/3;
		DisplayMetrics displayMetrics = new DisplayMetrics(); 
		displayMetrics = getContext().getResources().getDisplayMetrics();
		Radius = displayMetrics.widthPixels/2;
		RadiusAverage = Radius/2;
		valueRiseRadius = Radius/Step;
		valueRiseAnpha = AlphaMax/Step;
	}
	
	public void setImageForImageView(int res){
		iv.setImageResource(res);
	}
	
	public void setImageView(ImageView iv){
		this.iv = iv;
	};
	
	@Override
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(!isStop){

			paintCircle1.setColor(Color.argb((int)alphaCircle1,255, 206, 255));
            paintCircle2.setColor(Color.argb((int)alphaCircle2,255, 206, 255));
            strokeCircle1.setColor(Color.argb((int)alphaCircle1,255, 150, 255));
            strokeCircle2.setColor(Color.argb((int)alphaCircle2,255, 150, 255));
            
		    if(enablePaint1){
		        //reset
		        if (radius1 >= Radius)
		        {
                    radius1 = (int)RadiusMin;
                    alphaCircle1 = AlphaMax;
                    enablePaint1 =false;		            
		        }
		        else 
		        {
                    radius1 += valueRiseRadius;
                    if(alphaCircle1>0)
                        alphaCircle1-=valueRiseAnpha;
                    if (radius1>=RadiusAverage) 
                    	enablePaint2 =true;
		        }

				strokeCircle1.setStyle(Paint.Style.STROKE) ;
				strokeCircle1.setStrokeWidth(5);
                canvas.drawCircle(centerX,centerY, radius1, paintCircle1);
                canvas.drawCircle(centerX,centerY, radius1, strokeCircle1);
		    }
		    if(enablePaint2){
		      //reset
                if (radius2 >= Radius)
                {
                    radius2 = (int)RadiusMin;
                    alphaCircle2 = AlphaMax;
                    enablePaint2 =false;                    
                }
                else 
                {
                    radius2 += valueRiseRadius;
                    if(alphaCircle2>0)
                        alphaCircle2 -= valueRiseAnpha;
                    if (radius2>=RadiusAverage) 
                    	enablePaint1 =true;
                }

        		strokeCircle2.setStyle(Paint.Style.STROKE) ;
				strokeCircle2.setStrokeWidth(5);
                canvas.drawCircle(centerX,centerY, radius2, paintCircle2);
                canvas.drawCircle(centerX,centerY, radius2, strokeCircle2);
		    }
			invalidate();
		}
		
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		centerX = w/2;
		centerY = h/2;
	}

	public void recycleAvatar(){
	    if(civAvatar != null){
    	    Drawable drawable = civAvatar.getDrawable();
            if(drawable instanceof BitmapDrawable)
            {
                BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
                bitmapDrawable.getBitmap().recycle();
            }
	    }
	}
	
}
