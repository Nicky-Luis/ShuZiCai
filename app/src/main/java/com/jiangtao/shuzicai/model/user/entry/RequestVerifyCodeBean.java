package com.jiangtao.shuzicai.model.user.entry;

/**
 * Created by Nicky on 2017/1/19.
 */

public class RequestVerifyCodeBean {
    private String mobilePhoneNumber;
    private String template;

    public RequestVerifyCodeBean(String mobilePhoneNumber, String template) {
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public RequestVerifyCodeBean() {
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }
}
