package com.jiangtao.shuzicai;


import com.jiangtao.shuzicai.basic.app.BaseApp;

/**
 * Created by Nicky on 2016/11/26.
 * app对象
 */

public class PlayerApplication extends BaseApp {

    //静态获取实例
    public static PlayerApplication getApp() {
        // 单例模式获取唯一的MyApplication实例
        return (PlayerApplication)mInstance;
    }


    @Override
    public String getApplicationName() {
        return getResources().getString(R.string.app_name);
    }
}
