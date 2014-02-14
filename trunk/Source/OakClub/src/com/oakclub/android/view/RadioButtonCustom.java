package com.oakclub.android.view;

import com.oakclub.android.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

public class RadioButtonCustom extends RadioButton {

    Drawable buttonDrawable;
    
    public RadioButtonCustom(Context context, Drawable drawable) {
        super(context);
        buttonDrawable = drawable;
        setButtonDrawable(android.R.color.transparent);
    }

    public RadioButtonCustom(Context context) {
        super(context);
        init();
    }

    public RadioButtonCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public RadioButtonCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    private void init(){
        buttonDrawable = getResources().getDrawable(R.drawable.radiogroup_selector2);
        setButtonDrawable(android.R.color.transparent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if (buttonDrawable != null) {
            buttonDrawable.setState(getDrawableState());
            final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
            final int height = buttonDrawable.getIntrinsicHeight();

            int y = 0;

            switch (verticalGravity) {
                case Gravity.BOTTOM:
                    y = getHeight() - height;
                    break;
                case Gravity.CENTER_VERTICAL:
                    y = (getHeight() - height) / 2;
                    break;
            }

            int buttonWidth = buttonDrawable.getIntrinsicWidth();
            int buttonLeft = getWidth() - buttonWidth*2;
            buttonDrawable.setBounds(buttonLeft, y, buttonLeft+buttonWidth, y + height);
            buttonDrawable.draw(canvas);
        }
        
    }
    
    
}
