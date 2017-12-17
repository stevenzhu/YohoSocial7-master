package com.iyoho.social.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import io.rong.imlib.RongIMClient;

/**
 * Created by ab053167 on 2017/12/5.
 */

public class ChildViewPager extends ViewPager {
private ViewGroup parent;
    public ChildViewPager(Context context){
        super(context);
    }
    public ChildViewPager(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
    }
   public void setNestedpParent(ViewGroup parent){
       this.parent=parent;
   }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(parent!=null){
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        if(parent!=null){
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptHoverEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(parent!=null){
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(ev);
    }
}