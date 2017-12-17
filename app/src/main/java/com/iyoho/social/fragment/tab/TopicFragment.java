package com.iyoho.social.fragment.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.iyoho.social.R;
import com.iyoho.social.activity.SwipeToLoadActivity;
import com.iyoho.social.base.IBaseActivity;
import com.iyoho.social.base.IBaseFragment;

import io.rong.imkit.RongIM;

import com.iyoho.social.R;

import static com.iyoho.social.R.id.tvBtn;


public class TopicFragment extends Fragment {
    private static final String TAG = "TopicFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_topic, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
        initData();
    }




    public void initView(View view) {
        Button  btn=view.findViewById(tvBtn);
        btn.setText("Topic");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IBaseActivity.start(getActivity(), SwipeToLoadActivity.class,null);
            }
        });
    }


    public void initEvent() {

    }


    public void initData() {

    }


}