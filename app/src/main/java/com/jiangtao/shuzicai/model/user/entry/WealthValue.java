package com.jiangtao.shuzicai.model.user.entry;

import com.blankj.utilcode.utils.LogUtils;

/**
 * Created by Nicky on 2017/1/26.
 */

public class WealthValue {

    //objectId
    private String objectId;
    //用户id
    private String userId;
    //金币值
    private float goldValue;
    //银币值
    private float silverValue;

    public WealthValue(String objectId, String userId, float goldValue, float silverValue) {
        this.objectId = objectId;
        this.userId = userId;
        this.goldValue = goldValue;
        this.silverValue = silverValue;
    }

    public WealthValue() {
    }

    public String getObjectId() {
        return objectId;
    }

    public String getUserId() {
        return userId;
    }

    public float getGoldValue() {
        return goldValue;
    }

    public float getSilverValue() {
        return silverValue;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setGoldValue(float goldValue) {
        LogUtils.i("金币值：" + goldValue);
        this.goldValue = goldValue;
    }

    public void setSilverValue(float silverValue) {
        this.silverValue = silverValue;
    }
}
