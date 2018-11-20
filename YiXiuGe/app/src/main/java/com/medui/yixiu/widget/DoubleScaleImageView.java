package com.medui.yixiu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.xson.common.utils.L;

/**
 * Created by xkai on 2017/8/9.
 * 单双击监听
 */

public class DoubleScaleImageView extends ImageView implements GestureDetector.OnGestureListener {

    private GestureDetector gestureScanner;

    public DoubleScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureScanner = new GestureDetector(this);
        gestureScanner.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
//                Log.d("onDoubleTap", "单击");
                if (mDoubleClickListener != null) {
                    mDoubleClickListener.onSingleTapConfirmed();
                }
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //双击时产生一次
                if (mDoubleClickListener != null) {
                    mDoubleClickListener.onDoubleTap();
                }
                L.d("onDoubleTap", "双击时产生一次");
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                //双击时产生两次
                L.d("onDoubleTap", "双击时产生两次");
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureScanner.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

    DoubleClickListener mDoubleClickListener;

    public void setDoubleClickListener(DoubleClickListener doubleClickListener) {
        mDoubleClickListener = doubleClickListener;
    }

    //单双击接口
    public interface DoubleClickListener {
        void onSingleTapConfirmed();//单击

        void onDoubleTap();//双击
    }
}
