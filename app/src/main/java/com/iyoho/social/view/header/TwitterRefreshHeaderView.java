package com.iyoho.social.view.header;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.iyoho.social.R;
import com.iyoho.social.view.RotateLoading;


/**
 * Created by Aspsine on 2015/9/9.
 */
public class TwitterRefreshHeaderView extends SwipeRefreshHeaderLayout {

   // private ImageView ivArrow;

    private ImageView ivSuccess;

    private TextView tvRefresh;
    private RotateLoading rotateLoading;

    private int mHeaderHeight;

    private Animation rotateUp;

    private Animation rotateDown;

    private boolean rotated = false;

    public TwitterRefreshHeaderView(Context context) {
        this(context, null);
    }

    public TwitterRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwitterRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.refresh_header_height_twitter);
        rotateUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up);
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.rotate_down);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvRefresh = (TextView) findViewById(R.id.tvRefresh);
       // ivArrow = (ImageView) findViewById(ivArrow);
        ivSuccess = (ImageView) findViewById(R.id.ivSuccess);
        rotateLoading= (RotateLoading) findViewById(R.id.progressbar);
    }

    @Override
    public void onRefresh() {
        ivSuccess.setVisibility(GONE);
       // ivArrow.clearAnimation();
        //ivArrow.setVisibility(GONE);
        rotateLoading.setVisibility(VISIBLE);
        if(!rotateLoading.isStart()){
            rotateLoading.start();
        }
        tvRefresh.setText(R.string.refreshing);
    }

    @Override
    public void onPrepare() {
        Log.d("TwitterRefreshHeader", "onPrepare()");
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
           // ivArrow.setVisibility(VISIBLE);
            rotateLoading.setVisibility(GONE);
            if(rotateLoading.isStart()){
                rotateLoading.stop();
            }
            //progressBar.setVisibility(GONE);
            ivSuccess.setVisibility(GONE);
            if (y > mHeaderHeight) {
                tvRefresh.setText(R.string.release_to_refresh);
                if (!rotated) {
                   // ivArrow.clearAnimation();
                  //  ivArrow.startAnimation(rotateUp);
                    rotated = true;
                }
            } else if (y < mHeaderHeight) {
                if (rotated) {
                  //  ivArrow.clearAnimation();
                   // ivArrow.startAnimation(rotateDown);
                    rotated = false;
                }

                tvRefresh.setText(R.string.swipe_to_refresh);
            }
        }
    }

    @Override
    public void onRelease() {
        Log.d("TwitterRefreshHeader", "onRelease()");
    }

    @Override
    public void onComplete() {
        rotated = false;
        //ivSuccess.setVisibility(VISIBLE);
        ivSuccess.setVisibility(GONE);
        //ivArrow.clearAnimation();
        //ivArrow.setVisibility(GONE);
        rotateLoading.setVisibility(GONE);
        if(rotateLoading.isStart()){
            rotateLoading.stop();
        }
        //progressBar.setVisibility(GONE);
        tvRefresh.setText(R.string.complete);
    }

    @Override
    public void onReset() {
        rotated = false;
        ivSuccess.setVisibility(GONE);
        //ivArrow.clearAnimation();
        //ivArrow.setVisibility(GONE);
       // progressBar.setVisibility(GONE);
        rotateLoading.setVisibility(GONE);
        if(rotateLoading.isStart()){
            rotateLoading.stop();
        }
    }

}
