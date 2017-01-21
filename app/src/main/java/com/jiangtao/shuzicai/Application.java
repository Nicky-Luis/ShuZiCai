package com.jiangtao.shuzicai;


import android.content.Context;

import com.blankj.utilcode.utils.Utils;
import com.jiangtao.shuzicai.basic.app.BasicApp;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.utils.AppCrashUtils;

/**
 * Created by Nicky on 2016/11/26.
 * app对象
 */

public class Application extends BasicApp {

    //全局上下文
    public static Context APPContext;

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
    }
}
