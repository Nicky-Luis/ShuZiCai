package com.jiangtao.shuzicai.model.user.presenter;

import android.content.Context;

import com.blankj.utilcode.utils.LogUtils;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.common.message.LoginMsg;
import com.jiangtao.shuzicai.model.user.entry.UserModel;
import com.jiangtao.shuzicai.model.user.interfaces.ILoginPresenter;
import com.jiangtao.shuzicai.model.user.interfaces.ILoginView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by Nicky on 2017/1/23.
 */

public class LoginPresenter implements ILoginPresenter {

    //上下文
    private Context mContext;
    //view对象
    private ILoginView loginView;

    public LoginPresenter(Context mContext, ILoginView registerSetInfoView) {
        this.mContext = mContext;
        this.loginView = registerSetInfoView;
    }

    @Override
    public void startLogin(String account, String password) {
        //登录
        APIInteractive.startLogin(account, password, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                loginView.onLoginFailed();
            }

            @Override
            public void onSucceed(JSONObject result) {
                LogUtils.i("登录成功,result:" + result);
                Application.userInstance = new UserModel(result);
                loginView.onLoginSucceed();
                EventBus.getDefault().post(new LoginMsg(true));
            }
        });
    }
}
