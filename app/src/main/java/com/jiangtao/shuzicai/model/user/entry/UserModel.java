package com.jiangtao.shuzicai.model.user.entry;

import org.json.JSONObject;

/**
 * Created by Nicky on 2017/1/19.
 * 用户
 */

public class UserModel {

    //昵称
    private String nickName;
    //电话号码
    private String mobilePhoneNumber;
    //token
    private String token;
    //objectId
    private String objectId;
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

    public UserModel(String nickName, String mobilePhoneNumber, String token, String onjectID, String inviteeCode,
                     String invitationCode, String headImageUrl, int gender, String address) {
        this.nickName = nickName;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.token = token;
        this.objectId = onjectID;
        InviteeCode = inviteeCode;
        InvitationCode = invitationCode;
        this.headImageUrl = headImageUrl;
        this.gender = gender;
        this.address = address;
    }

    public UserModel(JSONObject result) {
        setModelData(result);
    }

    public void setModelData(JSONObject result) {
        try {
            setMobilePhoneNumber(result.optString("mobilePhoneNumber"));
            setObjectId(result.optString("objectId"));
            setNickName(result.optString("nickName"));
            setInviteeCode(result.optString("InviteeCode"));
            setInvitationCode(result.optString("InvitationCode"));
            setToken(result.optString("sessionToken"));
            setGender(result.optInt("gender"));
            setAddress(result.optString("address"));
            setHeadImageUrl(result.optString("headImageUrl"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserModel() {
    }


    public String getNickName() {
        return nickName;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public String getToken() {
        return token;
    }

    public String getObjectId() {
        return objectId;
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

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public void setToken(String token) {
        if (token==null||token.equals("")){
            return;
        }
        this.token = token;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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
}
