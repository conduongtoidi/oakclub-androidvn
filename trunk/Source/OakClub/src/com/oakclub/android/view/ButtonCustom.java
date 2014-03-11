package com.oakclub.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonCustom extends Button {

	public int state; //0 hide 1 left 2 right
	public ButtonCustom(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ButtonCustom(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ButtonCustom(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
}
