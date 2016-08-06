package com.example.lvd.myapplication;

/**
 * Created by lvd on 17/07/2016.
 */
import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeTouchListener implements View.OnTouchListener {

    private Activity activity;
    static final int MIN_DISTANCE_LEFT_RIGHT = 150;
    static final int MIN_DISTANCE_TOP_BOTTOM = 100;
    private float downX, downY, upX, upY;

    private long lastClickTime = 0;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

    public OnSwipeTouchListener(final Activity activity) {
        this.activity = activity;
    }

    public void onRightToLeftSwipe() {}

    public void onLeftToRightSwipe(){}

    public void onTopToBottomSwipe(){}

    public void onBottomToTopSwipe(){}

    public void onDoubleClick(){}

    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                //   return true;

                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                    this.onDoubleClick();
                    return true;
                }
                lastClickTime = clickTime;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if(Math.abs(deltaX) > MIN_DISTANCE_LEFT_RIGHT){
                    // left or right
                    if(deltaX < 0) { this.onLeftToRightSwipe(); return true; }
                    if(deltaX > 0) { this.onRightToLeftSwipe(); return true; }
                } else { Log.i( "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE_LEFT_RIGHT, ""); }

                // swipe vertical?
                if(Math.abs(deltaY) > MIN_DISTANCE_TOP_BOTTOM){
                    // top or down
                    if(deltaY < 0) { this.onTopToBottomSwipe(); return true; }
                    if(deltaY > 0) { this.onBottomToTopSwipe(); return true; }
                } else {
                    Log.i( "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE_TOP_BOTTOM, "");
                }
            }
        }
        return false;
    }
}

