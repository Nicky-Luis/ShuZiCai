package com.jiangtao.shuzicai.model.user.presenter;

import android.content.Context;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.basic.network.NetworkRequest;
import com.jiangtao.shuzicai.basic.utils.PhoneUtils;
import com.jiangtao.shuzicai.model.user.entry.RequestVerifyCodeBean;
import com.jiangtao.shuzicai.model.user.interfaces.IRegisterPhoneView;
import com.jiangtao.shuzicai.model.user.interfaces.IRegisterPresenter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nicky on 2017/1/19.
 */

public class RegisterPresenter implements IRegisterPresenter {

    //上下文
    private Context mContext;
    //view对象
    private IRegisterPhoneView registerPhoneView;

    public RegisterPresenter(Context mContext, IRegisterPhoneView registerPhoneView) {
        this.mContext = mContext;
        this.registerPhoneView = registerPhoneView;
    }

    @Override
    public void getVerifyCode(final String phone) {
        PhoneUtils phoneUtils = new PhoneUtils(phone);
        if (!phoneUtils.isLawful()) {
            registerPhoneView.onPhoneErr();
        } else {
            LogUtils.e("------获取验证码-----");
            RequestVerifyCodeBean bean = new RequestVerifyCodeBean(phone, "shuzicai");
            //查询
            APIInteractive.requestSmsCode(NetworkRequest.createJsonString(bean), new INetworkResponse() {
                @Override
                public void onFailure(int code) {
                    LogUtils.e("获取验证码失败");
                    ToastUtils.showLongToast("获取验证码失败");
                }

                @Override
                public void onSucceed(String result) {

                    try {
                        JSONObject myJsonObject = new JSONObject(result);
                        String smsId = myJsonObject.optString("smsId");
                        getSmsStatue(smsId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }

                    LogUtils.e("获取验证码成功，result = " + result);
                }
            });
        }
    }

    /**
     * 验证状态
     *
     * @param code
     * @param phone
     */
    @Override
    public void verifySmsCode(String code, String phone) {

        //使用查询短信状态接口来查询该条短信是否发送成功
        APIInteractive.verifySmsCode(code, phone, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                ToastUtils.showLongToast("验证码验证失败");
            }

            @Override
            public void onSucceed(String result) {
                LogUtils.e("获取验证码成功，result = " + result);
                try {
                    JSONObject myJsonObject = new JSONObject(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取短信验证码发送状态
     *
     * @param ssid
     */
    private void getSmsStatue(String ssid) {
        //使用查询短信状态接口来查询该条短信是否发送成功
        APIInteractive.querySms(ssid, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                LogUtils.e("获取验证码状态失败");
            }

            @Override
            public void onSucceed(String result) {
                try {
                    JSONObject myJsonObject = new JSONObject(result);
                    String smsId = myJsonObject.optString("sms_state");
                    if (smsId.equals("SUCCESS")) {
                        ToastUtils.showLongToast("验证码已发送");
                    } else if (smsId.equals("FAIL")) {
                        ToastUtils.showLongToast("验证码发送失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
