package com.jiangtao.shuzicai.model.user.entry;

/**
 * Created by Nicky on 2017/2/14.
 * 记录邀请相关的信息
 */

public class InviteRecord {
    //时间
    private String time;
    //受邀者
    private String userName;
    //充值金额
    private float rechargeValue;
    //奖励金额
    private float rewardValue;

    public InviteRecord(String time, String name, float rechargeValue, float rewardValue) {
        this.time = time;
        userName = name;
        this.rechargeValue = rechargeValue;
        this.rewardValue = rewardValue;
    }

    public InviteRecord() {
    }

    public String getTime() {
        return time;
    }

    public String getUserName() {
        return userName;
    }


    public float getRechargeValue() {
        return rechargeValue;
    }

    public float getRewardValue() {
        return rewardValue;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public void setRechargeValue(float rechargeValue) {
        this.rechargeValue = rechargeValue;
    }

    public void setRewardValue(float rewardValue) {
        this.rewardValue = rewardValue;
    }
}
