package com.oakclub.android.view;

import com.oakclub.android.util.OakClubUtil;

import android.content.Context;
import android.util.AttributeSet;

public class TextViewCustomScreen extends TextViewWithFont {

	private int size = (int) (OakClubUtil.getWidthScreen(getContext())/45);
	
	public TextViewCustomScreen(Context context) {
		super(context);
		init();
	}
	public TextViewCustomScreen(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public TextViewCustomScreen(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

    private void init() {
        this.setTextSize(OakClubUtil.convertDpToPixel(size, getContext()));
    }
	
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
