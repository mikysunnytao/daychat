package com.daychat.live.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.daychat.live.R;




public class MyFrameLayout3 extends FrameLayout {

    private final float mRatio;
    private final float mOffestY;

    public MyFrameLayout3(@NonNull Context context) {
        this(context, null);
    }

    public MyFrameLayout3(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyFrameLayout3(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyFrameLayout2);
        mRatio = 1f;
        mOffestY = ta.getDimension(R.styleable.MyFrameLayout2_mfl_offestY, 0);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (widthSize * mRatio + mOffestY), MeasureSpec.EXACTLY);
        Log.e("onMeasure","widthMeasureSpec"+widthMeasureSpec+"  heightMeasureSpec"+heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
