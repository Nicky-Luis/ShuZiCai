package com.jiangtao.shuzicai;


import android.content.Context;

import com.blankj.utilcode.utils.ThreadPoolUtils;
import com.blankj.utilcode.utils.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jiangtao.shuzicai.basic.app.BasicApp;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.utils.AppCrashUtils;
import com.jiangtao.shuzicai.model.user.entry.UserModel;
import com.jiangtao.shuzicai.model.user.entry.WealthValue;

/**
 * Created by Nicky on 2016/11/26.
 * app对象
 */

public class Application extends BasicApp {

    //全局上下文
    public static Context APPContext;
    //线程池
    public static ThreadPoolUtils AppThreadPool;
    //用户实例
    public static UserModel userInstance;
    //用户的财富
    public static WealthValue wealthValue;

    //获取比目所有APP信息
    public static String getUserURl() {
        return getStringRes(Application.APPContext, R.string.Bmob_URL);
    }

    @Override
    public String getApplicationName() {
        return getResources().getString(R.string.app_name);
    }

    @Override
    public String getApplicationNameId() {
        return getResources().getString(R.string.app_name_id);
    }

    @Override
    public void onApplicationInit() {
        Application.APPContext = this;
        //初始化retrofit
        APIInteractive.initRetrofit();
        //utils初始化
        Utils.init(this);
        //初始化全局异常处理
        AppCrashUtils.getInstance().init(this);
        //初始化Fresco
        Fresco.initialize(this);
        //初始化线程池
        AppThreadPool = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 5);
    }
}
