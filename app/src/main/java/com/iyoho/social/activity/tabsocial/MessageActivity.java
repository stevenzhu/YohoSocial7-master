package com.iyoho.social.activity.tabsocial;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.iyoho.social.R;
import com.iyoho.social.base.IBaseActivity;
import com.iyoho.social.view.TypeFaceTextView;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

public class MessageActivity extends IBaseActivity {
    private ConversationListFragment conversationListFragment;
    private LinearLayout navigationBackLayout;
    private TypeFaceTextView navigationTitle;
    @Override
    public Bundle getBundle() throws Exception {
        return getBundle(MessageActivity.class);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_message;
    }

    @Override
    public void initView() {
        navigationBackLayout= (LinearLayout) findViewById(R.id.navigationBackLayout);
        navigationTitle= (TypeFaceTextView) findViewById(R.id.navigationTitle);
        conversationListFragment= (ConversationListFragment)getSupportFragmentManager().findFragmentById(R.id.conversationlist);
    }

    @Override
    public void initEvent() {
        navigationBackLayout.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                .build();
        conversationListFragment.setUri(uri);
        navigationTitle.setText("消息");
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
