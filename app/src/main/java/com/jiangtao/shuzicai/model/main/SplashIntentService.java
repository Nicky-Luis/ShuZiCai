package com.jiangtao.shuzicai.model.main;

import android.app.IntentService;
import android.content.Intent;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.AppConfigure;
import com.jiangtao.shuzicai.AppHandlerService;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.common.event_message.WealthChangeMsg;
import com.jiangtao.shuzicai.model.user.entry._User;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

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
            AppHandlerService.startLogin(name, password);
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
        BmobUser.loginByAccount(account, password, new LogInListener<_User>() {

            @Override
            public void done(_User user, BmobException e) {
                if (user != null) {
                    Application.userInstance = user;
                    LogUtils.i("SplashIntentService 登录成功");
                } else {
                    Application.userInstance = null;
                    ToastUtils.showShortToast("登录失败");
                    LogUtils.e("SplashIntentService 登录失败：" + e);
                }
                EventBus.getDefault().post(new WealthChangeMsg());
            }
        });
    }

}

