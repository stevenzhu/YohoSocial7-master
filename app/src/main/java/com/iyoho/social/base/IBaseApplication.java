package com.iyoho.social.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import com.baidu.mapapi.SDKInitializer;
import com.iyoho.social.utils.AndroidUtilsCode;
import com.iyoho.social.utils.JPushUtils;
import com.iyoho.social.utils.LogUtil;
import com.mob.MobApplication;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.CacheType;
import com.okhttplib.annotation.Encoding;
import com.okhttplib.cookie.PersistentCookieJar;
import com.okhttplib.cookie.cache.SetCookieCache;
import com.okhttplib.cookie.persistence.SharedPrefsCookiePersistor;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by ab053167 on 2017/9/12.
 */

public class IBaseApplication extends MobApplication  {
    private static List<Activity> mActivitys = Collections.synchronizedList(new LinkedList<Activity>());

    private static IBaseApplication application;
    public static IBaseApplication getInstance(){
        return application;
    }
    public static String downloadFileDir = Environment.getExternalStorageDirectory()+"/yoho_down/";
    public static String cacheDir = Environment.getExternalStorageDirectory().getPath()+"/yoho_cache/";
    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(getApplicationContext());
        AndroidUtilsCode.init(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
        /*初始化推送*/
        JPushUtils.getInstance().initJPush(getApplicationContext());
        JPushUtils.getInstance().registerMessageReceiver(getApplicationContext());
        JPushUtils.getInstance().setStyleCustom(getApplicationContext());

        PlatformConfig.setWeixin("wx1a4482edd9c1d0bf", "076695267e3978b7c245bf50e43b4316");
        PlatformConfig.setQQZone("1106109640", "1ZGhWAQpvUeRrFMA");
        PlatformConfig.setSinaWeibo("1658074632", "a673364d560295197bb3e9b4297f497d", "http://sns.whalecloud.com");
        UMShareAPI.init(getApplicationContext(),"59f714c7a40fa3301900000d");

        application=this;
        OkHttpUtil.init(application)
                .setConnectTimeout(15)//连接超时时间
                .setWriteTimeout(15)//写超时时间
                .setReadTimeout(15)//读超时时间
                .setMaxCacheSize(50 * 1024 * 1024)//缓存空间大小
                .setCacheType(CacheType.FORCE_NETWORK)//缓存类型
                .setHttpLogTAG("HttpLog")//设置请求日志标识
                .setIsGzip(false)//Gzip压缩，需要服务端支持
                .setShowHttpLog(true)//显示请求日志
                .setShowLifecycleLog(false)//显示Activity销毁日志
                .setRetryOnConnectionFailure(false)//失败后不自动重连
                .setCachedDir(new File(cacheDir))//设置缓存目录
                .setDownloadFileDir(downloadFileDir)//文件下载保存目录
                .setResponseEncoding(Encoding.UTF_8)//设置全局的服务器响应编码
                .setRequestEncoding(Encoding.UTF_8)//设置全局的请求参数编码
                .addResultInterceptor(HttpInterceptor.ResultInterceptor)//请求结果拦截器
                .addExceptionInterceptor(HttpInterceptor.ExceptionInterceptor)//请求链路异常拦截器
                .setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(application)))//持久化cookie
                .build();

        registerActivityListener();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /*---栈管理---*/

    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public void pushActivity(Activity activity) {
        mActivitys.add(activity);
        LogUtil.d("IBaseApplication:size:push",""+mActivitys.size());
        for(int x=0;x<mActivitys.size();x++){
            LogUtil.d("IBaseApplication:size:push",""+mActivitys.get(x).getLocalClassName());
        }
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public void popActivity(Activity activity) {
        mActivitys.remove(activity);
        LogUtil.d("IBaseApplication:size:pop",""+mActivitys.size());
        for(int x=0;x<mActivitys.size();x++){
            LogUtil.d("IBaseApplication:size:pop",""+mActivitys.get(x).getLocalClassName());
        }
    }



    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return null;
        }
        Activity activity = mActivitys.get(mActivitys.size()-1);
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public static void finishCurrentActivity() {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        Activity activity = mActivitys.get(mActivitys.size()-1);
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        if (activity != null) {
            mActivitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        for (Activity activity : mActivitys) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }
    /**
     * 结束指定类名的Activity之后的栈
     */
    public static void finishActivityAfter(Class<?> cls) {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        for(int x=mActivitys.size()-1;x<mActivitys.size();x--){
            Activity activity=mActivitys.get(x);
            if(mActivitys.size()==1){
                return;
            }
            if (!activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }
    /**
     * 结束指定类名的Activity之后的栈
     */
    public static void finishActivityBefore(Class<?> cls) {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        for(int x=0;x<mActivitys.size();x++){
            Activity activity=mActivitys.get(x);
            if(mActivitys.size()==1){
                return;
            }
            if (!activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束指定类名的Activity之間的
     */
    public static void finishActivity(Class<?> cls1,Class<?> cls2) {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
            if(mActivitys.size()==1){
                return;
            }
            int startIndex=mActivitys.indexOf(cls1);
            int endIndex=mActivitys.indexOf(cls2);
            if(startIndex!=-1&&endIndex!=-1){
                if(startIndex<endIndex){
                    while (endIndex-1!=startIndex+1){
                        finishActivity(mActivitys.remove(startIndex+1));
                        startIndex=startIndex+1;
                    }
                }else if(endIndex<startIndex){
                    while (startIndex-1!=endIndex+1){
                        finishActivity(mActivitys.remove(endIndex+1));
                        endIndex=endIndex+1;
                    }
                }
            }

    }
    /**
     * 按照指定类名找到activity
     *
     * @param cls
     * @return
     */
    public static Activity findActivity(Class<?> cls) {
        Activity targetActivity = null;
        if (mActivitys != null) {
            for (Activity activity : mActivitys) {
                if (activity.getClass().equals(cls)) {
                    targetActivity = activity;
                    break;
                }
            }
        }
        return targetActivity;
    }

    /**
     * @return 作用说明 ：获取当前最顶部activity的实例
     */
    public Activity getTopActivity() {
        Activity mBaseActivity = null;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity;

    }

    /**
     * @return 作用说明 ：获取当前最顶部的acitivity 名字
     */
    public String getTopActivityName() {
        Activity mBaseActivity = null;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity.getClass().getName();
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            activity.finish();
        }
        mActivitys.clear();
    }

    /**
     * 退出应用程序
     */
    public  static void appExit() {
        try {
            LogUtil.d("IBaseApplication","app exit");
            //finishAllActivity();
            ActivityManager am = (ActivityManager) getInstance().getSystemService(ACTIVITY_SERVICE);
            am.killBackgroundProcesses(getInstance().getPackageName());
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    private void registerActivityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    /**
                     *  监听到 Activity创建事件 将该 Activity 加入list
                     */
                    pushActivity(activity);

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (null==mActivitys&&mActivitys.isEmpty()){
                        return;
                    }
                    if (mActivitys.contains(activity)){
                        /**
                         *  监听到 Activity销毁事件 将该Activity 从list中移除
                         */
                        popActivity(activity);
                    }
                }
            });
        }
    }
}
