package com.iyoho.social.activity.message;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iyoho.social.R;
import com.iyoho.social.base.IBaseActivity;
import com.iyoho.social.view.TypeFaceTextView;

import io.rong.imkit.fragment.ConversationFragment;

/**
 * Created by ab053167 on 2017/11/2.
 */

public class ConversationActivity extends IBaseActivity {
    private ConversationFragment  conversation;
    private LinearLayout navigationBackLayout;
    private TypeFaceTextView navigationTitle;
    private String mTargetId;
    @Override
    public Bundle getBundle() throws Exception {
        return getBundle(ConversationActivity.class);
    }


    @Override
    public int initLayout() {
        return R.layout.activity_message_conversation;
    }

    @Override
    public void initView() {
        Toast.makeText(this,"ConversationActivityonCreate",Toast.LENGTH_SHORT).show();
        navigationBackLayout= (LinearLayout) findViewById(R.id.navigationBackLayout);
        navigationTitle= (TypeFaceTextView) findViewById(R.id.navigationTitle);
        conversation= (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);
        Intent intent=getIntent();
        if(intent==null&&intent.getData()==null){
            return;
        }
        mTargetId = getIntent().getData().getQueryParameter("targetId");
    }

    @Override
    public void initEvent() {
        navigationBackLayout.setOnClickListener(this);
    }

    @Override
    public void initData() {
        navigationTitle.setText(mTargetId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigationBackLayout:
                finish();
                break;

        }
    }
}
