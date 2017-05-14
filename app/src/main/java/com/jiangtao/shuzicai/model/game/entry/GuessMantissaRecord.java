package com.jiangtao.shuzicai.model.game.entry;

import cn.bmob.v3.BmobObject;

/**
 * Created by Nicky on 2017/2/19.
 * 尾数预测记录
 */

public class GuessMantissaRecord extends BmobObject {

    //游戏类型
    public final static int Guess_Type_Percentile = 0;//百分位直选
    public final static int Guess_Type_DoubleDirect = 1;//双数直选
    public final static int Guess_Type_DoubleGroup = 2;//双数组选
    //指数类型
    public final static int Index_Type_Hushen = 0;
    public final static int Index_Type_Gold = 1;
    //用户id
    private String userId;
    //押注金币数值
    private float goldValue;
    //类型
    private int guessType;
    //指数类型,黄金或者沪深300
    private int indexType;
    //押注期数
    private int periodNum;
    //押注的数据
    private int guessValue;
    //实际的指数
    private float indexResult;
    //状态,0:未开奖，1：中奖，2：未中奖
    private int betStatus;
    //获取的奖励数量
    private int rewardCount;
    //奖励是否已经同步：0：未同步，1：已经同步
    private int rewardFlag;
    //预测是否已经处理：0：未处理，1：已经处理
    private int handlerFlag;

    public GuessMantissaRecord() {
    }

    public int getBetStatus() {
        return betStatus;
    }

    public void setBetStatus(int betStatus) {
        this.betStatus = betStatus;
    }

    public String getUserId() {
        return userId;
    }

    public float getGoldValue() {
        return goldValue;
    }

    public int getGuessType() {
        return guessType;
    }

    public int getIndexType() {
        return indexType;
    }

    public int getPeriodNum() {
        return periodNum;
    }

    public int getGuessValue() {
        return guessValue;
    }

    public float getIndexResult() {
        return indexResult;
    }


    public int getRewardCount() {
        return rewardCount;
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

    public void setGoldValue(float goldValue) {
        this.goldValue = goldValue;
    }

    public void setGuessType(int guessType) {
        this.guessType = guessType;
    }

    public void setIndexType(int indexType) {
        this.indexType = indexType;
    }

    public void setPeriodNum(int periodNum) {
        this.periodNum = periodNum;
    }

    public void setGuessValue(int guessValue) {
        this.guessValue = guessValue;
    }

    public void setIndexResult(float indexResult) {
        this.indexResult = indexResult;
    }


    public void setRewardCount(int rewardCount) {
        this.rewardCount = rewardCount;
    }

    public void setRewardFlag(int rewardFlag) {
        this.rewardFlag = rewardFlag;
    }

    public void setHandlerFlag(int handlerFlag) {
        this.handlerFlag = handlerFlag;
    }

}
