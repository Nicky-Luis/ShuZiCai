package com.jiangtao.shuzicai;


import com.blankj.utilcode.utils.Utils;
import com.jiangtao.shuzicai.basic.app.BasicApp;
import com.jiangtao.shuzicai.basic.network.APIInteractive;

/**
 * Created by Nicky on 2016/11/26.
 * app对象
 */

public class Application extends BasicApp {

    //静态获取实例
    public static Application getApp() {
        // 单例模式获取唯一的MyApplication实例
        return (Application) mInstance;
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
        //初始化retrofit
        APIInteractive.initRetrofit();
        //utils初始化
        Utils.init(this);
    }
}
