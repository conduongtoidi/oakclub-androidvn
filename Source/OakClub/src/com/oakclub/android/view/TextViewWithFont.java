package com.oakclub.android.view;

import com.oakclub.android.util.FontCache;
import com.oakclub.android.util.OakClubUtil;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class TextViewWithFont extends TextView {

	private String font = "helveticaneue.ttf";
	private int size = (int) (OakClubUtil.getWidthScreen(getContext())/45);
	
	public TextViewWithFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewWithFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewWithFont(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = FontCache.get("fonts/" + font, getContext());
            if(tf != null) {
                this.setTypeface(tf);
            }
        }
        //this.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        //this.setTextSize(OakClubUtil.convertDpToPixel(size, getContext()));
    }
    

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
