package com.jiangtao.shuzicai.model.user.entry;

/**
 * Created by Nicky on 2017/1/19.
 * 注册实例
 */

public class RegisterBean {
    //电话
    private String mobilePhoneNumber;
    //验证码
    private String smsCode;

    public RegisterBean(String mobilePhoneNumber, String smsCode) {
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.smsCode = smsCode;
    }

    public RegisterBean() {
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
