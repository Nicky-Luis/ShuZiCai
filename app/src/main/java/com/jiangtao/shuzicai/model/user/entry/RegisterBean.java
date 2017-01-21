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
    //密码
    private String password;
    //受邀请码
    private String InviteeCode;
    //邀请码
    private String InvitationCode;

    public RegisterBean(String mobilePhoneNumber, String smsCode, String password, String inviteeCode, String
            invitationCode) {
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.smsCode = smsCode;
        this.password = password;
        InviteeCode = inviteeCode;
        InvitationCode = invitationCode;
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

    public String getPassword() {
        return password;
    }

    public String getInviteeCode() {
        return InviteeCode;
    }

    public String getInvitationCode() {
        return InvitationCode;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setInviteeCode(String inviteeCode) {
        InviteeCode = inviteeCode;
    }

    public void setInvitationCode(String invitationCode) {
        InvitationCode = invitationCode;
    }
}
