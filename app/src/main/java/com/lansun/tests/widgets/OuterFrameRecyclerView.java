package com.lansun.tests.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * Created by ly on 1/17/17.
 */

public class OuterFrameRecyclerView extends RecyclerView{

    private GestureDetectorCompat gdc;

    public OuterFrameRecyclerView(Context context) {
        this(context, null);
    }

    public OuterFrameRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OuterFrameRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gdc = new GestureDetectorCompat(context, new SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e) {
                return super.onDown(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return Math.abs(distanceY)>=Math.abs(distanceX);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return Math.abs(velocityY)>=Math.abs(velocityX)&&velocityY>800;
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e)&&gdc.onTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }
}
