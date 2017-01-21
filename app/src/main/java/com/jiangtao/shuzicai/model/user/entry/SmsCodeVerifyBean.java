package com.jiangtao.shuzicai.model.user.entry;

/**
 * Created by Nicky on 2017/1/21.
 */

public class SmsCodeVerifyBean {
    private String mobilePhoneNumber;

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getMobilePhoneNumber() {

        return mobilePhoneNumber;
    }

    public SmsCodeVerifyBean() {

    }

    public SmsCodeVerifyBean(String mobilePhoneNumber) {

        this.mobilePhoneNumber = mobilePhoneNumber;
    }
}
