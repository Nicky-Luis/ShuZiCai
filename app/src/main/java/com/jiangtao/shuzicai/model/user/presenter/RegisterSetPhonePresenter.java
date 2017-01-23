package com.jiangtao.shuzicai.model.user.presenter;

import android.content.Context;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.basic.network.NetworkRequest;
import com.jiangtao.shuzicai.model.user.entry.RegisterBean;
import com.jiangtao.shuzicai.model.user.entry.RequestVerifyCodeBean;
import com.jiangtao.shuzicai.model.user.entry.UserModel;
import com.jiangtao.shuzicai.model.user.interfaces.IRegisterSetPhoneView;
import com.jiangtao.shuzicai.model.user.interfaces.IRegisterSetPresenter;
import com.jiangtao.shuzicai.model.user.utils.ShareCodeUtil;

import org.json.JSONObject;

/**
 * Created by Nicky on 2017/1/19.
 */

public class RegisterSetPhonePresenter implements IRegisterSetPresenter {

    //上下文
    private Context mContext;
    //view对象
    private IRegisterSetPhoneView registerPhoneView;

    public RegisterSetPhonePresenter(Context mContext, IRegisterSetPhoneView registerPhoneView) {
        this.mContext = mContext;
        this.registerPhoneView = registerPhoneView;
    }

    @Override
    public void getVerifyCode(final String phone) {
        LogUtils.v("开始获取验证码，手机：" + phone);
        RequestVerifyCodeBean bean = new RequestVerifyCodeBean(phone, "shuzicai");
        //查询
        APIInteractive.requestSmsCode(NetworkRequest.createJsonString(bean), new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                LogUtils.e("获取验证码失败");
                ToastUtils.showLongToast("获取验证码失败");
            }

            @Override
            public void onSucceed(JSONObject result) {
                try {
                    String smsId = result.optString("smsId");
                    registerPhoneView.onGetVeryCode(smsId);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                LogUtils.i("获取验证码成功，result = " + result);
            }
        });
    }

    /**
     * 获取短信验证码发送状态
     *
     * @param ssid
     */
    @Override
    public void getSmsStatue(String ssid) {
        //使用查询短信状态接口来查询该条短信是否发送成功
        APIInteractive.querySms(ssid, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                LogUtils.e("获取验证码状态失败");
            }

            @Override
            public void onSucceed(JSONObject result) {
                LogUtils.i("状态，result = " + result);
                try {
                    String smsId = result.optString("sms_state");
                    if (smsId.equals("SUCCESS") || smsId.equals("SENDING")) {
                        ToastUtils.showLongToast("验证码已发送");
                    } else if (smsId.equals("FAIL")) {
                        ToastUtils.showLongToast("验证码发送失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void startRegister(String phoen, String smsCode, String password, String inviteeCode) {
        final RegisterBean bean = new RegisterBean();
        bean.setMobilePhoneNumber(phoen);
        bean.setSmsCode(smsCode);
        bean.setPassword(password);
        bean.setInviteeCode(inviteeCode);

        ShareCodeUtil.generateRandomStr(6, new ShareCodeUtil.IShareCallBack() {

            @Override
            public void shareCodeExist() {
                //注册失败
                registerPhoneView.onRegisterFailed();
            }

            @Override
            public void getShareCode(String code) {
                bean.setInvitationCode(code);
                register(bean);
            }
        });
    }

    /**
     * 注册
     *
     * @param bean
     */
    private void register(RegisterBean bean) {
        //开始注册用户
        APIInteractive.register(bean, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                registerPhoneView.onRegisterFailed();
                LogUtils.e("注册失败,code:" + code);
            }

            @Override
            public void onSucceed(JSONObject result) {
                LogUtils.i("注册成功,result:" + result);
                String username = result.optString("username");
                String mobilePhoneNumber = result.optString("mobilePhoneNumber");
                String objectId = result.optString("objectId");
                String sessionToken = result.optString("sessionToken");

                if (null == Application.userInstance) {
                    Application.userInstance = new UserModel();
                }
                Application.userInstance.setMobilePhoneNumber(mobilePhoneNumber);
                Application.userInstance.setObjectId(objectId);
                Application.userInstance.setToken(sessionToken);

                registerPhoneView.onRegisterSucceed();
            }
        });
    }
}
