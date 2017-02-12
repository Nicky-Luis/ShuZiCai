package com.jiangtao.shuzicai.model.main;

import android.app.IntentService;
import android.content.Intent;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiangtao.shuzicai.AppConfigure;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.BmobQueryUtils;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.model.user.entry.UserModel;
import com.jiangtao.shuzicai.model.user.entry.WealthValue;

import org.json.JSONObject;

import java.util.List;

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
                //查询财富数据
                getWealthValue();
            }
        });
    }

    /**
     * 获取财富
     */
    private void getWealthValue() {
        if (null == Application.userInstance) {
            return;
        }
        String userId = Application.userInstance.getObjectId();

        BmobQueryUtils utils = BmobQueryUtils.newInstance();
        String where = utils.setValue("userId").equal(userId);

        //获取当前用户的信息
        APIInteractive.getWealthValue(where, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                LogUtils.e("财富数据更新失败");
            }

            @Override
            public void onSucceed(JSONObject result) {
                //更新数据
                try {
                    String jArray = result.optString("results");
                    List<WealthValue> wealthValues = new Gson().fromJson(jArray, new TypeToken<List<WealthValue>>() {
                    }.getType());
                    if (wealthValues.size() > 0) {
                        Application.wealthValue = wealthValues.get(0);
                    } else {
                        ToastUtils.showShortToast("财富数据更新失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

