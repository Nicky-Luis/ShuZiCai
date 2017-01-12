package com.jiangtao.shuzicai;


import com.jiangtao.shuzicai.basic.app.BasicApp;
import com.jiangtao.shuzicai.basic.network.APIInteractive;

/**
 * Created by Nicky on 2016/11/26.
 * app对象
 */

public class FuckApplication extends BasicApp {

    //静态获取实例
    public static FuckApplication getApp() {
        // 单例模式获取唯一的MyApplication实例
        return (FuckApplication) mInstance;
    }

    //获取比目所有APP信息
    public static String getUserURl() {
        return "https://api.bmob.cn/1/";
    }

    @Override
    public String getApplicationName() {
        return getResources().getString(R.string.app_name);
    }

    @Override
    public void onApplicationInit() {
        APIInteractive.initRetrofit();
    }
}
