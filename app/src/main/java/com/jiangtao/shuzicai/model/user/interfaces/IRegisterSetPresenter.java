package com.jiangtao.shuzicai.model.user.interfaces;

/**
 * Created by Nicky on 2017/1/19.
 */

public interface IRegisterSetPresenter {

    //获取验证码
    void getVerifyCode(String phone);

    //获取验证码状态
    void getSmsStatue(String ssid);

    //开始注册
    void startRegister(String phoen, String smsCode, String password, String inviteeCode);
}
