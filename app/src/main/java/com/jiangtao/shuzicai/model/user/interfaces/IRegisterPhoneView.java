package com.jiangtao.shuzicai.model.user.interfaces;

/**
 * Created by Nicky on 2017/1/19.
 */

public interface IRegisterPhoneView {

    //已发送验证码
    void onGetVeryCode(String ssid);

    //注册成功
    void  onRegisterSucceed();


    //注册失败
    void  onRegisterFailed();

}
