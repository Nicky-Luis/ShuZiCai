package com.jiangtao.shuzicai.model.main;

import android.app.IntentService;
import android.content.Intent;

import com.blankj.utilcode.utils.LogUtils;
import com.jiangtao.shuzicai.AppConfigure;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.model.user.entry.UserModel;

import org.json.JSONObject;

/**
 * Created by Nicky on 2017/1/23.
 */

public class SplashIntentService extends IntentService {

    public SplashIntentService() {
        super("Splash");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (AppConfigure.userIsLogin()) {
            String name = AppConfigure.getUserName();
            String password = AppConfigure.getUserPassword();
            LogUtils.i("-----开始登录,name:" + name);
            startLogin(name, password);
        } else {
            LogUtils.i("-----不需要自动登录----");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    ///////////////////////////////////////////////////////////

    /**
     * 开始登录
     *
     * @param account
     * @param password
     */
    public void startLogin(String account, String password) {
        //登录
        APIInteractive.startLogin(account, password, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                LogUtils.i("登录失败,code:" + code);
            }

            @Override
            public void onSucceed(JSONObject result) {
                LogUtils.i("登录成功,result:" + result);
                Application.userInstance = new UserModel(result);
            }
        });
    }
}
