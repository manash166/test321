package com.example.test321;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

public class ClearableEditText extends AppCompatEditText {

    public ClearableEditText(Context context) {
        super(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Drawable endDrawable = getCompoundDrawables()[2]; // Right drawable
            if (endDrawable != null) {
                int drawableStart = getWidth() - getPaddingEnd() - endDrawable.getIntrinsicWidth();
                if (event.getX() >= drawableStart) {
                    setText(""); // Clear the text
                    performClick(); // Accessibility
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick(); // Keeps accessibility support
    }
}
