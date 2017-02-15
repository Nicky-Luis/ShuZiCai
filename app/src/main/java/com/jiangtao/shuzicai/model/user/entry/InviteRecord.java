package com.jiangtao.shuzicai.model.user.entry;

/**
 * Created by Nicky on 2017/2/14.
 * 记录邀请相关的信息
 */

public class InviteRecord {
    //时间
    private String  time;
    //受邀者
    private String  InviteeUser;
    //充值金额
    private float rechargeValue;
    //奖励金额
    private float rewardValue;

    public InviteRecord(String time, String inviteeUser,
                        float rechargeValue, float rewardValue) {
        this.time = time;
        InviteeUser = inviteeUser;
        this.rechargeValue = rechargeValue;
        this.rewardValue = rewardValue;
    }

    public InviteRecord() {
    }

    public String getTime() {
        return time;
    }

    public String getInviteeUser() {
        return InviteeUser;
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

    public void setInviteeUser(String inviteeUser) {
        InviteeUser = inviteeUser;
    }


    public void setRechargeValue(float rechargeValue) {
        this.rechargeValue = rechargeValue;
    }

    public void setRewardValue(float rewardValue) {
        this.rewardValue = rewardValue;
    }
}
