package com.iyoho.social.fragment.tab;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.iyoho.social.Entry.TestListEntry;
import com.iyoho.social.R;
import com.iyoho.social.activity.SwipeToLoadActivity;
import com.iyoho.social.activity.tabsocial.NearbyPartnerMapActivity;
import com.iyoho.social.adapter.RecyclerViewAdapter;
import com.iyoho.social.adapter.ViewPagerImageAdapter;
import com.iyoho.social.base.IBaseActivity;
import com.iyoho.social.base.IBaseFragment;

import im.quar.autolayout.view.AutoLinearLayout;
import im.quar.autolayout.view.AutoRecyclerView;
import com.iyoho.social.server.ServerCallback;
import com.iyoho.social.server.ServerInterfaces;
import com.iyoho.social.utils.FastJsonUtils;
import com.iyoho.social.view.ChildViewPager;
import com.iyoho.social.view.ScaleCircleNavigator;
import com.iyoho.social.view.TypeFaceTextView;
import com.yoho.glide.GlideImageLoader;
import com.yoho.glide.GlideImageView;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.baidu.location.h.k.P;
import static com.baidu.location.h.k.m;
import static com.baidu.location.h.k.p;
import static com.baidu.location.h.k.t;
import static com.iyoho.social.R.id.cityNm;
import static com.iyoho.social.R.id.daysTv;
import static com.iyoho.social.R.id.temperatureTv;
import static com.iyoho.social.R.id.weatherTv;
import static com.iyoho.social.R.id.weekTv;


public class FindFragment extends IBaseFragment  implements OnRefreshListener,OnLoadMoreListener {
    private SwipeToLoadLayout swipeToLoadLayout=null;
    private ListView listView;
    private MyAdapter myAdapter;
    private List<TestListEntry> testListEntries;

    private static final String[] AVATARS = {
            "http://tupian.qqjay.com/u/2011/0729/e755c434c91fed9f6f73152731788cb3.jpg",
            "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
            "http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
            "http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
            "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg"
    };
    private List<String> mDataList = Arrays.asList(AVATARS);
    private ViewPagerImageAdapter mExamplePagerAdapter =null;
    @Override
    public int initLayout() {
        return R.layout.fragment_find;
    }

    @Override
    public void initView(View view) {
        swipeToLoadLayout= (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        listView= (ListView) view.findViewById(R.id.swipe_target);
//        LinearLayout linearLayout= (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_item,null);
//        listView.addHeaderView(linearLayout);
        listView.setDivider(getResources().getDrawable(R.drawable.listview_divider));
        listView.setDividerHeight(25);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0||position!=1){
                   // Toast.makeText(getActivity(),resultBeanList.get(position-1).getTemperature(),Toast.LENGTH_SHORT).show();
                   // Toast.makeText(getActivity(),resultBeanList.get(position).getTemperature(),Toast.LENGTH_SHORT).show();
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
        testListEntries=new ArrayList<TestListEntry>();
        testListEntries.add(new TestListEntry());
        testListEntries.add(new TestListEntry());
        for(int x=0;x<10;x++){
            TestListEntry testListEntry=new TestListEntry();
            testListEntry.setSuccess1(x+"#");
            testListEntry.setSuccess2(x+"##");
            testListEntry.setSuccess3(x+"###");
            testListEntry.setSuccess4(x+"####");
            testListEntry.setSuccess5(x+"#####");
            testListEntry.setSuccess6(x+"#####");
            List<TestListEntry.ResultBean> resultBeanList=new ArrayList<TestListEntry.ResultBean>();
            for(int i=0;i<x;i++){
                TestListEntry.ResultBean resultBean=new TestListEntry.ResultBean();
                resultBean.setCityid("http://tupian.qqjay.com/u/2011/0729/e755c434c91fed9f6f73152731788cb3.jpg");
                resultBeanList.add(resultBean);
            }
            testListEntry.setResult(resultBeanList);
            testListEntries.add(testListEntry);
        }
        myAdapter=new MyAdapter(testListEntries);
        listView.setAdapter(myAdapter);
        swipeToLoadLayout.setRefreshing(false);
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
        private List<TestListEntry> resultBeanList;
        public MyAdapter(List<TestListEntry> resultBeanList){
            this.resultBeanList=resultBeanList;
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
            if(position==0){
                return 0;
            }else if(position==1){
                return 1;
            }else{
                if(resultBeanList.get(position).getResult()!=null&&resultBeanList.get(position).getResult().size()<3){
                    return 2;
                }else{
                    return 3;
                }
            }
        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(getItemViewType(position)==0){
                ViewPagerHolder viewHolder;
                if(convertView==null){
                    convertView= LayoutInflater.from(getActivity()).inflate(R.layout.find_head1,parent,false);
                    viewHolder=new ViewPagerHolder(convertView);
                    convertView.setTag(viewHolder);
                }else{
                    viewHolder= (ViewPagerHolder) convertView.getTag();
                }
                viewHolder.setData(mDataList);
            }else if(getItemViewType(position)==1){
                HorizontalScrollViewHolder horizontalScrollViewHolder;
                if(convertView==null){
                    convertView= LayoutInflater.from(getActivity()).inflate(R.layout.find_head2,parent,false);
                    horizontalScrollViewHolder=new HorizontalScrollViewHolder(convertView);
                    convertView.setTag(horizontalScrollViewHolder);
                }else{
                    horizontalScrollViewHolder= (HorizontalScrollViewHolder) convertView.getTag();
                }
                horizontalScrollViewHolder.setData(mDataList);
            }else if(getItemViewType(position)==2){
                ViewHolderItem2 viewHolderItem2;
                if(convertView==null){
                    convertView= LayoutInflater.from(getActivity()).inflate(R.layout.find_list_item1,parent,false);
                    viewHolderItem2=new ViewHolderItem2(convertView);
                    convertView.setTag(viewHolderItem2);
                }else{
                    viewHolderItem2= (ViewHolderItem2) convertView.getTag();
                }
                viewHolderItem2.setData(resultBeanList.get(position));
            }else if(getItemViewType(position)==3){
                ViewHolderItem3 viewHolderItem3;
                if(convertView==null){
                    convertView= LayoutInflater.from(getActivity()).inflate(R.layout.find_list_item2,parent,false);
                    viewHolderItem3=new ViewHolderItem3(convertView);
                    convertView.setTag(viewHolderItem3);
                }else{
                    viewHolderItem3= (ViewHolderItem3) convertView.getTag();
                }
                viewHolderItem3.setData(resultBeanList.get(position));
            }

            return convertView;
        }
    }
    //----itemType==0------
    class ViewPagerHolder{
        private ChildViewPager mViewPager;
        private MagicIndicator magicIndicator;
        private AutoLinearLayout viewpagerIndicateLayout;
        private int currentItem;
        private ScheduledExecutorService scheduledExecutorService;
        public ViewPagerHolder(View convertView){
            mViewPager = (ChildViewPager) convertView.findViewById(R.id.view_pager);
            viewpagerIndicateLayout=convertView.findViewById(R.id.viewpagerIndicateLayout);
            magicIndicator = (MagicIndicator) convertView.findViewById(R.id.magic_indicator1);
        }
        public void setData(List<String> mDataList){
            mExamplePagerAdapter = new ViewPagerImageAdapter(getContext(),mDataList);
            mViewPager.setAdapter(mExamplePagerAdapter);
            mViewPager.setOffscreenPageLimit(mDataList.size());
            if(mDataList!=null&&mDataList.size()>1){
                viewpagerIndicateLayout.setVisibility(View.VISIBLE);
                ScaleCircleNavigator scaleCircleNavigator = new ScaleCircleNavigator(getContext());
                scaleCircleNavigator.setCircleCount(mDataList.size());
                scaleCircleNavigator.setNormalCircleColor(getResources().getColor(R.color.normal_circle_color));
                scaleCircleNavigator.setSelectedCircleColor(getResources().getColor(R.color.selected_circle_color));
                scaleCircleNavigator.setCircleClickListener(new ScaleCircleNavigator.OnCircleClickListener() {
                    @Override
                    public void onClick(int index) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                magicIndicator.setNavigator(scaleCircleNavigator);
                ViewPagerHelper.bind(magicIndicator, mViewPager);
            }else{
                viewpagerIndicateLayout.setVisibility(View.GONE);
            }
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            //每隔2秒钟切换一张图片
            scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 3, 3, TimeUnit.SECONDS);

            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentItem = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }

        public class ViewPagerTask implements Runnable{
            @Override
            public void run() {
                currentItem = (currentItem +1) % mDataList.size();
                handler.obtainMessage().sendToTarget();
            }
        }
        Handler handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mViewPager.setCurrentItem(currentItem);
            }
        };
    }

    //----itemType==1-------
    class HorizontalScrollViewHolder{
        private RecyclerView autoRecyclerView;
        private AutoLinearLayout nearbyPartnerLayout;
        public HorizontalScrollViewHolder(View convertView){
            autoRecyclerView = (RecyclerView) convertView.findViewById(R.id.recycleView);
            nearbyPartnerLayout=convertView.findViewById(R.id.nearbyPartnerLayout);
        }
        public void setData(final List<String> mDataList){
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            autoRecyclerView.setLayoutManager(linearLayoutManager);
            autoRecyclerView.setHasFixedSize(true);
            autoRecyclerView.addItemDecoration(new SpaceItemDecoration(mDataList,30));
            RecyclerViewAdapter recyclerViewAdapter=new RecyclerViewAdapter(getContext(),mDataList);
            recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickRVListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(),""+position,Toast.LENGTH_SHORT).show();
                }
            });
            autoRecyclerView.setAdapter(recyclerViewAdapter);
            nearbyPartnerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IBaseActivity.start(getActivity(), NearbyPartnerMapActivity.class,null);
                }
            });

        }
    }
    //----itemType==2-----
    class ViewHolderItem2{
        private GlideImageView userHeadPic;
        private TypeFaceTextView userNickName;
        private TypeFaceTextView themeTitle;
        private TypeFaceTextView themeContent;
        private GlideImageView themePic;
        private TypeFaceTextView themeAddress;
        private TypeFaceTextView themeTime;

        public ViewHolderItem2(View convertView){
            userHeadPic=convertView.findViewById(R.id.userHeadPic);
            userNickName=convertView.findViewById(R.id.userNickName);
            themeTitle=convertView.findViewById(R.id.themeTitle);
            themeContent=convertView.findViewById(R.id.themeContent);
            themePic=convertView.findViewById(R.id.themePic);
            themeAddress=convertView.findViewById(R.id.themeAddress);
            themeTime=convertView.findViewById(R.id.themeTime);
        }
        public void setData(TestListEntry resultBean){
            GlideImageLoader.create(userHeadPic).load(resultBean.getSuccess1());
            userNickName.setText(resultBean.getSuccess2());
            themeTitle.setText(resultBean.getSuccess3());
            themeContent.setText(resultBean.getSuccess4());
            if(resultBean.getResult()!=null&&resultBean.getResult().size()>0){
                GlideImageLoader.create(themePic).load(resultBean.getResult().get(0).getCityid());
            }
            themeAddress.setText(resultBean.getSuccess5());
            themeTime.setText(resultBean.getSuccess6());
        }
    }
    //----itemType==2-----
    class ViewHolderItem3{
        private GlideImageView userHeadPic;
        private TypeFaceTextView userNickName;
        private TypeFaceTextView themeTitle;
        private TypeFaceTextView themeContent;
        private GlideImageView themePic0;
        private GlideImageView themePic1;
        private GlideImageView themePic2;
        private TypeFaceTextView themeAddress;
        private TypeFaceTextView themeTime;

        public ViewHolderItem3(View convertView){
            userHeadPic=convertView.findViewById(R.id.userHeadPic);
            userNickName=convertView.findViewById(R.id.userNickName);
            themeTitle=convertView.findViewById(R.id.themeTitle);
            themeContent=convertView.findViewById(R.id.themeContent);
            themePic0=convertView.findViewById(R.id.themePic0);
            themePic1=convertView.findViewById(R.id.themePic1);
            themePic2=convertView.findViewById(R.id.themePic2);
            themeAddress=convertView.findViewById(R.id.themeAddress);
            themeTime=convertView.findViewById(R.id.themeTime);
        }
        public void setData(TestListEntry resultBean){
            GlideImageLoader.create(userHeadPic).load(resultBean.getSuccess1());
            userNickName.setText(resultBean.getSuccess2());
            themeTitle.setText(resultBean.getSuccess3());
            themeContent.setText(resultBean.getSuccess4());
            GlideImageLoader.create(themePic0).load(resultBean.getResult().get(0).getCityid());
            GlideImageLoader.create(themePic1).load(resultBean.getResult().get(1).getCityid());
            GlideImageLoader.create(themePic2).load(resultBean.getResult().get(2).getCityid());
            themeAddress.setText(resultBean.getSuccess5());
            themeTime.setText(resultBean.getSuccess6());
        }
    }
    class SpaceItemDecoration extends RecyclerView.ItemDecoration{
        private int mSpace;
        private List<String> mLists;
        public void getItemOffsets(Rect outRect,View view,RecyclerView parent,RecyclerView.State state){
            super.getItemOffsets(outRect,view,parent,state);
            if(parent.getChildAdapterPosition(view)!=(mLists.size()-1)){
                outRect.right=mSpace;
            }
        }
        public SpaceItemDecoration(List<String> mLists,int mSpace){
            this.mSpace=mSpace;
            this.mLists=mLists;
        }
        };
    }