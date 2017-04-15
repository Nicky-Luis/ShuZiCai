package com.jiangtao.shuzicai.model.game.entry;

import cn.bmob.v3.BmobObject;

/**
 * Created by Nicky on 2017/2/16.
 * 涨跌预测记录
 */
public class GuessForecastRecord extends BmobObject {

    //涨
    public final static int ForecastDown = 0;
    public final static int ForecastUp = 1;
    //用户id
    private String userId;
    //押注银元数值
    private float betSilverValue;
    //押注涨跌情况，0:跌，1：涨
    private int betValue;
    //押注期数
    private int periodNum;
    //状态,0:未开奖，1：中奖，2：未中奖
    private int betStatus;
    //实际结果，0:跌，1：涨
    private int betResult;
    //实际的指数
    private float indexResult;
    //获取的奖励数量
    private float rewardValue;
    //奖励是否已经同步：0：未同步，1：已经同步
    private int rewardFlag;
    //预测是否已经处理：0：未处理，1：已经处理
    private int handlerFlag;

    public GuessForecastRecord(){
    }

    public GuessForecastRecord(String userId, float betSilverValue, int betValue, int periodNum, int betStatus, int
            betResult, float indexResult, float rewardValue, int rewardFlag, int handlerFlag) {
        this.userId = userId;
        this.betSilverValue = betSilverValue;
        this.betValue = betValue;
        this.periodNum = periodNum;
        this.betStatus = betStatus;
        this.betResult = betResult;
        this.indexResult = indexResult;
        this.rewardValue = rewardValue;
        this.rewardFlag = rewardFlag;
        this.handlerFlag = handlerFlag;
    }


    public String getUserId() {
        return userId;
    }

    public float getBetSilverValue() {
        return betSilverValue;
    }

    public int getBetValue() {
        return betValue;
    }

    public int getPeriodNum() {
        return periodNum;
    }

    public int getBetStatus() {
        return betStatus;
    }

    public int getBetResult() {
        return betResult;
    }

    public float getIndexResult() {
        return indexResult;
    }

    public float getRewardValue() {
        return rewardValue;
    }

    public int getRewardFlag() {
        return rewardFlag;
    }

    public int getHandlerFlag() {
        return handlerFlag;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setBetSilverValue(float betSilverValue) {
        this.betSilverValue = betSilverValue;
    }

    public void setBetValue(int betValue) {
        this.betValue = betValue;
    }

    public void setPeriodNum(int periodNum) {
        this.periodNum = periodNum;
    }

    public void setBetStatus(int betStatus) {
        this.betStatus = betStatus;
    }

    public void setBetResult(int betResult) {
        this.betResult = betResult;
    }

    public void setIndexResult(float indexResult) {
        this.indexResult = indexResult;
    }

    public void setRewardValue(float rewardValue) {
        this.rewardValue = rewardValue;
    }

    public void setRewardFlag(int rewardFlag) {
        this.rewardFlag = rewardFlag;
    }

    public void setHandlerFlag(int handlerFlag) {
        this.handlerFlag = handlerFlag;
    }
}
