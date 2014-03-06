package com.oakclub.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.oakclub.android.image.SmartImageView;
import com.oakclub.android.util.OakClubUtil;

public class CornerImageView extends SmartImageView {

	private Bitmap framedPhoto;
	
	public CornerImageView(Context context) {
		super(context);
	}
	public CornerImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public CornerImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		try {
			framedPhoto = createFramedPhoto(10, 10,Color.RED);
			if (framedPhoto != null) {
				canvas.drawBitmap(framedPhoto, 0, 0, null);
			}
		} catch (Exception e) {
			framedPhoto.recycle();
			e.printStackTrace();
		}
		
	}
	private Bitmap createFramedPhoto(int corner, int border, int  color) {
		Bitmap output = null;
		Drawable imageDrawable = getDrawable();
		try {
			if (imageDrawable != null) {
				output = Bitmap.createBitmap(getWidth(), getHeight(),
						Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(output);

			    int borderSize = (int)OakClubUtil.convertDpToPixel(border, getContext());
			    int cornerSize = (int)OakClubUtil.convertDpToPixel(corner, getContext());
				RectF outerRect = new RectF(0, 0, getWidth(), getHeight());
				
				Paint paint = new Paint();
			    paint.setAntiAlias(true);
			    canvas.drawARGB(0, 0, 0, 0);
			    paint.setColor(0xff424242);
			    paint.setStrokeWidth((float) borderSize);
			    canvas.drawRoundRect(outerRect, cornerSize, cornerSize, paint);
			    
				paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
				imageDrawable.setBounds(0, 0, getWidth(), getHeight());

				canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
				imageDrawable.draw(canvas);
				canvas.restore();
				return output;
			}
		} catch (Exception e) {
			output.recycle();
			e.printStackTrace();
		}
		return null;
	}
	
}
