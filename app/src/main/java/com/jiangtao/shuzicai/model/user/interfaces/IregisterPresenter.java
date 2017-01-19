package com.jiangtao.shuzicai.model.user.interfaces;

/**
 * Created by Nicky on 2017/1/19.
 */

public interface IRegisterPresenter {

    //获取验证码
    void getVerifyCode(String phone);

    //验证
    void verifySmsCode(String code, String phone);
}
