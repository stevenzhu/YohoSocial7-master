package com.iyoho.social.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.iyoho.social.R;
import com.yoho.glide.GlideImageLoader;
import com.yoho.glide.GlideImageView;
import com.yoho.glide.progress.CircleProgressView;
import com.yoho.glide.progress.OnGlideImageViewProgressListener;

import java.util.List;

import static android.R.id.list;


/**
 * Created by hackware on 2016/9/10.
 */

public class ViewPagerImageAdapter extends PagerAdapter {
    private List<String> mDataList;
    private GlideImageView glideImageView=null;
    private CircleProgressView circleProgressView=null;
    private ImageView[] mImageViews;
    public ViewPagerImageAdapter(final Context context, List<String> dataList) {
        mDataList = dataList;
        if(mDataList!=null){
            //将图片装载到数组中
            mImageViews = new ImageView[mDataList.size()];
            for(int x=0; x<mImageViews.length; x++){
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mImageViews[x] = imageView;
                Glide.with(context).load(mDataList.get(x)).into(imageView);
                final int position=x;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,position+"",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if(mImageViews.length>0){
//            ((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
//            return mImageViews[position % mImageViews.length];
            container.addView(mImageViews[position]);
            return mImageViews[position];
        }else{
            return 0;
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
