package com.jiangtao.shuzicai.model.user.entry;

/**
 * Created by Nicky on 2017/1/23.
 */

public class UpdateInfoBean {

    //昵称
    private String nickName;
    //头像
    private String headImageUrl;
    //性别
    private int gender;
    //地址
    private String address;

    public UpdateInfoBean() {
    }

    public UpdateInfoBean(String nickName, String headImageUrl, int gender, String address) {
        this.nickName = nickName;
        this.headImageUrl = headImageUrl;
        this.gender = gender;
        this.address = address;
    }

    public String getNickName() {
        return nickName;
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

    public void setNickName(String nickName) {
        this.nickName = nickName;
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
}
