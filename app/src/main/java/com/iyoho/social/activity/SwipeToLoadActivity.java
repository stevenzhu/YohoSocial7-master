package com.iyoho.social.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.iyoho.social.Entry.TestListEntry;
import com.iyoho.social.R;
import com.iyoho.social.adapter.ViewPagerImageAdapter;
import com.iyoho.social.base.IBaseActivity;
import com.iyoho.social.server.ServerCallback;
import com.iyoho.social.server.ServerInterfaces;
import com.iyoho.social.utils.FastJsonUtils;
import com.iyoho.social.view.ChildViewPager;
import com.iyoho.social.view.ScaleCircleNavigator;
import com.yoho.glide.GlideImageLoader;
import com.yoho.glide.GlideImageView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SwipeToLoadActivity extends IBaseActivity implements OnRefreshListener,OnLoadMoreListener {
    private SwipeToLoadLayout swipeToLoadLayout = null;
    private ListView listView;
    private MyAdapter myAdapter;
    private List<TestListEntry.ResultBean> resultBeanList;
    private static final String[] AVATARS = {"http://tupian.qqjay.com/u/2011/0729/e755c434c91fed9f6f73152731788cb3.jpg", "http://img1.touxiang.cn/uploads/allimg/111029/2330264224-36.png", "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg", "http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg", "http://img1.touxiang.cn/uploads/20121224/24-054837_708.jpg", "http://img1.touxiang.cn/uploads/20121212/12-060125_658.jpg", "http://img1.touxiang.cn/uploads/20130608/08-054059_703.jpg", "http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg", "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg"};
    private List<String> mDataList = Arrays.asList(AVATARS);
    private ViewPagerImageAdapter mExamplePagerAdapter = null;

    @Override
    public Bundle getBundle() throws Exception {
        return getBundle(SwipeToLoadActivity.class);
    }

    @Override
    public int initLayout() {
        return R.layout.fragment_find;
    }

    @Override
    public void initView() {
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        listView = (ListView) findViewById(R.id.swipe_target);
//        LinearLayout linearLayout= (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_item,null);
//        listView.addHeaderView(linearLayout);
        listView.setDivider(getResources().getDrawable(R.drawable.listview_divider));
        listView.setDividerHeight(25);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0 || position != 1) {
                    // Toast.makeText(getActivity(),resultBeanList.get(position-1).getTemperature(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), resultBeanList.get(position).getTemperature(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void initEvent() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void initData() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        //http://api.k780.com:88/?app=weather.future&weaid=1&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json
        String url = "http://api.k780.com/";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("app", "weather.future");
        map.put("weaid", "1");
        map.put("appkey", "14540");
        map.put("format", "json");
        map.put("sign", "4d832b278666cf512481795d47060017");
        ServerInterfaces.getInstance().postAsync(getApplicationContext(), url, map, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                System.out.println("--" + result);
                swipeToLoadLayout.setRefreshing(false);
                try {
                    final TestListEntry entry = FastJsonUtils.getSingleBean(result, TestListEntry.class);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            resultBeanList = new ArrayList<TestListEntry.ResultBean>();
                            resultBeanList.add(new TestListEntry.ResultBean());
                            resultBeanList.add(new TestListEntry.ResultBean());
                            if (entry.getResult() != null) {
                                resultBeanList.addAll(entry.getResult());
                            }
                            //resultBeanList.addAll(entry.getResult());
                            myAdapter = new MyAdapter(resultBeanList);
                            listView.setAdapter(myAdapter);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode, String strMsg) {

            }
        });
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.setRefreshing(false);
        }
        if (swipeToLoadLayout.isLoadingMore()) {
            swipeToLoadLayout.setLoadingMore(false);
        }
    }

    //listview adapter
    class MyAdapter extends BaseAdapter {
        private List<TestListEntry.ResultBean> resultBeanList;

        public MyAdapter(List<TestListEntry.ResultBean> resultBeanList) {
            this.resultBeanList = resultBeanList;
        }

        @Override
        public int getCount() {
            return resultBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return resultBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 0;
            } else if (position == 1) {
                return 1;
            } else {
                return 2;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (getItemViewType(position) == 0) {
                ViewPagerHolder viewHolder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.find_head1, parent, false);
                    viewHolder = new ViewPagerHolder(convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewPagerHolder) convertView.getTag();
                }
                viewHolder.setData(mDataList);
            } else if (getItemViewType(position) == 1) {
                HorizontalScrollViewHolder horizontalScrollViewHolder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_item, parent, false);
                    horizontalScrollViewHolder = new HorizontalScrollViewHolder(convertView);
                    convertView.setTag(horizontalScrollViewHolder);
                } else {
                    horizontalScrollViewHolder = (HorizontalScrollViewHolder) convertView.getTag();
                }
            } else {
                ViewHolder viewHolder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_swipe_to_layout_item, parent, false);
                    viewHolder = new ViewHolder(convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                System.out.println("-----0" + resultBeanList.get(position).getWeather());
                viewHolder.setData(resultBeanList.get(position));
            }

            return convertView;
        }
    }

    class ViewPagerHolder {
        private ChildViewPager mViewPager;
        private MagicIndicator magicIndicator;

        public ViewPagerHolder(View convertView) {
            mViewPager = (ChildViewPager) convertView.findViewById(R.id.view_pager);
            magicIndicator = (MagicIndicator) convertView.findViewById(R.id.magic_indicator1);
        }

        public void setData(final List<String> mDataList) {
            mExamplePagerAdapter = new ViewPagerImageAdapter(SwipeToLoadActivity.this,mDataList);
           mViewPager.setAdapter(mExamplePagerAdapter);
            ScaleCircleNavigator scaleCircleNavigator = new ScaleCircleNavigator(getApplicationContext());
            scaleCircleNavigator.setCircleCount(mDataList.size());
            scaleCircleNavigator.setNormalCircleColor(Color.LTGRAY);
            scaleCircleNavigator.setSelectedCircleColor(Color.DKGRAY);
            scaleCircleNavigator.setCircleClickListener(new ScaleCircleNavigator.OnCircleClickListener() {
                @Override
                public void onClick(int index) {
                    mViewPager.setCurrentItem(index);
                }
            });
            magicIndicator.setNavigator(scaleCircleNavigator);
            ViewPagerHelper.bind(magicIndicator, mViewPager);
            mViewPager.setNestedpParent((ViewGroup) mViewPager.getParent());
        }
    }


    class HorizontalScrollViewHolder {
        public HorizontalScrollViewHolder(View convertView) {

        }

    }

    class ViewHolder {
        private TextView daysTv;
        private TextView weekTv;
        private TextView cityNm;
        private TextView temperatureTv;
        private TextView weatherTv;
        private GlideImageView weatherImgView;

        public ViewHolder(View convertView) {
            weatherImgView = convertView.findViewById(R.id.weatherIcon);
            cityNm = convertView.findViewById(R.id.cityNm);
            daysTv = convertView.findViewById(R.id.daysTv);
            weekTv = convertView.findViewById(R.id.weekTv);
            temperatureTv = convertView.findViewById(R.id.temperatureTv);
            weatherTv = convertView.findViewById(R.id.weatherTv);
        }

        public void setData(TestListEntry.ResultBean resultBean) {
            System.out.println("-----1" + resultBean.getCitynm());
            GlideImageLoader.create(weatherImgView).load(resultBean.getWeather_icon());
            cityNm.setText(resultBean.getCitynm());
            daysTv.setText(resultBean.getDays());
            weekTv.setText(resultBean.getWeek());
            temperatureTv.setText(resultBean.getTemperature());
            weatherTv.setText(resultBean.getWeather());
        }
    }
}

