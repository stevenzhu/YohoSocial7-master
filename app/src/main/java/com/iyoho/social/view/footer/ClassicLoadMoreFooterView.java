package com.iyoho.social.view.footer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.iyoho.social.R;
import com.iyoho.social.view.RotateLoading;


/**
 * Created by Aspsine on 2015/9/2.
 */
public class ClassicLoadMoreFooterView extends SwipeLoadMoreFooterLayout {
    private TextView tvLoadMore;
    private ImageView ivSuccess;
    private RotateLoading rprogressBar;
    private int mFooterHeight;

    public ClassicLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public ClassicLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFooterHeight = getResources().getDimensionPixelOffset(R.dimen.load_more_footer_height_classic);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvLoadMore = (TextView) findViewById(R.id.tvLoadMore);
        ivSuccess = (ImageView) findViewById(R.id.ivSuccess);
        rprogressBar = (RotateLoading) findViewById(R.id.progressbar);
    }

    @Override
    public void onPrepare() {
        ivSuccess.setVisibility(GONE);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            ivSuccess.setVisibility(GONE);
            rprogressBar.setVisibility(GONE);
            if(rprogressBar.isStart()){
                rprogressBar.stop();
            }
            if (-y >= mFooterHeight) {
                tvLoadMore.setText(R.string.release_to_load_more);
            } else {
                tvLoadMore.setText(R.string.swipe_to_load_more);
            }
        }
    }

    @Override
    public void onLoadMore() {
        tvLoadMore.setText(R.string.loading_more);
        rprogressBar.setVisibility(VISIBLE);
        if(!rprogressBar.isStart()){
            rprogressBar.start();
        }
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        rprogressBar.setVisibility(GONE);
        if(rprogressBar.isStart()){
            rprogressBar.stop();
        }
        ivSuccess.setVisibility(VISIBLE);
    }

    @Override
    public void onReset() {
        ivSuccess.setVisibility(GONE);
    }
}
