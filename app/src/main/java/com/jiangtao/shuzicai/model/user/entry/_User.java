package com.jiangtao.shuzicai.model.user.entry;

import cn.bmob.v3.BmobUser;

/**
 * Created by Nicky on 2017/1/19.
 * 用户
 */

public class _User extends BmobUser {

    //昵称
    private String nickName;
    //受邀请码
    private String InviteeCode;
    //邀请码
    private String InvitationCode;
    //头像
    private String headImageUrl;
    //性别
    private int gender;
    //地址
    private String address;
    //金币值
    private float goldValue;
    //银币值
    private float silverValue;

    public _User() {
    }

    public _User(String nickName, String inviteeCode, String invitationCode, String headImageUrl, int gender, String
            address, float goldValue, float silverValue) {
        this.nickName = nickName;
        InviteeCode = inviteeCode;
        InvitationCode = invitationCode;
        this.headImageUrl = headImageUrl;
        this.gender = gender;
        this.address = address;
        this.goldValue = goldValue;
        this.silverValue = silverValue;
    }

    public String getNickName() {
        return nickName;
    }

    public String getInviteeCode() {
        return InviteeCode;
    }

    public String getInvitationCode() {
        return InvitationCode;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public int getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public float getGoldValue() {
        return goldValue;
    }

    public float getSilverValue() {
        return silverValue;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setInviteeCode(String inviteeCode) {
        InviteeCode = inviteeCode;
    }

    public void setInvitationCode(String invitationCode) {
        InvitationCode = invitationCode;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGoldValue(float goldValue) {
        this.goldValue = goldValue;
    }

    public void setSilverValue(float silverValue) {
        this.silverValue = silverValue;
    }
}
