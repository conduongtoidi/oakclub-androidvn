package com.oakclub.android.view;

import com.oakclub.android.image.SmartImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


public class XImageview extends SmartImageView {

	private Bitmap framedPhoto;
	private float radius = 1000.0f;
	protected int type = 0;

	public XImageview(Context context) {
		super(context);
	}

	public XImageview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public XImageview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (type != 0) {
			// if (framedPhoto == null) {
			try {
				framedPhoto = createFramedPhoto();
				// }
				if (framedPhoto != null) {
					canvas.drawBitmap(framedPhoto, 0, 0, null);
				}
			} catch (Exception e) {
				framedPhoto.recycle();
				e.printStackTrace();
			}
		} else {
			super.onDraw(canvas);
		}
	}

	private Bitmap createFramedPhoto() {
		Bitmap output = null;
		Drawable imageDrawable = getDrawable();
		try {
			if (imageDrawable != null) {
				output = Bitmap.createBitmap(getWidth(), getHeight(),
						Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(output);

				RectF outerRect = new RectF(0, 0, getWidth(), getHeight());
				float outerRadius = radius;
				if (type == 2) {
					outerRadius = getWidth() / 10f;
				}
				Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
				paint.setColor(Color.RED);
				canvas.drawRoundRect(outerRect, outerRadius, outerRadius, paint);

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
