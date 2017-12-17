package com.iyoho.social.fragment.tab;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iyoho.social.R;
import com.iyoho.social.activity.tabsocial.MessageActivity;
import com.iyoho.social.base.IBaseActivity;
import com.iyoho.social.base.IBaseFragment;
import com.iyoho.social.view.CustomViewPager;
import com.iyoho.social.view.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import im.quar.autolayout.view.AutoLinearLayout;

public class SocialTabFragment extends Fragment implements View.OnClickListener{
    public List<Fragment> fragments;
    private CustomViewPager mViewPager;
    private static final String[] CHANNELS = new String[]{"话题", "发现", "圈子"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private AutoLinearLayout messageLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_social_tab, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        initEvent();
    }

    public void initView(View view) {
        messageLayout=view.findViewById(R.id.messageLayout);
        fragments = new ArrayList<>();
        TopicFragment topicFragment = new TopicFragment();
        Bundle bundle0 = new Bundle();
        bundle0.putString("socialType", "topic");
        topicFragment.setArguments(bundle0);
        fragments.add(topicFragment);

       FindFragment findFragment = new FindFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("socialType", "find");
        findFragment.setArguments(bundle1);
        fragments.add(findFragment);

        CircleFragment circleFragment = new CircleFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("socialType", "circle");
        circleFragment.setArguments(bundle2);
        fragments.add(circleFragment);

        mViewPager = (CustomViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        initMagicIndicator(view);
        mViewPager.setCurrentItem(1);

    }


    public void initData() {

    }


    public void initEvent() {
        messageLayout.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(getContext(), ""+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.messageLayout:
             IBaseActivity.start(getActivity(), MessageActivity.class,null);
             break;
     }
    }

    private void initMagicIndicator(View view) {
        MagicIndicator magicIndicator = (MagicIndicator)view.findViewById(R.id.magic_indicator2);
        //设置背景
        // magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        //设置是否平分
        //commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(getContext());
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setTextSize(17);
                simplePagerTitleView.setNormalColor(Color.parseColor("#ddffffff"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#ffffff"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                // BezierPagerIndicator indicator = new BezierPagerIndicator(context);
                // indicator.setColors(Color.parseColor("#ff4a42"), Color.parseColor("#fcde64"), Color.parseColor("#73e8f4"));
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                //设置滑动拉长的动画
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
                indicator.setLineHeight(UIUtil.dip2px(context, 1));
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(UIUtil.dip2px(context, 20));
                indicator.setRoundRadius(UIUtil.dip2px(context, 2));
                indicator.setYOffset(UIUtil.dip2px(context, 7));
                indicator.setColors(Color.parseColor("#ffffff"));
                return indicator;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                if (index == 0) {
                    return 1.0f;
                } else if (index == 1) {
                    return 1.0f;
                } else {
                    return 1.0f;
                }
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        //設置Tab间隔
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return UIUtil.dip2px(getActivity(), 35);
            }
        });

        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }
}
